package com.example.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination.databinding.ItemListBinding

class Adapter:RecyclerView.Adapter<Adapter.UsersViewHolder>() {
    private val list = ArrayList<User>()
    inner class UsersViewHolder(private val binding: ItemListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            with(binding){
                tvEmail.text = user.email
                tvName.text = user.first_name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() =list.size

    fun addList(items: ArrayList<User>){
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}