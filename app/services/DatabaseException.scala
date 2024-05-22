package services

case class DatabaseException(message: String, cause: Throwable) extends Exception(message, cause)