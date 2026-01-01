package com.stealth.manager.modules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stealth.manager.R

class ModuleAdapter(private val modules: List<StealthModule>) :
    RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.module_title)
        val switcher: Switch = view.findViewById(R.id.module_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_module, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = modules[position]
        holder.title.text = module.name
        holder.switcher.isChecked = module.isEnabled
        // 修正了之前变量赋值的语法错误
        holder.switcher.setOnCheckedChangeListener { _, isChecked ->
            module.isEnabled = isChecked
        }
    }

    override fun getItemCount() = modules.size
}