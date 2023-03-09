package com.kemalakkus.chachistockapp.repository

import com.kemalakkus.chachistockapp.db.ProductBillDao
import com.kemalakkus.chachistockapp.model.ProductBill
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductBillRepository
@Inject constructor(val productBillDao: ProductBillDao) {
    suspend fun insertProduct(productBill: ProductBill){
        productBillDao.insertProductBill(productBill)
    }
    suspend fun updatetProduct(productBill: ProductBill){
        productBillDao.updateProductBill(productBill)
    }
    fun getInfo(): Flow<ProductBill> {
        return productBillDao.getInfo()
    }
}