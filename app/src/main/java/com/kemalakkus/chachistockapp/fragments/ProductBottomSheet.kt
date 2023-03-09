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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kemalakkus.chachistockapp.R
import com.kemalakkus.chachistockapp.databinding.FragmentProductBottomSheetBinding
import com.kemalakkus.chachistockapp.model.Product
import com.kemalakkus.chachistockapp.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class ProductBottomSheet(var product: Product?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentProductBottomSheetBinding
    private var selectedBitmap : Bitmap? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var byteArray:ByteArray?=null
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentProductBottomSheetBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(product==null){
            binding.sheetTaskText.text="Ürün Ekle"
        }else {
            binding.sheetTaskText.text = "Ürünü Düzenle"
            binding.productCode.setText(product!!.urunAdi)
            binding.productPrice.setText(product!!.fiyat)
            binding.productQuantity.setText(product!!.adet!!.toString())
            if (product!!.image != null) {
                binding.image.setImageBitmap(
                    BitmapFactory.decodeByteArray(product!!.image,
                    0,
                    product!!.image!!.size))
            }
        }
        binding.createProdduct.setOnClickListener{
            saveNewTask()
        }
        selectImage()
        registerLauncher()
    }

    private fun saveNewTask() {
        val productCode = binding.productCode.text.toString().trim()
        val productQuantity = binding.productQuantity.text.toString().trim()
        val productPrice = binding.productPrice.text.toString().trim()
        if (selectedBitmap!=null) {
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            byteArray = outputStream.toByteArray()
        }else if (product!=null){
            byteArray=product!!.image
        }else{
            byteArray=null
        }
        if (product == null) {
            if (productCode.isNotEmpty() && productPrice.isNotEmpty() && productQuantity.isNotEmpty() && selectedBitmap!=null){
                val newTask = Product( productCode, productPrice, productQuantity.toInt(),0,byteArray)
                viewModel.insertProduct(newTask)
                Toast.makeText(context,"Ürün başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()
                dismiss()

            }else{
                Toast.makeText(context,"Tüm boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }else{
            val updateTask=Product(productCode,productPrice,productQuantity.toInt(),product!!.listeAdedi,byteArray,product!!.isAddList,false,product!!.id)
            viewModel.updateProduct(updateTask)
            Toast.makeText(context,"Ürün başarıyla düzenlendi", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun selectImage() {
        binding.imageProduct.setOnClickListener{view->
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
                            binding.image.setImageBitmap(selectedBitmap)


                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageData)
                            binding.image.setImageBitmap(selectedBitmap)

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


}