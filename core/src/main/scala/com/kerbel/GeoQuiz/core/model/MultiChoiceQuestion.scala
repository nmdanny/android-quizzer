package com.kerbel.GeoQuiz.core.model

import com.kerbel.GeoQuiz.core.model.Difficulty.Difficulty

/**
  * A multiple choice question
  *
  * @param category The question's category
  * @param difficulty The question's difficulty
  * @param question The question
  * @param correctAnswer The correct answer
  * @param incorrectAnswers A list of incorrect answers.
  */
final case class MultiChoiceQuestion(category: String, difficulty: Difficulty, question: String, correctAnswer: String, incorrectAnswers: Seq[String]) extends Question with ChoiceQuestion {
  type Choice = String
  override def choiceIsCorrect (choice: String): Boolean = choice == correctAnswer
  override val allAnswers : Seq[Choice] = incorrectAnswers :+ correctAnswer
}
