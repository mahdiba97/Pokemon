package com.mahdiba97.pokemon.ui.pokemonList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.mahdiba97.pokemon.POKEMON_DETAIL_SCREEN
import com.mahdiba97.pokemon.data.models.PokemonListEntry

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pokemon", color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                modifier = Modifier.align(CenterHorizontally),
                fontWeight = FontWeight.ExtraBold
            )
            Searchbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                hint = "Search..."
            ) {
                viewModel.searchPokemon(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonRecycler(navController = navController)
        }
    }
}

@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    hint: String, onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(true)
    }

    var itClose by remember {
        mutableStateOf(false)
    }
    val focusRequest = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    Box(modifier = modifier) {
        BasicTextField(
            value = text, onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1, singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused == false && text.isEmpty()
                    itClose = isHintDisplayed == false

                }
                .focusRequester(focusRequest)
        )
        if (isHintDisplayed) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = hint,
                    color = Color.LightGray,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else if (itClose) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .align(CenterEnd)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        itClose = false
                        isHintDisplayed = true
                        text = ""
                        onSearch("")
                        focusManager.clearFocus(true)
                    }
            )
        }
    }
}

@Composable
fun PokemonRecycler(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val endReach by remember { viewModel.endReached }
    val isSearching by remember { viewModel.isSearching }
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else pokemonList.size / 2 + 1
        items(itemCount) {
            if (it >= itemCount - 1 && !endReach && !isLoading && !isSearching) {
                viewModel.loadPokemonPaginated()
            }
            PokemonRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
        if (isLoading) CircularProgressIndicator(color = MaterialTheme.colors.primary)
        if (loadError.isNotEmpty()) RetrySection(error = loadError) {
            viewModel.loadPokemonPaginated()
        }
    }
}

@Composable
fun PokemonEntry(
    entry: PokemonListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(contentAlignment = Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .width(180.dp)
            .aspectRatio(1f)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "$POKEMON_DETAIL_SCREEN/${dominantColor.toArgb()}/${entry.name}"
                )
            }) {
        Column {
//            CoilImage(
//                request = ImageRequest.Builder(LocalContext.current).data(entry.imageUrl)
//               .target { it ->
//                    viewModel.calculateDominantColor(it) { dominantColor = it }
//                }.build(),
//                fadeIn = true,
//                contentDescription = entry.name,
//                modifier = Modifier
//                    .size(120.dp)
//                    .align(CenterHorizontally)
//            ) {
//                CircularProgressIndicator(
//                    color = MaterialTheme.colors.primary,
//                    modifier = Modifier.scale(0.5f)
//                )
//            }
            val context = LocalContext.current
            var visibility by remember {
                mutableStateOf(true)
            }
            val loader = ImageLoader(context)
            val req = ImageRequest.Builder(context)
                .data(entry.imageUrl)
                .target(onSuccess = { it ->
                    viewModel.calculateDominantColor(it) {
                        dominantColor = it
                        visibility = false
                    }
                }, onStart = {
                    if (visibility)
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .scale(0.5f)
                                .align(CenterHorizontally)
                                .fillMaxSize()
                        )
                }).build()

            loader.enqueue(req)

            Image(
                painter = rememberImagePainter(
                    data = entry.imageUrl,
                    builder = {
                        transformations(CircleCropTransformation())

                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = entry.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Composable
fun PokemonRow(
    rowIndex: Int,
    entries: List<PokemonListEntry>,
    navController: NavController
) {
    Column {
        Row {
            PokemonEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokemonEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
            } else Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column() {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { onRetry() }, modifier = Modifier.align(CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}