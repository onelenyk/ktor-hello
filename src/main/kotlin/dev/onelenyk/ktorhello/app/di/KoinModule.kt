package dev.onelenyk.ktorhello.app.di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import dev.onelenyk.ktorhello.app.routing.ServerRouting
import dev.onelenyk.ktorhello.data.db.MongoDBManager
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val koinModule =
    module {
        single { dotenv() }
        single { database(get()) }
        single { ServerRouting() }
        single { provideCoroutineScope() }
        single { provideDbCredentials(get()) }
        single { MongoDBManager(get()) }
    }

@OptIn(DelicateCoroutinesApi::class)
private fun provideCoroutineScope(): CoroutineScope {
    return GlobalScope
}

fun database(mongoDBManager: MongoDBManager): MongoDatabase {
    val database = mongoDBManager.getDatabase()
    return database
}

fun provideDbCredentials(dotenv: Dotenv): DbCredentials {
    val username = dotenv["DB_USERNAME"]
    val pass = dotenv["DB_PASSWORD"]
    val connection = dotenv["DB_CONNECTION"]
    return DbCredentials(username, pass, connection)
}

fun provideServerPort(dotenv: Dotenv): Int {
    val port = dotenv["PORT"].toIntOrNull()
    return port ?: 8080
}

data class DbCredentials(val username: String, val password: String, val connection: String)
