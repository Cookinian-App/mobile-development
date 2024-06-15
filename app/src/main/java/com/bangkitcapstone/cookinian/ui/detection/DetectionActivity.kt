package com.bangkitcapstone.cookinian.ui.detection

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivityDetectionBinding
import com.bangkitcapstone.cookinian.helper.ObjectDetectionHelper
import org.tensorflow.lite.task.vision.detector.Detection
import java.text.NumberFormat

class DetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionBinding

    private lateinit var objectDetectionHelper: ObjectDetectionHelper
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        currentImageUri?.let {
            binding.ivDetectionPhoto.setImageURI(it)
            analyzeImage(it)
        }

        setupToolbar()
    }

    private fun analyzeImage(uri: Uri) {
        objectDetectionHelper = ObjectDetectionHelper(
            context = this,
            detectionListener = object : ObjectDetectionHelper.DetectionListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@DetectionActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Detection>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty()) {
                                val sortedDetections = it.sortedByDescending { it.categories[0].score }
                                val result = sortedDetections.joinToString(", ") { detection ->
                                    val category = detection.categories[0]
                                    category.label
                                }
                                binding.edDetectionResult.setText(result)
                            } else {
                                binding.edDetectionResult.setText(getString(R.string.object_detection_failed))
                            }
                        }
                    }
                }
            }
        )
        objectDetectionHelper.detectObjects(uri)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = "Hasil Deteksi"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "com.bangkitcapstone.cookinian.ui.detection.EXTRA_IMAGE_URI"
    }
}