package dev.onelenyk.ktorhello.data.db

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import dev.onelenyk.ktorhello.app.di.DbCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.UuidRepresentation

class MongoDBManager(dbCredentials: DbCredentials) {
    private val connectionString =
        "mongodb+srv://${dbCredentials.username}:${dbCredentials.password}${dbCredentials.connection}"

    private val serverApi = ServerApi.builder().version(ServerApiVersion.V1).build()
    private val mongoClientSettings =
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD) // Specify the desired UUID representation
            .applyConnectionString(ConnectionString(connectionString)).serverApi(serverApi).build()

    private val mongoClient = MongoClient.create(mongoClientSettings)

    private val database: MongoDatabase = mongoClient.getDatabase("Cluster0")

    fun <T : Any> getCollection(
        collectionName: String,
        clazz: Class<T>,
    ): MongoCollection<T> {
        return database.getCollection(collectionName, clazz)
    }

    fun getDatabase(): MongoDatabase {
        return database
    }

    suspend fun pingDatabase(): String {
        return withContext(Dispatchers.IO) {
            val pingResult = database.runCommand(Document("ping", 1))
            pingResult.toJson()
        }
    }

    fun close() {
        mongoClient.close()
    }
}
