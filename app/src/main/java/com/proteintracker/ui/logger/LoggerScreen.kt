package com.proteintracker.ui.logger

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
import androidx.compose.material3.Chip
import androidx.compose.material3.ChipDefaults
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proteintracker.data.model.MealType
import com.proteintracker.data.model.Recipe
import com.proteintracker.ui.common.AppIcons
import com.proteintracker.ui.common.ProteinCard
import com.proteintracker.viewmodel.LoggerViewModel
import kotlinx.coroutines.launch

@Composable
fun LoggerScreen(
    viewModel: LoggerViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.collectAsStateWithLifecycle
    
    val recipes by viewModel.filteredRecipes.collectAsStateWithLifecycle()
    val selectedMealType by viewModel.selectedMealType.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    var expandedMealType by remember { mutableStateOf(false) }
    var showCustomMeal by remember { mutableStateOf(false) }
    var customFoodName by remember { mutableStateOf("") }
    var customProtein by remember { mutableStateOf("") }
    var customCalories by remember { mutableStateOf("") }
    var customCarbs by remember { mutableStateOf("") }
    var customFat by remember { mutableStateOf("") }
    
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
                    text = stringResource(com.proteintracker.R.string.logger_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.IconButton(onClick = { showCustomMeal = true }) {
                    Icon(imageVector = AppIcons.Add, contentDescription = "Refeição personalizada")
                }
            }
            
            // Meal Type Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MealType.values().forEach { type ->
                            Chip(
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.setMealType(type) },
                                selected = selectedMealType == type,
                                onSelectedChange = { _ -> viewModel.setMealType(type) },
                                colors = ChipDefaults.elevatedChipColors(
                                    containerColor = if (selectedMealType == type)
                                        androidx.compose.material3.MaterialTheme.colorScheme.primary
                                    else
                                        androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                    labelColor = if (selectedMealType == type)
                                        androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                                    else
                                        androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                                ),
                                label = { Text(text = type.displayName, fontSize = 11.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) }
                            )
                        }
                    }
                }
            }
            
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                )
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    leadingIcon = { Icon(imageVector = AppIcons.Search, contentDescription = null) },
                    placeholder = { Text(text = stringResource(com.proteintracker.R.string.recipes_search_hint)) },
                    singleLine = true,
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest,
                        focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                    )
                )
            }
            
            // Recipe List
            if (recipes.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                ) {
                    com.proteintracker.ui.common.EmptyState(
                        icon = AppIcons.Search,
                        title = "Nenhuma receita encontrada",
                        message = "Tente buscar com outros termos ou adicione uma refeição personalizada",
                        actionText = "Refeição personalizada",
                        onAction = { showCustomMeal = true }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipes) { recipe ->
                        RecipeLoggerCard(
                            recipe = recipe,
                            mealType = selectedMealType,
                            onAdd = { viewModel.logRecipe(recipe) }
                        )
                    }
                }
            }
            
            // Custom Meal Dialog
            if (showCustomMeal) {
                CustomMealDialog(
                    mealType = selectedMealType,
                    onDismiss = { showCustomMeal = false },
                    onSave = { name, protein, calories, carbs, fat ->
                        viewModel.logCustomMeal(name, protein, calories, carbs, fat)
                        showCustomMeal = false
                        customFoodName = ""
                        customProtein = ""
                        customCalories = ""
                        customCarbs = ""
                        customFat = ""
                    }
                )
            }
        }
    }
}

@Composable
fun RecipeLoggerCard(
    recipe: Recipe,
    mealType: MealType,
    onAdd: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.padding
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
    import com.proteintracker.data.model.RecipeCategory
    import com.proteintracker.ui.common.AppIcons
    
    val categoryIcon = when (recipe.category) {
        RecipeCategory.SAVORY -> AppIcons.Food
        RecipeCategory.SWEET -> AppIcons.Favorite
        RecipeCategory.BOTH -> AppIcons.Navigation
    }
    val categoryColor = when (recipe.category) {
        RecipeCategory.SAVORY -> androidx.compose.material3.MaterialTheme.colorScheme.primary
        RecipeCategory.SWEET -> androidx.compose.material3.MaterialTheme.colorScheme.secondary
        RecipeCategory.BOTH -> androidx.compose.material3.MaterialTheme.colorScheme.tertiary
    }
    
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
                        .size(48.dp)
                        .background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = null,
                        tint = categoryColor
                    )
                }
                
                Column(crossAxisAlignment = Alignment.Start) {
                    Text(
                        text = recipe.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "${recipe.proteinPerServing.roundToInt()}g proteína",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor
                        )
                        Text(
                            text = "${recipe.prepTimeMinutes} min",
                            fontSize = 12.sp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (recipe.isQuick) {
                            Text(
                                text = "Rápida",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.warning
                            )
                        }
                        if (recipe.isMealPrep) {
                            Text(
                                text = "Meal Prep",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
            
            Button(
                onClick = onAdd,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = categoryColor
                )
            ) {
                Text("Adicionar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun CustomMealDialog(
    mealType: MealType,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int, Double, Double) -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.material3.Button
    import androidx.compose.material3.Card
    import androidx.compose.material3.Icon
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.proteintracker.ui.common.AppIcons
    
    var foodName by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    
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
                    Text(
                        text = "Refeição Personalizada - ${mealType.displayName}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    androidx.compose.material3.IconButton(onClick = onDismiss) {
                        Icon(imageVector = AppIcons.Close, contentDescription = "Fechar")
                    }
                }
                
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Nome do alimento") },
                    singleLine = true
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Proteína (g)") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = androidx.compose.ui.text.input.VisualTransformation { output ->
                            androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() || it == '.' })
                        }
                    )
                    
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Calorias") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = androidx.compose.ui.text.input.VisualTransformation { output ->
                            androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() })
                        }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Carboidratos (g)") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = androidx.compose.ui.text.input.VisualTransformation { output ->
                            androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() || it == '.' })
                        }
                    )
                    
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        modifier = Modifier.weight(1f),
                        label = { Text(text = "Gorduras (g)") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = androidx.compose.ui.text.input.VisualTransformation { output ->
                            androidx.compose.ui.text.AnnotatedString(output.text.filter { it.isDigit() || it == '.' })
                        }
                    )
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
                            val p = protein.toDoubleOrNull() ?: 0.0
                            val c = calories.toIntOrNull() ?: 0
                            val cb = carbs.toDoubleOrNull() ?: 0.0
                            val f = fat.toDoubleOrNull() ?: 0.0
                            if (foodName.isNotBlank() && p > 0) {
                                onSave(foodName, p, c, cb, f)
                            }
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