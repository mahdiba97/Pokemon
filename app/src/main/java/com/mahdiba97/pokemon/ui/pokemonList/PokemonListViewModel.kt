package com.mahdiba97.pokemon.ui.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.mahdiba97.pokemon.PAGE_SIZE
import com.mahdiba97.pokemon.data.models.PokemonListEntry
import com.mahdiba97.pokemon.repository.PokemonRepository
import com.mahdiba97.pokemon.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0
    var pokemonList = mutableStateOf<List<PokemonListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }


    fun loadPokemonPaginated() {
        viewModelScope.launch {
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    val entries = result.data.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/pokeapi/sprites/master/sprites/pokemon/${number}.png"
//                        Log.i("MyTag", url)
                        PokemonListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    currentPage++
                    isLoading.value = false
                    loadError.value = ""
                    pokemonList.value += entries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

            }
        }
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate {
            it?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}