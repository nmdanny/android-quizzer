package com.kerbel.GeoQuiz.core

package object model {
  sealed trait AnswerStatus
  case object Correct extends AnswerStatus
  case object Incorrect extends AnswerStatus
  case object Unanswered extends AnswerStatus

  /**
    * The difficulty of the quiz.
    */
  object Difficulty extends Enumeration {
    type Difficulty = Value
    val Easy,Medium,Hard = Value
  }

}
