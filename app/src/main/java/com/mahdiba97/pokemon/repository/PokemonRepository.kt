package com.mahdiba97.pokemon.repository

import com.mahdiba97.pokemon.data.remote.PokeService
import com.mahdiba97.pokemon.data.remote.responses.Pokemon
import com.mahdiba97.pokemon.data.remote.responses.PokemonList
import com.mahdiba97.pokemon.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val pokeService: PokeService
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            pokeService.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred!")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            pokeService.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred!")
        }
        return Resource.Success(response)
    }
}