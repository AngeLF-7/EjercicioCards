package com.example.ejerciciocards

enum class Prioridad(val label: String) {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja")
}

// 2. Clase que representa a cada tarea
data class Task(
    val id: Int,
    var title: String,
    var description: String,
    // Por defecto la pongo en no completada
    var isCompleted: Boolean = false,
    // Por defecto la prioridad es baja
    var priority: Prioridad = Prioridad.BAJA
)