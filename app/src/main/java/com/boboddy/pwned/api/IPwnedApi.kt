package com.boboddy.pwned.api

import com.boboddy.pwned.model.Breach
import com.boboddy.pwned.model.BreachesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by nicke on 2/4/2018.
 */

interface IPwnedApi {
    @Headers("User-Agent: Pwned-Android-app")
    @GET("breachedaccount/{account}")
    fun getBreachesForAccount(@Path("account") account: String): Call<List<Breach>>
}