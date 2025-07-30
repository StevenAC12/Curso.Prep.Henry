package com.radiolauncher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.radiolauncher.R
import com.radiolauncher.adapters.RadioPresetAdapter
import com.radiolauncher.models.RadioPreset
import com.radiolauncher.utils.RadioManager

class RadioFragment : Fragment() {
    
    // UI Components
    private lateinit var btnFM: Button
    private lateinit var btnAM: Button
    private lateinit var tvFrequency: TextView
    private lateinit var pbSignalStrength: ProgressBar
    private lateinit var btnFreqDown: Button
    private lateinit var btnFreqUp: Button
    private lateinit var seekbarFrequency: SeekBar
    private lateinit var btnScan: Button
    private lateinit var rvPresets: RecyclerView
    private lateinit var seekbarVolume: SeekBar
    private lateinit var btnMute: Button
    
    // Radio Management
    private lateinit var radioManager: RadioManager
    private lateinit var presetAdapter: RadioPresetAdapter
    private var currentBand = RadioManager.BAND_FM
    private var currentFrequency = 101.5f
    private var isMuted = false
    private var currentVolume = 50
    
    // Presets
    private val presets = mutableListOf<RadioPreset>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_radio, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        initializeRadio()
        setupBandSelection()
        setupFrequencyControls()
        setupPresets()
        setupVolumeControls()
        loadSavedPresets()
        
