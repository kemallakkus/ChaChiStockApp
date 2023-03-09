package com.kemalakkus.chachistockapp.db

import androidx.room.*
import com.kemalakkus.chachistockapp.model.Product
import com.kemalakkus.chachistockapp.model.ProductBill
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)
    @Update
    suspend fun updateProduct(product: Product)
    @Query("UPDATE product SET isAddList = :newAddList,isEnable=:isEnableQuantity WHERE id = :userId")
    suspend fun updateAddList(userId: Int, newAddList: Int,isEnableQuantity:Boolean)
    @Query("UPDATE product SET listeAdedi = :newListeAdedi,adet=:newAdet WHERE id = :userId")
    suspend fun updateQuantity(userId: Int, newListeAdedi: Int,newAdet:Int)
    @Delete
    suspend fun deleteProduct(product: Product)
    @Query("SELECT * FROM product ORDER BY id")
    fun readAllProduct(): Flow<List<Product>>
}

@Dao
interface ProductBillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductBill(productBill: ProductBill)
    @Update
    suspend fun updateProductBill(productBill: ProductBill)
    @Query("SELECT * FROM productbill LIMIT 1")
    fun getInfo(): Flow<ProductBill>
}