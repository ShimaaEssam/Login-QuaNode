package com.shimaa.loginapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity()  {
    lateinit var username: EditText
    lateinit var pass: EditText
    lateinit var signUp: Button
    lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    lateinit var signinTV:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = (FirebaseAuth.getInstance()as  FirebaseAuth)

        signUp = findViewById(R.id.signup)
        username = findViewById(R.id.nameupET)
        pass = findViewById(R.id.passupET)
        progressBar = findViewById(R.id.progressBar)
        signinTV=findViewById(R.id.sIn)


        signinTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }

        signUp.setOnClickListener (View.OnClickListener {
            val email = username.getText().toString().trim()
            val password = pass.getText().toString().trim()
            val checker = email.isEmpty()|| password.isEmpty()||!isEmailValid(email)||password.length<6

            if (checker) {
                Toast.makeText(this@SignupActivity, "Email or Password is incorrect!",
                        Toast.LENGTH_SHORT).show()
                return@OnClickListener

            }
            progressBar.visibility = View.VISIBLE

            //create user
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                progressBar.visibility = View.GONE
                if (!it.isSuccessful) {
                    Toast.makeText(this, "Authen" +
                            "tication failed." + it.getException(),
                            Toast.LENGTH_LONG).show()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    //modify it to home
                    this.finish()
                }
            }

        })

    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.GONE

    }


}

