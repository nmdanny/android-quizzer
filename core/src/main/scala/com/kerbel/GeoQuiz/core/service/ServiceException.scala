package com.kerbel.GeoQuiz.core.service


/**
  * An exception in the API service layer.
  * @param message The exception's message.
  * @param cause A nullable(optional) [[java.lang.Throwable]] linked to the exception.
  */
case class ServiceException (message: String = null, cause: Throwable = null) extends RuntimeException(ServiceException.defaultMessage(message,cause),cause)

object ServiceException {
  protected def defaultMessage(message: String, cause: Throwable): String = {
    if (message != null) message
    else if (cause != null) cause.toString
    else null
  }
}