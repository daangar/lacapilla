package com.lacapillasv.app.ui.preachings.videos

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.base.MoreObjects
import com.lacapillasv.app.R
import com.lacapillasv.app.model.Video
import kotlinx.android.synthetic.main.fragment_guides.*
import kotlinx.android.synthetic.main.fragment_guides.loading
import kotlinx.android.synthetic.main.fragment_videos.*

class VideosFragment: Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(VideosViewModel::class.java)
    }
    private val adapter by lazy { VideosAdapter() }

    private lateinit var _context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setObservers()
        viewModel.getVideos()
    }

    private fun setObservers() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { show ->
            showLoading(show)
        })

        viewModel.showVideos().observe(viewLifecycleOwner, Observer {videos ->
            adapter.addVideos(videos)
        })
    }

    private fun setupUI() {
        setupListVideos()
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter.setOnClickLisgtener { _, video ->
            showSelectedVideo(video)
        }
    }

    private fun showSelectedVideo(video: Video) {
        if (video.url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
            try {
                startActivity(intent)
            }
            catch(ex: ActivityNotFoundException) {
                Toast.makeText(_context,
                    "Ocurrió un problema al intentar cargar el video. Por favor pruebe nuevamente",
                Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setupListVideos() {
        with(rvVideos) {
            this.layoutManager = LinearLayoutManager(_context)
            this.setHasFixedSize(true)
            this.adapter = this@VideosFragment.adapter
        }
    }


    private fun showLoading(show: Boolean) {
        loading.isVisible = show
    }


}