package com.mathewsmobile.pwned.list

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mathewsmobile.pwned.GlideApp
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.activities.PwnDetailActivity
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import kotlinx.android.synthetic.main.pwn_list_cell.view.*

/**
 * Created by nicke on 2/6/2018.
 */

class PwnListHolder(private val row: View): RecyclerView.ViewHolder(row) {
    fun bind(breach: Breach) = with(row, {
        breach_name.text = breach.name

        GlideApp.with(this)
                .load(breach.getLogoUrl())
                .placeholder(R.mipmap.ic_placeholder)
                .into(breach_logo)

        row.setOnClickListener {
            val context = it.context
            val intent = Intent(context, PwnDetailActivity::class.java)
            intent.putExtra(breachKey, breach)
            context.startActivity(intent)
        }
    })
}