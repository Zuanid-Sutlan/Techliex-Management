package com.techliexai.management.data.database

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.firestore

object FirebaseDatabase {
    fun getAccountReference(): CollectionReference = Firebase.firestore.collection("accounts")
    fun getProductHuntReference(): CollectionReference = Firebase.firestore.collection("products")
    fun getOrderReference(): CollectionReference = Firebase.firestore.collection("orders")
}