package com.kerbel.GeoQuiz.core.Service

import argonaut.Parse
import com.kerbel.GeoQuiz.core.Model._
import io.taig.communicator._
import io.taig.communicator.request
import io.taig.communicator.request.Request
import monix.eval._
import monix.reactive._
import monix.execution.Scheduler

import language.higherKinds
import scalaz._
import Scalaz._
import scala.concurrent.ExecutionContext
import okhttp3.OkHttpClient

/**
  * The quiz API client service.
  * @tparam Entry The type of the quiz entries.
  * @tparam M The monad used for the service functions.
  */
trait QuizService[Entry,M[_]] {
  def getQuiz : M[Entry]
  def getQuizzes(count : Int) : M[Seq[Entry]]
}

/**
  * A service for getting quizzes from an HTTP service.
  */
object QuizService extends QuizService[QuizEntry,Task] {
  lazy implicit val client = new OkHttpClient()


  override def getQuiz : Task[QuizEntry] = getQuizzes(1).flatMap(seq =>
     seq.headOption match {
       case Some(entry) => Task.now(entry)
       case None => Task.raiseError(ServiceException(message = "Quiz list was empty."))
  })

  override def getQuizzes (count: Int) : Task[Seq[QuizEntry]] = for {
    req <- Request(new OkHttpRequest.Builder().url(s"https://www.opentdb.com/api.php?amount=$count").build()).parse[String]
    seqOrError = Parse.decode(req.body)(QuizEntry.decodeResponse)
    res <- catchLeft(seqOrError)(err => ServiceException(message = s"There was an error parsing the quiz-list: $err"))
  } yield res


  /** Lifts a disjunction into a task, collapsing the left projection into a task error.
    *
    * @param disj The disjunction.
    * @tparam E The error type, which must be a [[java.lang.Throwable]]
    * @tparam A The right projection type - used for the Task's type.
    * @return A task of type A.
    */
  def catchLeft[E <: Throwable,A](disj : \/[E,A]) : Task[A] = disj match {
    case -\/(err) => Task.raiseError(err)
    case \/-(a) => Task.now(a)
  }

  /** Lifts a disjunction into a task, collapsing the left projection into a task error.
    *
    * @param disj The disjunction.
    * @param toThrowable Converts the disjunction's error type to a [[java.lang.Throwable]].
    * @tparam E The error type
    * @tparam A The right projection type - used for the Task's type.
    * @return A task of type A.
    */
  def catchLeft[E,A] (disj: \/[E,A])(implicit toThrowable: E => Throwable) : Task[A] = catchLeft(disj.leftMap(toThrowable))
}