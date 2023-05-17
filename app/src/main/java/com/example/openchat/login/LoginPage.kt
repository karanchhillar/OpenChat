package com.example.openchat.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.openchat.MainActivity
import com.example.openchat.R
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private lateinit var Username : EditText
    private lateinit var Password : EditText
    private lateinit var LoginButton  : Button
    private lateinit var SignupButton  : Button
    private lateinit var ForgotPassword : TextView
    private lateinit var auth : FirebaseAuth

//    val SHARED_PREFS = "sharedPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        Username = findViewById(R.id.editTextUsername)
        Password = findViewById(R.id.editTextPassword)
        LoginButton = findViewById(R.id.buttonLogin)
        SignupButton = findViewById(R.id.buttonSignUp)
        ForgotPassword = findViewById(R.id.textViewForgotPassword)

        auth = FirebaseAuth.getInstance()

//        supportActionBar?.hide()

        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // User is logged in, navigate to the login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        SignupButton.setOnClickListener {
            val intent = Intent(this , SignUpPage::class.java)
            startActivity(intent)
        }

        LoginButton.setOnClickListener {
            val username = Username.text.toString()
            val password = Password.text.toString()
            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Fill all column's", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "You have logged in", Toast.LENGTH_SHORT).show()

//                        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        val intent = Intent(this , MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}