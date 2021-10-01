package com.mahdiba97.pokemon.data.remote

import com.mahdiba97.pokemon.data.remote.responses.Pokemon
import com.mahdiba97.pokemon.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,  //assign page size ,how many pokemon we load at once
        @Query("offset") offset: Int //from which pokemon we want to start
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon
}