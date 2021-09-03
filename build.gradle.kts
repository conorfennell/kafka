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

dependencies {
    implementation("org.apache.kafka:kafka-streams:6.2.0-ccs")
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

