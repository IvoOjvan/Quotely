package com.example.qoutely.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.qoutely.model.HomeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel(
    private val navController: NavController
) : ViewModel() {
    val db = Firebase.firestore
    var state by mutableStateOf(HomeModel())
    var notificationShown by mutableStateOf(false)

    init{
        getCurrentUser()
    }

    fun getCurrentUser(){
        val user = FirebaseAuth.getInstance().currentUser?.email

        db.collection("users")
            .whereEqualTo("email", user)
            .get()
            .addOnSuccessListener { documents->
                for(document in documents){
                    state = state.copy(
                        firstname = document.getString("Name").toString(),
                        lastname = document.getString("Lastname").toString()
                    )
                }
            }
    }

    fun logoutUser(){
        FirebaseAuth.getInstance().signOut()
        notificationShown = false
        navController.popBackStack()
    }


}