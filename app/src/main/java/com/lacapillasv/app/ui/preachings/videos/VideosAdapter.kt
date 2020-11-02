package com.lacapillasv.app.ui.preachings.videos

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lacapillasv.app.R
import com.lacapillasv.app.model.Video
import kotlinx.android.synthetic.main.item_guide.view.*
import kotlinx.android.synthetic.main.item_guide.view.tvDay
import kotlinx.android.synthetic.main.item_guide.view.tvMonth
import kotlinx.android.synthetic.main.item_video.view.*

class VideosAdapter: RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    private val data = mutableListOf<Video>()
    private lateinit var onClickListener: ((View, Video) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindItem(item)
    }

    fun addVideos(videos: List<Video>) {
        data.clear()
        data.addAll(videos)
        notifyDataSetChanged()
    }

    fun setOnClickLisgtener(listener: ((View, Video) -> Unit)) {
        onClickListener = listener
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var video: Video

        init {
            itemView.setOnClickListener {view ->
                if(::video.isInitialized && ::onClickListener.isInitialized) {
                    onClickListener.invoke(view, video)
                }
            }
        }

        fun bindItem(video: Video) {
            this.video = video
            with(itemView) {
                this.tvMonth.text = monthName(
                    DateFormat.format("MM", video.releaseDate).toString().toInt())
                this.tvDay.text = DateFormat.format("dd", video.releaseDate).toString()
                this.tvTitle.text = video.title
                this.tvSubTitle.text = video.subTitle
            }
        }

        private fun monthName(month: Int) = when(month) {
            1 -> "ENE"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "ABR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AGO"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DIC"
            else -> ""
        }
    }

}