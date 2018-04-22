package com.mathewsmobile.pwned.feature.pwned

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Menu
import android.view.MenuItem
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.feature.info.InfoActivity
import com.mathewsmobile.pwned.feature.pwned.detail.PwnDetailFragment
import com.mathewsmobile.pwned.feature.pwned.search.PwnedFragment
import com.mathewsmobile.pwned.shared.model.Breach
import com.mathewsmobile.pwned.constants.firstRunKey
import com.mathewsmobile.pwned.constants.sharedPrefs
import com.mathewsmobile.pwned.shared.SingleFragmentActivity

class PwnedActivity : SingleFragmentActivity(), PwnedFragment.DetailNavigator, FragmentManager.OnBackStackChangedListener {

    lateinit var viewModel: PwnedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()

        supportFragmentManager.addOnBackStackChangedListener(this)

        viewModel = ViewModelProviders.of(this).get(PwnedViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        setActionBarTitle("pwned")

        showInfoAlertOnFirstRun()
    }

    override fun viewDetails(breach: Breach) {
        val fm = supportFragmentManager

        val detail = PwnDetailFragment()

        viewModel.getSelectedBreach().value = breach

        setActionBarTitle(breach.title)

        fm.beginTransaction()
                .add(R.id.fragment_container, detail)
                .addToBackStack(null)
                .commit()
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        val canback = supportFragmentManager.backStackEntryCount > 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(canback)
    }

    override fun onSupportNavigateUp(): Boolean {
        //This method is called when the up button is pressed. Just the pop back stack.
        supportFragmentManager.popBackStack()
        return true
    }

    override fun createFragment(): Fragment {
        val fragment = PwnedFragment()
        fragment.detailHandler = this
        return fragment
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
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

