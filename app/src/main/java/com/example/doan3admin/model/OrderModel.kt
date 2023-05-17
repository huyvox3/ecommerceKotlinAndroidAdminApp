package com.example.doan3admin.model

data class OrderModel(
    var price: String? ="",
    var status:String? = "",
    var userID:String? = "",
    var orderID:String? = "",
    var orderLines: ArrayList<OrderLineModel>? = ArrayList()
) {


}
