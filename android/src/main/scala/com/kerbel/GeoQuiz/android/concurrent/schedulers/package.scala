package com.kerbel.GeoQuiz.android.concurrent

import java.util.concurrent.Executor

import monix.execution.Scheduler

import scala.concurrent.ExecutionContext

/**
  * This package provides a Java [[java.util.concurrent.Executor]], Scala [[scala.concurrent.ExecutionContext]] and a Monix [[monix.execution.Scheduler]] for the Android main application thread. (where UI is ran)
  */
package object schedulers {
  final val MAIN_THREAD : Executor = new UiThreadExecutor()
  final val MAIN_THREAD_EC : ExecutionContext = ExecutionContext.fromExecutor(MAIN_THREAD)
  final val MAIN_THREAD_SCHEDULER : Scheduler = Scheduler(MAIN_THREAD_EC)
  object Implicit {
    implicit val MAIN_THREAD = schedulers.MAIN_THREAD
    implicit val MAIN_THREAD_EC = schedulers.MAIN_THREAD_EC
    implicit val MAIN_THREAD_SCHEDULER = schedulers.MAIN_THREAD_SCHEDULER
  }
}
