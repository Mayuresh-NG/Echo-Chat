import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.firebase.Model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class ChatActivityViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun getCurrentUserEmail(): String? {
        return Firebase.auth.currentUser?.email
    }

    fun sendMessage(senderEmail: String, receiverEmail: String, messageContent: String) {
        val message = Message(
            sender = senderEmail,
            receiver = receiverEmail,
            content = messageContent,
            timestamp = System.currentTimeMillis()
        )
        db.collection("messages").add(message)
    }

    fun listenForMessages(senderEmail: String, receiverEmail: String, onMessageReceived: (Message) -> Unit) {
        db.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val message = dc.document.toObject(Message::class.java)
                            if ((message.sender == senderEmail && message.receiver == receiverEmail) ||
                                (message.sender == receiverEmail && message.receiver == senderEmail)
                            ) {
                                onMessageReceived(message)
                            }
                        }
                        else -> {
                            Log.d(TAG, "listenForMessages: DocumentChange.Type.MODIFIED")
                        }
                    }
                }
            }
    }
}