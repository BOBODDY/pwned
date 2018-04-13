package com.mathewsmobile.pwned.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.api.PwnedApi
import com.mathewsmobile.pwned.list.PwnListAdapter
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.*
import kotlinx.android.synthetic.main.activity_pwned.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PwnedActivity : AppCompatActivity() {

    lateinit var pwnAdapter: PwnListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwned)

        pwnAdapter = PwnListAdapter(this, emptyList())

        pwn_list.adapter = pwnAdapter

        pwn_check.setOnClickListener {
            checkForPwnage(it)
        }

        pwn_list.layoutManager = LinearLayoutManager(this)
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
                dialogInterface, _ ->
                dialogInterface.dismiss()
            })

            val dialog = alertBuilder.create()
            dialog.show()

            val editor = sharedPrefs.edit()
            editor.putBoolean(firstRunKey, false)
            editor.apply()
        }
    }

    fun checkForPwnage(view: View) {
        val account = account_entry.text.toString()

        val backgroundTask = PwnCheckTask()
        backgroundTask.execute(account)
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
}

