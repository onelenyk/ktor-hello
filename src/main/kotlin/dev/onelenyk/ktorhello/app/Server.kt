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
    private val logger = org.slf4j.LoggerFactory.getLogger(Server::class.java)

    fun start(): NettyApplicationEngine {
        logger.info("Starting Ktor server initialization...")
        val port = System.getenv("PORT")?.toInt() ?: provideServerPort(dotenv = dotenv()) ?: 8080
        logger.info("Configuring server on port: $port")
        
        val server = embeddedServer(Netty, port = port) {
            module(this)
        }
        
        try {
            logger.info("Starting server...")
            server.start(wait = true)
            logger.info("Server successfully started on port: $port")
        } catch (e: Exception) {
            logger.error("Failed to start server: ${e.message}", e)
            throw e
        }
        return server
    }

    fun module(application: Application) =
        application.apply {
            logger.info("Configuring application modules...")
            
            logger.info("Installing Koin dependency injection...")
            install(Koin) {
                logger
                modules(koinModule)
            }
            
            logger.info("Setting up request logging...")
            install(CallLogging)
            
            logger.info("Configuring request validation...")
            install(RequestValidation)
            
            logger.info("Setting up content serialization...")
            configureSerialization()

            logger.info("Configuring routing...")
            configureRouting()
            
            logger.info("Application modules configuration completed")
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
