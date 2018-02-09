package com.boboddy.pwned.activities

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import butterknife.*
import com.boboddy.pwned.R
import com.boboddy.pwned.api.IPwnedApi
import com.boboddy.pwned.list.PwnListAdapter
import com.boboddy.pwned.model.Breach
import com.boboddy.pwned.util.breachKey
import com.boboddy.pwned.util.endpointUrl
import com.boboddy.pwned.util.pwnedText
import com.boboddy.pwned.util.safeText
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

    @OnItemClick(R.id.pwn_list)
    fun viewPwnage(position: Int) {
        val breach = pwnAdapter.dataSet[position]

        val intent = Intent(this, PwnDetailActivity::class.java)
        intent.putExtra(breachKey, breach)
        startActivity(intent)
    }

    inner class PwnCheckTask: AsyncTask<String, Void, List<Breach>>() {

        private val pwnedApi: IPwnedApi

        init {
            val retrofit = Retrofit.Builder()
                    .baseUrl(endpointUrl)
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
                pwnedStatus.text = pwnedText
                pwnedStatus.setTextColor(resources.getColor(R.color.pwnedColor))

                pwnAdapter.dataSet = result
                pwnAdapter.notifyDataSetChanged()
                pwnedList.visibility = View.VISIBLE
            } else {
                pwnedStatus.text = safeText
                pwnedStatus.setTextColor(resources.getColor(R.color.safeColor))
                pwnedList.visibility = View.GONE
            }
        }
    }
}

