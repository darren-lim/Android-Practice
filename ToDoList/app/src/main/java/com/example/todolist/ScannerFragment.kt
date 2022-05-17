package com.example.todolist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.todolist.databinding.ScannerFragmentBinding
import com.labters.documentscanner.ImageCropActivity
import com.labters.documentscanner.helpers.ScannerConstants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class ScannerFragment : Fragment() {
    private lateinit var binding: ScannerFragmentBinding
    lateinit var btnSave: Button
    lateinit var imgBitmap: ImageView
    lateinit var mCurrentPhotoPath: Uri
    lateinit var resolver: ContentResolver
    lateinit var mContext: Context

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = ScannerFragmentBinding.inflate(layoutInflater, container, false)
        resolver = requireActivity().contentResolver
        mContext = this.requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave = view.findViewById(R.id.saveButton)
        imgBitmap = view.findViewById(R.id.imgBitmap)
        binding.btnGallery.setOnClickListener {
            askPermission()
            setViewGallery()
        }
        binding.btnCamera.setOnClickListener {
            askPermission()
            setViewCamera()
        }
        btnSave.setOnClickListener {
            val path = saveToInternalStorage(ScannerConstants.selectedImageBitmap)
            if(path != null){
                Toast.makeText(mContext, "Successfully Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mContext, "Error, Unable to be saved", Toast.LENGTH_SHORT).show()
            }
        }
        askPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImage = data.data
            var btimap: Bitmap? = null
            try {
                val inputStream = selectedImage?.let { resolver.openInputStream(it) }
                btimap = BitmapFactory.decodeStream(inputStream)
                ScannerConstants.selectedImageBitmap = btimap
                startActivityForResult(
                        Intent(mContext, ImageCropActivity::class.java),
                        1234
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == 1231 && resultCode == Activity.RESULT_OK) {
            if (android.os.Build.VERSION.SDK_INT >= 29){
                // To handle deprecation use
                val source = ImageDecoder.createSource(
                    resolver,
                    mCurrentPhotoPath
                )
                val decodedBitmap = ImageDecoder.decodeBitmap(source)
                ScannerConstants.selectedImageBitmap = decodedBitmap.copy(Bitmap.Config.ARGB_8888, true)
            } else{
                // Use older version
                ScannerConstants.selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        resolver,
                        mCurrentPhotoPath
                )
            }
            startActivityForResult(Intent(mContext, ImageCropActivity::class.java), 1234)


        } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            if (ScannerConstants.selectedImageBitmap != null) {
                imgBitmap.setImageBitmap(ScannerConstants.selectedImageBitmap)
                imgBitmap.visibility = View.VISIBLE
                btnSave.visibility = View.VISIBLE
            } else
                Toast.makeText(mContext, "Not OK camera image", Toast.LENGTH_LONG).show()
        }
    }

    private fun askPermission() {
        if (
            ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA), 1)
        }
    }

    private fun setViewGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1111)
    }

    private fun setViewCamera() {
        val cameraIntent = Intent(ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(mContext.packageManager) != null) {
            var photoFile: Uri? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.i("Scanner", "IOException $ex")
            }
            if (photoFile != null) {
                val mBuilder = StrictMode.VmPolicy.Builder()
                StrictMode.setVmPolicy(mBuilder.build())
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
                startActivityForResult(cameraIntent, 1231)
            } else {
                Log.d("camera", "FAIL")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")
        val uri: Uri? =
            mContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            mCurrentPhotoPath = uri
        }
        return uri
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")
        val uri: Uri? =
                mContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            val outputStream: OutputStream? = mContext.contentResolver.openOutputStream(uri)
            if(outputStream!= null){
                try {
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            values.put(MediaStore.Images.Media.IS_PENDING, false)
            mContext.contentResolver.update(uri, values, null, null)
        }
        return uri
    }
}