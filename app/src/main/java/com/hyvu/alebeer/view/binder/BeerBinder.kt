package com.hyvu.alebeer.view.binder

import android.view.View
import android.view.ViewGroup
import com.hyvu.alebeer.model.BeerItem
import com.hyvu.alebeer.view.customview.BeerItemView
import mva3.adapter.ItemBinder
import mva3.adapter.ItemViewHolder

class BeerBinder(private val mListener: Listener): ItemBinder<BeerItem, BeerBinder.BeerViewHolder>() {

    interface Listener {
        fun onSave(item: BeerItem)
    }

    inner class BeerViewHolder(v: View): ItemViewHolder<BeerItem>(v) {

        var beerItemView: BeerItemView? = null

        init {
            beerItemView = v as BeerItemView
        }
    }

    override fun bindViewHolder(holder: BeerViewHolder?, item: BeerItem?) {
        holder?.beerItemView?.bindView(item)
    }

    override fun createViewHolder(parent: ViewGroup?): BeerViewHolder {
        return BeerViewHolder(BeerItemView(parent!!.context).apply {
            setListener(object : BeerItemView.Listener {
                override fun onSave(item: BeerItem) {
                    mListener.onSave(item)
                }
            })
        })
    }

    override fun canBindData(item: Any?): Boolean {
        return item is BeerItem?
    }


}