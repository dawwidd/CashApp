package com.example.cashapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashapp.R
import com.example.cashapp.adapters.TransactionAdapter
import com.example.cashapp.databinding.ActivityMainBinding
import com.example.cashapp.services.RetrofitInstance
import com.example.cashapp.utils.Toaster
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var transactionAdapter: TransactionAdapter

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId", 0)

        val addTransactionButton = findViewById<Button>(R.id.buttonAddTransaction)

        addTransactionButton.setOnClickListener() {
            redirectToAddTransactionActivity(userId)
        }

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            binding.noTransactionsAlert.isVisible = false
            val response = try {
                RetrofitInstance.api.getUserTransactions(userId)
            } catch (e: IOException) {
                Log.e("MainActivity", "IOException, you may not have internet connection")
                binding.progressBar.isVisible = false
                Toaster.toast("You may not have internet connection or server is not available", applicationContext)
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("MainActivity", "Invalid http response")
                binding.progressBar.isVisible = false
                Toaster.toast("Invalid http response", applicationContext)
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                transactionAdapter.transactions = response.body()!!
            }
            else if (response.isSuccessful && response.body() != null && response.body()!!.isEmpty()){
                binding.noTransactionsAlert.isVisible = true
            }
            else {
                Log.e("MainActivity", "Response not successful")
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun setupRecyclerView() = binding.rvTransactions.apply {
        transactionAdapter = TransactionAdapter()
        adapter = transactionAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun redirectToAddTransactionActivity(userId: Int) {
        val intent = Intent(this, AddTransaction::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}