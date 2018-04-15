package com.mathewsmobile.pwned.viewmodels

import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import android.view.View
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.api.PwnedApi
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.endpointUrl
import com.mathewsmobile.pwned.util.pwnedText
import com.mathewsmobile.pwned.util.safeText
import kotlinx.android.synthetic.main.activity_pwned.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PwnedViewModel: ViewModel() {
    private var userEmail = String()
    fun getEmail(): String {
        return userEmail
    }

    var breaches = emptyList<Breach>()

    fun checkForPwn(email: String) {
        val backgroundTask = PwnCheckTask()
        backgroundTask.execute(email)
    }

    inner class PwnCheckTask: AsyncTask<String, Void, List<Breach>>() {

        private val pwnedApi: PwnedApi

        init {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endpointUrl)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            pwnedApi = retrofit.create(PwnedApi::class.java)
        }

        override fun doInBackground(vararg p0: String?): List<Breach>? {
            val account = p0[0]

            var response: Response<List<Breach>>? = null
            if (account != null) {

                try {
                    response = pwnedApi.getBreachesForAccount(account).execute()
                } catch (e: Exception) {
                    print(e.localizedMessage)
                }

                if (response != null && response.isSuccessful) {
                    return response.body()
                }
            }

            return response?.body()
        }

        override fun onPostExecute(result: List<Breach>?) {
            super.onPostExecute(result)

//            if (result != null && result.isNotEmpty()) {
//                pwned_result.text = pwnedText
//                pwned_result.setTextColor(resources.getColor(R.color.pwnedColor))
//
//                pwnAdapter.data = result
//                pwnAdapter.notifyDataSetChanged()
//                pwn_list.visibility = View.VISIBLE
//            } else {
//                pwned_result.text = safeText
//                pwned_result.setTextColor(resources.getColor(R.color.safeColor))
//                pwn_list.visibility = View.GONE
//            }
        }
    }
}
