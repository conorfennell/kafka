package com.idiomcentric.topologies

import com.idiomcentric.topics.TopicsAdmin
import com.idiomcentric.topologies.StringJoin.Companion.Topics.words
import com.idiomcentric.topologies.StringJoin.Companion.Topics.wordsContainNumber
import com.idiomcentric.topologies.StringJoin.Companion.Topics.wordsLength
import com.idiomcentric.topologies.StringJoin.Companion.Topics.wordsMerged
import com.idiomcentric.topologies.StringJoin.Companion.createTopics
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.kstream.KStream
import java.time.Duration
import java.util.Properties

class StringJoin {
    companion object {
        object Topics {
            val words = "words"
            val wordsLength = "words_length"
            val wordsContainNumber = "words_contain_number"
            val wordsMerged = "words_merged"
        }
        fun createTopics() {
            TopicsAdmin.create(listOf(words, wordsLength, wordsContainNumber, wordsMerged))
        }

        fun properties(): Properties = Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "stringjoin-lambda-example")
            put(StreamsConfig.CLIENT_ID_CONFIG, "stringkoin-client-id")
            // Where to find Kafka broker(s).
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            // Specify default (de)serializers for record keys and for record values.
            put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()::class.java.name)
            put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()::class.java.name)
            // Records should be flushed every 10 seconds. This is less than the default
            // in order to keep this example interactive.
            put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000)
            // For illustrative purposes we disable record caches.
            put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0)
            // Use a temporary directory for storing state, which will be automatically removed after the test.
//    put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath())
        }
    }
}

fun main() {
    createTopics()
    val stream = StreamsBuilder()
    val words: KStream<String, String> = stream.stream<String, String>(Topics.words)

    val wordsLength: KStream<String, String> = words
        .mapValues { word -> word.length.toString() }

    val containsNumber: KStream<String, String> = words
        .mapValues { word -> word.contains("\\d".toRegex()).toString() }

    wordsLength.join(
        containsNumber,
        { length: String, hasNumber: String -> length + hasNumber },
        JoinWindows.of(Duration.ofMinutes(120L))
    ).to(wordsMerged)

    val topology = stream.build()

    val kafkaStreams = KafkaStreams(topology, properties())
    kafkaStreams.start()

    Runtime
        .getRuntime()
        .addShutdownHook(Thread(kafkaStreams::close))

    Thread.sleep(1000000000000)
}
