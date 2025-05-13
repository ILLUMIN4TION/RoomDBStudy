package com.example.roomdbstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(
    private var scheduleList: MutableList<Schedule>,
    private val onItemClick: (Schedule) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private val selectedItems = mutableSetOf<Schedule>()

    fun getSelectedItems(): List<Schedule> = selectedItems.toList()

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descTextView: TextView = itemView.findViewById(R.id.descTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.itemCheckBox)

        init {
            itemView.setOnClickListener {
                val schedule = scheduleList[adapterPosition]
                onItemClick(schedule)  // 클릭 시 콜백 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = scheduleList[position]

        holder.titleTextView.text = schedule.title
        holder.descTextView.text = schedule.desc

        // 체크박스 리스너 재설정 방지
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedItems.contains(schedule)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(schedule)
            } else {
                selectedItems.remove(schedule)
            }
        }
    }

    override fun getItemCount(): Int = scheduleList.size


    fun updateData(newList: List<Schedule>) {
        scheduleList.clear()
        scheduleList.addAll(newList)
        selectedItems.clear()
        notifyDataSetChanged()
    }
}