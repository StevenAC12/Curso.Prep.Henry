package com.radiolauncher

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.radiolauncher.fragments.RadioFragment
import com.radiolauncher.fragments.MediaFragment
import com.radiolauncher.fragments.AppsFragment
import com.radiolauncher.fragments.PhoneFragment
import com.radiolauncher.fragments.NavigationFragment
import com.radiolauncher.fragments.SettingsFragment
import com.radiolauncher.utils.SystemInfoManager
import com.radiolauncher.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var tvTime: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var ivBluetoothStatus: ImageView
    private lateinit var ivWifiStatus: ImageView
    
    // Navigation
    private lateinit var navRadio: LinearLayout
    private lateinit var navMedia: LinearLayout
    private lateinit var navApps: LinearLayout
    private lateinit var navPhone: LinearLayout
    private lateinit var navNavigation: LinearLayout
    private lateinit var navSettings: LinearLayout
    
    private var currentFragment: Fragment? = null
    private var currentNavIndex = 0
    
    // System Info Manager
    private lateinit var systemInfoManager: SystemInfoManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        initializeSystemInfo()
        setupNavigation()
        startTimeUpdater()
        
        // Mostrar fragmento inicial (Radio)
        showFragment(RadioFragment(), 0)
    }
    
    private fun initializeViews() {
        // Header components
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        tvTemperature = findViewById(R.id.tv_temperature)
        ivWeatherIcon = findViewById(R.id.iv_weather_icon)
        ivBluetoothStatus = findViewById(R.id.iv_bluetooth_status)
        ivWifiStatus = findViewById(R.id.iv_wifi_status)
        
        // Navigation components
        navRadio = findViewById(R.id.nav_radio)
        navMedia = findViewById(R.id.nav_media)
        navApps = findViewById(R.id.nav_apps)
        navPhone = findViewById(R.id.nav_phone)
        navNavigation = findViewById(R.id.nav_navigation)
        navSettings = findViewById(R.id.nav_settings)
    }
    
    private fun initializeSystemInfo() {
        systemInfoManager = SystemInfoManager(this)
        updateSystemInfo()
    }
    
    private fun setupNavigation() {
        navRadio.setOnClickListener { 
            showFragment(RadioFragment(), 0)
        }
        
        navMedia.setOnClickListener { 
            showFragment(MediaFragment(), 1)
        }
        
        navApps.setOnClickListener { 
            showFragment(AppsFragment(), 2)
        }
        
        navPhone.setOnClickListener { 
            showFragment(PhoneFragment(), 3)
        }
        
        navNavigation.setOnClickListener { 
            showFragment(NavigationFragment(), 4)
        }
        
        navSettings.setOnClickListener { 
            showFragment(SettingsFragment(), 5)
        }
    }
    
    private fun showFragment(fragment: Fragment, navIndex: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        
        if (currentFragment != null) {
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        
        transaction.replace(R.id.content_frame, fragment)
        transaction.commit()
        
        currentFragment = fragment
        updateNavigation(navIndex)
    }
    
    private fun updateNavigation(selectedIndex: Int) {
        currentNavIndex = selectedIndex
        
        val navItems = listOf(
            navRadio to R.id.nav_radio_icon to R.id.nav_radio_text,
            navMedia to R.id.nav_media_icon to R.id.nav_media_text,
            navApps to R.id.nav_apps_icon to R.id.nav_apps_text,
            navPhone to R.id.nav_phone_icon to R.id.nav_phone_text,
            navNavigation to R.id.nav_navigation_icon to R.id.nav_navigation_text,
            navSettings to R.id.nav_settings_icon to R.id.nav_settings_text
        )
        
        navItems.forEachIndexed { index, (container, iconId, textId) ->
            val icon = container.findViewById<ImageView>(iconId)
            val text = container.findViewById<TextView>(textId)
            
            if (index == selectedIndex) {
                // Selected state
                icon.setColorFilter(getColor(R.color.nav_selected))
                text.setTextColor(getColor(R.color.nav_selected))
            } else {
                // Unselected state
                icon.setColorFilter(getColor(R.color.nav_unselected))
                text.setTextColor(getColor(R.color.nav_unselected))
            }
        }
    }
    
    private fun startTimeUpdater() {
        lifecycleScope.launch {
            while (true) {
                updateTimeAndDate()
                delay(1000) // Update every second
            }
        }
    }
    
    private fun updateTimeAndDate() {
        val now = Date()
        
        // Update time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        tvTime.text = timeFormat.format(now)
        
        // Update date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
        tvDate.text = dateFormat.format(now)
    }
    
    private fun updateSystemInfo() {
        // Update weather info (mock data for now)
        tvTemperature.text = "25°C"
        
        // Update connectivity status
        updateBluetoothStatus()
        updateWifiStatus()
    }
    
    private fun updateBluetoothStatus() {
        val isBluetoothConnected = systemInfoManager.isBluetoothConnected()
        ivBluetoothStatus.visibility = if (isBluetoothConnected) View.VISIBLE else View.GONE
        
        if (isBluetoothConnected) {
            ivBluetoothStatus.setColorFilter(getColor(R.color.info))
        }
    }
    
    private fun updateWifiStatus() {
        val isWifiConnected = systemInfoManager.isWifiConnected()
        
        if (isWifiConnected) {
            ivWifiStatus.setColorFilter(getColor(R.color.success))
        } else {
            ivWifiStatus.setColorFilter(getColor(R.color.error))
        }
    }
    
    override fun onResume() {
        super.onResume()
        updateSystemInfo()
    }
    
    override fun onBackPressed() {
        // En un launcher, el botón back no debe cerrar la app
        // En su lugar, vamos al fragmento de inicio (Radio)
        if (currentNavIndex != 0) {
            showFragment(RadioFragment(), 0)
        }
        // No llamamos a super.onBackPressed() para evitar cerrar el launcher
    }
    
    override fun onPause() {
        super.onPause()
        // Mantener el launcher activo en background
    }
}