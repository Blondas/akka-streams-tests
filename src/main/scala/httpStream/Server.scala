package httpStream

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object Server extends App {

  implicit val system = ActorSystem("books")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val books: Route = path("books") {
    get {
      complete("")
    }
  }

  
}
