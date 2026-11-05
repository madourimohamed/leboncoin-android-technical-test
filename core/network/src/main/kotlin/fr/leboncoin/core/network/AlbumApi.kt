package fr.leboncoin.core.network

import fr.leboncoin.core.network.model.NetworkAlbum
import retrofit2.http.GET

interface AlbumApi {

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<NetworkAlbum>
}
