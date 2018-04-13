package com.mathewsmobile.pwned.list

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.mathewsmobile.pwned.activities.PwnDetailActivity
import com.mathewsmobile.pwned.model.Breach
import com.mathewsmobile.pwned.util.breachKey
import kotlinx.android.synthetic.main.pwn_list_cell.view.*

/**
 * Created by nicke on 2/6/2018.
 */

class PwnListHolder(private val row: View): RecyclerView.ViewHolder(row) {
    fun bind(breach: Breach) = with(row, {
        breach_name.text = breach.Name

        Glide.with(row)
                .load(breach.getLogoUrl())
                .into(breach_logo)

        row.setOnClickListener {
            val context = it.context
            val intent = Intent(context, PwnDetailActivity::class.java)
            intent.putExtra(breachKey, breach)
            context.startActivity(intent)
        }
    })
}