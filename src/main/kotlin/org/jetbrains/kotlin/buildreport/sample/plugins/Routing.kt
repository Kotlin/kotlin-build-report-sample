package org.jetbrains.kotlin.buildreport.sample.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

// List of fields could be found here:
// https://github.com/JetBrains/kotlin/blob/master/libraries/tools/kotlin-gradle-plugin/src/common/kotlin/org/jetbrains/kotlin/gradle/plugin/statistics/CompileStatisticsData.kt#L15
@Serializable
data class CompileStatisticsData(
    val taskName: String?,
    val durationMs: Long,
    val tags: List<String>,
    val nonIncrementalAttributes: List<String>
)

val outputFile = File("statistics.csv")

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

fun Application.configureRouting() {

    routing {
        post("/") {
            val customer = call.receive<CompileStatisticsData>()
            val buildDescription = if (customer.tags.contains("NON_INCREMENTAL"))
                customer.nonIncrementalAttributes.joinToString(";")
            else
                "Incremental build"
                outputFile.appendText("${customer.taskName},${customer.durationMs},$buildDescription\r\n")
            call.respondText("OK", status = HttpStatusCode.OK)
        }
    }

}
