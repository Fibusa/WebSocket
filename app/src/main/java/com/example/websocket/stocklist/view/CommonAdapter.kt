package com.example.websocket.stocklist.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket.stocklist.domain.CommonData
import com.example.websocket.R
import com.example.websocket.StockDataView

class CommonAdapter(
    private val onClick: (CommonData) -> Unit
):
    RecyclerView.Adapter<CommonAdapter.CommonViewHolder>() {

    private var list: List<CommonData> = listOf()

    fun show(listData: List<CommonData>){
        list = listData
        Log.d("flow3","$list")
        notifyDataSetChanged()

    }

    inner class CommonViewHolder(view: View, val onClick: (CommonData) -> Unit): RecyclerView.ViewHolder(view){
        private val stock = itemView.findViewById<StockDataView>(R.id.stockData)
        init{
            itemView.setOnClickListener {
                onClick(list[adapterPosition])
            }
        }

        fun bind(data: CommonData){
            stock.showStock(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.item, parent, false)
        return CommonViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}