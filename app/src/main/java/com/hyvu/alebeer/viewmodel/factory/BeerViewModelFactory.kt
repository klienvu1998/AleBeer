package com.hyvu.alebeer.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyvu.alebeer.Injection
import com.hyvu.alebeer.viewmodel.BeerViewModel

class BeerViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BeerViewModel(
            Injection.provideBeerRepo()
        ) as T
    }
}