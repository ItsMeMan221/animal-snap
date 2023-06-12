package com.albertukrida.capstoneproject_animalsnap.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DataItem
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.ItemHistoryBinding
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.squareup.picasso.Picasso

class HistoryAdapter(private val activity: Activity) : PagingDataAdapter<DataItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(activity, binding, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val activity: Activity, private val binding: ItemHistoryBinding, _view: View) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var avatar: String
        private lateinit var date: String
        private lateinit var name: String
        private lateinit var status: String
        private var mContext = _view.context

        fun bind(data: DataItem) {
            avatar = data.gambarHewan
            date = data.tanggalKlasifikasi
            name = data.namaHewan
            status = data.statusHewan

            Picasso.get().load(avatar).into(binding.ivItemPhoto)
            binding.tvItemDate.text = Utils(mContext).getDate(date)
            binding.tvItemName.text = name
            binding.tvItemStatus.text = buildString {
                append("status: ")
                append(status)
            }

            if(status == "Rendah"){
                binding.tvItemStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_700))
            }else{
                binding.tvItemStatus.setTextColor(ContextCompat.getColor(mContext, R.color.dark_red))
            }

            binding.item.setOnClickListener {
                ApiCall(mContext).getClassifyResult(activity, data.idClassification)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem.idClassification == newItem.idClassification
            }
        }
    }
}