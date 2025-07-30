package com.radiolauncher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radiolauncher.R
import com.radiolauncher.models.RadioPreset
import com.radiolauncher.utils.RadioManager

class RadioPresetAdapter(
    private val presets: List<RadioPreset>,
    private val onPresetClick: (RadioPreset) -> Unit
) : RecyclerView.Adapter<RadioPresetAdapter.PresetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_radio_preset, parent, false)
        return PresetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
        holder.bind(presets[position])
    }

    override fun getItemCount(): Int = presets.size

    inner class PresetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPresetNumber: TextView = itemView.findViewById(R.id.tv_preset_number)
        private val tvFrequency: TextView = itemView.findViewById(R.id.tv_frequency)
        private val tvStationName: TextView = itemView.findViewById(R.id.tv_station_name)
        private val btnPresetOptions: ImageButton = itemView.findViewById(R.id.btn_preset_options)

        fun bind(preset: RadioPreset) {
            tvPresetNumber.text = preset.number.toString()
            
            tvFrequency.text = if (preset.band == RadioManager.BAND_FM) {
                "${String.format("%.1f", preset.frequency)} MHz"
            } else {
                "${String.format("%.0f", preset.frequency)} kHz"
            }
            
            tvStationName.text = preset.name.ifEmpty { 
                if (preset.band == RadioManager.BAND_FM) "FM" else "AM" 
            }

            itemView.setOnClickListener {
                onPresetClick(preset)
            }

            btnPresetOptions.setOnClickListener {
                // TODO: Mostrar opciones (editar, eliminar, etc.)
            }
        }
    }
}