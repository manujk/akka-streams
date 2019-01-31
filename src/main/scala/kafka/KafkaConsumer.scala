package kafka

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

object KafkaConsumer extends App {

  case class Message(request: HttpRequest, response: HttpResponse)


  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val config = system.settings.config.getConfig("akka.kafka.consumer")
  val consumerSettings =ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("192.168.1.142:9092,192.168.1.143:9092,192.168.1.144:9092")
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")


  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val source = Consumer.plainSource(consumerSettings, Subscriptions.topics("ccp-1001"))

    val sink = Sink.foreach[ConsumerRecord[String, String]](r => println(s"[output] => ${r.key()} : ${r.value()}"))

    source ~> sink

    ClosedShape
  })

  graph.run()


}


