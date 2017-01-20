package com.kerbel.GeoQuiz.core.util.scalaz

import scalaz._
import Scalaz._
import monix.eval._

object MonixTaskUtils {

  /** Lifts a disjunction into a task, collapsing the left projection into a task error.
    *
    * @param disj The disjunction.
    * @tparam E The error type, which must be a [[java.lang.Throwable]]
    * @tparam A The right projection type - used for the Task's type.
    * @return A task of type A.
    */
  def catchLeft[E <: Throwable,A](disj : \/[E,A]) : Task[A] = disj match {
    case -\/(err) => Task.raiseError(err)
    case \/-(a) => Task.now(a)
  }

  /** Lifts a disjunction into a task, collapsing the left projection into a task error.
    *
    * @param disj The disjunction.
    * @param toThrowable Converts the disjunction's error type to a [[java.lang.Throwable]].
    * @tparam E The error type
    * @tparam A The right projection type - used for the Task's type.
    * @return A task of type A.
    */
  def catchLeft[E,A] (disj: \/[E,A])(implicit toThrowable: E => Throwable) : Task[A] = catchLeft(disj.leftMap(toThrowable))
}
