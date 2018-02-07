package com.boboddy.pwned

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.boboddy.pwned.api.IPwnedApi
import com.boboddy.pwned.list.PwnListAdapter
import com.boboddy.pwned.model.Breach
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PwnedActivity : AppCompatActivity() {

    @BindView(R.id.account_entry)
    lateinit var accountEntry: EditText

    @BindView(R.id.pwn_check)
    lateinit var checkButton: Button

    @BindView(R.id.pwned_result)
    lateinit var pwnedStatus: TextView

    @BindView(R.id.pwn_list)
    lateinit var pwnedList: ListView
    lateinit var pwnAdapter: PwnListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwned)

        ButterKnife.bind(this)

        pwnAdapter = PwnListAdapter(this)

        pwnedList.adapter = pwnAdapter
    }

    @OnClick(R.id.pwn_check)
    fun checkForPwnage(view: View) {
        val account = accountEntry.text.toString()

        val backgroundTask = PwnCheckTask()
        backgroundTask.execute(account)
    }

    inner class PwnCheckTask: AsyncTask<String, Void, List<Breach>>() {

        private val pwnedApi: IPwnedApi

        init {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://haveibeenpwned.com/api/v2/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            pwnedApi = retrofit.create(IPwnedApi::class.java)
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
                pwnedStatus.text = "PWNED"

                pwnAdapter.dataSet = result
                pwnAdapter.notifyDataSetChanged()
                pwnedList.visibility = View.VISIBLE
            } else {
                pwnedStatus.text = "SAFE"
                pwnedList.visibility = View.GONE
            }
        }
    }
}

