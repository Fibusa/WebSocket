package com.example.websocket.stockinfo.view

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.websocket.R
import com.example.websocket.StockApplication
import com.example.websocket.databinding.ActivityStockInfoBinding
import com.example.websocket.stockinfo.domain.InfoViewData
import com.example.websocket.stockinfo.domain.InfoViewModel
import com.example.websocket.stockinfo.domain.ScreenState
import com.google.android.flexbox.FlexboxLayout

class StockInfoActivity : AppCompatActivity() {

    private lateinit var viewModel: InfoViewModel
    private lateinit var binding: ActivityStockInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = (application as StockApplication).infoViewModel
        viewModel.loadData(intent.getStringExtra("symbol")!!)

        viewModel.livedata.observe(this, Observer {
            when(it){
                is ScreenState.Load -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorButton.visibility = View.INVISIBLE
                    dataVisibility(View.INVISIBLE)
                }
                is ScreenState.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    dataVisibility(View.VISIBLE)
                    postData(it.data)
                }
                is ScreenState.Error ->{
                    dataVisibility(View.INVISIBLE)
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.errorButton.visibility = View.VISIBLE
                    Toast.makeText(this,it.e.message,Toast.LENGTH_LONG).show()

                }
            }
        })

        binding.button.setOnClickListener {
            viewModel.setFavorite(intent.getStringExtra("symbol")!!)
        }

        binding.errorButton.setOnClickListener {
            viewModel.loadData(intent.getStringExtra("symbol")!!)
        }

        viewModel.buttonLiveData.observe(this, Observer {
            if(it == 1){
                binding.button.setText(R.string.remove_to)
                binding.button.setBackgroundColor(resources.getColor(R.color.gray,null))
            }else{
                binding.button.setText(R.string.add_to)
                binding.button.setBackgroundColor(resources.getColor(R.color.green,null))
            }
        })
    }

    private fun dataVisibility(visibility: Int){
        binding.flexBox.visibility = visibility
        binding.imageView.visibility = visibility
        binding.symbolTextView.visibility = visibility
        binding.button.visibility = visibility
    }

    private fun postData(data: InfoViewData){
        binding.symbolTextView.text = data.name
        binding.exchangeTextView.text = data.exchange
        binding.countryTextView.text = data.country
        binding.currentTextView.text = data.currency
        binding.industryTextView.text = data.industry
        binding.imageView.load(data.logo){
            size(256,256)
        }
    }

}