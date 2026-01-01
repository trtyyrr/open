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

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.module_title)
        val switcher: Switch = v.findViewById(R.id.module_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = modules[position]
        holder.title.text = m.name
        holder.switcher.isChecked = m.isEnabled
        holder.switcher.setOnCheckedChangeListener { _, isChecked -> m.isEnabled = isChecked }
    }

    override fun getItemCount() = modules.size
}