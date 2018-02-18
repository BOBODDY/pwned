package com.boboddy.pwned.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.boboddy.pwned.R
import com.boboddy.pwned.model.Breach

/**
 * Created by nicke on 2/6/2018.
 */

class PwnListAdapter(context: Context) : BaseAdapter() {

    var dataSet: List<Breach> = emptyList()

    private val inflator: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var viewHolder: PwnListHolder? = null
        if (view != null) {
            viewHolder = view.tag as PwnListHolder
        } else {
            view = inflator.inflate(R.layout.pwn_list_cell, parent, false)
            viewHolder = PwnListHolder(view)
            view.tag = viewHolder
        }

        viewHolder.label.text = dataSet[position].Name
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