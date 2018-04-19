package com.mathewsmobile.pwned.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathewsmobile.pwned.GlideApp
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import kotlinx.android.synthetic.main.content_pwn_detail.*
import java.text.NumberFormat
import java.util.*

class PwnDetailFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_pwn_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detail_what_mean.setOnClickListener {
            showWhatTheseMean()
        }

        val breach = arguments?.getSerializable(breachKey) as Breach
        setupDetails(breach)
    }

    private fun setupDetails(breach: Breach) {
        detail_breach_name.text = breach.title
        activity?.actionBar?.title = breach.title
        detail_breach_description.text = Html.fromHtml(breach.description, 0)
        detail_breach_description.movementMethod = LinkMovementMethod.getInstance()
        detail_breach_date.text = breach.breachDate
        detail_breach_count.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(breach.pwnCount) + " affected"

        if (breach.isActive) {
            detail_is_active_icon.setImageResource(R.mipmap.ic_check)
        } else {
            detail_is_active_icon.setImageResource(R.mipmap.ic_x)
        }

        if (breach.isVerified) {
            detail_is_verified_icon.setImageResource(R.mipmap.ic_check)
        } else {
            detail_is_verified_icon.setImageResource(R.mipmap.ic_x)
        }

        if (breach.isSpamList) {
            detail_is_spam_icon.setImageResource(R.mipmap.ic_check)
        } else {
            detail_is_spam_icon.setImageResource(R.mipmap.ic_x)
        }

        val compromisedData = listDataClasses(breach.dataClasses)
        detail_data_classes.text = compromisedData

        GlideApp.with(this)
                .load(breach.getLogoUrl())
                .placeholder(R.mipmap.ic_placeholder)
                .into(detail_breach_logo)
    }

    fun listDataClasses(classes: List<String>): String {
        var s = ""
        for (dataType in classes) {
            s += dataType + "\n"
        }
        return s
    }

    fun showWhatTheseMean() {
        val alertBuilder = AlertDialog.Builder(context)

        alertBuilder.setTitle(R.string.explanation_title)
        alertBuilder.setMessage(R.string.explanation_message)

        alertBuilder.setPositiveButton(R.string.ok, {
            dialogInterface, _ ->
            dialogInterface.dismiss()
        })

        val dialog = alertBuilder.create()
        dialog.show()
    }
}