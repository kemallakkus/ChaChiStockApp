package com.kemalakkus.chachistockapp.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kemalakkus.chachistockapp.R
import com.kemalakkus.chachistockapp.adapter.BillAdapter
import com.kemalakkus.chachistockapp.databinding.FragmentBillBinding
import com.kemalakkus.chachistockapp.model.Product
import com.kemalakkus.chachistockapp.viewmodels.ProductBillViewModel
import com.kemalakkus.chachistockapp.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillFragment : Fragment() {
    private lateinit var binding: FragmentBillBinding
    private val productViewModel: ProductViewModel by viewModels()
    private val productBillViewModel: ProductBillViewModel by viewModels()
    private var addTolist= arrayListOf<Product>()
    private var priceList= arrayListOf<Int>()

    private val billAdapter= BillAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentBillBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRec()
        observeData()
        binding.infoLineer.setOnClickListener{
            findNavController().navigate(R.id.action_billFragment_to_billProductInfoFragment)
        }
        productBillViewModel.getInfo().observe(viewLifecycleOwner, Observer {productBill->
            productBill?.let {
                binding.billAdress.setText(it.adres)
                binding.billPhone.setText(it.telNo)
                binding.billMail.setText(it.mail)
                binding.billSevkDate.setText("Tarih:${it.sevkTarihi}")
                if (it.logoImage != null) {
                    binding.billLogo.setImageBitmap(
                        BitmapFactory.decodeByteArray(it.logoImage,
                        0,
                        it.logoImage.size))
                }
            }
        })
        findNavController().popBackStack(R.id.action_billFragment_to_listProductFragment,true)

    }

    private fun observeData() {
        productViewModel.realAllProduct().observe(viewLifecycleOwner, Observer {
            for(item in it){
                if(item.isAddList==1){
                    addTolist.add(item)
                    priceList.add(item.fiyat!!.toInt()*item.listeAdedi)
                }
            }
            binding.tamamla.setOnClickListener {view->
                for (item in it){
                    productViewModel.updateAddList(item.id,0,false)
                    productViewModel.updateQuantityList(item.id,0,item.adet!!)
                }
                view.findNavController().navigate(R.id.action_billFragment_to_listProductFragment)
                Toast.makeText(context,"Liste Tamamlandı", Toast.LENGTH_SHORT).show()
            }
            billAdapter.differ.submitList(addTolist)
            binding.totalResult.text="${priceList.sum().toString()}TL"

        })
    }
    private fun setRec() {
        binding.billRec.apply {
            adapter=billAdapter
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_billFragment_to_listProductFragment)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }
}