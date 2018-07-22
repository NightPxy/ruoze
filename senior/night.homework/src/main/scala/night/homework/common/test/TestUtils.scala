package night.homework.common.test

import scala.concurrent.{Await, Awaitable, Future}
import scala.util.{Failure, Success}

/**
  * Created by Night on 2018/7/22.
  */
trait TestUtils {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  def assertOptionIsNone[T](value: Option[T]) = value match {
    case Some(_) => throw new Throwable("assertOptionIsNone failed");
    case _ =>
  }

  def assertOptionNotNone[T](value: Option[T]) = value match {
    case None => throw new Throwable("assertOptionNotNone failed");
    case _ =>
  }

  def assertException(execute: => Any) = {
    try {
      execute
      throw new Throwable("assertException failed");
    }
    catch {
      case _: Throwable => ;
    }
  }

  def assertFutureSuccess[T](execute: Future[T]) = {
    execute onComplete {
      case Success(_) =>
      case Failure(err) => throw new Throwable(s"assertFutureSuccess failed:Failure ${err}");
      case _ => throw new Throwable("assertFutureSuccess failed:_");
    }
  }

  def assertFutureFailure[T](execute: Future[T]) = {
    execute onComplete {
      case Success(_) => throw new Throwable("assertFutureFailure failed:Success");
      case Failure(_) =>
      case _ => throw new Throwable("assertFutureFailure failed:_");
    }
  }

  def assertFutureSuccess_resultNotNone[T](execute: Future[Option[T]]) = {
    execute onComplete {
      case Success(result) => result match {
        case Some(_) =>
        case None => throw new Throwable("assertFutureSuccess_resultNotNone failed:Result None");
      }
      case Failure(err) => throw new Throwable(s"assertFutureSuccess_resultNotNone failed:Failure ${err}");
      case _ => throw new Throwable("assertFutureSuccess_resultNotNone failed:_");
    }
  }

  def assertFutureSuccess_resultIsNone[T](execute: Future[Option[T]]) = {
    execute onComplete {
      case Success(result) => result match {
        case Some(_) => throw new Throwable("assertFutureSuccess_resultIsNone failed:Result Some");
        case None =>
      }
      case Failure(_) => throw new Throwable("assertFutureSuccess_resultIsNone failed:Failure");
      case _ => throw new Throwable("assertFutureSuccess_resultIsNone failed:_");
    }
  }

  def await[T](awaitable: Awaitable[T], timeOut: Duration = 60.seconds) = {
    Await.result(awaitable, timeOut)
  }
}
