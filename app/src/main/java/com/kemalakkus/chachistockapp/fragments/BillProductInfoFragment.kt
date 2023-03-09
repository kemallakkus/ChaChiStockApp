package com.kemalakkus.chachistockapp.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kemalakkus.chachistockapp.R
import com.kemalakkus.chachistockapp.databinding.FragmentBillProductInfoBinding
import com.kemalakkus.chachistockapp.model.ProductBill
import com.kemalakkus.chachistockapp.viewmodels.ProductBillViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class BillProductInfoFragment : Fragment() {
    private lateinit var binding: FragmentBillProductInfoBinding
    private val productBillViewModel: ProductBillViewModel by viewModels()
    private var selectedBitmap : Bitmap? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var byteArray:ByteArray?=null
    var bundle=Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentBillProductInfoBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productBillViewModel.getInfo().observe(viewLifecycleOwner, Observer { prodBill->
            if (prodBill!=null){
                binding.adressInfo.setText(prodBill.adres)
                binding.phoneInfo.setText(prodBill.telNo)
                binding.mailInfo.setText(prodBill.mail)
                binding.enthusiasm.setText(prodBill.sevkTarihi)
                if (prodBill.logoImage != null) {
                    binding.logo.setImageBitmap(
                        BitmapFactory.decodeByteArray(prodBill.logoImage,
                        0,
                        prodBill.logoImage.size))
                }
            }
            setBillInfo(prodBill)
        })
        selectImage()
        registerLauncher()
    }
    private fun setBillInfo(productBill: ProductBill?) {
        binding.back.setOnClickListener {
            val adres = binding.adressInfo.text.toString().trim()
            val tel = binding.phoneInfo.text.toString().trim()
            val mail = binding.mailInfo.text.toString().trim()
            val sevkTarihi = binding.enthusiasm.text.toString().trim()
            if (selectedBitmap!=null) {
                val smallBitmap = makeSmallerBitmap(selectedBitmap!!, 300)
                val outputStream = ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                byteArray = outputStream.toByteArray()
            }else if (productBill!=null){
                byteArray=productBill!!.logoImage
            }else{
                byteArray=null
            }
            if (productBill==null) {
                if (adres.isNotEmpty() && tel.isNotEmpty() && mail.isNotEmpty() &&  sevkTarihi.isNotEmpty()
                    &&selectedBitmap!=null) {
                    val newTask = ProductBill(adres, tel, mail,byteArray,sevkTarihi)
                    productBillViewModel.insertProduct(newTask)

                } else {
                    Toast.makeText(context, "Bütün Alanları Doldurmalısın", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                if (adres.isNotEmpty() && tel.isNotEmpty() && mail.isNotEmpty() && sevkTarihi.isNotEmpty()
                    &&selectedBitmap!=null) {
                    val updateTask = ProductBill(adres,
                        tel,
                        mail,
                        byteArray,
                        sevkTarihi,
                        productBill.billId)
                    productBillViewModel.updateProduct(updateTask)
                }
            }
            findNavController().navigate(R.id.action_billProductInfoFragment_to_billFragment)
        }
    }
    private fun selectImage() {
        binding.logoYukle.setOnClickListener{view->
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }
    fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    val imageData = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(requireContext().contentResolver, imageData!!)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.logo.setImageBitmap(selectedBitmap)


                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageData)
                            binding.logo.setImageBitmap(selectedBitmap)

                        }
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun makeSmallerBitmap(image: Bitmap, maximumSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}