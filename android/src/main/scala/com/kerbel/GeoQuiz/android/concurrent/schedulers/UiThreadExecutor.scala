package com.kerbel.GeoQuiz.android.concurrent.schedulers

import java.util.concurrent.Executor

import android.os.{Handler, Looper}


class UiThreadExecutor extends Executor {
  private final val handler = new Handler(Looper.getMainLooper)
  override def execute(cmd: Runnable) = handler.post(cmd)
}
