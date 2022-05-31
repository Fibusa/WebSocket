package com.example.websocket

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.websocket.stocklist.domain.CommonData

class StockDataView: ConstraintLayout {

    private lateinit var textSymbol : TextView
    private lateinit var textPrice : TextView
    private lateinit var textChange : TextView
    private lateinit var textChangeP : TextView

    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    //endregion
    private fun init(attrs: AttributeSet) {
        (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.common_data_item, this, true)
        textSymbol = findViewById(R.id.textName)
        textPrice = findViewById(R.id.textPrice)
        textChange = findViewById(R.id.textPriceChange)
        textChangeP = findViewById(R.id.textPriceChangeP)
    }

    fun showStock(data: CommonData){
        textSymbol.text = data.symbol
        textPrice.text = String.format("%.2f",data.price) + "$"
        textChange.text = String.format("%.2f",data.priceChange) + "$"
        textChangeP.text = String.format("%.2f",data.priceChangePercent) + "%"
        if(data.priceChange >= 0 ){
            textChange.setTextColor(resources.getColor(R.color.green))
            textChangeP.setTextColor(resources.getColor(R.color.green))
        }
        else{
            textChange.setTextColor(resources.getColor(R.color.red))
            textChangeP.setTextColor(resources.getColor(R.color.red))
        }
    }
}