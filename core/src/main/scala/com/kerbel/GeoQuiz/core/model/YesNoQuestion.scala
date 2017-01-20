package com.kerbel.GeoQuiz.core.model

import com.kerbel.GeoQuiz.core.model.Difficulty.Difficulty

/**
  * A trivia question that is either true or false.
  *
  * @param category The question's category
  * @param difficulty The question's difficulty
  * @param question The statement
  * @param isTrue Is the statement true?
  */
final case class YesNoQuestion(category: String, difficulty: Difficulty, question: String,isTrue: Boolean) extends Question with ChoiceQuestion {
  type Choice = Boolean
  override def choiceIsCorrect (choice: Boolean): Boolean = choice == isTrue
  override val allAnswers: Seq[Choice] = Seq(true,false)
}
