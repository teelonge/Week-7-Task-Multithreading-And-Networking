package com.example.week_7_task.secondimplementation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.week_7_task.PICK_IMAGE
import com.example.week_7_task.R
import com.example.week_7_task.databinding.SendImageFragmentBinding
import com.example.week_7_task.getFileName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * This fragment handles requesting permission to access storage, creating the imageUri
 * and also the requestBody used by the viewModel to make a network call to upload image
 */
class SendImage : Fragment() {

    private lateinit var viewModel: SendImageViewModel
    private var _binding: SendImageFragmentBinding? = null
    private val binding: SendImageFragmentBinding
        get() = _binding!!

    private var imageUri: Uri? = null
    private lateinit var file: File

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = SendImageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SendImageViewModel::class.java)

        /*
            Request permission to read contents of the gallery and sets the image to the imageView
         */
        binding.imgProfile.setOnClickListener {
            changeImageFromGallery()
        }

        // Requests permission again if denied the first time
        binding.btnRequestAgain.setOnClickListener {
            binding.textView2.visibility = View.GONE
            binding.btnRequestAgain.visibility = View.GONE
            changeImageFromGallery()
        }

        /*
          Uploads the selected image from the gallery to the server
         */
        binding.btnSend.setOnClickListener {
            if (imageUri != null){
                uploadImage()
                binding.uploadProgressBar.visibility = View.VISIBLE
            }else{
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        // Observes changes for the server response and sets the text value for the result
        viewModel.uploadImageResponse.observe(viewLifecycleOwner, {
            binding.txtResult.text = it
            binding.txtResult.visibility = View.VISIBLE
            binding.uploadProgressBar.visibility = View.GONE
        })

    }

    // Checks if permission is granted and if not permission is requested and image set to the imageView
    private fun changeImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PICK_IMAGE
            )
        } else {
            val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }
    }

    // Checks for the request code and
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeImageFromGallery()
            } else {
                binding.textView2.text = getString(R.string.permission_denied)
                binding.textView2.visibility = View.VISIBLE
                binding.btnRequestAgain.visibility = View.VISIBLE

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            try {
                imageUri = data?.data!!
                binding.imgProfile.setImageURI(imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /*
        Generates the file and requestBody which is used to make the call to the server from the
        sendImageViewModel
     */
    private fun uploadImage() {
        file = File(requireActivity().cacheDir, requireActivity().contentResolver.getFileName(imageUri!!))
        val reqFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())

        viewModel.uploadImage(file, reqFile)

    }
}
