package com.albertukrida.capstoneproject_animalsnap.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.AllAnimalsResponseItem
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.ItemAnimalBinding
import com.squareup.picasso.Picasso

class AnimalAdapter(private var listAnimal: List<AllAnimalsResponseItem>, private val activity: Activity) : RecyclerView.Adapter<AnimalAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAnimalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, activity)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listAnimal[position]
        holder.bind(data)
    }

    class MyViewHolder(private val binding: ItemAnimalBinding, private val activity: Activity) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var avatar: String
        private lateinit var name: String
        private lateinit var desc: String

        fun bind(data: AllAnimalsResponseItem) {
            avatar = data.gambar
            name = data.nama
            desc = data.deskripsi

            Picasso.get().load(avatar).into(binding.ivAnimal)
            binding.tvName.text = name
            binding.tvDesc.text = desc

            binding.item.setOnClickListener { view ->
                ApiCall(view.context).getAnimalById(activity, data.animalId.toString())
            }
        }
    }

    override fun getItemCount() = listAnimal.size
}