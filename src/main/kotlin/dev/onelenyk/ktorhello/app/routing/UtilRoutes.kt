package dev.onelenyk.ktorhello.app.routing

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.RootRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

class UtilRoutes() {
    fun registerRoutes(routing: Routing) {
        routing.get("/routes") {
            val routesList = routing.getAllRoutes()
            call.respond(routesList)
        }
        routing.get("/live") {
            call.respond(HttpStatusCode.OK)
        }
        routing.get("/hello") {
            call.respondText("Hello, Ktor!", ContentType.Text.Plain)
        }
        routing.staticResources("/", "dokka")
    }

    private fun Route.getAllRoutes(): List<String> {
        val routesList = mutableListOf<String>()
        this.children.forEach { route ->
            route.toRouteList(routesList)
        }
        return routesList
    }

    private fun Route.toRouteList(
        routesList: MutableList<String>,
        parentPath: String = "",
    ) {
        val currentPath =
            if (this.selector is RootRouteSelector) {
                parentPath
            } else {
                val segment = this.selector.toString()
                if (parentPath.isEmpty()) segment else "$parentPath/$segment"
            }

        if (this.children.isEmpty()) {
            routesList.add(currentPath)
        } else {
            this.children.forEach { child ->
                child.toRouteList(routesList, currentPath)
            }
        }
    }
}
