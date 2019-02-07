package th.co.truecorp.campaign.topup.core.traits

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.ConsumerRecord

trait KafkaConsumerSource {

  def build(topic: String, groupID: String, bootstrapServers:String) (implicit system: ActorSystem): Source[ConsumerRecord[String, String], Consumer.Control]

  // Get from config
  def build(topic:String) (implicit system: ActorSystem): Source[ConsumerRecord[String, String], Consumer.Control]


}