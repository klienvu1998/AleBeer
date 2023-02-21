package com.hyvu.alebeer.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.hyvu.alebeer.databinding.ActivityMainBinding
import com.hyvu.alebeer.view.adapter.BeerTabsAdapter

class MainActivity : AppCompatActivity() {

    object Tab {
        const val BEER = "Beer"
        const val FAVORITE = "Favorite"
    }

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mTabLayoutAdapter: BeerTabsAdapter
    private val tabs = arrayOf(Tab.BEER, Tab.FAVORITE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
    }

    private fun initView() {
        initTabLayoutWithViewPager()
    }

    private fun initTabLayoutWithViewPager() {
        mTabLayoutAdapter = BeerTabsAdapter(this, tabs.size)
        mBinding.viewPagerContainer.adapter = mTabLayoutAdapter

        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPagerContainer) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

}