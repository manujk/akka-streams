import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}

object HelloGraphDSL extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()
  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val source = Source(1 to 100)
    val multiplier = Flow[Int].map(_ * 2)
    val sink = Sink.foreach[Int](s => println(s"[output] => $s"))

    source ~> multiplier ~> sink

    ClosedShape
  })

  graph.run()

}
