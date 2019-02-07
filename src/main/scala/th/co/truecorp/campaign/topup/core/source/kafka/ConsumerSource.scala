package th.co.truecorp.campaign.topup.core.source.kafka

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import th.co.truecorp.campaign.topup.core.traits.KafkaConsumerSource

object ConsumerSource extends KafkaConsumerSource{
  def build(topic: String, groupID: String, bootstrapServers:String) (implicit system: ActorSystem): Source[ConsumerRecord[String, String], Consumer.Control] = {

    val config = system.settings.config.getConfig("akka.kafka.consumer")
    val consumerSettings = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withGroupId(groupID)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    Consumer.plainSource(consumerSettings, Subscriptions.topics(topic))
  }


  def build(topic:String) (implicit system: ActorSystem): Source[ConsumerRecord[String, String], Consumer.Control] = {

    val config = system.settings.config.getConfig("akka.kafka.consumer")
    val consumerSettings = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(config.getString("bootstrap-servers"))
      .withGroupId(config.getString("group-id"))
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    Consumer.plainSource(consumerSettings, Subscriptions.topics(topic))
  }
}
