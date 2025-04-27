import io.ktor.http.HttpStatusCode
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class GeneralRoutesTest : BaseTest() {
    @BeforeTest
    fun setup() {
        setupApiClients(port = 8080)
    }

    @Test
    fun testLiveRoute() =
        runBlocking {
            val response = generalApiClient.checkLive()
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun testRoutesRoute() =
        runBlocking {
            val response = generalApiClient.getRoutes()
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun testRootRoute() =
        runBlocking {
            val response = generalApiClient.getRoot()
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
        }
}
