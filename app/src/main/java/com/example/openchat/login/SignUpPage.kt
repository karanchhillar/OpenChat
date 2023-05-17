package com.example.openchat.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.openchat.MainActivity
import com.example.openchat.R
import com.example.openchat.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpPage : AppCompatActivity() {

    private lateinit var name : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var signupButton: Button
    private lateinit var auth : FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        name = findViewById(R.id.editTextName2)
        email = findViewById(R.id.editTextEmail2)
        password = findViewById(R.id.editTextPassword2)
        signupButton = findViewById(R.id.buttonSignUp2)

        auth = FirebaseAuth.getInstance()

        signupButton.setOnClickListener {
            if (name.text.toString().isEmpty() || email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(this, "Fill all Column's", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email.text.toString() , password.text.toString())
                .addOnCompleteListener { task->
                    if (task.isSuccessful){

                        addUserToDatabase(name.text.toString() , email.text.toString() , auth.currentUser?.uid!!)

                        Toast.makeText(this, "Created a new email", Toast.LENGTH_SHORT).show()
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

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        dbRef = FirebaseDatabase.getInstance().getReference()

        dbRef.child("user").child(uid).setValue(User(name , email, uid))
    }
}