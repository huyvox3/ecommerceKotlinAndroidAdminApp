package com.example.doan3admin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import com.example.doan3admin.R
import com.example.doan3admin.adapter.sliderAdapter
import com.example.doan3admin.databinding.FragmentSliderBinding
import com.example.doan3admin.model.sliderModel
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class sliderFragment : Fragment() {

    private  lateinit var binding: FragmentSliderBinding
    private lateinit var list:ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>
    private lateinit var dialog: Dialog
    private lateinit var adapter: sliderAdapter
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val imageUrl = it.data!!.data
           if (imageUrl != null){
               list.add(imageUrl)
               adapter.notifyDataSetChanged()

           }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list = ArrayList()
        listImages = ArrayList()
        // Inflate the layout for this fragment
        binding = FragmentSliderBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            uploadBtn.setOnClickListener {
              uploadImage()
            }
            adapter =sliderAdapter(list,requireContext())
            rv.adapter = adapter
        }
        return binding.root
    }


    private var i = 0
    private fun uploadImage(){
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"

        val storage = FirebaseStorage.getInstance()

        val storageRef = storage.reference


        val imgRef = storageRef.child("slider/$fileName")


        imgRef.putFile(list[i])
            .addOnSuccessListener {
              it.storage.downloadUrl.addOnSuccessListener {image->
                  listImages.add(image!!.toString())
                  if (list.size == listImages.size){

                      storeData()
                  }else{
                      i++
                      uploadImage()
                  }
              }

            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with the storage.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db = FirebaseFirestore.getInstance().collection("slider")
        val data = sliderModel(
            listImages
        )

        db.document("items").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Slider Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Something went wong", Toast.LENGTH_SHORT).show()
            }
    }

}