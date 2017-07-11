package simpleStream

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import simpleStream.StreamForeach2App.system

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Failure, Success}

// never ending app:
object StreamForeach1App extends App{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val s = new SimpleStream

  val done: NotUsed = s.streamForEach1.run()
}

// app which ends after stream is completed:
object StreamForeach2App extends App{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val s = new SimpleStream

  val done: Future[Done] = s.streamForEach2.run()

  done.onComplete(_ => system.terminate())
}

// never ending app2:
object streamSeq2App extends App{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val s = new SimpleStream

  val done: Future[immutable.Seq[Int]] = s.streamSeq2.run()

  done onComplete {
    case Success(e) =>
      println(e)
      system.terminate()
    case Failure(e) =>
      e.printStackTrace()
      system.terminate()
  }
}

// steram with buffer stage:
object StreamBufferApp extends App{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val s = new SimpleStream
  val done: NotUsed = s.bufferStream.run()
}
