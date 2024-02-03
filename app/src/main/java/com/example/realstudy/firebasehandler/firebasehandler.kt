package com.example.realstudy.firebasehandler

import com.google.firebase.database.FirebaseDatabase

class FirebaseHandler() {
    val database = FirebaseDatabase.getInstance("https://studyreal-98599-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users/userId1/friends/friendId1")

    fun helloWorld() {
        myRef.setValue("Hello, World")
    }
}
fun main(){

}