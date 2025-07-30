package com.radiolauncher.models

data class RadioPreset(
    val number: Int,
    val frequency: Float,
    val band: Int,
    val name: String = ""
)