package com.lacapillasv.app.ui.knowus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lacapillasv.app.R

class KnowUsFragment : Fragment() {

    private lateinit var knowUsViewModel: KnowUsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        knowUsViewModel =
                ViewModelProviders.of(this).get(KnowUsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_knowus, container, false)
    }
}