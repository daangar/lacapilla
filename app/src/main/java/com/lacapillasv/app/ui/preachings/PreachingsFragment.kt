package com.lacapillasv.app.ui.preachings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lacapillasv.app.R
import kotlinx.android.synthetic.main.fragment_preachings.*

class PreachingsFragment : Fragment() {

    private lateinit var preachingsViewModel: PreachingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        preachingsViewModel =
                ViewModelProvider(this).get(PreachingsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_preachings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        btnYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=yREpi89CUKI"))
            try {
                startActivity(intent)
            }
            catch(ex: ActivityNotFoundException) {

            }
        }
    }

}