package com.example.studydetector

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studydetector.databinding.ListItemBinding
import com.example.studydetector.databinding.RecylerListBinding
import com.example.studydetector.roomdb.StudyTable

class DBAdapter (private var dataset: MutableList<StudyTable>): RecyclerView.Adapter<DBAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: RecylerListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = dataset.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecylerListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    fun setList(newList: MutableList<StudyTable>) {
        this.dataset = newList
    }

    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        val binding = (holder as MyViewHolder).binding
        var int_study_time = Integer.parseInt((dataset[position].studyTime).toString()) / 60
        var min_study_time = int_study_time.toDouble() / 60 // 분 단위로 변경
        var double_goal_time = (Integer.parseInt((dataset[position].goalTime).toString())).toDouble()
        var cal_study_time = min_study_time / double_goal_time * 100
        var result = cal_study_time.toInt()

        Log.d("time", int_study_time.toString())
        Log.d("min_time", min_study_time.toString())
        Log.d("double_goaltime", double_goal_time.toString())
        Log.d("최종 퍼센트", cal_study_time.toString())
        binding.listTitle.text = dataset[position].name
        binding.listDate.text = dataset[position].date
        binding.listTime.text = (dataset[position].goalTime).toString() + "시간"
        binding.listPercentage.text = result.toString() + "%"
    }
}
