package com.hyvu.alebeer.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hyvu.alebeer.view.fragment.BeerFragment
import com.hyvu.alebeer.view.fragment.FavoriteFragment

class BeerTabsAdapter(activity: FragmentActivity, private val numberOfPage: Int): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = numberOfPage

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> BeerFragment()
            else -> FavoriteFragment()
        }
        return fragment
    }
}