package com.example.ejerciciocards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaListaTareas()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaTareas() {
    val tareas = remember {
        mutableStateListOf(
            Task(1, "Estudiar Jetpack Compose", "Repasar BadgedBox y DropdownMenu", priority = Prioridad.ALTA),
            Task(2, "Hacer la compra", "Comprar pechugas y patatas", priority = Prioridad.MEDIA),
            Task(3, "Ir al gimnasio", "Entrenar torso", isCompleted = true, priority = Prioridad.BAJA),
            Task(4, "Revisar código DAM", "Revisar los ejercicios del tema", priority = Prioridad.ALTA)
        )
    }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf(Prioridad.BAJA) }
    var verPrioridad by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Mis Tareas",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // FORMULARIO PARA AÑADIR TAREA
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Añadir Nueva Tarea",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        OutlinedButton(onClick = { verPrioridad = true }) {
                            Text("Prioridad: ${prioridad.label}")
                        }

                        DropdownMenu(
                            expanded = verPrioridad,
                            onDismissRequest = { verPrioridad = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Alta") },
                                onClick = {
                                    prioridad = Prioridad.ALTA
                                    verPrioridad = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Media") },
                                onClick = {
                                    prioridad = Prioridad.MEDIA
                                    verPrioridad = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Baja") },
                                onClick = {
                                    prioridad = Prioridad.BAJA
                                    verPrioridad = false
                                }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (titulo.isNotBlank()) {
                                val nuevaTarea = Task(
                                    id = (tareas.maxOfOrNull { it.id } ?: 0) + 1,
                                    title = titulo,
                                    description = descripcion,
                                    priority = prioridad
                                )
                                tareas.add(nuevaTarea)

                                titulo = ""
                                descripcion = ""
                                prioridad = Prioridad.MEDIA
                            }
                        }
                    ) {
                        Text("Añadir")
                    }
                }
            }
        }

        // PENDIENTES
        Text(
            text = "Pendientes:",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        for (tarea in tareas) {
            if (!tarea.isCompleted) {
                TarjetaTarea(
                    tarea = tarea,
                    onCambioEstado = {
                        val indice = tareas.indexOf(tarea)
                        if (indice != -1) {
                            tareas[indice] = tarea.copy(isCompleted = !tarea.isCompleted)
                        }
                    },
                    onCambioPrioridad = { nuevaPrioridadSeleccionada ->
                        val indice = tareas.indexOf(tarea)
                        if (indice != -1) {
                            tareas[indice] = tarea.copy(priority = nuevaPrioridadSeleccionada)
                        }
                    }
                )
            }
        }

        // DIVIDER
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 2.dp,
            color = Color.Black
        )

        // COMPLETADAS
        Text(
            text = "Completadas:",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        for (tarea in tareas) {
            if (tarea.isCompleted) {
                TarjetaTarea(
                    tarea = tarea,
                    onCambioEstado = {
                        val indice = tareas.indexOf(tarea)
                        if (indice != -1) {
                            tareas[indice] = tarea.copy(isCompleted = !tarea.isCompleted)
                        }
                    },
                    onCambioPrioridad = { nuevaPrioridadSeleccionada ->
                        val indice = tareas.indexOf(tarea)
                        if (indice != -1) {
                            tareas[indice] = tarea.copy(priority = nuevaPrioridadSeleccionada)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaTarea(
    tarea: Task,
    onCambioEstado: () -> Unit,
    onCambioPrioridad: (Prioridad) -> Unit
) {
    var verMenuPrioridad by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BadgedBox(
                    badge = {
                        val colorBadge = when (tarea.priority) {
                            Prioridad.ALTA -> Color.Red
                            Prioridad.MEDIA -> Color.Yellow
                            Prioridad.BAJA -> Color.Green
                        }

                        Badge(containerColor = colorBadge) {
                            Text(tarea.priority.label, color = Color.Black)
                        }
                    }
                ) {
                    Text(
                        text = tarea.title,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = tarea.description
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onCambioEstado() }) {
                    Text(if (tarea.isCompleted) "No completada" else "Completar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    OutlinedButton(onClick = { verMenuPrioridad = true }) {
                        Text("Prioridad")
                    }

                    DropdownMenu(
                        expanded = verMenuPrioridad,
                        onDismissRequest = { verMenuPrioridad = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Alta") },
                            onClick = {
                                onCambioPrioridad(Prioridad.ALTA)
                                verMenuPrioridad = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Media") },
                            onClick = {
                                onCambioPrioridad(Prioridad.MEDIA)
                                verMenuPrioridad = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Baja") },
                            onClick = {
                                onCambioPrioridad(Prioridad.BAJA)
                                verMenuPrioridad = false
                            }
                        )
                    }
                }
            }
        }
    }
}