package com.shimaa.loginapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var signOut: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        signOut = findViewById(R.id.logout)

        signOut.setOnClickListener { SignOut() }
    }

    fun SignOut() {
        auth.signOut()
        startActivity(Intent(this,LoginActivity::class.java))
        this.finish()
    }

}
