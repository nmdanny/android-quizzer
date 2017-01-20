package com.kerbel.GeoQuiz.core.serviceSpec

import com.kerbel.GeoQuiz.core.model.Question
import com.kerbel.GeoQuiz.core.service.QuizService
import monix.eval._
import org.scalatest._
import monix.execution._
import monix.execution.Scheduler.Implicits.global

import scala.util.Success

class QuizServiceSpec extends AsyncFlatSpec with Matchers{
  "getQuiz" should "work" in {
    val fut = QuizService.getQuiz().runAsync
    fut.map(e => e shouldNot be (null))
  }
  "getQuizzes" should "work when batched" in {
    val fut = Task.gather(Seq.fill(3)(QuizService.getQuizzes(50))).map(seqseqs => seqseqs.flatten).runAsync
    fut.map(es => es.length should be (50*3))
  }
}
