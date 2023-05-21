package com.example.openchat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openchat.R
import com.example.openchat.adapters.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : TextView
    private lateinit var sentButton : Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var DbRef : DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        DbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageTextView)
        sentButton = findViewById(R.id.sentButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this , messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)


        // used for keyboard bt
//        val rootView = findViewById<View>(android.R.id.content)
//        rootView.viewTreeObserver.addOnGlobalLayoutListener {
//            val rect = Rect()
//            rootView.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = rootView.height
//            val keyboardHeight = screenHeight - rect.bottom
//
//            if (keyboardHeight > screenHeight * 0.15) {
//                (chatRecyclerView.layoutManager as LinearLayoutManager).reverseLayout = false
//                adjustRecyclerViewHeightForKeyboard(true)
//            } else {
//                (chatRecyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
//                adjustRecyclerViewHeightForKeyboard(false)
//            }
//            chatRecyclerView.requestLayout()
//        }


        //logic for adding data to recycler view
        DbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                        chatRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        // logic for adding the message to the database
        sentButton.setOnClickListener {

            val message = messageBox.text.toString()
            val messageObject = Message(message , senderUid)

            DbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    DbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.text = ""
        }


    }
//    private fun adjustRecyclerViewHeightForKeyboard(isKeyboardOpen: Boolean) {
//        val layoutParams = chatRecyclerView.layoutParams as ViewGroup.MarginLayoutParams
//
//        if (isKeyboardOpen) {
//            val keyboardHeight = calculateKeyboardHeight()
//            layoutParams.bottomMargin = keyboardHeight
//        } else {
//            layoutParams.bottomMargin = 0
//        }
//        chatRecyclerView.layoutParams = layoutParams
//    }
//    private fun calculateKeyboardHeight(): Int {
//        val rect = Rect()
//        val rootView = findViewById<View>(android.R.id.content)
//        rootView.getWindowVisibleDisplayFrame(rect)
//        val screenHeight = rootView.height
//        val keyboardHeight = screenHeight - rect.bottom
//        return keyboardHeight
//    }


}