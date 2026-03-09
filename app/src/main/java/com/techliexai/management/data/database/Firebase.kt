package com.techliexai.management.data.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Firebase {

    fun getAccountReference(): DatabaseReference = FirebaseDatabase.getInstance().getReference("accounts")
    fun getProductHuntReference(): DatabaseReference = FirebaseDatabase.getInstance().getReference("products")
    fun getOrderReference(): DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")


}