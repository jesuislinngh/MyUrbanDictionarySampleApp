package com.fireguard.android.myurbandictionarysampleapp

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val API_URL_BASE = "https://mashape-community-urban-dictionary.p.rapidapi.com"

const val API_KEY_DICTIONARY = "db50d29118msh4172e370618d643p1294b3jsnc9607fc9cec0"

// JSON parser object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit object builder
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(API_URL_BASE)
    .build()

// Dictionary api definition
interface DictionaryService {

    @Headers(
        "x-rapidapi-host:mashape-community-urban-dictionary.p.rapidapi.com",
        "x-rapidapi-key:${API_KEY_DICTIONARY}"
    )
    @GET("define")
    fun getTermAsync(@Query("term") term: String): Deferred<DefinitionList>
}

// Dictionary API object
object DictionaryApi {
    val retrofitService: DictionaryService by lazy {
        retrofit.create(DictionaryService::class.java)
    }
}