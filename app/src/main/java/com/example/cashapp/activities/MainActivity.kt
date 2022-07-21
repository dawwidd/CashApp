package com.example.cashapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashapp.R
import com.example.cashapp.adapters.TransactionAdapter
import com.example.cashapp.databinding.ActivityMainBinding
import com.example.cashapp.services.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getUserTransactions(1)
            } catch (e: IOException) {
                Log.e("MainActivity", "IOException, you may not have internet connection")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("MainActivity", "Invalid http response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                transactionAdapter.transactions = response.body()!!
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
}