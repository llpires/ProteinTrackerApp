package com.proteintracker.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proteintracker.data.model.MonthlyStats
import com.proteintracker.data.model.WeeklyStats
import com.proteintracker.ui.common.AppIcons
import com.proteintracker.ui.common.ProgressRing
import com.proteintracker.ui.common.ProteinCard
import com.proteintracker.viewmodel.ProgressViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.collectAsStateWithLifecycle
    
    val period by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val weekStart by viewModel.weekStart.collectAsStateWithLifecycle()
    val monthlyStats by viewModel.monthlyStats.collectAsStateWithLifecycle()
    val weeklyStats by viewModel.weeklyStats.collectAsStateWithLifecycle()
    
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
                    text = stringResource(com.proteintracker.R.string.progress_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Period Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProgressPeriod.values().forEach { p ->
                        Button(
                            onClick = { viewModel.setPeriod(p) },
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (period == p)
                                    androidx.compose.material3.MaterialTheme.colorScheme.primary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = when (p) {
                                    ProgressPeriod.DAILY -> "Diário"
                                    ProgressPeriod.WEEKLY -> "Semanal"
                                    ProgressPeriod.MONTHLY -> "Mensal"
                                },
                                fontWeight = FontWeight.Medium,
                                color = if (period == p)
                                    androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            
            when (period) {
                ProgressPeriod.DAILY -> DailyProgressView(weeklyStats = weeklyStats)
                ProgressPeriod.WEEKLY -> WeeklyProgressView(
                    weeklyStats = weeklyStats,
                    weekStart = weekStart,
                    onPreviousWeek = { viewModel.previousWeek() },
                    onNextWeek = { viewModel.nextWeek() }
                )
                ProgressPeriod.MONTHLY -> MonthlyProgressView(
                    monthlyStats = monthlyStats,
                    onPreviousMonth = { viewModel.previousMonth() },
                    onNextMonth = { viewModel.nextMonth() }
                )
            }
            
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 80.dp))
        }
    }
}

@Composable
fun DailyProgressView(weeklyStats: WeeklyStats?) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.proteintracker.ui.common.AppIcons
    import com.proteintracker.ui.common.ProteinCard
    import com.proteintracker.ui.common.ProgressRing
    import kotlinx.datetime.LocalDate
    
    val today = LocalDate.now()
    val todayLog = weeklyStats?.dailyLogs?.firstOrNull { it.date == today }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (todayLog != null) {
            // Main Progress Ring
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val progress = todayLog.progressPercent / 100f
                val color = when {
                    todayLog.isTargetMet -> androidx.compose.material3.MaterialTheme.colorScheme.success
                    progress >= 0.75f -> androidx.compose.material3.MaterialTheme.colorScheme.primary
                    progress >= 0.5f -> androidx.compose.material3.MaterialTheme.colorScheme.warning
                    else -> androidx.compose.material3.MaterialTheme.colorScheme.error
                }
                ProgressRing(
                    progress = progress,
                    size = 180,
                    strokeWidth = 16,
                    progressColor = color,
                    centerText = "${todayLog.totalProtein.roundToInt()}/${todayLog.targetProtein.roundToInt()}g"
                )
            }
            
            // Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProteinCard(
                    title = "Proteína",
                    value = "${todayLog.totalProtein.roundToInt()}g",
                    subtitle = "${todayLog.totalCalories} kcal",
                    icon = AppIcons.Food,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
                ProteinCard(
                    title = "Restante",
                    value = "${todayLog.remainingProtein.roundToInt()}g",
                    subtitle = "${todayLog.progressPercent.roundToInt()}%",
                    icon = AppIcons.Navigation,
                    color = if (todayLog.isTargetMet) 
                        androidx.compose.material3.MaterialTheme.colorScheme.success 
                    else 
                        androidx.compose.material3.MaterialTheme.colorScheme.warning
                )
            }
            
            // Meal breakdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Detalhamento por refeição",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    todayLog.mealsByType().entries.sortedBy { it.key.order }.forEach { (type, meals) ->
                        val totalProtein = meals.sumOf { it.proteinGrams }
                        if (meals.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = type.displayName,
                                    fontSize = 14.sp,
                                    color = Color(type.colorRes.toInt())
                                )
                                Text(
                                    text = "${totalProtein.roundToInt()}g",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ) {
                com.proteintracker.ui.common.EmptyState(
                    icon = AppIcons.Food,
                    title = "Nenhum dado hoje",
                    message = "Registre suas refeições para ver o progresso"
                )
            }
        }
    }
}

