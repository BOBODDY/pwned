package com.boboddy.pwned.activities

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindDimen
import butterknife.BindView
import butterknife.ButterKnife
import com.boboddy.pwned.R
import com.boboddy.pwned.model.Breach
import com.boboddy.pwned.model.DataClass
import com.boboddy.pwned.util.breachKey

import kotlinx.android.synthetic.main.activity_pwn_detail.*

class PwnDetailActivity : AppCompatActivity() {

    @BindView(R.id.detail_breach_logo)
    lateinit var breachLogo: ImageView

    @BindView(R.id.detail_breach_name)
    lateinit var breachName: TextView

    @BindView(R.id.detail_breach_date)
    lateinit var breachDate: TextView

    @BindView(R.id.detail_breach_count)
    lateinit var breachPwnCount: TextView

    @BindView(R.id.detail_breach_description)
    lateinit var breachDescription: TextView

    @BindView(R.id.detail_data_classes)
    lateinit var breachDataClasses: TextView

    @BindView(R.id.detail_is_verified_icon)
    lateinit var isVerifiedIcon: ImageView

    @BindView(R.id.detail_is_active_icon)
    lateinit var isActiveIcon: ImageView

    @BindView(R.id.detail_is_spam_icon)
    lateinit var isSpamIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwn_detail)
        setSupportActionBar(toolbar)

        ButterKnife.bind(this)

        // TODO Set Up as Home

        if (intent != null) {
            val bundle = intent.extras

            if (bundle[breachKey] != null) {
                val breach = bundle[breachKey] as Breach
                setupDetails(breach)
            }
        }
    }

    fun setupDetails(breach: Breach) {
        breachName.text = breach.Title
        breachDescription.text = Html.escapeHtml(breach.Description)
        breachDate.text = breach.BreachDate
        breachPwnCount.text = breach.PwnCount.toString() + " affected"

        if (breach.IsActive) {
            isActiveIcon.setImageResource(R.mipmap.ic_check)
        } else {
            isActiveIcon.setImageResource(R.mipmap.ic_x)
        }

        if (breach.IsVerified) {
            isVerifiedIcon.setImageResource(R.mipmap.ic_check)
        } else {
            isVerifiedIcon.setImageResource(R.mipmap.ic_x)
        }

        if (breach.IsSpamList) {
            isSpamIcon.setImageResource(R.mipmap.ic_check)
        } else {
            isSpamIcon.setImageResource(R.mipmap.ic_x)
        }

        val compromisedData = listDataClasses(breach.DataClasses)
        breachDataClasses.text = compromisedData
    }

    fun listDataClasses(classes: List<String>): String {
        var s = ""
        for (dataType in classes) {
            s += dataType + "\n"
        }
        return s
    }
}
