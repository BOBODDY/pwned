package com.mathewsmobile.pwned.feature.pwned

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import com.mathewsmobile.pwned.shared.model.Breach
import com.mathewsmobile.pwned.constants.endpointUrl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PwnedViewModel: ViewModel() {
    private var breaches: MutableLiveData<List<Breach>>? = null
    private var selectedBreach: MutableLiveData<Breach>? = null

    fun getBreaches(): MutableLiveData<List<Breach>> {
        if (breaches == null) {
            breaches = MutableLiveData()
        }
        return breaches!!
    }

    fun getSelectedBreach(): MutableLiveData<Breach> {
        if (selectedBreach == null) {
            selectedBreach = MutableLiveData()
        }
        return selectedBreach!!
    }

    fun checkForPwn(email: String) {
        PwnCheckTask().execute(email)
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

            breaches?.value = result
        }
    }
}
