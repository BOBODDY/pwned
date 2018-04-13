package com.mathewsmobile.pwned.model

import com.mathewsmobile.pwned.util.imageUrl
import java.io.Serializable

/**
 * Created by nicke on 2/4/2018.
 */

data class Breach(
        val Title: String,
        val Name: String,
        val Domain: String,
        val BreachDate: String,
        val PwnCount: Int,
        val Description: String,
        val DataClasses: List<String>,
        val IsVerified: Boolean,
        val IsFabricated: Boolean,
        val IsSensitive: Boolean,
        val IsActive: Boolean,
        val IsSpamList: Boolean,
        val LogoType: String) : Serializable {

    fun getLogoUrl (): String {
        return "$imageUrl$Name.$LogoType"
    }
}