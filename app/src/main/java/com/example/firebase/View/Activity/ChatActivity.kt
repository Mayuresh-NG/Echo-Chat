package com.example.firebase.View.Activity

import ChatActivityViewModel
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.View.Adapter.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerViewChatMessages: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var editTextMessageInput: EditText
    private lateinit var buttonSend: ImageButton
    private lateinit var chatViewModel: ChatActivityViewModel
    private var receiverEmail: String? = null
    private var senderEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val textViewChatTitle = findViewById<TextView>(R.id.textViewChatTitle)
        recyclerViewChatMessages = findViewById(R.id.recyclerViewChatMessages)
        editTextMessageInput = findViewById(R.id.editTextMessageInput)
        buttonSend = findViewById(R.id.buttonSend)

        chatViewModel = ViewModelProvider(this)[ChatActivityViewModel::class.java]

        receiverEmail = intent.getStringExtra("email")
        senderEmail = chatViewModel.getCurrentUserEmail()

        textViewChatTitle.text = receiverEmail

        chatAdapter = ChatAdapter()
        recyclerViewChatMessages.layoutManager = LinearLayoutManager(this)
        recyclerViewChatMessages.adapter = chatAdapter

        buttonSend.setOnClickListener {
            sendMessage()
        }

        chatViewModel.listenForMessages(senderEmail!!, receiverEmail!!) { message ->
            chatAdapter.addMessage(message)
        }
    }

    private fun sendMessage() {
        val messageContent = editTextMessageInput.text.toString()
        if (messageContent.isNotEmpty() && senderEmail != null && receiverEmail != null) {
            chatViewModel.sendMessage(senderEmail!!, receiverEmail!!, messageContent)
            editTextMessageInput.text.clear()
        }
    }
}