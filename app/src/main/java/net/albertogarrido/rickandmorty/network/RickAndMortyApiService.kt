package net.albertogarrido.rickandmorty.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApiService {

    companion object {
        const val E_CHARACTERS = "character/"
        const val E_SEARCH = "character/"
    }

    @GET(E_CHARACTERS)
    fun characters(@Query("page") page: Int): Call<CharactersResponse>

    @GET(E_SEARCH)
    fun search(@Query("name") term: String, @Query("page") page: Int): Call<CharactersResponse>
}