package com.example.doan3admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.doan3admin.adapter.OrderlineAdapter
import com.example.doan3admin.databinding.ActivityOrderDetailsBinding
import com.example.doan3admin.model.OrderLineModel
import com.example.doan3admin.model.OrderModel
import com.google.firebase.firestore.FirebaseFirestore

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var list:ArrayList<OrderLineModel>
    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var orderID:String
    private lateinit var order:OrderModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        list = ArrayList()
        orderID = intent.getStringExtra("orderID").toString()

        FirebaseFirestore.getInstance().collection("orders").
            document(orderID)
            .get()
            .addOnSuccessListener {

                list.clear()

                order = it.toObject(OrderModel::class.java)!!
                Log.e("ORDER", order.toString())


                FirebaseFirestore.getInstance().collection("orders")
                    .document(orderID).collection("orderLines")
                    .get().addOnSuccessListener {
                        for (doc in it){
                            list.add(doc.toObject(OrderLineModel::class.java))
                        }

                        if (!list.isEmpty() && it != null){
                            binding.itemDetailsRv.visibility = View.VISIBLE
                            binding.emptyOrderDetailsTv.visibility = View.INVISIBLE
                            binding.linearLayout.visibility = View.VISIBLE
                            binding.itemDetailsRv.adapter = OrderlineAdapter(list,this)

                            binding.orderIDTv.text = "Order ID: \n ${order.orderID}"
                            binding.orderPriceTv.text = "Price: $${order.price}"
                            binding.orderStatusTv.text = "Status: ${order.status}"



                        }
                    }




        }



        setContentView(binding.root)
    }
}