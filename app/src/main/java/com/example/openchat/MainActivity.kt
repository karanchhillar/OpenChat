package com.example.openchat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.openchat.adapters.UserAdapter
import com.example.openchat.login.LoginPage
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerViewChats : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var userAdapter: UserAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutButton : Button = findViewById(R.id.logout_button)
        auth = FirebaseAuth.getInstance()

        userList = ArrayList()
        userAdapter = UserAdapter(this , userList)


        logoutButton.setOnClickListener {
            auth.signOut()

            val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            val intent = Intent(this , LoginPage::class.java)
            startActivity(intent)
            finish()
        }


//        this is not working , but can work
//        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
//
//        if (!isLoggedIn) {
//            // User is not logged in, navigate to the login activity
//            val intent = Intent(this, LoginPage::class.java)
//            startActivity(intent)
//            finish()
//        }
    }
}