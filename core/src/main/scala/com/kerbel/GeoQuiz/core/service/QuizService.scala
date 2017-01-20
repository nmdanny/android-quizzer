package com.kerbel.GeoQuiz.core.service

import com.kerbel.GeoQuiz.core.model._
import com.kerbel.GeoQuiz.core.util.scalaz.MonixTaskUtils._


import argonaut.Parse
import io.taig.communicator._
import io.taig.communicator.request
import io.taig.communicator.request.Request
import monix.eval._
import scalaz._
import Scalaz._
import scala.concurrent.ExecutionContext
import okhttp3.OkHttpClient
import language.higherKinds


/**
  * The quiz API client service.
  * @tparam Entry The type of the quiz entries.
  * @tparam M The monad used for the service functions.
  */
// TODO: do I really need to abstract over both the monad and the entry, do I even need a trait?
trait QuizService[Entry,M[_]] {
  def getQuiz() : M[Entry]
  def getQuizzes(count : Int) : M[Seq[Entry]]
}

/**
  * A service for getting quizzes from an HTTP service.
  */
object QuizService extends QuizService[Question,Task] {
  lazy implicit val client = new OkHttpClient()


  override def getQuiz() : Task[Question] = getQuizzes(1).flatMap(seq =>
     seq.headOption match {
       case Some(entry) => Task.now(entry)
       case None => Task.raiseError(ServiceException(message = "Quiz list was empty."))
  })

  override def getQuizzes (count: Int) : Task[Seq[Question]] = for {
    req <- Request(new OkHttpRequest.Builder().url(s"https://www.opentdb.com/api.php?amount=$count").build()).parse[String]
    seqOrError = Parse.decode(req.body)(Question.decodeResponse)
    res <- catchLeft(seqOrError)(err => ServiceException(message = s"There was an error parsing the quiz-list: $err"))
  } yield res


}