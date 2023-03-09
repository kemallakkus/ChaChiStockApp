package com.kemalakkus.chachistockapp.di

import android.content.Context
import androidx.room.Room
import com.kemalakkus.chachistockapp.db.ProductBillDao
import com.kemalakkus.chachistockapp.db.ProductBillDataBase
import com.kemalakkus.chachistockapp.db.ProductDao
import com.kemalakkus.chachistockapp.db.ProductDataBase
import com.kemalakkus.chachistockapp.repository.ProductBillRepository
import com.kemalakkus.chachistockapp.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesAlertDao(productDataBase: ProductDataBase):ProductDao = productDataBase.productDao()

    @Provides
    fun providesAlertBillDao(productBillDataBase: ProductBillDataBase): ProductBillDao =
        productBillDataBase.productBillDao()
    //Product Sınıfının
    @Provides
    @Singleton
    fun providesAlertDatabase(@ApplicationContext context: Context):ProductDataBase
            = Room.databaseBuilder(context,ProductDataBase::class.java,"AlertDatabase")
        .allowMainThreadQueries().build()
    //ProductBill Sınıfının
    @Provides
    @Singleton
    fun providesAlertBillDatabase(@ApplicationContext context: Context):ProductBillDataBase
            = Room.databaseBuilder(context,ProductBillDataBase::class.java,"AlertBillDatabase")
        .allowMainThreadQueries().build()

    @Provides
    fun providesUserRepository(prdouctDao: ProductDao) : ProductRepository
            = ProductRepository(prdouctDao)

    @Provides
    fun providesUserBillRepository(prdouctBillDao: ProductBillDao) : ProductBillRepository
            = ProductBillRepository(prdouctBillDao)

}