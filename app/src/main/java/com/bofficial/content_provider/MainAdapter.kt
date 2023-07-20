package com.bofficial.content_provider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val list:List<Contacts>) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textname: TextView = itemView.findViewById(R.id.tvName)
        val textnumber: TextView = itemView.findViewById(R.id.tvNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.MyViewHolder, position: Int) {
        val contactlist = list[position]

        holder.textname.text = contactlist.name
        holder.textnumber.text = contactlist.number
    }

    override fun getItemCount(): Int {
       return list.size
    }

}