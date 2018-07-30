package net.albertogarrido.rickandmorty.network

import com.google.gson.annotations.SerializedName

data class CharactersResponse(
        @SerializedName("info") val info: ResponseInfo,
        @SerializedName("results") val results: List<RickAndMortyCharacter>
)

data class ResponseInfo(
        @SerializedName("count") val count: Int,
        @SerializedName("pages") val pages: Int,
        @SerializedName("next") val next: String,
        @SerializedName("prev") val prev: String
)

data class RickAndMortyCharacter constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("status") val status: String,
        @SerializedName("image") val avatar: String
)
