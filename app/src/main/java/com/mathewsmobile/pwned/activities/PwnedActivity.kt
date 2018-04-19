package com.mathewsmobile.pwned.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Menu
import android.view.MenuItem
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.fragments.PwnDetailFragment
import com.mathewsmobile.pwned.fragments.PwnedFragment
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import com.mathewsmobile.pwned.util.firstRunKey
import com.mathewsmobile.pwned.util.sharedPrefs

class PwnedActivity : SingleFragmentActivity(), PwnedFragment.DetailNavigator, FragmentManager.OnBackStackChangedListener {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()

        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onResume() {
        super.onResume()

        setActionBarTitle("pwned")

        showInfoAlertOnFirstRun()
    }

    override fun viewDetails(breach: Breach) {
        val fm = supportFragmentManager

        val detail = PwnDetailFragment()
        val args = Bundle()
        args.putSerializable(breachKey, breach)

        detail.arguments = args

        setActionBarTitle(breach.title)

        fm.beginTransaction()
                .add(R.id.fragment_container, detail)
//                .replace(R.id.fragment_container, detail)
                .addToBackStack(null)
                .commit()
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

