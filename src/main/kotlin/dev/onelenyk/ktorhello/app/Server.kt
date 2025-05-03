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
import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger

class Server {
    val log = logger(TRACE)

    fun start(): NettyApplicationEngine {
        log.config { "Starting Ktor server initialization..." }
        val port = System.getenv("PORT")?.toInt() ?: provideServerPort(dotenv = dotenv()) ?: 8080
        log.info { "Configuring server on port: $port" }

        val server =
            embeddedServer(Netty, port = port) {
                module(this)
            }

        try {
            log.severe { "Starting server..." }
            server.start(wait = true)
            log.severe { "Server successfully started on port: $port" }
        } catch (e: Exception) {
            log.warning { "Failed to start server: ${e.message}" }
            throw e
        }
        return server
    }

    fun module(application: Application) =
        application.apply {
            log.fine { "Configuring application modules..." }

            log.fine { "Installing Koin dependency injection..." }
            install(Koin) {
                log
                modules(koinModule)
            }

            log.fine { "Setting up request logging..." }
            install(CallLogging)

            log.fine { "Configuring request validation..." }
            install(RequestValidation)

            log.finest { "Setting up content serialization..." }
            configureSerialization()

            log.finest { "Configuring routing..." }
            configureRouting()

            log.finest { "Application modules configuration completed" }
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
