package com.mathewsmobile.pwned.feature.pwned.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathewsmobile.pwned.R
import com.mathewsmobile.pwned.feature.pwned.PwnedActivity
import com.mathewsmobile.pwned.shared.model.Breach
import com.mathewsmobile.pwned.constants.pwnedText
import com.mathewsmobile.pwned.constants.safeText
import com.mathewsmobile.pwned.feature.pwned.PwnedViewModel
import kotlinx.android.synthetic.main.activity_pwned.*

class PwnedFragment : Fragment(), PwnListHolder.OnActionCompleted {

    lateinit var detailHandler: DetailNavigator

    var pwnAdapter: PwnListAdapter? = null

    lateinit var viewModel: PwnedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_pwned, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.actionBar?.hide()
        detailHandler = activity as PwnedActivity

        viewModel = ViewModelProviders.of(activity!!).get(PwnedViewModel::class.java)

        viewModel.getBreaches().observe(this, Observer { breaches ->
            updateList(breaches)
        })

        pwn_check.setOnClickListener {
            viewModel.checkForPwn(account_entry.text.toString())
        }

        if (context != null) {
            pwn_list.layoutManager = LinearLayoutManager(context)
        }
    }

    fun updateList(breaches: List<Breach>?) {
        if (breaches == null) {
            return
        }

        if (pwnAdapter == null) {
            pwnAdapter = PwnListAdapter(this.requireContext(), emptyList(), this)
            pwn_list.adapter = pwnAdapter
        }

        pwnAdapter!!.data = breaches
        pwnAdapter!!.notifyDataSetChanged()

        if (breaches.isNotEmpty()) {
            pwned_result.text = pwnedText
            pwned_result.setTextColor(resources.getColor(R.color.pwnedColor))
            pwn_list.visibility = View.VISIBLE
        } else {
            pwned_result.text = safeText
            pwned_result.setTextColor(resources.getColor(R.color.safeColor))
            pwn_list.visibility = View.GONE
        }
    }

    override fun onClick(breach: Breach) {
        detailHandler.viewDetails(breach)
    }

    interface DetailNavigator { fun viewDetails(breach: Breach)}
}