package com.example.studydetector.yolo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.camera.core.*
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.camera.core.Preview.PreviewOutput
import androidx.core.app.ActivityCompat
import com.example.studydetector.DBAdapter
import com.example.studydetector.MainActivity
import com.example.studydetector.databinding.ActivityObjectDetectionBinding
import com.example.studydetector.roomdb.StudyDatabase
import com.example.studydetector.roomdb.StudyTable
import kotlinx.coroutines.*

abstract class AbstractCameraXActivity<R> : BaseModuleActivity() {
    lateinit var binding: ActivityObjectDetectionBinding
    private var startTime:Long = 0
    private var intent_name:String = ""
    private var intent_date:String = ""
    private var intent_study_time:String = ""
    private var mLastAnalysisResultTime: Long = 0
    protected abstract val contentViewLayoutId: Int
    protected abstract val cameraPreviewTextureView: TextureView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var strArray = Array<String>(5){""}
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startTime = System.currentTimeMillis()


        // 데이터 수신
        val intent = intent
        val intent_name = intent.extras!!.getString("name")
        val intent_date = intent.extras!!.getString("date")
        val intent_study_time = intent.extras!!.getString("studyTime")
        Log.d("수신한 intent", "------------------------------------------------------------")
        Log.d("수신한 intent : name", intent_name.toString() + "------------------------------------------------------------")
        Log.d("수신한 intent : date", intent_date.toString() + "------------------------------------------------------------")
        Log.d("수신한 intent : study_time", intent_study_time.toString() + "------------------------------------------------------------")

        startBackgroundThread()


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS,
                REQUEST_CODE_CAMERA_PERMISSION
            )
        } else {
            setupCameraX()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                    this,
                    "You can't use object detection example without granting CAMERA permission",
                    Toast.LENGTH_LONG
                )
                    .show()
                finish()
            } else {
                setupCameraX()
            }
        }
    }

    private fun setupCameraX() {
        val textureView = cameraPreviewTextureView
        val previewConfig = PreviewConfig.Builder().build()
        val preview = Preview(previewConfig)
        preview.onPreviewOutputUpdateListener =
            OnPreviewOutputUpdateListener { output: PreviewOutput ->
                textureView.setSurfaceTexture(output.surfaceTexture)
            }
        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .setTargetResolution(Size(480, 640))
            .setCallbackHandler(mBackgroundHandler)
            .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            .setTargetRotation(Surface.ROTATION_90)
            .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)
        imageAnalysis.analyzer =
            ImageAnalysis.Analyzer { image: ImageProxy?, rotationDegrees: Int ->
                if (SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 500) {
                    return@Analyzer
                }
                val result = analyzeImage(image, rotationDegrees)
                if (result != null) {
                    mLastAnalysisResultTime = SystemClock.elapsedRealtime()
                    var threadval = true
                    binding.btnStop.setOnClickListener {
                        val endTime: Long = System.currentTimeMillis()
                        val runtime_check = ((endTime-startTime)/1000).toString()
                        Log.d("timecheck", "onCreate:"+(endTime-startTime)/1000)
                        // DB관련 코드
                        var db = StudyDatabase.getDatabase(applicationContext)
                        var adapter = DBAdapter(mutableListOf())
                        val elem = StudyTable(
                            intent.extras!!.getString("name").toString(),
                            intent.extras!!.getString("date").toString(),
                            intent.extras!!.getString("studyTime").toString(),
                            runtime_check
                        );
                        CoroutineScope(Dispatchers.IO).launch {
                            db!!.studyDAO().insert(elem)
                            withContext(Dispatchers.Main) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                        threadval = false
                        stopBackgroundThread()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity();
                        System.runFinalization();
                        System.exit(0);
                    }
                    if (threadval) {
                        runOnUiThread {
                            applyToUiAnalyzeImageResult(result)
                        }
                    }
                }
            }
        CameraX.bindToLifecycle(this, preview, imageAnalysis)
    }


    @WorkerThread
    protected abstract fun analyzeImage(image: ImageProxy?, rotationDegrees: Int): R?
    @UiThread
    protected abstract fun applyToUiAnalyzeImageResult(result: R)

    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSION = 200
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}