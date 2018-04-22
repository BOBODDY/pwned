package com.mathewsmobile.pwned.shared.model

import com.mathewsmobile.pwned.constants.imageUrl
import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Created by nicke on 2/4/2018.
 */

data class Breach(
        @Json(name = "Title") val title: String,
        @Json(name = "Name") val name: String,
        @Json(name = "Domain") val domain: String,
        @Json(name = "BreachDate") val breachDate: String,
        @Json(name = "PwnCount") val pwnCount: Int,
        @Json(name = "Description") val description: String,
        @Json(name = "DataClasses") val dataClasses: List<String>,
        @Json(name = "IsVerified") val isVerified: Boolean,
        @Json(name = "IsFabricated") val isFabricated: Boolean,
        @Json(name = "IsSensitive") val isSensitive: Boolean,
        @Json(name = "IsActive") val isActive: Boolean,
        @Json(name = "IsSpamList") val isSpamList: Boolean,
        @Json(name = "LogoType") val logoType: String) : Serializable {

    fun getLogoUrl (): String {
        return "${imageUrl}$name.$logoType"
    }
}