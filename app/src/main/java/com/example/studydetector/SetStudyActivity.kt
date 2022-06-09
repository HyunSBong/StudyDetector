package com.example.studydetector

import android.R
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.studydetector.databinding.ActivitySetStudyBinding
import com.example.studydetector.roomdb.StudyDatabase
import com.example.studydetector.roomdb.StudyTable
import com.example.studydetector.yolo.StudyPrepare
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class SetStudyActivity : AppCompatActivity() {
    lateinit var binding: ActivitySetStudyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var study_name_input_txt: EditText = binding.studyNameInput
        var study_time_input_txt: EditText = binding.studyTimeInput
        // 현재시간을 가져오기
        val now: Long = System.currentTimeMillis()
        // 현재 시간을 Date 타입으로 변환
        val date = Date(now)
        // 날짜, 시간을 가져오고 싶은 형태 선언
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("ko", "KR"))

        binding.btnCancle.setOnClickListener {
            val intent = Intent(this@SetStudyActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnApply.setOnClickListener {
            Log.d("TAG", study_name_input_txt.getText().toString())
            val intent = Intent(this@SetStudyActivity, StudyPrepare::class.java)
            intent.putExtra("name", study_name_input_txt.getText().toString())
            intent.putExtra("date", dateFormat.format(date).toString())
            intent.putExtra("studyTime", study_time_input_txt.getText().toString())
            startActivity(intent)
            finish()
        }
    }
}