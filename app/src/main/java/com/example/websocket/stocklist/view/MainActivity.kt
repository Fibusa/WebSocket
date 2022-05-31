package com.example.websocket.stocklist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.websocket.stocklist.domain.MainViewModel
import com.example.websocket.StockApplication
import com.example.websocket.databinding.ActivityMainBinding
import com.example.websocket.stockinfo.view.StockInfoActivity
import com.example.websocket.stocklist.domain.CommonData
import com.example.websocket.stocklist.domain.MainState
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = (application as StockApplication).mainViewModel
        val adapter = CommonAdapter{ stock -> adapterOnClick(stock) }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        viewModel.startObserve()
        binding.progressBar.visibility = View.INVISIBLE
        binding.errorButton.visibility = View.INVISIBLE

        viewModel.flowData.observe(this, Observer {
                when (it) {
                    is MainState.Load -> {
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorButton.visibility = View.INVISIBLE
                    }
                    is MainState.Success -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorButton.visibility = View.INVISIBLE
                        adapter.show(it.data)
                    }
                    is MainState.Error -> {
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorButton.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, it.e, Toast.LENGTH_LONG).show()
                    }
                }
            })

        lifecycleScope.launch {
            viewModel.page.collectLatest {
                binding.page.text = (it+1).toString()
                binding.buttonBefore.isEnabled = it != 0
            }
        }

        binding.searchEditText.addTextChangedListener {
            viewModel.page.value = 0
            viewModel.setSearchBy(it.toString())
        }

        binding.buttonNext.setOnClickListener {
            viewModel.stopFlow()
            viewModel.page.value += 1
            viewModel.startObserve()
        }

        binding.buttonBefore.setOnClickListener {
            viewModel.stopFlow()
            if(viewModel.page.value != 0)
                viewModel.page.value -= 1
            viewModel.startObserve()
        }

        binding.errorButton.setOnClickListener {
            viewModel.stopFlow()
            viewModel.startObserve()
        }
    }
    private fun adapterOnClick(commonData: CommonData) {
        val intent = Intent(this, StockInfoActivity::class.java )
        intent.putExtra("symbol", commonData.symbol)
        this.startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopFlow()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopFlow()
    }

}