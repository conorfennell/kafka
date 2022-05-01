package com.idiomcentric.com.idiomcentric.topologies

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsConfig
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

class ProducerWordTest {

    @Test
    fun producer() {
        val wordProducer = KafkaProducer<String, String>(producerProperties())
        val record = ProducerRecord<String, String>("words", null, "myword")
        println(wordProducer.send(record).get())
    }

    private fun producerProperties(): Properties = Properties().apply {
        put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        // Specify default (de)serializers for record keys and for record values.
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
    }

    @Test
    fun startMoreConsumers() {
        repeat(2) {
            thread {
                consumer()
            }
        }

        runBlocking {
            delay(1000000000)
        }
    }

    @Test
    fun startConsumers() {
        repeat(2) {
            thread {
                consumer()
            }
        }

        runBlocking {
            delay(1000000000)
        }
    }

    fun consumer() {
        val wordConsumer = KafkaConsumer<String, String>(consumerProperties())
        wordConsumer.subscribe(Pattern.compile("words"))

        repeat(100) {
            val records = wordConsumer.poll(Duration.ofSeconds(1L))
            records.mapNotNull { it.value() }.forEach(::println)
        }
    }

    private fun consumerProperties(): Properties = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.GROUP_ID_CONFIG, "word-yo")
        put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false)
        // Specify default (de)serializers for record keys and for record values.
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
    }
}
