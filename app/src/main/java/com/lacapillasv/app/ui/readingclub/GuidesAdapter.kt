package com.lacapillasv.app.ui.readingclub

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lacapillasv.app.R
import com.lacapillasv.app.model.Guide
import kotlinx.android.synthetic.main.item_guide.view.*

class GuidesAdapter: RecyclerView.Adapter<GuidesAdapter.ViewHolder>() {

    private val data = mutableListOf<Guide>()
    private lateinit var onClickListener: ((View, Guide) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guide, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindItem(item)
    }

    fun addGuides(guides: List<Guide>) {
        data.clear()
        data.addAll(guides)
        notifyDataSetChanged()
    }

    fun setOnClickLisgtener(listener: ((View, Guide) -> Unit)) {
        onClickListener = listener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var guide: Guide

        init {
            itemView.setOnClickListener { view ->
                if (::guide.isInitialized && ::onClickListener.isInitialized) {
                    onClickListener.invoke(view, guide)
                }
            }
        }

        fun bindItem(guide: Guide) {
            this.guide = guide
            with(itemView) {
                this.tvMonth.text = monthName(
                    DateFormat.format("MM", guide.releaseDate).toString().toInt())
                this.tvDay.text = DateFormat.format("dd", guide.releaseDate).toString()
                this.tvGuideName.text = guide.name
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