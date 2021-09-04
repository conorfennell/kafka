plugins {
    kotlin("jvm") version "1.5.21"
}

group = "com.idiomcentric"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

object V {
    const val guice = "5.0.1"
    const val guava = "30.1-jre"
    const val kotlin = "1.5.20"
    const val ktor = "1.6.1"
    const val jackson = "2.12.4"
    const val exposed = "0.32.1"
    const val hikaricp = "4.0.3"
    const val postgres = "42.2.2"

    const val kotlinLogging = "2.0.8"
    const val logback = "0.1.5"
    const val logbackClassic = "1.2.3"

    const val junit = "5.7.2"
    const val kotest = "4.3.0"
    const val h2 = "1.4.199"

    const val kafka = "6.2.0-ccs"
}

dependencies {
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
    implementation("ch.qos.logback:logback-classic:${V.logbackClassic}")
    implementation("ch.qos.logback.contrib:logback-json-classic:${V.logback}")
    implementation("ch.qos.logback.contrib:logback-jackson:${V.logback}")
    implementation("org.apache.kafka:kafka-streams:${V.kafka}")
    implementation("org.apache.kafka:kafka-clients:${V.kafka}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    testImplementation("org.junit.platform:junit-platform-launcher:1.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.7.2")
    testImplementation("org.testcontainers:testcontainers:1.16.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget =  "1.8"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget =  "1.8"
        }
    }

    test {
        useJUnitPlatform()
    }
}

