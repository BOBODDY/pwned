package com.mathewsmobile.pwned.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.model.Breach
import com.squareup.picasso.Picasso

/**
 * Created by nicke on 2/6/2018.
 */

class PwnListAdapter(val context: Context) : BaseAdapter() {

    var dataSet: List<Breach> = emptyList()

    private val inflator: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val breach = dataSet[position]
        var view = convertView
        var viewHolder: PwnListHolder?

        if (view != null) {
            viewHolder = view.tag as PwnListHolder
        } else {
            view = inflator.inflate(R.layout.pwn_list_cell, parent, false)
            viewHolder = PwnListHolder(view)
            view.tag = viewHolder
        }

        viewHolder.label.text = breach.Title

        // TODO: figure out how to load SVGs using Picasso
        Picasso.get()
                .load(breach.getLogoUrl())
                .placeholder(R.mipmap.ic_placeholder)
                .into(viewHolder.logo)
        return view!!
    }

    override fun getItem(p0: Int): Any {
        return dataSet[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return dataSet.size
    }

}