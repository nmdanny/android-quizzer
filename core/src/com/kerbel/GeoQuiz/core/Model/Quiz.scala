package com.kerbel.GeoQuiz.core.Model

import com.kerbel.GeoQuiz.core.Model.QuizEntry._


import scala.language.higherKinds
import scala.util.Try
import scalaz._
import Scalaz._

case class Quiz(questions: Set[QuizEntry#Question]) {
  def answer(quizEntry: QuizEntry)(choice: quizEntry.Choice) : Quiz = this.copy(questions = questions + quizEntry.answer(choice))
  val correctAnswers : Int = questions.count(_.isCorrect)
  val incorrectAnswers : Int = questions.count(_.isIncorrect)
  val answered : Int = questions.count(_.isAnswered)
  val remainingQuestions : Int  = questions.count(!_.isAnswered)

}

object Quiz {
  import com.kerbel.GeoQuiz.Service.QuizService
  def fromService[M[_]](count: Int)(implicit m: Monad[M], quizService: QuizService[QuizEntry,M]) : M[Quiz] = for {
    seq <- quizService.getQuizzes(count)
  } yield Quiz(seq.map(_.mkQuestion).toSet)
}