        updateUI()
    }
    
    private fun initializeViews(view: View) {
        btnFM = view.findViewById(R.id.btn_fm)
        btnAM = view.findViewById(R.id.btn_am)
        tvFrequency = view.findViewById(R.id.tv_frequency)
        pbSignalStrength = view.findViewById(R.id.pb_signal_strength)
        btnFreqDown = view.findViewById(R.id.btn_freq_down)
        btnFreqUp = view.findViewById(R.id.btn_freq_up)
        seekbarFrequency = view.findViewById(R.id.seekbar_frequency)
        btnScan = view.findViewById(R.id.btn_scan)
        rvPresets = view.findViewById(R.id.rv_presets)
        seekbarVolume = view.findViewById(R.id.seekbar_volume)
        btnMute = view.findViewById(R.id.btn_mute)
    }
    
    private fun initializeRadio() {
        radioManager = RadioManager(requireContext())
        radioManager.initialize()
        
        // Set initial frequency and band
        radioManager.setBand(currentBand)
        radioManager.setFrequency(currentFrequency)
        radioManager.setVolume(currentVolume)
    }
    
    private fun setupBandSelection() {
        btnFM.setOnClickListener {
            switchBand(RadioManager.BAND_FM)
        }
        
        btnAM.setOnClickListener {
            switchBand(RadioManager.BAND_AM)
        }
    }
    
    private fun setupFrequencyControls() {
        btnFreqDown.setOnClickListener {
            changeFrequency(-0.1f)
        }
        
        btnFreqUp.setOnClickListener {
            changeFrequency(0.1f)
        }
        
        seekbarFrequency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newFrequency = if (currentBand == RadioManager.BAND_FM) {
                        88.0f + (progress / 10.0f) // 88.0 - 108.0 MHz
                    } else {
                        530f + (progress * 10f) // 530 - 1700 kHz
                    }
                    setFrequency(newFrequency)
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        btnScan.setOnClickListener {
            startScan()
        }
    }
    
    private fun setupPresets() {
        presetAdapter = RadioPresetAdapter(presets) { preset ->
            // Preset clicked
            setFrequency(preset.frequency)
            radioManager.setBand(preset.band)
            currentBand = preset.band
            updateUI()
        }
        
        rvPresets.layoutManager = LinearLayoutManager(requireContext())
        rvPresets.adapter = presetAdapter
    }
    
    private fun setupVolumeControls() {
        seekbarVolume.progress = currentVolume
        seekbarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    currentVolume = progress
                    radioManager.setVolume(currentVolume)
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        btnMute.setOnClickListener {
            toggleMute()
        }
    }
    
    private fun switchBand(band: Int) {
        currentBand = band
        radioManager.setBand(band)
        
        if (band == RadioManager.BAND_FM) {
            currentFrequency = 101.5f
            seekbarFrequency.max = 200 // 88.0 - 108.0 (200 steps of 0.1)
        } else {
            currentFrequency = 720f
            seekbarFrequency.max = 117 // 530 - 1700 (117 steps of 10)
        }
        
        setFrequency(currentFrequency)
        updateUI()
    }
    
    private fun changeFrequency(delta: Float) {
        val newFrequency = currentFrequency + delta
        val minFreq = if (currentBand == RadioManager.BAND_FM) 88.0f else 530f
        val maxFreq = if (currentBand == RadioManager.BAND_FM) 108.0f else 1700f
        
        if (newFrequency >= minFreq && newFrequency <= maxFreq) {
            setFrequency(newFrequency)
        }
    }
    
    private fun setFrequency(frequency: Float) {
        currentFrequency = frequency
        radioManager.setFrequency(frequency)
        updateFrequencyDisplay()
        updateSeekBar()
        updateSignalStrength()
    }
    
    private fun startScan() {
        btnScan.isEnabled = false
        btnScan.text = "Buscando..."
        
        // Simulate scanning (in a real implementation, this would use the radio hardware)
        radioManager.startScan { foundFrequency ->
            if (foundFrequency > 0) {
                setFrequency(foundFrequency)
            }
            
            btnScan.isEnabled = true
            btnScan.text = getString(R.string.radio_scan)
        }
    }
    
    private fun toggleMute() {
        isMuted = !isMuted
        radioManager.setMuted(isMuted)
        
        btnMute.text = if (isMuted) "Unmute" else getString(R.string.radio_mute)
        seekbarVolume.isEnabled = !isMuted
    }
    
    private fun updateUI() {
        updateBandButtons()
        updateFrequencyDisplay()
        updateSeekBar()
        updateSignalStrength()
    }
    
    private fun updateBandButtons() {
        if (currentBand == RadioManager.BAND_FM) {
            btnFM.setBackgroundColor(requireContext().getColor(R.color.button_primary))
            btnAM.setBackgroundColor(requireContext().getColor(R.color.button_secondary))
        } else {
            btnFM.setBackgroundColor(requireContext().getColor(R.color.button_secondary))
            btnAM.setBackgroundColor(requireContext().getColor(R.color.button_primary))
        }
    }
    
    private fun updateFrequencyDisplay() {
        tvFrequency.text = if (currentBand == RadioManager.BAND_FM) {
            String.format("%.1f", currentFrequency)
        } else {
            String.format("%.0f", currentFrequency)
        }
    }
    
    private fun updateSeekBar() {
        val progress = if (currentBand == RadioManager.BAND_FM) {
            ((currentFrequency - 88.0f) * 10).toInt()
        } else {
            ((currentFrequency - 530f) / 10).toInt()
        }
        seekbarFrequency.progress = progress
    }
    
    private fun updateSignalStrength() {
        // Simulate signal strength based on frequency
        val signalStrength = radioManager.getSignalStrength()
        pbSignalStrength.progress = signalStrength
        
        val color = when {
            signalStrength >= 70 -> R.color.radio_signal_strong
            signalStrength >= 40 -> R.color.radio_signal_medium
            else -> R.color.radio_signal_weak
        }
        pbSignalStrength.progressTintList = 
            requireContext().getColorStateList(color)
    }
    
    private fun loadSavedPresets() {
        // Load presets from SharedPreferences or database
        presets.clear()
        presets.addAll(listOf(
            RadioPreset(1, 101.5f, RadioManager.BAND_FM, "Radio Nacional"),
            RadioPreset(2, 89.7f, RadioManager.BAND_FM, "FM Azul"),
            RadioPreset(3, 95.1f, RadioManager.BAND_FM, "Rock & Pop"),
            RadioPreset(4, 107.9f, RadioManager.BAND_FM, "La Metro"),
            RadioPreset(5, 720f, RadioManager.BAND_AM, "AM 720"),
            RadioPreset(6, 870f, RadioManager.BAND_AM, "Radio Rivadavia")
        ))
        presetAdapter.notifyDataSetChanged()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        radioManager.release()
    }
}