package com.example.doan3admin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.doan3admin.R
import com.example.doan3admin.adapter.addProductImgsAdapter
import com.example.doan3admin.databinding.FragmentAddProductsBinding
import com.example.doan3admin.model.addProductModel
import com.example.doan3admin.model.categoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match

class addProductsFragment : Fragment() {
    private lateinit var list:ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>
    private lateinit var adapter: addProductImgsAdapter
    private var coverImage: Uri? = null
    private lateinit var dialog: Dialog
    private var  coverImgURl: String? = ""
    private lateinit var categoryList: ArrayList<String>
    private lateinit var binding: FragmentAddProductsBinding
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            coverImage = it.data!!.data
            binding.productCoverImg.setImageURI(coverImage)
                binding.productCoverImg.visibility =  View.VISIBLE
        }
    }

    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val imageUrl = it.data!!.data
            if (imageUrl != null) {
                list.add(imageUrl)
                adapter.notifyDataSetChanged()
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAddProductsBinding.inflate(layoutInflater)
        list = ArrayList()
        listImages = ArrayList()
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.selectCoverImg.setOnClickListener(){
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)

        }

        binding.productImgBtn.setOnClickListener(){
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)

        }
        setProductCategory()

        adapter = addProductImgsAdapter(list)

        binding.productImgRv.adapter = adapter

        binding.submitProductBtn.setOnClickListener(){
            validateData()
        }
        return binding.root
    }


    private fun uploadImage(){
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference


        val imgRef = storageRef.child("products/$fileName")


        imgRef.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image->
                   coverImgURl = image.toString()
                    uploadProductImage()
                }



            }.addOnFailureListener {

                Toast.makeText(requireContext(),"Something went wrong with the storage.", Toast.LENGTH_SHORT).show()
            }
    }
    private var i = 0

    private fun uploadProductImage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference


        val imgRef = storageRef.child("products/$fileName")


        imgRef.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image->
                   listImages.add(image!!.toString())
                    if (list.size == listImages.size){
                        storeData()


                    }else{
                        i++
                        uploadProductImage()
                    }

                }


            }.addOnFailureListener {

                Toast.makeText(requireContext(),"Something went wrong with the storage.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db = FirebaseFirestore.getInstance().collection("products")
        val key = db.document().id


        val data = addProductModel(
            binding.productNameEdt.text.toString() ,

            binding.productDesEdt.text.toString(),

            coverImgURl.toString(),

            categoryList[binding.productCategoryDropdown.selectedItemPosition],

            key,

            binding.productQtEdt.text.toString(),

            binding.productPriceEdt.text.toString(),

            listImages


        )

        db.add(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(),"Product added",Toast.LENGTH_SHORT).show()

            clear()
        }
            .addOnFailureListener(){
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateData() {
        if(binding.productNameEdt.text.toString().isEmpty()){
            binding.productNameEdt.requestFocus()
            binding.productNameEdt.error = "Empty"

        }else if (binding.productQtEdt.text.toString().isEmpty()){
            binding.productQtEdt.requestFocus()
            binding.productQtEdt.error = "Empty"

        } else if (binding.productPriceEdt.text.toString().isEmpty()){
            binding.productPriceEdt.requestFocus()
            binding.productPriceEdt.error = "Empty"

        }else if (coverImage == null){
            Log.e("Image", "cover img : {$coverImage}")
            Toast.makeText(requireContext(),"Please select Cover Image",Toast.LENGTH_SHORT).show()
        }else if (list.size < 1){

            Toast.makeText(requireContext(),"Please select Product Images",Toast.LENGTH_SHORT).show()
        }else {
            uploadImage()
        }
    }

    private fun setProductCategory(){
        categoryList = ArrayList()
        val db  = Firebase.firestore.collection("categories")
        db.get().addOnSuccessListener {
                categoryList.clear()
                for (doc in it.documents){
                    val data = doc.toObject(categoryModel::class.java)
                    categoryList.add(data!!.name!!)
                }
                categoryList.add(0,"Select Category")

                val arraAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item_layout,categoryList)
                binding.productCategoryDropdown.adapter = arraAdapter
        }
    }

    private fun clear(){
        binding.apply {
            productNameEdt.text!!.clear()
            productDesEdt.text!!.clear()
            productPriceEdt.text!!.clear()
            productQtEdt.text!!.clear()
            list.clear()
            listImages.clear()
            productCoverImg.setImageURI(null)
            coverImage =null
            coverImgURl = null
        }
    }
}