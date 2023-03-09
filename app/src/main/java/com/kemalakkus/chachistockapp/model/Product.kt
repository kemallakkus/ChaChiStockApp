package com.kemalakkus.chachistockapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Product(
    val urunAdi:String?,
    val fiyat:String?,
    val adet:Int?,
    var listeAdedi:Int=0,
    val image:ByteArray?,
    var isAddList:Int=0,
    var isEnable:Boolean=false,
    @PrimaryKey(autoGenerate = true)
    val id: Int=0
): Parcelable {

}
@Entity
@Parcelize
data class ProductBill(
    val adres:String?,
    val telNo:String?,
    val mail:String?,
    val logoImage:ByteArray?,
    val sevkTarihi:String?,
    @PrimaryKey(autoGenerate = true)
    val billId: Int=0
): Parcelable {

}
