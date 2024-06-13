package com.example.qoutely.viewmodel

import android.util.Log
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qoutely.network.RetrofitClient
import com.example.qoutely.network.model.Quote
import com.example.qoutely.network.model.QuoteResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale.Category

class SearchViewModel : ViewModel() {
    var qoutesReponse: QuoteResponse by mutableStateOf(QuoteResponse())
    var errorMessage: String by mutableStateOf("")
    var targetQuotes: List<Quote> by mutableStateOf(emptyList())

    val db = Firebase.firestore

    init{
        getAllQoutes()
    }

    fun getAllQoutes(){
        viewModelScope.launch {
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.getAllQoutes()
                qoutesReponse = apiResponse

                //
                qoutesReponse.qoutes?.add(
                    Quote(
                        id = 121,
                        category = "Life",
                        quote = "It ain’t about how hard you hit. It’s about how hard you can get hit " +
                                "and keep moving forward; how much you can take and keep moving forward. " +
                                "That’s how winning is done!",
                        description = "It's all about how much you can take and keep moving.",
                        author = "Rocky Balboa"
                    )
                )
            }catch (e: Exception){
                errorMessage = e.message!!.toString()
            }
        }
    }


    fun getQuotesByCategory(category: String){
        targetQuotes = qoutesReponse.qoutes?.filter{ it.category.equals(category)} ?: emptyList()
        Log.i("Quotes", targetQuotes.toString())
    }

    fun saveQuote(quote: Quote){
        var currentUser = FirebaseAuth.getInstance().currentUser?.email
        var quoteForSaving = hashMapOf(
            "id" to quote.id,
            "quote" to quote.quote.toString(),
            "author" to quote.author.toString(),
            "description" to quote.description.toString(),
            "category" to quote.category.toString(),
            "user" to currentUser.toString()
        )

        db.collection("quotes")
            .whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.email)
            .whereEqualTo("id", quote.id)
            .get()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                db.collection("quotes")
                    .add(quoteForSaving)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }
    }

    fun saveQuote(quote: Quote, showMessage: (String) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser?.email
        val quoteForSaving = hashMapOf(
            "id" to quote.id,
            "quote" to quote.quote.toString(),
            "author" to quote.author.toString(),
            "description" to quote.description.toString(),
            "category" to quote.category.toString(),
            "user" to currentUser.toString()
        )

        db.collection("quotes")
            .whereEqualTo("user", currentUser)
            .whereEqualTo("id", quote.id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    db.collection("quotes")
                        .add(quoteForSaving)
                        .addOnSuccessListener {
                            showMessage("Quote saved successfully")
                        }
                        .addOnFailureListener {
                            showMessage("Failed to save quote")
                        }
                } else {
                    showMessage("Quote already exists")
                }
            }
            .addOnFailureListener {
                showMessage("Error checking existing quotes")
            }
    }
}