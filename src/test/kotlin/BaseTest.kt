import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import org.junit.AfterClass
import org.junit.BeforeClass

open class BaseTest {
    protected lateinit var generalApiClient: GeneralRoutesApiClient
    protected lateinit var modelRoutesApiClient: ModelRoutesApiClient
    protected lateinit var dynamicRoutesApiClient: DynamicRoutesApiClient

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            // Global setup if necessary
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            // Global teardown if necessary
        }
    }

    protected fun setupApiClients(port: Int = 8080) {
        val client =
            HttpClient(CIO) {
                followRedirects = false
                install(Logging)
            }

        val baseUrl = "http://localhost:$port"
        generalApiClient = GeneralRoutesApiClient(client, baseUrl)
        modelRoutesApiClient = ModelRoutesApiClient(client, baseUrl)
        dynamicRoutesApiClient = DynamicRoutesApiClient(client, baseUrl)
    }
}
