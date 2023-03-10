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
import com.hyvu.alebeer.databinding.FragmentFavoriteBinding
import com.hyvu.alebeer.model.BeerItem
import com.hyvu.alebeer.utils.EventObserver
import com.hyvu.alebeer.utils.hideSoftKeyboard
import com.hyvu.alebeer.view.binder.BeerBinder
import com.hyvu.alebeer.view.customview.BeerItemView
import com.hyvu.alebeer.viewmodel.BeerViewModel
import com.hyvu.alebeer.viewmodel.factory.BeerViewModelFactory
import mva3.adapter.ListSection
import mva3.adapter.MultiViewAdapter

class FavoriteFragment : Fragment() {

    private lateinit var mBinding: FragmentFavoriteBinding

    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), BeerViewModelFactory())[BeerViewModel::class.java]
    }

    private val mAdapter by lazy {
        MultiViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observerLiveData()
    }

    private fun observerLiveData() {
        mViewModel.onSave.observe(viewLifecycleOwner, EventObserver {
            mViewModel.favoriteBeersSection.add(it)
        })

        mViewModel.isUpdateFavorite.observe(viewLifecycleOwner, EventObserver {
            if (it) mAdapter.notifyDataSetChanged()
        })
    }

    private fun initView() {
        mAdapter.registerItemBinders(BeerBinder(BeerItemView.Mode.FAVORITE, mViewModel.startTime, mBeerBinderListener))
        mBinding.rcvBeer.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rcvBeer.adapter = mAdapter
        mBinding.rcvBeer.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        mBinding.rcvBeer.setOnTouchListener { view, _ ->
            hideSoftKeyboard(view)
            false
        }

        mAdapter.addSection(mViewModel.favoriteBeersSection)
    }

    private val mBeerBinderListener = object : BeerBinder.Listener {
        override fun onDelete(item: BeerItem, position: Int) {
            super.onDelete(item, position)
            view?.let { hideSoftKeyboard(it) }
            mViewModel.deleteBeerFromDb(item) { isDeleted ->
                if (isDeleted) {
                    mViewModel.favoriteBeersSection.remove(position)
                    mAdapter.notifyItemRangeChanged(position, mViewModel.favoriteBeersSection.size())
                } else {
                    Toast.makeText(context, "Could not delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onUpdate(item: BeerItem, position: Int) {
            super.onUpdate(item, position)
            view?.let { hideSoftKeyboard(it) }
            mViewModel.updateBeerNoteFromDb(item) { isUpdated ->
                if (isUpdated) {
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    mAdapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(context, "Could not update", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.removeAllSections()
    }
}