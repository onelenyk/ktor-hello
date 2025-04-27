package dev.onelenyk.ktorhello.app

import dev.onelenyk.ktorhello.app.di.koinModule
import dev.onelenyk.ktorhello.app.di.provideServerPort
import dev.onelenyk.ktorhello.app.routing.ServerRouting
import io.github.cdimascio.dotenv.dotenv
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

class Server {
    fun start(): NettyApplicationEngine {
        val server =
            embeddedServer(
                Netty,
                port = provideServerPort(dotenv = dotenv()) ?: 8080,
            ) {
                module(this)
            }
        server.start(wait = true)
        return server
    }

    fun module(application: Application) =
        application.apply {
            install(Koin) {
                slf4jLogger()
                modules(koinModule)
            }
            install(CallLogging)
            install(RequestValidation)
            configureSerialization()

            configureRouting()
        }

    private fun Application.configureSerialization() =
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }

    private fun Application.configureRouting() {
        val router: ServerRouting by inject()

        routing {
            router.registerRoutes(this)
        }
    }
}
