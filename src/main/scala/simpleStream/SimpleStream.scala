package simpleStream

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}

import scala.collection.immutable
import scala.concurrent.Future

class SimpleStream {

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val flow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ * 2)

  val sinkForeEach: Sink[Any, Future[Done]] = Sink.foreach(println)
  val sinkSeq: Sink[Int, Future[immutable.Seq[Int]]] = Sink.seq[Int]
  val sinkFold: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  val streamForEach1: RunnableGraph[NotUsed] =  source via flow to sinkForeEach
  val streamForEach2: RunnableGraph[Future[Done]] = source.via(flow).toMat(sinkForeEach)(Keep.right)

  val streamSeq1: RunnableGraph[NotUsed] = source via flow to sinkSeq
  val streamSeq2: RunnableGraph[Future[immutable.Seq[Int]]] = source.via(flow).toMat(sinkSeq)(Keep.right)

  val streamFold1: RunnableGraph[NotUsed] = source via flow to sinkFold
  val streamFold2: RunnableGraph[Future[Int]] = source.via(flow).toMat(sinkFold)(Keep.right)
}
