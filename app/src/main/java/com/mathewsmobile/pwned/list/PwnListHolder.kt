package com.mathewsmobile.pwned.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mathewsmobile.pwned.R

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