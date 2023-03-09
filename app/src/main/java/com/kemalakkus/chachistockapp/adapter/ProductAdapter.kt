package com.kemalakkus.chachistockapp.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.chachistockapp.databinding.ProductItemBinding
import com.kemalakkus.chachistockapp.model.Product

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root)


    private val diffUtil=object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view=ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.binding.apply {
            productUrunAdi.text=product.urunAdi
            productAdet.text=product.adet.toString()
            productFiyat.text="${product.fiyat} $"
            product.image?.let {
                productImage.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
            }
        }
        if (product.isEnable==false){
            holder.binding.adetBelirle.text="Adet Giriniz"
        }else{
            holder.binding.adetBelirle.text="${product.listeAdedi.toString()} Adet"
        }
        holder.binding.linearBack.setOnClickListener {
            onlickEdit?.let {
                it.invoke(product)
            }
        }
        holder.binding.addProductToList.setOnClickListener {
            addToList?.let {
                it.invoke(product)
            }

        }
        holder.binding.adetBelirle.setOnClickListener {
            addQuantity?.let {
                it.invoke(product)
            }

        }

    }
    var onlickEdit:((Product)-> Unit)? = null
    var addToList:((Product)-> Unit)? = null
    var addQuantity:((Product)-> Unit)? = null

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}