package com.mahdiba97.pokemon.ui.pokemonDetail

import androidx.lifecycle.ViewModel
import com.mahdiba97.pokemon.data.remote.responses.Pokemon
import com.mahdiba97.pokemon.repository.PokemonRepository
import com.mahdiba97.pokemon.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }
}