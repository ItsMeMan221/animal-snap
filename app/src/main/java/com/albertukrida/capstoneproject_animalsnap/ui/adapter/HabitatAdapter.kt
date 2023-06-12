package com.albertukrida.capstoneproject_animalsnap.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.AllHabitatsResponseItem
import com.albertukrida.capstoneproject_animalsnap.databinding.ItemHabitatBinding
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.squareup.picasso.Picasso

class HabitatAdapter(private var listHabitat: List<AllHabitatsResponseItem>) : RecyclerView.Adapter<HabitatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHabitatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listHabitat[position]
        holder.bind(data)
    }

    class MyViewHolder(private val binding: ItemHabitatBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var avatar: String
        private lateinit var name: String
        private lateinit var desc: String

        fun bind(data: AllHabitatsResponseItem) {
            avatar = data.gambar
            name = data.nama
            desc = data.deskripsi

            Picasso.get().load(avatar).into(binding.ivItemPhoto)
            binding.tvItemName.text = name

            binding.item.setOnClickListener { view ->
                showClassDesc(view)
            }
        }

        private fun showClassDesc(view: View){
            val showDialog = AlertDialog.Builder(view.context)
                .setView(R.layout.dialog_class_desc)
                .setTitle(view.context.resources.getString(R.string.habitat_dialog))
                .create()
            showDialog.show()

            val picture = showDialog.findViewById<ImageView>(R.id.iv_picture)!!
            val tvName = showDialog.findViewById<TextView>(R.id.tv_class)!!
            val tvDesc = showDialog.findViewById<TextView>(R.id.tv_desc)!!

            Picasso.get().load(avatar).into(picture)
            tvName.text = name
            tvDesc.text = desc

            picture.setOnClickListener { Utils(view.context).showPicture(avatar) }
        }
    }

    override fun getItemCount() = listHabitat.size
}