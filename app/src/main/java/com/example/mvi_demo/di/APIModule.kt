package com.example.mvi_demo.di

import com.example.mvi_demo.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object APIModule {

    private const val BASE_URL = "https://5e510330f2c0d300147c034c.mockapi.io/"

    @Provides
    @ActivityScoped
    fun getRetrofit(): ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(ApiService::class.java)
}