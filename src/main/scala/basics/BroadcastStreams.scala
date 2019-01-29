package basics

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}

object BroadcastStreams extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()
  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val source = Source(1 to 1000000)
    val multiplierTwo = Flow[Int].map(_ * 2)
    val multiplierThree = Flow[Int].map(_ * 3)
    val sink = Sink.foreach[Int](s => println(s"[output] => $s"))

    val broadcast = b.add(Broadcast[Int](2))
    source ~> broadcast.in

    broadcast.out(0) ~> multiplierTwo ~> sink
    broadcast.out(1) ~> multiplierThree ~> sink

    ClosedShape
  })

  graph.run()
}
