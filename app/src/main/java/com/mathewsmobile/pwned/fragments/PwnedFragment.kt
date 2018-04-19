package com.mathewsmobile.pwned.fragments

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.api.PwnedApi
import com.mathewsmobile.pwned.list.PwnListAdapter
import com.mathewsmobile.pwned.list.PwnListHolder
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import com.mathewsmobile.pwned.util.endpointUrl
import com.mathewsmobile.pwned.util.pwnedText
import com.mathewsmobile.pwned.util.safeText
import kotlinx.android.synthetic.main.activity_pwned.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PwnedFragment : Fragment(), PwnListHolder.OnActionCompleted {

    lateinit var detailHandler: PwnedFragment.DetailNavigator

    lateinit var pwnAdapter: PwnListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_pwned, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.actionBar?.hide()

        pwnAdapter = PwnListAdapter(this.requireContext(), emptyList(), this)
        pwn_list.adapter = pwnAdapter

        pwn_check.setOnClickListener {
            checkForPwnage()
        }

        if (context != null) {
            pwn_list.layoutManager = LinearLayoutManager(context)
        }
    }

    fun checkForPwnage() {
        val account = account_entry.text.toString()

        val backgroundTask = PwnCheckTask()
        backgroundTask.execute(account)
    }

    override fun onClick(breach: Breach) {
        detailHandler.viewDetails(breach)
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

            if (result != null && result.isNotEmpty()) {
                pwned_result.text = pwnedText
                pwned_result.setTextColor(resources.getColor(R.color.pwnedColor))

                pwnAdapter.data = result
                pwnAdapter.notifyDataSetChanged()
                pwn_list.visibility = View.VISIBLE
            } else {
                pwned_result.text = safeText
                pwned_result.setTextColor(resources.getColor(R.color.safeColor))
                pwn_list.visibility = View.GONE
            }
        }
    }

    interface DetailNavigator { fun viewDetails(breach: Breach)}
}