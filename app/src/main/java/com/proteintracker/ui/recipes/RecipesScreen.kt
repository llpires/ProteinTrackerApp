package com.proteintracker.ui.recipes

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proteintracker.data.model.Recipe
import com.proteintracker.data.model.RecipeCategory
import com.proteintracker.ui.common.AppIcons
import com.proteintracker.ui.common.ProteinCard
import com.proteintracker.viewmodel.RecipesViewModel

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToRecipeDetail: (Recipe) -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.collectAsStateWithLifecycle
    
    val recipes by viewModel.filteredRecipes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val showQuickOnly by viewModel.showQuickOnly.collectAsStateWithLifecycle()
    val showMealPrepOnly by viewModel.showMealPrepOnly.collectAsStateWithLifecycle()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with search
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
                    text = stringResource(com.proteintracker.R.string.recipes_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.IconButton(onClick = { viewModel.clearFilters() }) {
                    Icon(imageVector = AppIcons.Filter, contentDescription = "Limpar filtros")
                }
            }
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = { Icon(imageVector = AppIcons.Search, contentDescription = null) },
                placeholder = { Text(text = stringResource(com.proteintracker.R.string.recipes_search_hint)) },
                singleLine = true,
                colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
                )
            )
            
            // Filter Chips
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
                    // Category filter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Chip(
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setCategory(null) },
                            selected = selectedCategory == null,
                            onSelectedChange = { _ -> viewModel.setCategory(null) },
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = if (selectedCategory == null)
                                    androidx.compose.material3.MaterialTheme.colorScheme.primary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                labelColor = if (selectedCategory == null)
                                    androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            ),
                            label = { Text(text = stringResource(com.proteintracker.R.string.recipes_filter_all), fontSize = 11.sp) }
                        )
                        
                        Chip(
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setCategory(RecipeCategory.SAVORY) },
                            selected = selectedCategory == RecipeCategory.SAVORY,
                            onSelectedChange = { _ -> viewModel.setCategory(RecipeCategory.SAVORY) },
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = if (selectedCategory == RecipeCategory.SAVORY)
                                    androidx.compose.material3.MaterialTheme.colorScheme.primary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                labelColor = if (selectedCategory == RecipeCategory.SAVORY)
                                    androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            ),
                            label = { Text(text = stringResource(com.proteintracker.R.string.recipes_filter_savory), fontSize = 11.sp) }
                        )
                        
                        Chip(
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setCategory(RecipeCategory.SWEET) },
                            selected = selectedCategory == RecipeCategory.SWEET,
                            onSelectedChange = { _ -> viewModel.setCategory(RecipeCategory.SWEET) },
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = if (selectedCategory == RecipeCategory.SWEET)
                                    androidx.compose.material3.MaterialTheme.colorScheme.secondary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                labelColor = if (selectedCategory == RecipeCategory.SWEET)
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            ),
                            label = { Text(text = stringResource(com.proteintracker.R.string.recipes_filter_sweet), fontSize = 11.sp) }
                        )
                    }
                    
                    // Quick & Meal Prep filters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Chip(
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.toggleQuickFilter() },
                            selected = showQuickOnly,
                            onSelectedChange = { _ -> viewModel.toggleQuickFilter() },
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = if (showQuickOnly)
                                    androidx.compose.material3.MaterialTheme.colorScheme.warning
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                labelColor = if (showQuickOnly)
                                    androidx.compose.material3.MaterialTheme.colorScheme.onWarning
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            ),
                            label = { 
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = AppIcons.Timer, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text(text = stringResource(com.proteintracker.R.string.recipes_filter_quick), fontSize = 11.sp)
                                }
                            }
                        )
                        
                        Chip(
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.toggleMealPrepFilter() },
                            selected = showMealPrepOnly,
                            onSelectedChange = { _ -> viewModel.toggleMealPrepFilter() },
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = if (showMealPrepOnly)
                                    androidx.compose.material3.MaterialTheme.colorScheme.tertiary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                labelColor = if (showMealPrepOnly)
                                    androidx.compose.material3.MaterialTheme.colorScheme.onTertiary
                                else
                                    androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            ),
                            label = { 
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = AppIcons.Fitness, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text(text = stringResource(com.proteintracker.R.string.recipes_filter_meal_prep), fontSize = 11.sp)
                                }
                            }
                        )
                    }
                }
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
                        message = "Tente ajustar os filtros ou buscar com outros termos"
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
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onNavigateToRecipeDetail(recipe) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(com.proteintracker.ui.common.clickable(Modifier, onClick)),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
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
                        .size(56.dp)
                        .background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
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
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "${recipe.proteinPerServing.roundToInt()}g proteína",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor
                        )
                        Text(
                            text = "${recipe.prepTimeMinutes} min",
                            fontSize = 12.sp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (recipe.isQuick) {
                            Chip(
                                onClick = { },
                                enabled = false,
                                colors = ChipDefaults.elevatedChipColors(
                                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.warning.copy(alpha = 0.2f),
                                    labelColor = androidx.compose.material3.MaterialTheme.colorScheme.warning
                                ),
                                label = { 
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(imageVector = AppIcons.Timer, contentDescription = null, modifier = Modifier.size(12.dp))
                                        Text(text = "Rápida", fontSize = 10.sp)
                                    }
                                }
                            )
                        }
                        if (recipe.isMealPrep) {
                            Chip(
                                onClick = { },
                                enabled = false,
                                colors = ChipDefaults.elevatedChipColors(
                                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                                    labelColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary
                                ),
                                label = { 
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(imageVector = AppIcons.Fitness, contentDescription = null, modifier = Modifier.size(12.dp))
                                        Text(text = "Meal Prep", fontSize = 10.sp)
                                    }
                                }
                            )
                        }
                    }
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
fun RecipeDetailScreen(
    recipe: Recipe,
    onNavigateBack: () -> Unit,
    onAddToToday: (Recipe) -> Unit
) {
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
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
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.proteintracker.data.model.RecipeCategory
    import com.proteintracker.ui.common.AppIcons
    import kotlinx.serialization.json.Json
    import kotlinx.serialization.decodeFromString
    
    val json = Json { ignoreUnknownKeys = true }
    val ingredients = try { json.decodeFromString<List<String>>(recipe.ingredients) } catch (e: Exception) { emptyList<String>() }
    val instructions = try { json.decodeFromString<List<String>>(recipe.instructions) } catch (e: Exception) { emptyList<String>() }
    
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
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = categoryColor.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(categoryColor, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = null,
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(crossAxisAlignment = Alignment.Start) {
                            Text(
                                text = recipe.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = recipe.category.displayName,
                                fontSize = 14.sp,
                                color = categoryColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    androidx.compose.material3.IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = AppIcons.Back, contentDescription = "Voltar")
                    }
                }
                
                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProteinCard(
                        title = "Proteína",
                        value = "${recipe.proteinPerServing.roundToInt()}g",
                        icon = AppIcons.Food,
                        color = categoryColor,
                        modifier = Modifier.weight(1f)
                    )
                    ProteinCard(
                        title = "Calorias",
                        value = "${recipe.caloriesPerServing}",
                        icon = AppIcons.Fire,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.warning,
                        modifier = Modifier.weight(1f)
                    )
                    ProteinCard(
                        title = "Tempo",
                        value = "${recipe.prepTimeMinutes} min",
                        icon = AppIcons.Timer,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Secondary stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProteinCard(
                        title = "Carboidratos",
                        value = "${recipe.carbsPerServing.roundToInt()}g",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    ProteinCard(
                        title = "Gorduras",
                        value = "${recipe.fatPerServing.roundToInt()}g",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    ProteinCard(
                        title = "Porções",
                        value = "${recipe.servings}",
                        icon = AppIcons.Navigation,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (recipe.isQuick) {
                        Chip(
                            onClick = { },
                            enabled = false,
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.warning.copy(alpha = 0.2f),
                                labelColor = androidx.compose.material3.MaterialTheme.colorScheme.warning
                            ),
                            label = { 
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = AppIcons.Timer, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text(text = "Rápida (<15min)", fontSize = 11.sp)
                                }
                            }
                        )
                    }
                    if (recipe.isMealPrep) {
                        Chip(
                            onClick = { },
                            enabled = false,
                            colors = ChipDefaults.elevatedChipColors(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                                labelColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary
                            ),
                            label = { 
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = AppIcons.Fitness, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text(text = "Meal Prep", fontSize = 11.sp)
                                }
                            }
                        )
                    }
                }
            }
        }
        
        // Ingredients
        if (ingredients.isNotEmpty()) {
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
                        text = stringResource(com.proteintracker.R.string.recipes_ingredients),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ingredients.forEach { ingredient ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = AppIcons.Check,
                                    contentDescription = null,
                                    tint = categoryColor,
                                    modifier = Modifier.size(16.dp).padding(end = 8.dp)
                                )
                                Text(text = ingredient, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
        
        // Instructions
        if (instructions.isNotEmpty()) {
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
                        text = stringResource(com.proteintracker.R.string.recipes_instructions),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        instructions.forEachIndexed { index, instruction ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(categoryColor, RoundedCornerShape(12.dp))
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Text(text = instruction, fontSize = 14.sp, modifier = Modifier.weight(1f).padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }
        
        // Tips
        if (recipe.tips.isNotBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.Info,
                            contentDescription = null,
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(com.proteintracker.R.string.recipes_tips),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Text(
                        text = recipe.tips,
                        fontSize = 14.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                        fontStyle = androidx.compose.ui.text.style.FontStyle.Italic
                    )
                }
            }
        }
        
        // Add to today button
        Button(
            onClick = { onAddToToday(recipe) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, bottom = 24.dp)
                .height(56.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = categoryColor
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = AppIcons.Add, contentDescription = null, modifier = Modifier.size(20.dp).padding(end = 8.dp))
                Text("Adicionar ao dia de hoje", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}