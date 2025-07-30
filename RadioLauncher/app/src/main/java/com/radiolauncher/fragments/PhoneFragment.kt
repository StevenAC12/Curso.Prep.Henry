package com.radiolauncher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.radiolauncher.R

class PhoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(android.R.layout.simple_list_item_1, container, false)
        view.findViewById<TextView>(android.R.id.text1).apply {
            text = "Teléfono\n\nEsta sección incluirá:\n• Llamadas recientes\n• Contactos favoritos\n• Marcador\n• Bluetooth calling"
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        return view
    }
}