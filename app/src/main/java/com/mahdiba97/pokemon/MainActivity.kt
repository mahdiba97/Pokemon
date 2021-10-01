package com.mahdiba97.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahdiba97.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = POKEMON_LIST_SCREEN) {
                    composable(POKEMON_LIST_SCREEN) {

                    }
                    composable("$POKEMON_DETAIL_SCREEN/{$ARG_DOMINANT_COLOR}/{$ARG_POKEMON_NAME}",
                        arguments = listOf(
                            navArgument(ARG_DOMINANT_COLOR) {
                                type = NavType.IntType
                            },
                            navArgument(ARG_POKEMON_NAME) {
                                type = NavType.StringType
                            }
                        )) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt(ARG_DOMINANT_COLOR)
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString(ARG_POKEMON_NAME)
                        }


                    }

                }
            }
        }
    }
}
