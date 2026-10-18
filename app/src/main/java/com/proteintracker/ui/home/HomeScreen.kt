package com.proteintracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.proteintracker.data.model.Meal
import com.proteintracker.data.model.MealType
import com.proteintracker.ui.common.AppIcons
import com.proteintracker.ui.common.CustomNavBarItem
import com.proteintracker.ui.common.EmptyState
import com.proteintracker.ui.common.ProgressRing
import com.proteintracker.ui.common.ProteinCard
import com.proteintracker.viewmodel.HomeViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToLogger: () -> Unit,
    onNavigateToRecipes: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.collectAsStateWithLifecycle
    
    val dayLog by viewModel.dayLog.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val nextMeal by viewModel.nextMealType.collectAsStateWithLifecycle()
    
    val targetProtein = settings?.calculatedDailyTarget ?: 190.0
    val progress = (dayLog.totalProtein / targetProtein).coerceAtMost(1.0f)
    val progressColor = when {
        progress >= 1.0f -> androidx.compose.material3.MaterialTheme.colorScheme.success
        progress >= 0.75f -> androidx.compose.material3.MaterialTheme.colorScheme.primary
        progress >= 0.5f -> androidx.compose.material3.MaterialTheme.colorScheme.warning
        else -> androidx.compose.material3.MaterialTheme.colorScheme.error
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Progress Ring
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ProgressRing(
                    progress = progress,
                    size = 140,
                    strokeWidth = 14,
                    progressColor = progressColor,
                    centerText = "${dayLog.totalProtein.roundToInt()}/${targetProtein.roundToInt()}g"
                )
            }
            
            // Stats Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProteinCard(
                    title = stringResource(com.proteintracker.R.string.home_consumed),
                    value = "${dayLog.totalProtein.roundToInt()}g",
                    subtitle = "${dayLog.totalCalories} kcal",
                    icon = AppIcons.Food,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
                
                ProteinCard(
                    title = stringResource(com.proteintracker.R.string.home_remaining),
                    value = "${dayLog.remainingProtein.roundToInt()}g",
                    subtitle = "${dayLog.progressPercent.roundToInt()}% da meta",
                    icon = AppIcons.Navigation,
                    color = if (dayLog.isTargetMet) 
                        androidx.compose.material3.MaterialTheme.colorScheme.success 
                    else 
                        androidx.compose.material3.MaterialTheme.colorScheme.warning
                )
            }
            
            // Next Meal Card
            nextMeal?.let { mealType ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = AppIcons.Schedule,
                                contentDescription = null,
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp).padding(end = 12.dp)
                            )
                            Column {
                                Text(
                                    text = "Próxima refeição sugerida",
                                    fontSize = 12.sp,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = mealType.displayName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Button(onClick = onNavigateToLogger) {
                            Text("Registrar")
                        }
                    }
                }
            }
            
            // Today's Meals
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Refeições de hoje",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dayLog.date.format("dd/MM/yyyy"),
                        fontSize = 14.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (dayLog.meals.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    ) {
                        EmptyState(
                            icon = AppIcons.Food,
                            title = "Nenhuma refeição hoje",
                            message = "Toque em + para registrar sua primeira refeição",
                            actionText = "Adicionar refeição",
                            onAction = onNavigateToLogger
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(dayLog.meals) { meal ->
                            MealItemCard(meal = meal, onDelete = { viewModel.deleteMeal(meal) })
                        }
                    }
                }
            }
            
            // Quick Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToLogger,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(imageVector = AppIcons.Add, contentDescription = null, modifier = Modifier.size(20.dp).padding(end = 8.dp))
                    Text("Registrar", fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = onNavigateToRecipes,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(imageVector = AppIcons.Recipes, contentDescription = null, modifier = Modifier.size(20.dp).padding(end = 8.dp))
                    Text("Receitas", fontWeight = FontWeight.Bold)
                }
            }
            
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 80.dp))
        }
    }
}

@Composable
fun MealItemCard(
    meal: Meal,
    onDelete: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.proteintracker.ui.common.AppIcons
    
    val mealColor = Color(meal.mealType.colorRes.toInt())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(mealColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = when (meal.mealType) {
                            MealType.BREAKFAST -> AppIcons.Schedule
                            MealType.LUNCH -> AppIcons.Food
                            MealType.DINNER -> AppIcons.Navigation
                            MealType.PRE_WORKOUT, MealType.POST_WORKOUT -> AppIcons.Fitness
                            else -> AppIcons.Food
                        },
                        contentDescription = null,
                        tint = mealColor
                    )
                }
                
                Column(crossAxisAlignment = Alignment.Start) {
                    Text(
                        text = meal.foodName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = meal.mealType.displayName,
                        fontSize = 12.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${meal.proteinGrams.roundToInt()}g proteína",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
                if (meal.calories > 0) {
                    Text(
                        text = "${meal.calories} kcal",
                        fontSize = 12.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = AppIcons.Delete,
                        contentDescription = "Excluir",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}