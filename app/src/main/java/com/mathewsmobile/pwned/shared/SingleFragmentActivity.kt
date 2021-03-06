package com.mathewsmobile.pwned.shared

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mathewsmobile.pwned.R

abstract class SingleFragmentActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }

    }

    abstract fun createFragment(): Fragment
}