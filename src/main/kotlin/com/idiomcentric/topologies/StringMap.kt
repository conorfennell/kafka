package com.idiomcentric.topologies

import com.idiomcentric.topics.TopicsAdmin
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG
import org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.streams.StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG
import org.apache.kafka.streams.StreamsConfig.CLIENT_ID_CONFIG
import org.apache.kafka.streams.StreamsConfig.COMMIT_INTERVAL_MS_CONFIG
import org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG
import org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG
import org.apache.kafka.streams.kstream.KStream
import java.util.Properties

object Topics {
    val words = "words"
    val wordsLength = "words_length"
}


fun stringMap() {
    TopicsAdmin.create(Topics.words)
    TopicsAdmin.create(Topics.wordsLength)

    val stream = StreamsBuilder()
    val words: KStream<String, String> = stream.stream<String, String>(Topics.words)

    words
        .mapValues { word -> word.length.toString() }
        .to(Topics.wordsLength)


    val topology = stream.build()

    val kafkaStreams = KafkaStreams(topology, properties())
    kafkaStreams.start()

    Runtime
        .getRuntime()
        .addShutdownHook(Thread(kafkaStreams::close))

    Thread.sleep(1000000000000)
}

fun properties(): Properties = Properties().apply {
    put(APPLICATION_ID_CONFIG, "wordcount-lambda-example")
    put(CLIENT_ID_CONFIG, "wordcount-client-id")
    // Where to find Kafka broker(s).
    put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    // Specify default (de)serializers for record keys and for record values.
    put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()::class.java.name)
    put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()::class.java.name)
    // Records should be flushed every 10 seconds. This is less than the default
    // in order to keep this example interactive.
    put(COMMIT_INTERVAL_MS_CONFIG, 10 * 1000)
    // For illustrative purposes we disable record caches.
    put(CACHE_MAX_BYTES_BUFFERING_CONFIG, 0)
    // Use a temporary directory for storing state, which will be automatically removed after the test.
//    put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath())
    }
