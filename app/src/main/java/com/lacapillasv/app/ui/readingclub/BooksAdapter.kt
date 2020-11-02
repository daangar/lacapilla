package com.lacapillasv.app.ui.readingclub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lacapillasv.app.R
import com.lacapillasv.app.model.Book
import kotlinx.android.synthetic.main.item_book.view.*

class BooksAdapter: RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private val data = mutableListOf<Book>()
    private lateinit var onClickListener: ((View, Book) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindItem(item)
    }

    fun addBooks(books: List<Book>) {
        data.clear()
        data.addAll(books)
        notifyDataSetChanged()
    }

    fun setOnClickLisgtener(listener: ((View, Book) -> Unit)) {
        onClickListener = listener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var book: Book

        init {
            itemView.setOnClickListener { view ->
                if (::book.isInitialized && ::onClickListener.isInitialized) {
                    onClickListener.invoke(view, book)
                }
            }
        }

        fun bindItem(book: Book) {
            this.book = book
            itemView.textview_book_name.text = book.name
        }
    }
}