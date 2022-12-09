package com.dicoding.intermediate1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.intermediate1.databinding.HomePostRowBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var dataList = mutableListOf<UserDataResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomePostRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.setData(data)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: HomePostRowBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(data: UserDataResponse) {
            with(binding){
                rvProfileName.text = data.name
                Glide.with(itemView)
                    .load(data.photoUrl)
                    .into(imgStory)
                itemView.setOnClickListener {
                    val moveWithIntent = Intent(itemView.context, DetailActivity::class.java)
                    moveWithIntent.putExtra(Constanst.DetailStory.UserName.name, data.name)
                    moveWithIntent.putExtra(Constanst.DetailStory.ImageURL.name, data.photoUrl)
                    moveWithIntent.putExtra(Constanst.DetailStory.Description.name, data.description)
                    moveWithIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val option: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(imgStory, "rv_image"),
                        androidx.core.util.Pair(rvProfileName, "rv_name")
                    )
                    itemView.context.startActivity(moveWithIntent, option.toBundle())
                }
            }
        }
    }

    fun initData(data: List<UserDataResponse>) {
        dataList.clear()
        dataList = data.toMutableList()
    }
}
