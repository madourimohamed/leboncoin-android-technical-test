package fr.leboncoin.core.network

import fr.leboncoin.core.network.model.NetworkAlbum
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

class AlbumApiTest {

    private val server = MockWebServer()
    private lateinit var api: AlbumApi

    @Before
    fun setUp() {
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(NetworkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    @After
    fun tearDown() = server.close()

    @Test
    fun `getAlbums parses the payload and ignores unknown fields`() = runTest {
        server.enqueue(
            MockResponse.Builder()
                .body(
                    """
                    [
                      {
                        "albumId": 1,
                        "id": 2,
                        "title": "reprehenderit est deserunt velit ipsam",
                        "url": "https://placehold.co/600x600/771796/white/png",
                        "thumbnailUrl": "https://placehold.co/150x150/771796/white/png",
                        "unexpectedField": true
                      }
                    ]
                    """.trimIndent(),
                )
                .build(),
        )

        val albums = api.getAlbums()

        assertEquals(
            listOf(
                NetworkAlbum(
                    id = 2,
                    albumId = 1,
                    title = "reprehenderit est deserunt velit ipsam",
                    url = "https://placehold.co/600x600/771796/white/png",
                    thumbnailUrl = "https://placehold.co/150x150/771796/white/png",
                ),
            ),
            albums,
        )
        assertEquals("/img/shared/technical-test.json", server.takeRequest().url.encodedPath)
    }

    @Test
    fun `getAlbums throws HttpException on server error`() = runTest {
        server.enqueue(MockResponse.Builder().code(500).build())

        val error = runCatching { api.getAlbums() }.exceptionOrNull()

        assertEquals(500, (error as HttpException).code())
    }
}
