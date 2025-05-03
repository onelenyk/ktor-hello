package dev.onelenyk.ktorhello.app.routing

import io.ktor.server.routing.Routing

class ServerRouting() {
    private val utilRoutes = UtilRoutes()

    fun registerRoutes(routing: Routing) {
        utilRoutes.registerRoutes(routing)
    }
}
