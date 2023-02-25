package com.hyvu.alebeer.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyvu.alebeer.R
import com.hyvu.alebeer.databinding.FragmentBeerBinding
import com.hyvu.alebeer.model.BeerItem
import com.hyvu.alebeer.utils.EventObserver
import com.hyvu.alebeer.utils.hideSoftKeyboard
import com.hyvu.alebeer.view.binder.BeerBinder
import com.hyvu.alebeer.view.customview.BeerItemView
import com.hyvu.alebeer.viewmodel.BeerViewModel
import com.hyvu.alebeer.viewmodel.factory.BeerViewModelFactory
import mva3.adapter.MultiViewAdapter
import mva3.adapter.util.InfiniteLoadingHelper

class BeerFragment : Fragment() {

    private lateinit var mBinding: FragmentBeerBinding
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), BeerViewModelFactory())[BeerViewModel::class.java]
    }

    private val mAdapter by lazy {
        MultiViewAdapter()
    }
    private lateinit var infiniteLoadingHelper: InfiniteLoadingHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBeerBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observerLiveData()
    }

    private fun observerLiveData() {
        mViewModel.beers.observe(viewLifecycleOwner, EventObserver {
            val isInit = mViewModel.beerSection.data.isEmpty()
            mViewModel.beerSection.addAll(it)
            if (isInit) mBinding.rcvBeer.scrollToPosition(0)
        })

        mViewModel.isLoadMore.observe(viewLifecycleOwner) { isLoadMore ->
            if (isLoadMore == true) {
                infiniteLoadingHelper.markCurrentPageLoaded()
            } else {
                infiniteLoadingHelper.markAllPagesLoaded()
            }
        }

        mViewModel.onDelete.observe(viewLifecycleOwner, EventObserver { beerItem ->
            val position = mViewModel.beerSection.data.indexOfFirst { it.id == beerItem.id }
            if (position >= 0) mAdapter.notifyItemChanged(position)
        })

        mViewModel.onUpdate.observe(viewLifecycleOwner, EventObserver { beerItem ->
            val position = mViewModel.beerSection.data.indexOfFirst { it.id == beerItem.id }
            if (position >= 0) mAdapter.notifyItemChanged(position)
        })
    }

    private fun initView() {
        mAdapter.registerItemBinders(BeerBinder(BeerItemView.Mode.NORMAL, mBeerBinderListener))
        mBinding.rcvBeer.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rcvBeer.adapter = mAdapter
        mBinding.rcvBeer.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        mBinding.rcvBeer.setOnTouchListener { view, _ ->
            hideSoftKeyboard(view)
            false
        }

        infiniteLoadingHelper = object : InfiniteLoadingHelper(mBinding.rcvBeer, R.layout.item_loading_footer) {
            override fun onLoadNextPage(page: Int) {
                mViewModel.fetchBeers(page + 1)
            }
        }

        mAdapter.addSection(mViewModel.beerSection)
        mAdapter.setInfiniteLoadingHelper(infiniteLoadingHelper)
    }

    private val mBeerBinderListener = object : BeerBinder.Listener {
        override fun onSave(item: BeerItem, position: Int) {
            mViewModel.saveBeer(item) {
                if (it) {
                    mAdapter.notifyItemChanged(position)
                    view?.let { v -> hideSoftKeyboard(v) }
                } else {
                    Toast.makeText(context, getString(R.string.save_fail), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.removeAllSections()
    }

}