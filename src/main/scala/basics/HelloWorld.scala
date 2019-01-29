package basics

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object HelloWorld extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 100)
  val multiplier = Flow[Int].map(_ * 2)
  val sink = Sink.foreach[Int](s => println(s"[output] => $s"))

  val stream = source
    .via(multiplier)
    .to(sink)

  stream.run()

}
