import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.testkit.TestKit
import org.scalatest.{AsyncFlatSpecLike, Matchers}

import scala.collection.immutable
import scala.concurrent.Future

class SebastianSpec extends TestKit(ActorSystem("DestinationStreamSpec"))
  with AsyncFlatSpecLike with Matchers {

  implicit val materializer = ActorMaterializer()

  val sekwencjaZrodlowa: List[(String, Int)] = List(
    ("a" -> 1),
    ("a" -> 2),
    ("c" -> 3),
    ("c" -> 4),
    ("e" -> 5)
  )

  val toCoChcesz: Map[String, List[Int]] = Map(
    "a" -> List(1,2),
    "c" -> List(3,4),
    "e" -> List(5)
  )

  def transform(sz: Seq[(String,Int)]): Map[String, Seq[Int]] = sz.groupBy(e => e._1).mapValues(_.map(_._2)).toMap


  "transform" should "transform the collection in proper way" in {
    transform(sekwencjaZrodlowa) should contain theSameElementsAs toCoChcesz
  }

  "test stream z transformacja w flow" should "succeed" in {
    val source: Source[List[(String, Int)], NotUsed] = Source.repeat(sekwencjaZrodlowa).take(1)
    val flow: Flow[List[(String, Int)], Map[String, Seq[Int]], NotUsed] = Flow[ List[(String, Int)]] map transform

    val future: Future[immutable.Seq[Map[String, Seq[Int]]]] = source
      .via(flow)
      .runWith(Sink.seq)

    future.map( _ should contain only toCoChcesz )
  }

  "test stream z transformacja w zlewie" should "succeed" in {
    val source: Source[(String, Int), NotUsed] = Source(sekwencjaZrodlowa)

    val future: Future[Map[String, Seq[Int]]] = source
      .runWith(Sink.seq)
      .map(transform)

    future.map( _ shouldEqual toCoChcesz )
  }

}