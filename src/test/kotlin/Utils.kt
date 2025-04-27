import dev.onelenyk.ktorhello.app.Server
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder

fun ApplicationTestBuilder.setupTestApplication(server: Server) {
    environment {
        config =
            MapApplicationConfig(
                "ktor.deployment.port" to 8080.toString(),
            )
    }
    application {
        server.module(this)
    }
}

class GeneralRoutesApiClient(private val client: HttpClient, private val baseUrl: String) {
    suspend fun checkLive(): HttpResponse {
        return client.get("$baseUrl/live")
    }

    suspend fun getRoutes(): HttpResponse {
        return client.get("$baseUrl/routes")
    }

    suspend fun getDoc(): HttpResponse {
        return client.get("$baseUrl/doc")
    }

    suspend fun getRoot(): HttpResponse {
        return client.get(baseUrl)
    }
}

class ModelRoutesApiClient(private val client: HttpClient, private val baseUrl: String) {
    suspend fun createModel(
        modelName: String,
        modelJson: String,
    ): HttpResponse {
        return client.post("$baseUrl/models?name=$modelName") {
            contentType(ContentType.Application.Json)
            setBody(modelJson)
        }
    }

    suspend fun getModels(): HttpResponse {
        return client.get("$baseUrl/models")
    }

    suspend fun getModelById(id: String): HttpResponse {
        return client.get("$baseUrl/models/$id")
    }

    suspend fun updateModel(
        id: String,
        updates: String,
    ): HttpResponse {
        return client.put("$baseUrl/models/$id") {
            contentType(ContentType.Application.Json)
            setBody(updates)
        }
    }

    suspend fun deleteModel(id: String): HttpResponse {
        return client.delete("$baseUrl/models/$id")
    }
}

class DynamicRoutesApiClient(private val client: HttpClient, private val baseUrl: String) {
    val dynamic = "$baseUrl/dynamic"

    suspend fun createDynamicDocument(
        modelName: String,
        documentJson: String,
    ): HttpResponse {
        return client.post("$dynamic/$modelName") {
            contentType(ContentType.Application.Json)
            setBody(documentJson)
        }
    }

    suspend fun getAllDynamicDocuments(modelName: String): HttpResponse {
        return client.get("$dynamic/$modelName")
    }

    suspend fun getDynamicDocumentById(
        modelName: String,
        id: String,
    ): HttpResponse {
        return client.get("$dynamic/$modelName/$id")
    }

    suspend fun updateDynamicDocument(
        modelName: String,
        id: String,
        updates: String,
    ): HttpResponse {
        return client.put("$dynamic/$modelName/$id") {
            contentType(ContentType.Application.Json)
            setBody(updates)
        }
    }

    suspend fun deleteDynamicDocument(
        modelName: String,
        id: String,
    ): HttpResponse {
        return client.delete("$dynamic/$modelName/$id")
    }
}
