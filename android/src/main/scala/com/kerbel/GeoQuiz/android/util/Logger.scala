package com.kerbel.GeoQuiz.android.util

import android.content.Context
import android.util.Log
import android.widget.Toast
object Logger extends {
  val tag = "GeoQuiz.Logger"

  /**
    * A partial function error handler, logging via [[android.util.Log]]) and optionally via a toast(only works while inside the UI thread)
    * @param msg An optional log message to be included
    * @param doToast Should you toast the message?
    * @return A partial function that swallows [[scala.Throwable]]s, useful for various error handling functions. (such as [[scala.concurrent.Future.onComplete]])
    */
  def pfHandler(msg: String = "", doToast : Boolean = false) : PartialFunction[Throwable,Unit] = { case e => Logger.e(msg,Some(e),doToast) }

  def d(msg: String, tr: Option[Throwable]= None, toast: Boolean = false)(implicit ctx: Context = null) : Unit = {
    tr match {
      case Some(err) => Log.d(tag,msg,err)
      case None => Log.d(tag,msg)
    }
    if (toast && ctx != null) Toast.makeText(ctx,msg,Toast.LENGTH_SHORT)
  }
  def i(msg: String, tr: Option[Throwable]= None, toast: Boolean = false)(implicit ctx: Context = null) : Unit = {
    tr match {
      case Some(err) => Log.i(tag,msg,err)
      case None => Log.i(tag,msg)
    }
    if (toast && ctx != null) Toast.makeText(ctx,msg,Toast.LENGTH_SHORT)
  }
  def e(msg: String, tr: Option[Throwable]= None, toast: Boolean = false)(implicit ctx: Context = null) : Unit = {
    tr match {
      case Some(err) => Log.e(tag,msg,err)
      case None => Log.e(tag,msg)
    }
    if (toast && ctx != null) Toast.makeText(ctx,msg,Toast.LENGTH_SHORT)
  }
  def v(msg: String, tr: Option[Throwable]= None, toast: Boolean = false)(implicit ctx: Context = null) : Unit = {
    tr match {
      case Some(err) => Log.v(tag,msg,err)
      case None => Log.v(tag,msg)
    }
    if (toast && ctx != null) Toast.makeText(ctx,msg,Toast.LENGTH_SHORT)
  }
  def w(msg: String, tr: Option[Throwable]= None, toast: Boolean = false)(implicit ctx: Context = null) : Unit = {
    tr match {
      case Some(err) => Log.w(tag,msg,err)
      case None => Log.w(tag,msg)
    }
    if (toast && ctx != null) Toast.makeText(ctx,msg,Toast.LENGTH_SHORT)
  }
  def wtf(msg: String, tr: Option[Throwable]= None, toast: Boolean = false)(implicit ctx: Context = null) : Unit = {
    tr match {
      case Some(err) => Log.wtf(tag,msg,err)
      case None => Log.wtf(tag,msg)
    }
    if (toast && ctx != null) Toast.makeText(ctx,msg,Toast.LENGTH_SHORT)
  }


}
