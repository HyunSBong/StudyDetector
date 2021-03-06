package com.example.studydetector

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studydetector.databinding.ActivityMainBinding
import com.example.studydetector.roomdb.StudyDatabase
import com.example.studydetector.roomdb.StudyTable
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var db = StudyDatabase.getDatabase(applicationContext)
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val mainDateFormat =  SimpleDateFormat("yyyy년 MM월 dd일", Locale("ko", "KR"))
        binding.bannerToday.text = mainDateFormat.format(date).toString()

        // 초기 DB 설정
        val entryArr = mutableListOf(
            StudyTable("모바일컴퓨팅", "2022-06-13", "2", "3600"),
            StudyTable("인공지능", "2022-06-14", "3", "10800"),
            StudyTable("딥러닝", "2022-06-14", "4", "10800"),
            StudyTable("컴퓨터 아키텍처", "2022-06-14", "2", "3600")
        )
        for (entry in entryArr) {
            CoroutineScope(Dispatchers.IO).launch {
                db!!.studyDAO().insert(entry)
            }
        }

        // 리사이클러뷰 관련
        var adapter = DBAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.Main).launch {
            val getList = CoroutineScope(Dispatchers.IO).async {

                db!!.studyDAO().selectByDate(dateFormat.format(date).toString())
            }.await()
            withContext(Dispatchers.Main) {
                var summaryList = getSummary(getList)
                if (summaryList.isEmpty()) {
                    binding.summaryTodayContent2.setText("아직 없어요..")
                    binding.summaryTodayContent3.setText("언제 공부할건가요? 네?")
                }
                else {
                    binding.summaryTodayContent2.setText(summaryList.get(0).toString() + "시간")
                    binding.summaryTodayContent3.setText(summaryList.get(1).toString() + "분")
                }
                adapter.setList(getList)
                binding.recyclerView.adapter = adapter
            }
        }
        binding.btnSetStudy.setOnClickListener {
            val intent = Intent(this@MainActivity, SetStudyActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnCalendar.setOnClickListener {
            val cal = Calendar.getInstance()
            var db = StudyDatabase.getDatabase(applicationContext)
            var adapter = DBAdapter(mutableListOf())
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                CoroutineScope(Dispatchers.Main).launch {
                    val getList = CoroutineScope(Dispatchers.IO).async {
                        db!!.studyDAO().selectByDate("$y-0$m-$d")
                    }.await()
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, (getList.size).toString())
                        var summaryList = getSummary(getList)
                        if (summaryList.isEmpty()) {
                            binding.summaryTodayContent2.setText("아직 없어요..")
                            binding.summaryTodayContent3.setText("언제 공부할건가요? 네?")
                        }
                        else {
                            binding.summaryTodayContent2.setText(summaryList.get(0).toString() + "시간")
                            binding.summaryTodayContent3.setText(summaryList.get(1).toString() + "분")
                        }
                        adapter.setList(getList)
                        binding.recyclerView.adapter = adapter
                        binding.bannerToday.text = y.toString() + "년 " + m.toString() + "월 " + d.toString() +"일"
                    }
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#FFEFCB")
        }
    }
    fun getSummary(getList: MutableList<StudyTable>): ArrayList<Int> {
        var goal = 0 // 목표 공부량의 총합
        var total = 0 // 총 공부량의 총합
        var listSize = getList.size
        var l = ArrayList<Int>()
        if (!getList.isEmpty()) {
            for (position:Int in 0..listSize-1) {
                goal += Integer.parseInt((getList[position].goalTime).toString()) / 3600
                total += Integer.parseInt((getList[position].studyTime).toString()) / 60
            }
            l.add(goal)
            l.add(total)
        }
        return l
    }
}