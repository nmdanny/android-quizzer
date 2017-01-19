package com.kerbel.GeoQuiz.core.Model

import argonaut.{DecodeJson, DecodeResult}
import com.kerbel.GeoQuiz.core.Model.Difficulty.Difficulty

/**
  * The difficulty of the quiz.
  */
object Difficulty extends Enumeration {
  type Difficulty = Value
  val Easy,Medium,Hard = Value
}

/**
  * A quiz entry.
  * @see See below for implementations.
  *      [[com.kerbel.GeoQuiz.Model.MultiChoiceQuestion]]
  *      [[com.kerbel.GeoQuiz.Model.YesNoQuestion]]
  *
  */
sealed trait QuizEntry { outer =>
  type Choice
  val category: String
  val difficulty: Difficulty
  val question: String

  def choiceIsCorrect(choice: Choice): Boolean
  final def answer(choice: Choice): Question = Question(Some(choice))
  final def mkQuestion: Question = Question(None)
  final case class Question (answer: Option[Choice]) {
    val isCorrect : Boolean = answer.exists(ans => choiceIsCorrect(ans))
    val isIncorrect: Boolean = answer.exists(ans => !choiceIsCorrect(ans))
    val isAnswered: Boolean = answer.isDefined
    val status: AnswerStatus = if (!isAnswered) Unanswered
                               else if (isCorrect) Correct
                               else Incorrect
    val entry: QuizEntry = outer
    def changeAnswer(choice: Choice) = Question(Some(choice))
  }
  sealed trait AnswerStatus
  final case object Correct extends AnswerStatus
  final case object Incorrect extends AnswerStatus
  final case object Unanswered extends AnswerStatus
}

/***
  * A quiz entry which provides certain choices/possibilities.
  */
trait ChoiceQuestion extends { this: QuizEntry =>
  /**
    * All possible answers
    */
  val allAnswers: Seq[Choice]
}

/**
  * A trivia question that is either true or false.
  * @param category The question's category
  * @param difficulty The question's difficulty
  * @param question The statement
  * @param isTrue Is the statement true?
  */
final case class YesNoQuestion(category: String, difficulty: Difficulty, question: String,isTrue: Boolean) extends QuizEntry with ChoiceQuestion {
  type Choice = Boolean
  override def choiceIsCorrect (choice: Boolean): Boolean = choice == isTrue
  override val allAnswers: Seq[Choice] = Seq(true,false)
}

/**
  * A multiple choice question
  * @param category The question's category
  * @param difficulty The question's difficulty
  * @param question The question
  * @param correctAnswer The correct answer
  * @param incorrectAnswers A list of incorrect answers.
  */
final case class MultiChoiceQuestion(category: String, difficulty: Difficulty, question: String, correctAnswer: String, incorrectAnswers: Seq[String]) extends QuizEntry with ChoiceQuestion {
  type Choice = String
  override def choiceIsCorrect (choice: String): Boolean = choice == correctAnswer
  override val allAnswers : Seq[Choice] = incorrectAnswers :+ correctAnswer
}


object QuizEntry {
  implicit def decodeResponse : DecodeJson[Seq[QuizEntry]] = DecodeJson(c => c.downField("results").as[Seq[QuizEntry]])
  implicit def DecodeDifficulty : DecodeJson[Difficulty] = DecodeJson(c => c.focus.string.map(_.toLowerCase) match {
    case Some("easy") => DecodeResult.ok(Difficulty.Easy)
    case Some("medium") => DecodeResult.ok(Difficulty.Medium)
    case Some("hard") => DecodeResult.ok(Difficulty.Hard)
    case _ => DecodeResult.fail("Failed to decode Difficulty - is not easy,medium or hard.",c.history)
  })
  implicit def DecodeQuizEntry : DecodeJson[QuizEntry] = DecodeJson(c => for {
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

