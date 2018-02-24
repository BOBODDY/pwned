package com.boboddy.pwned.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.boboddy.pwned.R

class InfoActivity : AppCompatActivity() {

    @BindView(R.id.info_version)
    lateinit var versionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        ButterKnife.bind(this)

        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            versionText.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }
}
