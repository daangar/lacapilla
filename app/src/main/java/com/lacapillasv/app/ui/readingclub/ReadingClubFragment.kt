package com.lacapillasv.app.ui.readingclub

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.lacapillasv.app.R
import kotlinx.android.synthetic.main.fragment_reading_club.*

class ReadingClubFragment : Fragment() {

    companion object {
        const val BIBLE_POSITION = 0
        const val RELATED_POSITION = 1
    }

    private lateinit var viewModel: ReadingClubViewModel
    private lateinit var mContext: Context

    private val adapter by lazy {
        BooksAdapter()
    }

    private lateinit var navigation: NavController

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(ReadingClubViewModel::class.java)
        return inflater.inflate(R.layout.fragment_reading_club, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigation = Navigation.findNavController(view)
        setupUI()
        setObersvers()
        viewModel.showBibbleBooks()
    }

    private fun setObersvers() {

        viewModel.books.observe(viewLifecycleOwner, Observer {
            adapter.addBooks(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { show ->
            if (show) showLoading() else hideLoading()
        })
    }

    private fun setupUI() {
        setupListBooks()
        setupTabs()
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter.setOnClickLisgtener { _, book ->
            val action = ReadingClubFragmentDirections.actionReadingClubToGuides(book.id, book.name)
            navigation.navigate(action)
        }
    }

    private fun setupTabs() {
        lyTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    BIBLE_POSITION -> viewModel.showBibbleBooks()
                    RELATED_POSITION -> viewModel.showRelatedBooks()
                }
            }

        })
    }

    private fun setupListBooks() {
        with(rvBooks) {
            this.layoutManager = LinearLayoutManager(mContext)
            this.setHasFixedSize(true)
            this.adapter = this@ReadingClubFragment.adapter
        }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }
}