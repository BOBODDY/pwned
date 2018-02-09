package com.boboddy.pwned.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.boboddy.pwned.R
import kotlinx.android.synthetic.main.pwn_list_cell.view.*

/**
 * Created by nicke on 2/6/2018.
 */

class PwnListHolder(row: View) {
    @BindView(R.id.breach_name)
    lateinit var label: TextView

    @BindView(R.id.breach_logo)
    lateinit var logo: ImageView

    init {
        ButterKnife.bind(this, row)
    }
}