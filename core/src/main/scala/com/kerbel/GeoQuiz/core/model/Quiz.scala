package com.kerbel.GeoQuiz.core.model

import com.kerbel.GeoQuiz.core.model.Question._


import scala.language.higherKinds
import scala.util.Try
import scalaz._
import Scalaz._

case class Quiz(questions: Set[Question], answers: Seq[Question#Answer]) {
  def answer (quizEntry: Question)(choice: quizEntry.Choice) : Quiz = this.copy(questions = questions - quizEntry, answers = answers :+ quizEntry.answer(choice))
  val correctAnswers : Int = answers.count(_.isCorrect)
  val incorrectAnswers : Int = answers.count(_.isIncorrect)
  val answered : Int = answers.length
  val remainingQuestions : Int  = questions.size

}

object Quiz {
  import com.kerbel.GeoQuiz.core.service.QuizService
  def fromService[M[_]](count: Int)(implicit m: Monad[M], quizService: QuizService[Question,M]) : M[Quiz] = for {
    seq <- quizService.getQuizzes(count)
  } yield Quiz(seq.toSet, Seq.empty)
}