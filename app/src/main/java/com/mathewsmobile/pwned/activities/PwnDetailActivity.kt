package com.mathewsmobile.pwned.activities

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import kotlinx.android.synthetic.main.activity_pwn_detail.*
import kotlinx.android.synthetic.main.content_pwn_detail.*
import java.text.NumberFormat
import java.util.*

class PwnDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwn_detail)
        setSupportActionBar(toolbar)

        // TODO Set Up as Home
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            val bundle = intent.extras

            if (bundle[breachKey] != null) {
                val breach = bundle[breachKey] as Breach
                setupDetails(breach)
            }
        }

        detail_what_mean.setOnClickListener {
            showWhatTheseMean()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showWhatTheseMean() {
        val alertBuilder = AlertDialog.Builder(this)

        alertBuilder.setTitle(R.string.explanation_title)
        alertBuilder.setMessage(R.string.explanation_message)

        alertBuilder.setPositiveButton(R.string.ok, {
            dialogInterface, _ ->
            dialogInterface.dismiss()
        })

        val dialog = alertBuilder.create()
        dialog.show()
    }

    private fun setupDetails(breach: Breach) {
        detail_breach_name.text = breach.Title
        title = breach.Title
        detail_breach_description.text = Html.fromHtml(breach.Description, 0)
        detail_breach_description.movementMethod = LinkMovementMethod.getInstance()
        detail_breach_date.text = breach.BreachDate
        detail_breach_count.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(breach.PwnCount) + " affected"

        if (breach.IsActive) {
            detail_is_active_icon.setImageResource(R.mipmap.ic_check)
        } else {
            detail_is_active_icon.setImageResource(R.mipmap.ic_x)
        }

        if (breach.IsVerified) {
            detail_is_verified_icon.setImageResource(R.mipmap.ic_check)
        } else {
            detail_is_verified_icon.setImageResource(R.mipmap.ic_x)
        }

        if (breach.IsSpamList) {
            detail_is_spam_icon.setImageResource(R.mipmap.ic_check)
        } else {
            detail_is_spam_icon.setImageResource(R.mipmap.ic_x)
        }

        val compromisedData = listDataClasses(breach.DataClasses)
        detail_data_classes.text = compromisedData

        Glide.with(this)
                .load(breach.getLogoUrl())
                .into(detail_breach_logo)
    }

    fun listDataClasses(classes: List<String>): String {
        var s = ""
        for (dataType in classes) {
            s += dataType + "\n"
        }
        return s
    }
}
