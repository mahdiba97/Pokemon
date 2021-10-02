package com.mahdiba97.pokemon.di

import com.mahdiba97.pokemon.BASE_URL
import com.mahdiba97.pokemon.data.remote.PokeService
import com.mahdiba97.pokemon.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(service: PokeService) = PokemonRepository(service)

    @Singleton
    @Provides
    fun providePokeService(): PokeService {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(PokeService::class.java)
    }
}