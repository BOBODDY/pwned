package com.mathewsmobile.pwned.activities

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import kotlinx.android.synthetic.main.activity_pwn_detail.*
import java.text.NumberFormat
import java.util.*

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

    @BindView(R.id.detail_what_mean)
    lateinit var whatDoTheseMean: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pwn_detail)
        setSupportActionBar(toolbar)

        ButterKnife.bind(this)

        // TODO Set Up as Home
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            val bundle = intent.extras

            if (bundle[breachKey] != null) {
                val breach = bundle[breachKey] as Breach
                setupDetails(breach)
            }
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

    @OnClick(R.id.detail_what_mean)
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
        breachName.text = breach.Title
        title = breach.Title
        breachDescription.text = Html.fromHtml(breach.Description, 0)
        breachDescription.movementMethod = LinkMovementMethod.getInstance()
        breachDate.text = breach.BreachDate
        breachPwnCount.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(breach.PwnCount) + " affected"

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


//        Picasso.get()
//                .load(breach.getLogoUrl())
//                .placeholder(R.mipmap.ic_placeholder)
//                .into(breachLogo)
        Glide.with(this)
                .load(breach.getLogoUrl())
                .into(breachLogo)
    }

    fun listDataClasses(classes: List<String>): String {
        var s = ""
        for (dataType in classes) {
            s += dataType + "\n"
        }
        return s
    }
}
