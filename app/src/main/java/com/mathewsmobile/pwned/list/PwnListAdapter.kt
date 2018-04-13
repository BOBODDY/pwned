package com.mathewsmobile.pwned.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.model.Breach

/**
 * Created by nicke on 2/6/2018.
 */

class PwnListAdapter(val context: Context, var data: List<Breach>) : RecyclerView.Adapter<PwnListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PwnListHolder {
        val view = parent.inflate(R.layout.pwn_list_cell)
        view.setOnClickListener {  }
        return PwnListHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PwnListHolder, position: Int) {
        holder.bind(data[position])
    }

    // Inspired by https://antonioleiva.com/extension-functions-kotlin/
    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

}