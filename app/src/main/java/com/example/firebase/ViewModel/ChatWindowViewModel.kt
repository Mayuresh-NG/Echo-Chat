// ChatWindowViewModel.kt
package com.example.firebase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebase.Model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ChatWindowViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val currentUserEmail = Firebase.auth.currentUser?.email

    private val _users = MutableLiveData<List<User>?>()
    val users: MutableLiveData<List<User>?> get() = _users

    init {
        fetchUsersFromFirestore()
    }

    private fun fetchUsersFromFirestore() {
        db.collection("users")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val userList = querySnapshot?.documents?.mapNotNull { it.toObject(User::class.java) }
                val filteredUserList = userList?.filter { it.email != currentUserEmail }
                _users.value = filteredUserList
            }
            .addOnFailureListener {
            }
    }
}
