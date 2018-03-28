package com.mathewsmobile.pwned.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import butterknife.*
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.api.IPwnedApi
import com.mathewsmobile.pwned.list.PwnListAdapter
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.*
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

    override fun onResume() {
        super.onResume()

        showInfoAlertOnFirstRun()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_info) {
            val infoIntent = Intent(this, InfoActivity::class.java)
            startActivity(infoIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun showInfoAlertOnFirstRun(){
        val sharedPrefs = getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)

        val firstRun = sharedPrefs.getBoolean(firstRunKey, true)

        if (firstRun) {
            val alertBuilder = AlertDialog.Builder(this)

            alertBuilder.setTitle(R.string.first_run_title)
            alertBuilder.setMessage(R.string.first_run_info)

            alertBuilder.setPositiveButton(R.string.ok, {
                dialogInterface, i ->
                dialogInterface.dismiss()
            })

            val dialog = alertBuilder.create()
            dialog.show()

            val editor = sharedPrefs.edit()
            editor.putBoolean(firstRunKey, false)
            editor.apply()
        }
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

