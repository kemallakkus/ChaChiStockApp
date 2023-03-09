package com.kemalakkus.chachistockapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kemalakkus.chachistockapp.model.Product
import com.kemalakkus.chachistockapp.model.ProductBill

@Database(entities = [Product::class], version = 1)
abstract class ProductDataBase: RoomDatabase() {
    abstract fun productDao():ProductDao
}

@Database(entities = [ProductBill::class], version = 1)
abstract class ProductBillDataBase: RoomDatabase() {
    abstract fun productBillDao():ProductBillDao
}