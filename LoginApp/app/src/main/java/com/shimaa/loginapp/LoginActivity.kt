package com.shimaa.loginapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.facebook.*
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import org.json.JSONObject
import java.util.*




class LoginActivity : AppCompatActivity() {
    lateinit var usernameET: EditText
    lateinit var passET: EditText
    private lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var btnSignup: TextView
    lateinit var btnLogin: Button
    lateinit var facebook:LoginButton
    lateinit var gPlus:SignInButton
    lateinit var username: String
    lateinit var password: String
    val GOOGLE_LOG_IN_RC:Int=2
    lateinit var callbackManager:CallbackManager
    val EMAIL :String= "email"
    lateinit var mGoogleSignInClient:GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        callbackManager = CallbackManager.Factory.create()

        auth = (FirebaseAuth.getInstance()) as FirebaseAuth

        if (auth.currentUser != null) {
            //user is logged in
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            this.finish()
        }

        setContentView(R.layout.activity_login)

        usernameET = findViewById(R.id.nameinET)
        passET = findViewById(R.id.passinET)
        progressBar = findViewById(R.id.progressBar)
        btnSignup = findViewById(R.id.signupTV)
        btnLogin = findViewById(R.id.login)
        facebook=findViewById(R.id.facebook)
        gPlus=findViewById(R.id.gmail)
        facebook.setReadPermissions(Arrays.asList(EMAIL))

        btnSignup.setOnClickListener {
            var intent =Intent(this@LoginActivity,SignupActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            username = usernameET.getText().toString()
            password = passET.getText().toString()
            val checker = username.isEmpty()|| password.isEmpty()||!isEmailValid(username)||password.length<6

            if (checker) {
                Toast.makeText(this@LoginActivity, "Email or Password is incorrect!",
                        Toast.LENGTH_SHORT).show()
                return@OnClickListener

            }

            progressBar.setVisibility(View.VISIBLE)

            //authenticate user
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                progressBar.setVisibility(View.GONE)
                if (!it.isSuccessful) {
                    // there was an error

                        Toast.makeText(this@LoginActivity,"auth failed", Toast.LENGTH_LONG).show()


                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
            }
        })


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        gPlus.setOnClickListener {
            signIn()
        }


        // Callback registration
        facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                var accessToken=loginResult.accessToken
                var graphRequest=GraphRequest.newMeRequest(accessToken) { jsonObject: JSONObject, graphResponse: GraphResponse ->
                }
                var bundle=Bundle()
                bundle.putString("fields","email,id")
                graphRequest.parameters=bundle
                graphRequest.executeAsync()
                Toast.makeText(this@LoginActivity,"login with facebook Successfully",Toast.LENGTH_LONG).show()
                var intent =Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
            }
            override fun onCancel() {
                // App code
            }
            override fun onError(exception: FacebookException) {
                // App code
            }
        })
    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOG_IN_RC) {
            var task:Task<GoogleSignInAccount>  = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                Toast.makeText(this@LoginActivity,"login with google Successfully",Toast.LENGTH_LONG).show()
                var intent =Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }

    }

     fun isEmailValid(email: String): Boolean {
         return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    override fun onResume() {
        super.onResume()
        progressBar.setVisibility(View.GONE)

    }

}