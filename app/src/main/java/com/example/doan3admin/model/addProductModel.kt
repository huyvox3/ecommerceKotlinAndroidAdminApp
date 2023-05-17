package com.example.doan3admin.model

class addProductModel(var productName: String? = "",
                   var productDescription: String? = "",
                   var productCoverImage: String? = "",
                   var productCategory: String? = "",
                   var productId: String? = "",
                   var productQt: String? = "",
                   var productP: String? ="",
                   var productImgs: ArrayList<String>?
) {

    constructor() : this(null, null, null, null, null, null, null, null)

}