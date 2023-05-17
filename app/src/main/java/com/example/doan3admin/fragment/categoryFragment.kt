package com.example.doan3admin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

import com.example.doan3admin.R

import com.example.doan3admin.adapter.categoryAdapter
import com.example.doan3admin.databinding.FragmentCategoryBinding
import com.example.doan3admin.model.categoryModel
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*





class categoryFragment : Fragment() {
    private  lateinit var binding: FragmentCategoryBinding
    private  var imageUrl: Uri? = null
    private lateinit var dialog: Dialog
    private lateinit var adapter: categoryAdapter
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        getData()
        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            uploadBtn.setOnClickListener {
              validateData(binding.categoryName.text.toString())
            }
        }

        return binding.root

    }

    private fun getData() {
        val list = ArrayList<categoryModel>()
        val db = Firebase.firestore
        val collectionRef = db.collection("categories")
//        db.collection("items")
//            .get()
//            .addOnSuccessListener { result ->
//                val items = result.map {
//                    val item = it.toObject(categoryModel::class.java)
//                    item
//                }
//                binding.categoryRV.adapter = cateogryAdapter(list)
//            }
//            .addOnFailureListener { exception ->
//                Log.e(tag, "Error getting items", exception)
//            }


        val list1 =  ArrayList<categoryModel>()

        collectionRef.get()
            .addOnSuccessListener { documents ->
              list1.clear()

                for (document in documents) {
                    val data = document.toObject(categoryModel::class.java)
                    if (data != null) {
                        list1.add(data)


                    } else {
                        Log.e("Firestore", "Error converting document to categoryModel")
                    }
                }
                Log.d("Firestore", "Retrieved ${list1.size} documents")


                binding.categoryRV.adapter = categoryAdapter(list1)

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }




    }


    private fun validateData(categoryName: String) {
            if (imageUrl == null) {
            Toast.makeText(requireContext(),"Please select image",Toast.LENGTH_SHORT).show()
            }
            if (categoryName.isEmpty()){
                Toast.makeText(requireContext(),"Please provide category name",Toast.LENGTH_SHORT).show()

            }
            else{
                uploadImage(categoryName)
            }
        }

    private fun uploadImage(categoryName: String){
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference


        val imgRef = storageRef.child("categories/$fileName")


        imgRef.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image->
                    storeData(categoryName,image.toString())
                }


            }.addOnFailureListener {

                Toast.makeText(requireContext(),"Something went wrong with the storage.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(categoryName: String, url: String) {
        val db = FirebaseFirestore.getInstance().collection("categories")
        val key = db.document().id
        val data = categoryModel(
            key,
            categoryName,
            url,
        )

        db.add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.download))
                binding.categoryName.text = null
                getData()
                Toast.makeText(requireContext(),"Category Uploaded",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Something went wong", Toast.LENGTH_SHORT).show()
            }
    }

}



