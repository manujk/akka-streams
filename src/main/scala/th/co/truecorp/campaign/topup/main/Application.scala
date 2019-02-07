package th.co.truecorp.campaign.topup.main

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, ClosedShape, Supervision}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink}
import com.fasterxml.jackson.core.JsonParseException
import th.co.truecorp.campaign.topup.model.Topup
import org.apache.kafka.clients.consumer.ConsumerRecord

object Application {

  def main(args: Array[String]): Unit = {

    val decider: Supervision.Decider = {
      case _: JsonParseException => Supervision.Resume
      case _ => Supervision.Stop
    }

    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy(decider))
    implicit val executionContext = system.dispatcher

    val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      import th.co.truecorp.campaign.topup.core.source.kafka.ConsumerSource
      val source = ConsumerSource.build("ccp-1200", bootstrapServers = "172.16.2.142:9092,172.16.2.143:9092,172.16.2.144:9092", groupID = "campaign-topup")

      import th.co.truecorp.campaign.topup.core.utils.ParserJson.jsonParse
      val parser: Flow[ConsumerRecord[String, String], Option[Topup], NotUsed] = Flow[ConsumerRecord[String, String]].map(r => jsonParse(r.value()))

      import th.co.truecorp.campaign.topup.core.utils.ParserJson.validRecords
      val validKafkaData: Flow[Option[Topup], Option[Topup], NotUsed] = Flow[Option[Topup]].filter(validRecords)

      val topupData = builder.add(Broadcast[Option[Topup]](2))

      val sink = Sink.foreach(println)
      val sinkIgnore = Sink.ignore

      source ~> parser ~> validKafkaData ~> topupData.in

      topupData.out(0) ~> sink
      topupData.out(1) ~> sinkIgnore

      ClosedShape
    })
    graph.run()

  }

}
