package com.mathewsmobile.pwned.api

import com.mathewsmobile.pwned.model.Breach
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by nicke on 2/4/2018.
 */

interface PwnedApi {
    @Headers("User-Agent: Pwned-Android-app")
    @GET("breachedaccount/{account}")
    fun getBreachesForAccount(@Path("account") account: String): Call<List<Breach>>
}