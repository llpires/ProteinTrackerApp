package com.proteintracker.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proteintracker.data.model.UserSettings
import com.proteintracker.ui.common.AppIcons
import com.proteintracker.ui.common.ProteinCard
import com.proteintracker.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.collectAsStateWithLifecycle
    import androidx.compose.ui.text.input.VisualTransformation
    import kotlinx.coroutines.launch
    
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    
    var targetWeight by remember { mutableStateOf(settings?.targetWeightKg.toString()) }
    var currentWeight by remember { mutableStateOf(settings?.currentWeightKg.toString()) }
    var proteinPerKg by remember { mutableStateOf(settings?.proteinPerKg.toString()) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var reminderTimes by remember { mutableStateOf(settings?.reminderTimesList() ?: listOf("07:00", "10:00", "12:30", "16:00", "18:30", "21:00")) }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = AppIcons.Back, contentDescription = "Voltar")
                }
                Text(
                    text = stringResource(com.proteintracker.R.string.settings_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Profile Section
            SettingsSection(title = stringResource(com.proteintracker.R.string.settings_profile)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Target Weight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(crossAxisAlignment = Alignment.Start) {
                                Text(text = stringResource(com.proteintracker.R.string.settings_target_weight), fontSize = 16.sp)
                                Text(text = "Peso que você deseja atingir", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            OutlinedTextField(
                                value = targetWeight,
                                onValueChange = { targetWeight = it.filter { it.isDigit() || it == '.' } },
                                modifier = Modifier.width(100.dp),
                                singleLine = true,
                                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
                                visualTransformation = VisualTransformation { output ->
                                    androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() || it == '.' })
                                },
                                label = { Text("kg") }
                            )
                        }
                        
                        // Current Weight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(crossAxisAlignment = Alignment.Start) {
                                Text(text = stringResource(com.proteintracker.R.string.settings_current_weight), fontSize = 16.sp)
                                Text(text = "Seu peso atual", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            OutlinedTextField(
                                value = currentWeight,
                                onValueChange = { currentWeight = it.filter { it.isDigit() || it == '.' } },
                                modifier = Modifier.width(100.dp),
                                singleLine = true,
                                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
                                visualTransformation = VisualTransformation { output ->
                                    androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() || it == '.' })
                                },
                                label = { Text("kg") }
                            )
                        )
                        
                        // Protein per kg
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(crossAxisAlignment = Alignment.Start) {
                                Text(text = stringResource(com.proteintracker.R.string.settings_protein_per_kg), fontSize = 16.sp)
                                Text(text = "g de proteína por kg de peso alvo", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            OutlinedTextField(
                                value = proteinPerKg,
                                onValueChange = { proteinPerKg = it.filter { it.isDigit() || it == '.' } },
                                modifier = Modifier.width(100.dp),
                                singleLine = true,
                                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
                                visualTransformation = VisualTransformation { output ->
                                    androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() || it == '.' })
                                },
                                label = { Text("g/kg") }
                            )
                        }
                        
                        // Calculated target display
                        settings?.let { s ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(com.proteintracker.R.string.settings_daily_target),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${s.calculatedDailyTarget.roundToInt()}g/dia",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        // Save button
                        Button(
                            onClick = {
                                val tw = targetWeight.toDoubleOrNull() ?: 95.0
                                val cw = currentWeight.toDoubleOrNull() ?: 107.0
                                val ppk = proteinPerKg.toDoubleOrNull() ?: 2.0
                                viewModel.updateTargetWeight(tw)
                                viewModel.updateCurrentWeight(cw)
                                viewModel.updateProteinPerKg(ppk)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Salvar Perfil", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            // Reminders Section
            SettingsSection(title = stringResource(com.proteintracker.R.string.settings_reminders)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(crossAxisAlignment = Alignment.Start) {
                                Text(text = stringResource(com.proteintracker.R.string.settings_meal_reminders), fontSize = 16.sp)
                                Text(text = "Receba lembretes para registrar suas refeições", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = settings?.mealRemindersEnabled ?: true,
                                onCheckedChange = { viewModel.toggleReminders(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    trackColor = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
                        (settings?.mealRemindersEnabled ?: true).let { enabled ->
                            if (enabled) {
                                Button(
                                    onClick = { showReminderDialog = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Editar horários dos lembretes")
                                        Text(
                                            text = reminderTimes.joinToString(", "),
                                            fontSize = 14.sp,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Icon(imageVector = AppIcons.Edit, contentDescription = null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Notifications Section
            SettingsSection(title = stringResource(com.proteintracker.R.string.settings_notifications)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(crossAxisAlignment = Alignment.Start) {
                                Text(text = "Notificações push", fontSize = 16.sp)
                                Text(text = "Receba alertas de metas e lembretes", fontSize = 12.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = settings?.notificationsEnabled ?: true,
                                onCheckedChange = { viewModel.toggleNotifications(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    trackColor = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }
            
            // Data Section
            SettingsSection(title = stringResource(com.proteintracker.R.string.settings_data)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SettingsRow(
                            icon = AppIcons.Download,
                            title = stringResource(com.proteintracker.R.string.settings_export_csv),
                            subtitle = "Exportar seus dados para CSV",
                            onClick = { /* TODO: Implement export */ }
                        )
                        SettingsRow(
                            icon = AppIcons.Upload,
                            title = stringResource(com.proteintracker.R.string.settings_import_csv),
                            subtitle = "Importar dados de um CSV",
                            onClick = { /* TODO: Implement import */ }
                        )
                        SettingsRow(
                            icon = AppIcons.Delete,
                            title = stringResource(com.proteintracker.R.string.settings_clear_data),
                            subtitle = "Apagar todo o histórico (irreversível)",
                            onClick = { /* TODO: Implement clear with confirmation */ },
                            isDestructive = true
                        )
                    }
                }
            }
            
            // About Section
            SettingsSection(title = stringResource(com.proteintracker.R.string.settings_about)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SettingsRow(
                            icon = AppIcons.Info,
                            title = "ProteinTracker",
                            subtitle = "App para controle de proteína diária",
                            onClick = { }
                        )
                        SettingsRow(
                            icon = AppIcons.Star,
                            title = stringResource(com.proteintracker.R.string.settings_version),
                            subtitle = "Baseado nas 50 receitas do Chef Proteína",
                            onClick = { }
                        )
                        SettingsRow(
                            icon = AppIcons.Favorite,
                            title = "Meta padrão",
                            subtitle = "190g proteína/dia (2g/kg para 95kg alvo)",
                            onClick = { }
                        )
                    }
                }
            }
            
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 80.dp))
        }
    }
    
    // Reminder times dialog
    if (showReminderDialog) {
        ReminderTimesDialog(
            currentTimes = reminderTimes,
            onDismiss = { showReminderDialog = false },
            onSave = { newTimes ->
                reminderTimes = newTimes
                viewModel.updateReminderTimes(newTimes)
                showReminderDialog = false
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        content()
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.proteintracker.ui.common.clickable
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickable(Modifier, onClick)),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isDestructive) androidx.compose.material3.MaterialTheme.colorScheme.error else androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
                Column(crossAxisAlignment = Alignment.Start) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color = if (isDestructive) androidx.compose.material3.MaterialTheme.colorScheme.error else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = AppIcons.Navigation,
                contentDescription = null,
                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ReminderTimesDialog(
    currentTimes: List<String>,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Button
    import androidx.compose.material3.Card
    import androidx.compose.material3.Icon
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.proteintracker.ui.common.AppIcons
    
    var times by remember { mutableStateOf(currentTimes.toMutableList()) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Horários dos Lembretes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    androidx.compose.material3.IconButton(onClick = onDismiss) {
                        Icon(imageVector = AppIcons.Close, contentDescription = "Fechar")
                    }
                }
                
                Text(text = "Um horário por linha (formato HH:MM)", fontSize = 13.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    times.forEachIndexed { index, time ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = time,
                                onValueChange = { times[index] = it },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
                                visualTransformation = androidx.compose.ui.text.input.VisualTransformation { output ->
                                    var text = output.text.filter { it.isDigit() }
                                    if (text.length > 2) text = "${text.substring(0, 2)}:${text.substring(2)}"
                                    androidx.compose.ui.text.AnnotatedString(text)
                                }
                            )
                            if (times.size > 1) {
                                androidx.compose.material3.IconButton(onClick = { times.removeAt(index) }) {
                                    Icon(imageVector = AppIcons.Delete, contentDescription = "Remover", tint = androidx.compose.material3.MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                    
                    if (times.size < 8) {
                        Button(
                            onClick = { times.add("") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                            )
                        ) {
                            Row(horizontalArrangement = Arrangement.Center) {
                                Icon(imageVector = AppIcons.Add, contentDescription = null, modifier = Modifier.size(18.dp).padding(end = 8.dp))
                                Text("Adicionar horário")
                            }
                        }
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val validTimes = times.filter { it.matches("\\d{2}:\\d{2}".toRegex()) }
                            if (validTimes.isNotEmpty()) onSave(validTimes)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Salvar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}