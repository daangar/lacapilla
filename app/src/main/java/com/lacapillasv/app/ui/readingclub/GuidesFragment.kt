package com.lacapillasv.app.ui.readingclub

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lacapillasv.app.R
import com.lacapillasv.app.common.AppRequestPermissions
import com.lacapillasv.app.model.Guide
import kotlinx.android.synthetic.main.fragment_guides.*
import java.io.File

class GuidesFragment : Fragment() {

    private lateinit var viewModel: GuidesViewModel
    private lateinit var _Context: Context
    private var _guide: Guide? = null
    private lateinit var layout: View

    val args: GuidesFragmentArgs by navArgs()

    val storage = Firebase.storage

    private val adapter by lazy { GuidesAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _Context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel =
            ViewModelProvider(this).get(GuidesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_guides, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout = main_layout
        viewModel.setBookId(args.bookId)
        setupUI()
        setObservers()
        viewModel.showGuides()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == AppRequestPermissions.PERMISSION_REQUEST_STORAGE) {
            if (grantResults.size == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (_guide != null) {
                    downloadAndShowGuide(_guide!!)
                }
                else {
                    Snackbar.make(layout,
                        "Ocurrió un problema, por favor intenta nuevamente.",
                        Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(layout,
                    "Los permisos para escribir y leer en la memoria son necesarios para poder ejecutar esta función",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { show ->
            if (show) showLoading() else hideLoading()
        })

        viewModel.guides.observe(viewLifecycleOwner, Observer {
            adapter.addGuides(it)
        })
    }

    private fun setupUI() {
        tvBookName.text = args.bookName
        setupListGuides()
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter.setOnClickLisgtener { _, guide ->
            showGuideSelected(guide)
        }
    }

    private fun showGuideSelected(guide: Guide) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasStoragePermissions()) {
                downloadAndShowGuide(guide)
            } else {
                _guide = guide
                requestStoragePermissions()
            }
        } else {
            downloadAndShowGuide(guide)
        }
    }

    private fun downloadAndShowGuide(guide: Guide) {
        val guideRef = storage.reference.child(guide.fileReference)

        val rootPath = File(_Context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Guides")
        if (!rootPath.exists()) rootPath.mkdirs()

        val localFile = File(rootPath, "guia.pdf")
        showLoading()
        guideRef.getFile(localFile).addOnSuccessListener {
            hideLoading()
            showPdf(localFile)
        }.addOnFailureListener {
            hideLoading()
            Snackbar.make(layout,
                "Ocurrió un problema, por favor intenta nuevamente.",
                Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showPdf(file: File) {
        val fileUri = FileProvider.getUriForFile(_Context, _Context.applicationContext.packageName + ".provider", file)
        val pdfViewIntent = Intent(Intent.ACTION_VIEW)
        pdfViewIntent.setDataAndType(fileUri, "application/pdf")
        pdfViewIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent = Intent.createChooser(pdfViewIntent, "Open File")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(layout,
                "No cuentas con un visor de pdf, por favor instala uno desde la Playstore",
                Snackbar.LENGTH_LONG).show()
        } catch (e: Exception) {
            Snackbar.make(layout,
                "Ocurrió un problema, por favor intenta nuevamente.",
                Snackbar.LENGTH_LONG).show()
        }
    }

    private fun requestStoragePermissions() {
        var rationale = ""
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            rationale += AppRequestPermissions.WritePermissionRationaleMessage + "{enter}"
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
            rationale += AppRequestPermissions.ReadPermissionRationaleMessage


        rationale = ""
        if (rationale.isNotEmpty()) {
            val snackbar = Snackbar.make(layout, rationale, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("¿Deseas otorgar estos permisos?") {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE), AppRequestPermissions.PERMISSION_REQUEST_STORAGE)
            }.show()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), AppRequestPermissions.PERMISSION_REQUEST_STORAGE)
        }
    }

    private fun hasStoragePermissions(): Boolean {
        val writePermission = ActivityCompat.checkSelfPermission(_Context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val readPermission = ActivityCompat.checkSelfPermission(_Context,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        return (writePermission ==PackageManager.PERMISSION_GRANTED
                && readPermission == PackageManager.PERMISSION_GRANTED)
    }

    private fun setupListGuides() {
        with(rvGuides) {
            this.layoutManager = LinearLayoutManager(_Context)
            this.setHasFixedSize(true)
            this.adapter = this@GuidesFragment.adapter
        }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }
}