@Composable
fun WeeklyProgressView(
    weeklyStats: WeeklyStats?,
    weekStart: LocalDate,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Button
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
    import com.proteintracker.ui.common.AppIcons
    import com.proteintracker.ui.common.ProgressRing
    import com.proteintracker.ui.common.ProteinCard
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Week selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.IconButton(onClick = onPreviousWeek) {
                Icon(imageVector = AppIcons.Back, contentDescription = "Semana anterior")
            }
            Text(
                text = "${weekStart.format("dd/MM")} - ${weekStart.plusDays(6).format("dd/MM")}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            androidx.compose.material3.IconButton(onClick = onNextWeek) {
                Icon(imageVector = AppIcons.Navigation, contentDescription = "Próxima semana")
            }
        }
        
        weeklyStats?.let { stats ->
            // Summary cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProteinCard(
                    title = "Média diária",
                    value = "${stats.averageProtein.roundToInt()}g",
                    icon = AppIcons.Food,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
                ProteinCard(
                    title = "Dias na meta",
                    value = "${stats.daysOnTarget}/7",
                    icon = AppIcons.CheckCircle,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.success
                )
                ProteinCard(
                    title = "Sequência",
                    value = "${stats.currentStreak} dias",
                    icon = AppIcons.Fire,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.warning
                )
            }
            
            // Consistency ring
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ProgressRing(
                    progress = stats.consistencyPercent / 100f,
                    size = 140,
                    strokeWidth = 14,
                    progressColor = androidx.compose.material3.MaterialTheme.colorScheme.success,
                    centerText = "${stats.consistencyPercent.roundToInt()}%"
                )
            }
            
            // Daily breakdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Dias da semana",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(stats.dailyLogs) { dayLog ->
                            val isTargetMet = dayLog.isTargetMet
                            val progress = dayLog.progressPercent / 100f
                            val color = if (isTargetMet) 
                                androidx.compose.material3.MaterialTheme.colorScheme.success
                            else if (progress >= 0.75f)
                                androidx.compose.material3.MaterialTheme.colorScheme.primary
                            else if (progress >= 0.5f)
                                androidx.compose.material3.MaterialTheme.colorScheme.warning
                            else
                                androidx.compose.material3.MaterialTheme.colorScheme.error
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(crossAxisAlignment = Alignment.Start) {
                                    Text(
                                        text = dayLog.date.format("EEE, dd/MM"),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (dayLog.meals.isEmpty()) {
                                        Text(
                                            text = "Sem dados",
                                            fontSize = 12.sp,
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (dayLog.meals.isNotEmpty()) {
                                    ProgressRing(
                                        progress = progress,
                                        size = 60,
                                        strokeWidth = 6,
                                        progressColor = color,
                                        showPercentage = false
                                    )
                                    Text(
                                        text = "${dayLog.totalProtein.roundToInt()}g",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = color,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyProgressView(
    monthlyStats: MonthlyStats?,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Button
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
    import com.proteintracker.ui.common.AppIcons
    import com.proteintracker.ui.common.ProgressRing
    import com.proteintracker.ui.common.ProteinCard
    import kotlinx.datetime.LocalDate
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Month selector
        monthlyStats?.let { stats ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.IconButton(onClick = onPreviousMonth) {
                    Icon(imageVector = AppIcons.Back, contentDescription = "Mês anterior")
                }
                Text(
                    text = LocalDate(stats.year, stats.month, 1).format("MMMM yyyy").replaceFirstChar { it.uppercase() },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.IconButton(onClick = onNextMonth) {
                    Icon(imageVector = AppIcons.Navigation, contentDescription = "Próximo mês")
                }
            }
            
            // Summary cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProteinCard(
                    title = "Média diária",
                    value = "${stats.averageProtein.roundToInt()}g",
                    icon = AppIcons.Food,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
                ProteinCard(
                    title = "Dias na meta",
                    value = "${stats.daysOnTarget}/${stats.totalDaysTracked}",
                    icon = AppIcons.CheckCircle,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.success
                )
                ProteinCard(
                    title = "Dias rastreados",
                    value = "${stats.totalDaysTracked}",
                    icon = AppIcons.Fitness,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary
                )
            }
            
            // Consistency ring
            val consistency = if (stats.totalDaysTracked > 0) {
                (stats.daysOnTarget.toDouble() / stats.totalDaysTracked * 100)
            } else 0.0
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ProgressRing(
                    progress = (consistency / 100f).coerceIn(0f, 1f),
                    size = 140,
                    strokeWidth = 14,
                    progressColor = androidx.compose.material3.MaterialTheme.colorScheme.success,
                    centerText = "${consistency.roundToInt()}%"
                )
            }
            
            // Daily breakdown
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Dias do mês",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(stats.dailyLogs) { dayLog ->
                            if (dayLog.meals.isNotEmpty()) {
                                val isTargetMet = dayLog.isTargetMet
                                val progress = dayLog.progressPercent / 100f
                                val color = if (isTargetMet) 
                                    androidx.compose.material3.MaterialTheme.colorScheme.success
                                else if (progress >= 0.75f)
                                    androidx.compose.material3.MaterialTheme.colorScheme.primary
                                else if (progress >= 0.5f)
                                    androidx.compose.material3.MaterialTheme.colorScheme.warning
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.error
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dayLog.date.format("dd/MM (EEE)"),
                                        fontSize = 13.sp
                                    )
                                    ProgressRing(
                                        progress = progress,
                                        size = 50,
                                        strokeWidth = 5,
                                        progressColor = color,
                                        showPercentage = false
                                    )
                                    Text(
                                        text = "${dayLog.totalProtein.roundToInt()}g",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = color,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}