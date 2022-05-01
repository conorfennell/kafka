package com.idiomcentric.topics

import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.admin.NewTopic
import java.util.Properties

object TopicsAdmin {
    fun create(topics: Iterable<String>) {
        for (topic in topics) {
            create(topic)
        }
    }
    fun create(name: String) {
        val admin = Admin.create(properties())
        admin.createTopics(listOf(topic(name)))
    }

    fun properties(): Properties = Properties().apply {
        put(BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092")
    }

    fun topic(name: String): NewTopic = NewTopic(name, 10, 1)
}
