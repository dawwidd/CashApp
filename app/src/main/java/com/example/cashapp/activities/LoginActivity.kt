package com.example.cashapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.cashapp.R
import com.example.cashapp.models.User
import com.example.cashapp.services.RetrofitInstance
import com.example.cashapp.utils.Toaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val noAccountButton = findViewById<Button>(R.id.noAccountButton)

        loginButton.setOnClickListener() {
            login()
        }

        noAccountButton.setOnClickListener() {
//            redirectToRegisterActivity()
        }
    }

    private fun login() {
        val editTextMail = findViewById<EditText>(R.id.editTextMail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email = editTextMail.text.toString()
        val password = editTextPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toaster.toast("All the fields have to be filled", applicationContext)
        }
        else {
            val user = User(email, password)

            lifecycleScope.launchWhenCreated {
                val response = try {
                    RetrofitInstance.api.login(user)
                } catch (e: IOException) {
                    Log.e("MainActivity", "IOException, you may not have internet connection")
                    Toaster.toast("You may not have internet connection or server is not available", applicationContext)
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e("MainActivity", "Invalid http response")
                    Toaster.toast("Invalid http response", applicationContext)
                    return@launchWhenCreated
                }

                if(response.isSuccessful && response.body() != null) {
//                    redirectToRegisterActivity()
                    Toaster.toast("Logged in successfully", applicationContext)
                }
                else {
                    val errorMessage = response.errorBody()!!.string()
                    Log.e("MainActivity", errorMessage)
                    Toaster.toast(errorMessage, applicationContext)
                }
            }
        }
    }

//    private fun redirectToRegisterActivity() {
//        val intent = Intent(this, RegisterActivity::class.java)
//        startActivity(intent)
//    }
}