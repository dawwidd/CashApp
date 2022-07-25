package com.example.cashapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.cashapp.R
import com.example.cashapp.databinding.ActivityAddTransactionBinding
import com.example.cashapp.databinding.ActivityMainBinding
import com.example.cashapp.services.RetrofitInstance
import com.example.cashapp.utils.Toaster
import retrofit2.HttpException
import java.io.IOException


class AddTransaction : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            binding.progressBarAddTransaction.isVisible = true
            val response = try {
                RetrofitInstance.api.getCategories()
            } catch (e: IOException) {
                Log.e("AddTransaction", "IOException, you may not have internet connection")
                binding.progressBarAddTransaction.isVisible = false
                Toaster.toast("You may not have internet connection or server is not available", applicationContext)
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("AddTransaction", "Invalid http response")
                binding.progressBarAddTransaction.isVisible = false
                Toaster.toast("Invalid http response", applicationContext)
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                val rgp = binding.rgCategories

                for (category in response.body()!!) {
                    val radioButtonView = layoutInflater.inflate(R.layout.category_button, null, false)
                    val rbn = radioButtonView.findViewById<RadioButton>(R.id.rbCategory)
                    rbn.id = View.generateViewId()
                    rbn.text = category.name
                    rgp.addView(rbn)
                }
            }
            else {
                Log.e("AddTransaction", "Response not successful")
            }
            binding.progressBarAddTransaction.isVisible = false
        }
    }
}