package com.mathewsmobile.pwned.viewmodels

import android.arch.lifecycle.ViewModel

class PwnedViewModel: ViewModel() {
    private var userEmail = String()

    fun getEmail(): String {
        return userEmail
    }

    fun checkForPwn(email: String) {

    }
}
