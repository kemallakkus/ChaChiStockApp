package com.kemalakkus.chachistockapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kemalakkus.chachistockapp.model.ProductBill
import com.kemalakkus.chachistockapp.repository.ProductBillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductBillViewModel @Inject constructor(val repository: ProductBillRepository) : ViewModel() {

    fun insertProduct(productBill: ProductBill)=viewModelScope.launch {
        repository.insertProduct(productBill)
    }
    fun updateProduct(productBill: ProductBill)=viewModelScope.launch {
        repository.updatetProduct(productBill)
    }
    fun getInfo(): LiveData<ProductBill> {
        return repository.getInfo().flowOn(Dispatchers.Main)
            .asLiveData(viewModelScope.coroutineContext)
    }
}