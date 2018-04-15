package com.mathewsmobile.pwned.activities

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import com.mathewsmobile.pwned.viewmodels.PwnedViewModel
import kotlinx.android.synthetic.main.activity_pwned.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PwnedActivity : AppCompatActivity() {

    lateinit var pwnAdapter: PwnListAdapter

    lateinit var viewModel: PwnedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwned)

        viewModel = ViewModelProviders.of(this).get(PwnedViewModel::class.java)

        pwnAdapter = PwnListAdapter(this, viewModel.breaches)

        pwn_list.adapter = pwnAdapter

        pwn_check.setOnClickListener {
            viewModel.checkForPwn( account_entry.text.toString())
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
}

