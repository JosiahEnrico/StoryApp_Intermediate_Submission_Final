package com.example.mysubmission2.paging.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmission2.R
import com.example.mysubmission2.data.response.ListStoryItem
import com.example.mysubmission2.databinding.StoriesItemRowBinding
import com.example.mysubmission2.ui.detail.DetailActivity
import java.text.ParseException
import java.text.SimpleDateFormat

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = StoriesItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder(binding: StoriesItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.im_item_photo)
        private var tvUsername: TextView = itemView.findViewById(R.id.tv_item_name)
        private var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)

        fun bind(stories: ListStoryItem) {

            val inputFormat = "yyyy-MM-dd'T'HH:mmz"
            val outPutFormat = "MMMM dd', 'yyyy hh:mma"
            tvUsername.text = stories.name
            tvDescription.text = formatDate(stories.createdAt, inputFormat, outPutFormat)
            Glide.with(itemView.context)
                .load(stories.photoUrl)
                .into(imgPhoto)



            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("stories", stories)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvUsername, "name"),
                        Pair(tvDescription, "createdAt"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDate(dateToFormat: String, inputFormat: String?, outputFormat: String?): String? {
            try {
                val convertedDate = SimpleDateFormat(inputFormat)
                    .parse(dateToFormat)?.let {
                        SimpleDateFormat(outputFormat)
                            .format(
                                it
                            )
                    }
                //Update Date
                return convertedDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }
    }



    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}