package com.kemalakkus.chachistockapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.chachistockapp.databinding.BillItemBinding
import com.kemalakkus.chachistockapp.model.Product

class BillAdapter: RecyclerView.Adapter<BillAdapter.BillHolder>() {
    class BillHolder(val binding: BillItemBinding): RecyclerView.ViewHolder(binding.root)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillHolder {
        val view= BillItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BillHolder(view)
    }

    override fun onBindViewHolder(holder: BillHolder, position: Int) {
        val product=differ.currentList[position]
        holder.binding.apply {
            code.text=product.urunAdi
            quantity.text=product.listeAdedi.toString()
            price.text=product.fiyat
            total.text="${product.listeAdedi*product.fiyat!!.toInt()}"
        }
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}