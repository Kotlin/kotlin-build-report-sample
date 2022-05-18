package org.jetbrains.kotlin.buildreport.sample

import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import org.jetbrains.kotlin.buildreport.sample.plugins.configureRouting
import org.jetbrains.kotlin.buildreport.sample.plugins.configureSerialization

fun main() {
    embeddedServer(Jetty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
