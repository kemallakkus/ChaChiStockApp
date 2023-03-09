package com.kemalakkus.chachistockapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kemalakkus.chachistockapp.R
import com.kemalakkus.chachistockapp.adapter.ListAdapter
import com.kemalakkus.chachistockapp.databinding.FragmentListProductBinding
import com.kemalakkus.chachistockapp.model.Product
import com.kemalakkus.chachistockapp.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListProductFragment : Fragment() {
    private lateinit var binding: FragmentListProductBinding
    private var addTolist= arrayListOf<Product>()
    private val productViewModel: ProductViewModel by viewModels()
    val listAdapter= ListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentListProductBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRec()
        binding.addProducT.setOnClickListener {
            it.findNavController().navigate(R.id.action_listProductFragment_to_addProductFragment)
        }
        binding.bill.setOnClickListener {
            it.findNavController().navigate(R.id.action_listProductFragment_to_billFragment)
        }
        observeData()
        removeProduct(view)
    }

    private fun observeData() {
        productViewModel.realAllProduct().observe(viewLifecycleOwner, Observer {
            for (item in it){
                if (item.isAddList==1){
                    if (!addTolist.contains(item)){
                        addTolist.add(item)

                    }
                }
            }
            listAdapter.differ.submitList(addTolist)

        })

    }
    private fun setRec() {
        binding.cRec.apply {
            adapter=listAdapter
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }

    }
    private fun removeProduct(view: View) {
        val itemTouchHelperCallBack=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                val product =listAdapter.differ.currentList.get(position)
                productViewModel.updateAddList(product.id,0,false)
                productViewModel.updateQuantityList(product.id,0,product.adet!!+product.listeAdedi)
                Snackbar.make(view,"Deleted article successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Geri Al"){
                        productViewModel.insertProduct(product)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.cRec)
        }
    }
}