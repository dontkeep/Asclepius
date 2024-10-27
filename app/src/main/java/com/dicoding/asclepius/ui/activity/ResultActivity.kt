package com.dicoding.asclepius.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.local.entity.ResultEntity
import com.dicoding.asclepius.ui.ViewModelFactory
import com.dicoding.asclepius.viewmodel.ResultViewModel
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
   private lateinit var binding: ActivityResultBinding
   private lateinit var viewModel: ResultViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityResultBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
      viewModel = viewModels<ResultViewModel> { factory }.value

      val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
      val resultText = intent.getStringExtra(EXTRA_RESULT)
      val inferenceTime = intent.getLongExtra(EXTRA_INFERENCE, 0)
      val date = intent.getStringExtra(EXTRA_DATE)

      setResult(imageUri, resultText, inferenceTime, date)
      saveButton(imageUri)
   }

   private fun saveButton(imageUri: String?) {
      binding.saveButton.setOnClickListener {
         lifecycleScope.launch {
            if (imageUri != null) {
               viewModel.saveResult(
                  ResultEntity(
                     imageUri = imageUri,
                     resultText = binding.resultText.text.toString(),
                     inferenceTime = binding.inferenceTextView.text.toString(),
                     date = binding.dateTextView.text.toString()
                  )
               )
            }
         }

         finish()
      }
   }

   private fun setResult(
      imageUri: String?,
      resultText: String?,
      inferenceTime: Long,
      date: String?
   ) {
      binding.resultImage.setImageURI(Uri.parse(imageUri))
      binding.resultText.text = resultText
      binding.inferenceTextView.text = buildString {
      append("Inference Time: ")
      append(inferenceTime)
      append(" ms")
   }
      val formatedDate = date?.replace("T", " ")?.replace("-", "/")
      binding.dateTextView.text = buildString {
      append("Date: ")
      append(formatedDate)
   }
   }

   companion object {
      const val EXTRA_IMAGE_URI = "extra_image_uri"
      const val EXTRA_RESULT = "extra_result"
      const val EXTRA_INFERENCE = "extra_inference_time"
      const val EXTRA_DATE = "extra_date"
   }
}