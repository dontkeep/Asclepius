package com.dicoding.asclepius.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ui.activity.ResultActivity
import com.dicoding.asclepius.ui.activity.ResultActivity.Companion.EXTRA_DATE
import com.dicoding.asclepius.ui.activity.ResultActivity.Companion.EXTRA_IMAGE_URI
import com.dicoding.asclepius.ui.activity.ResultActivity.Companion.EXTRA_INFERENCE
import com.dicoding.asclepius.ui.activity.ResultActivity.Companion.EXTRA_RESULT
import com.dicoding.asclepius.viewmodel.HomeViewModel
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.gms.vision.classifier.Classifications
import java.io.File

class HomeFragment : Fragment() {
   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!
   private lateinit var imageClassifierHelper: ImageClassifierHelper
   private var selectedImageUri: Uri? = null
   private lateinit var viewModel: HomeViewModel

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      imageClassifierHelper =
         ImageClassifierHelper(context = requireContext(), classifierListener = null)

      viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

      viewModel.currentImageUri.observe(viewLifecycleOwner) {
         if (it != null) {
            selectedImageUri = it
            binding.previewImageView.setImageURI(selectedImageUri)
         }
      }
      binding.analyzeButton.setOnClickListener {
         selectedImageUri?.let { imageUri ->
            Log.d(TAG, imageUri.toString())
            Log.d(TAG, "Analyzing")
            imageAnalyzer(imageUri)
         } ?: run {
            Toast.makeText(
               requireContext(),
               "Please select an image first",
               Toast.LENGTH_SHORT
            ).show()
         }
      }
      binding.galleryButton.setOnClickListener {
         openGallery()
      }
   }

   private fun openGallery() {
      launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
   }

   private val launcherGallery = registerForActivityResult(
      ActivityResultContracts.PickVisualMedia()
   ) { uri: Uri? ->
      if (uri != null) {
         moveToCrop(uri)
      } else {
         Log.d("Photo Picker", "No media selected")
      }
   }

   private fun moveToCrop(uri: Uri) {
      val destinationUri =
         Uri.fromFile(File(requireContext().cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))
      val options = UCrop.Options().apply {
         setCompressionFormat(Bitmap.CompressFormat.JPEG)
         setCompressionQuality(90)
      }

      UCrop.of(uri, destinationUri)
         .withAspectRatio(1f, 1f)
         .withMaxResultSize(1080, 1080)
         .withOptions(options)
         .start(requireContext(), this)
   }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
         selectedImageUri = UCrop.getOutput(data)
         if (selectedImageUri != null) {
            viewModel.setImageUri(selectedImageUri)
            viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
               binding.previewImageView.setImageURI(uri)
            }
         }
      }
   }

   private fun imageAnalyzer(uri: Uri) {
      imageClassifierHelper = ImageClassifierHelper(
         context = requireContext(),
         classifierListener = object : ImageClassifierHelper.ClassifierListener {
            override fun onError(error: String) {
               Log.e(TAG, "Error: $error")
               Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }

            override fun onResults(
               results: List<Classifications>?,
               inferenceTime: Long,
               date: String
            ) {
               val resultText = results?.joinToString("\n") { classification ->
                  "Result: ${classification.categories[0].label} ${"%.2f".format(classification.categories[0].score * 100)}%"
               }
               val intent = Intent(requireContext(), ResultActivity::class.java)
               intent.apply {
                  putExtra(EXTRA_IMAGE_URI, uri.toString())
                  putExtra(EXTRA_RESULT, resultText)
                  putExtra(EXTRA_INFERENCE, inferenceTime)
                  putExtra(EXTRA_DATE, date)
               }
               startActivity(intent)
            }
         }
      )
      imageClassifierHelper.classifyStaticImage(uri)
   }

   companion object {
      private const val TAG = "HomeFragment"
   }
}