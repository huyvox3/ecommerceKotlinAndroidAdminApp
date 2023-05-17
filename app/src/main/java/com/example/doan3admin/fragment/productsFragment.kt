package com.example.doan3admin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.doan3admin.R
import com.example.doan3admin.adapter.categoryAdapter
import com.example.doan3admin.adapter.productAdapter
import com.example.doan3admin.databinding.FragmentProductsBinding
import com.example.doan3admin.model.addProductModel
import com.example.doan3admin.model.categoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class productsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(layoutInflater)
        binding.progressBar2
        binding.floatingActionButton.setOnClickListener(){
            Navigation.findNavController(it).navigate((R.id.action_productsFragment_to_addProductsFragment))
        }

        getData()
        return binding.root
    }

    private fun getData() {
        val list = ArrayList<addProductModel>()
        val db = Firebase.firestore
        val collectionRef = db.collection("products")

        collectionRef.get()
            .addOnSuccessListener { documents ->
                list.clear()
                for (doc in documents) {
                    Log.e("doc", "getData: {${documents.size()}}", )
                    Log.e("converting", "data:{${doc.toObject(addProductModel::class.java)}} ", )
                    val data = doc.toObject(addProductModel::class.java)

                    if (data != null) {
                        list.add(data)
                        Log.e("Firestore", "Converted document to ${data.productName} and ${data.productCoverImage}")
                    } else {
                        Log.e("Firestore", "Error converting document to categoryModel")
                    }
                    Log.e("list check", "list: ${list.size}", )
                }


               binding.productRv.adapter = productAdapter(list)

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }




    }
}