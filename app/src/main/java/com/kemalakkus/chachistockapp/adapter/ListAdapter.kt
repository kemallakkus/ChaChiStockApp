package com.kemalakkus.chachistockapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.chachistockapp.databinding.ListItemBinding
import com.kemalakkus.chachistockapp.model.Product

class ListAdapter: RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root)
    var colorsList :Array<String> = arrayOf( "#FFE500","#FFFFFFFF")
    private val diffUtil=object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id &&
                    oldItem.urunAdi==newItem.urunAdi

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem==newItem
        }
    }
    val differ= AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view= ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.binding.apply {
            listProductName.text=product.urunAdi
            listProductPrice.text="${product.fiyat} "
            listProductQuantity.text="${product.listeAdedi} "
        }
        holder.binding.listItemBack.setBackgroundColor(Color.parseColor(colorsList[position%2]))
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }
}