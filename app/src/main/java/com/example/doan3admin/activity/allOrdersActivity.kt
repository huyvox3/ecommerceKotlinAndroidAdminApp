package com.example.doan3admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.example.doan3admin.adapter.OrderAdapter
import com.example.doan3admin.databinding.ActivityAllOrdersBinding
import com.example.doan3admin.model.OrderLineModel
import com.example.doan3admin.model.OrderModel
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class allOrdersActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAllOrdersBinding
    private lateinit var orderModel: ArrayList<OrderModel>
    private lateinit var orderLine: ArrayList<OrderLineModel>

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)


        Log.e("ORDER Page", "onCreate: OK")
        binding = ActivityAllOrdersBinding.inflate(layoutInflater)
        orderModel = ArrayList()
        orderLine = ArrayList()
        val rv = binding.ordersRv
        val empty = binding.emptyTv

        FirebaseFirestore.getInstance().collection("orders")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    Log.e("DOC", doc.toObject(OrderModel::class.java).toString() )
                    orderModel.add(doc.toObject(OrderModel::class.java))
                }
                Log.e("List",orderModel.toString() )
                rv.adapter = OrderAdapter(orderModel,this)
                rv.visibility = View.VISIBLE

                empty.visibility = View.INVISIBLE

                Log.e("List",rv.adapter.toString() )

            }
            .addOnFailureListener {
                empty.visibility = View.VISIBLE
            }


        setContentView(binding.root)
    }







}