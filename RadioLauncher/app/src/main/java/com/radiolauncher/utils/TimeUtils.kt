package com.radiolauncher.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    
    fun getCurrentTime(): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(Date())
    }
    
    fun getCurrentDate(): String {
        val format = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
        return format.format(Date())
    }
    
    fun getCurrentTime12Hour(): String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(Date())
    }
    
    fun getCurrentDateShort(): String {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(Date())
    }
    
    fun formatDuration(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60))
        
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}