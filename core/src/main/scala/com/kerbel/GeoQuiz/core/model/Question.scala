package com.kerbel.GeoQuiz.core.model

import argonaut.{DecodeJson, DecodeResult}
import com.kerbel.GeoQuiz.core.model.Difficulty.Difficulty






/**
  * A quiz entry.
  *
  * @see See below for implementations.
  *      [[com.kerbel.GeoQuiz.core.model.MultiChoiceQuestion]]
  *      [[com.kerbel.GeoQuiz.core.model.YesNoQuestion]]
  *
  */
trait Question { outer =>
  type Choice
  val category: String
  val difficulty: Difficulty
  val question: String

  def choiceIsCorrect(choice: Choice): Boolean
  final def answer(choice: Choice): Answer = Answer(Some(choice))
  final case class Answer (answer: Option[Choice]) {
    val isCorrect : Boolean = answer.exists(ans => choiceIsCorrect(ans))
    val isIncorrect: Boolean = answer.exists(ans => !choiceIsCorrect(ans))
    val isAnswered: Boolean = answer.isDefined
    val status: AnswerStatus = if (!isAnswered) Unanswered
                               else if (isCorrect) Correct
                               else Incorrect
    val question: Question = outer
  }

}

/***
  * A quiz entry which provides certain choices/possibilities.
  */
trait ChoiceQuestion extends Question {
  /**
    * All possible answers
    */
  val allAnswers: Seq[Choice]
}

object Question {
  implicit def decodeResponse : DecodeJson[Seq[Question]] = DecodeJson(c => c.downField("results").as[Seq[Question]])
  implicit def DecodeDifficulty : DecodeJson[Difficulty] = DecodeJson(c => c.focus.string.map(_.toLowerCase) match {
    case Some("easy") => DecodeResult.ok(Difficulty.Easy)
    case Some("medium") => DecodeResult.ok(Difficulty.Medium)
    case Some("hard") => DecodeResult.ok(Difficulty.Hard)
    case _ => DecodeResult.fail("Failed to decode Difficulty - is not easy,medium or hard.",c.history)
  })
  implicit def DecodeQuizEntry : DecodeJson[Question] = DecodeJson(c => for {
    quizType <- (c --\ "type").as[String]
    question <- quizType match {
      case "boolean" => c.as[YesNoQuestion]
      case "multiple" => c.as[MultiChoiceQuestion]
      case _ => DecodeResult.fail("Failed to decode QuizEntry - unknown quiz type.",c.history) }
  } yield question)
  implicit def DecodeYesNoQuestion : DecodeJson[YesNoQuestion] = DecodeJson(c => for {
    category <- (c --\ "category").as[String]
    difficulty <- (c --\ "difficulty").as[Difficulty]
    question   <- (c --\ "question").as[String]
    isTrue     <- (c --\ "correct_answer").as[String].map(_.toBoolean)
  } yield YesNoQuestion(category,difficulty,question,isTrue))
  implicit def DecodeMultiChoiceQuestion : DecodeJson[MultiChoiceQuestion] = DecodeJson(c => for {
    category <- (c --\ "category").as[String]
    difficulty <- (c --\ "difficulty").as[Difficulty]
    question   <- (c --\ "question").as[String]
    correctAnswer <- (c --\ "correct_answer").as[String]
    incorrectAnswers <- (c --\ "incorrect_answers").as[Seq[String]]
  } yield MultiChoiceQuestion(category,difficulty,question,correctAnswer,incorrectAnswers))
}

