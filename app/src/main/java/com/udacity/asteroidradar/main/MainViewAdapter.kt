package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.AsteroidTable
import com.udacity.asteroidradar.databinding.LinearViewItemBinding

class MainViewAdapter(val clickListener: AsteroidListener) :
    ListAdapter<AsteroidTable, MainViewAdapter.ViewHolder>(AsteroidDiffCallback()) {


    class ViewHolder(val binding: LinearViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, asteroid: AsteroidTable) {
            println(asteroid.closeApproachDate)
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LinearViewItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }


    class AsteroidDiffCallback :
        DiffUtil.ItemCallback<AsteroidTable>() {


        override fun areItemsTheSame(oldItem: AsteroidTable, newItem: AsteroidTable): Boolean {
            return oldItem.asteroidId == newItem.asteroidId
        }

        override fun areContentsTheSame(oldItem: AsteroidTable, newItem: AsteroidTable): Boolean {
            return oldItem == newItem
        }
    }

    class AsteroidListener(val clickListener: (asteroidItem: AsteroidTable) -> Unit) {
        fun onClick(asteroidItem: AsteroidTable) {
            clickListener(asteroidItem)
        }
    }
}