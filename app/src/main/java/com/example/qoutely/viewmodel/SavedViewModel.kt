package com.example.qoutely.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.qoutely.network.model.Quote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SavedViewModel : ViewModel() {
    var allQuotes by mutableStateOf<List<Quote>>(emptyList())
    var targetQuotes by mutableStateOf<List<Quote>>(emptyList())
    val db = Firebase.firestore

    var categories by mutableStateOf<List<String>>(emptyList())
    var authors by mutableStateOf<List<String>>(emptyList())

    var selectedCategories: List<String> by mutableStateOf(emptyList())
    var selectedAuthors: List<String> by mutableStateOf(emptyList())

    init{
        getUserQuotes()
    }

    fun getUserQuotes(){
        db.collection("quotes")
            .whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                val quotes = mutableListOf<Quote>()
                val categoriesSet = mutableSetOf<String>()
                val authorsSet = mutableSetOf<String>()

                documents.forEach {document->
                    val quote = document.toObject(Quote::class.java)
                    Log.d("quute", quote.toString())
                    quotes.add(quote)
                    categoriesSet.add(quote.category.toString())
                    authorsSet.add(quote.author.toString())
                }

                allQuotes = quotes.toList()
                categories = categoriesSet.toList()
                authors = authorsSet.toList()
                targetQuotes = quotes.toList()
            }
            .addOnFailureListener {

            }
    }

    /*
    fun removeQuote(quote: Quote){
        db.collection("quotes")
            .whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.email.toString())
            .whereEqualTo("id", quote.id)
            .get()
            .addOnSuccessListener {documents->
                for (document in documents){
                    db.collection("quotes").document(document.id).delete()
                        .addOnSuccessListener {
                            allQuotes = allQuotes.filter { it.id != quote.id }
                            targetQuotes = targetQuotes.filter { it.id != quote.id }

                            categories = allQuotes.map { it.category }.distinct() as List<String>
                            authors = allQuotes.map { it.author }.distinct() as List<String>

                        }
                        .addOnFailureListener {

                        }
                }
            }
    }*/
    fun removeQuote(quote: Quote, showMessage: (String) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser?.email

        db.collection("quotes")
            .whereEqualTo("user", currentUser)
            .whereEqualTo("id", quote.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("quotes").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            showMessage("Quote removed successfully")
                            allQuotes = allQuotes.filter { it.id != quote.id }
                            targetQuotes = targetQuotes.filter { it.id != quote.id }

                            categories = allQuotes.map { it.category }.distinct() as List<String>
                            authors = allQuotes.map { it.author }.distinct() as List<String>

                        }
                        .addOnFailureListener {
                            showMessage("Failed to remove quote")
                        }
                }
            }
            .addOnFailureListener {
                showMessage("Error finding the quote to remove")
            }
    }

    fun updateCategories(category: String){
        selectedCategories = if(selectedCategories.contains(category)){
            selectedCategories.filter { it != category }
        }else{
            selectedCategories + category
        }
        updateTargetQuotes()
    }

    fun updateAuthors(author: String){
        selectedAuthors = if(selectedAuthors.contains(author)){
            selectedAuthors.filter { it != author }
        }else{
            selectedAuthors + author
        }
        updateTargetQuotes()
    }

    fun updateTargetQuotes(){
        targetQuotes = allQuotes.filter { quote ->
            (selectedAuthors.isEmpty() || selectedAuthors.contains(quote.author)) &&
                    (selectedCategories.isEmpty() || selectedCategories.contains(quote.category))
        }
    }

}