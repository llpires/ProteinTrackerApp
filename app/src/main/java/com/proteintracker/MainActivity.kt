package com.proteintracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proteintracker.ui.common.AppIcons
import com.proteintracker.ui.common.CustomNavBarItem
import com.proteintracker.ui.home.HomeScreen
import com.proteintracker.ui.logger.LoggerScreen
import com.proteintracker.ui.progress.ProgressScreen
import com.proteintracker.ui.recipes.RecipeDetailScreen
import com.proteintracker.ui.recipes.RecipesScreen
import com.proteintracker.ui.settings.SettingsScreen
import com.proteintracker.viewmodel.HomeViewModel
import com.proteintracker.viewmodel.LoggerViewModel
import com.proteintracker.viewmodel.ProgressViewModel
import com.proteintracker.viewmodel.RecipesViewModel
import com.proteintracker.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProteinTrackerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    MainNavHost()
                }
            }
        }
    }
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    var currentTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem(0, stringResource(com.proteintracker.R.string.nav_home), AppIcons.Home),
        TabItem(1, stringResource(com.proteintracker.R.string.nav_logger), AppIcons.Logger),
        TabItem(2, stringResource(com.proteintracker.R.string.nav_recipes), AppIcons.Recipes),
        TabItem(3, stringResource(com.proteintracker.R.string.nav_progress), AppIcons.Progress),
        TabItem(4, stringResource(com.proteintracker.R.string.nav_settings), AppIcons.Settings)
    )
    
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = hiltViewModel<HomeViewModel>(),
                onNavigateToLogger = { currentTab = 1; navController.navigate("logger") },
                onNavigateToRecipes = { currentTab = 2; navController.navigate("recipes") },
                onNavigateToProgress = { currentTab = 3; navController.navigate("progress") },
                onNavigateToSettings = { currentTab = 4; navController.navigate("settings") }
            )
        }
        composable("logger") {
            LoggerScreen(
                viewModel = hiltViewModel<LoggerViewModel>(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("recipes") {
            RecipesScreen(
                viewModel = hiltViewModel<RecipesViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecipeDetail = { recipe ->
                    navController.navigate("recipe_detail/${recipe.id}")
                }
            )
        }
        composable(
            route = "recipe_detail/{recipeId}",
            arguments = listOf(androidx.navigation.navArgument("recipeId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.getLong()!!
            val recipesViewModel = hiltViewModel<RecipesViewModel>()
            val recipe = recipesViewModel.filteredRecipes.firstOrNull { it.id == recipeId } ?: return@composable
            RecipeDetailScreen(
                recipe = recipe,
                onNavigateBack = { navController.popBackStack() },
                onAddToToday = { recipe ->
                    val loggerViewModel = hiltViewModel<LoggerViewModel>()
                    loggerViewModel.logRecipe(recipe)
                    navController.popBackStack()
                }
            )
        }
        composable("progress") {
            ProgressScreen(
                viewModel = hiltViewModel<ProgressViewModel>(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                viewModel = hiltViewModel<SettingsViewModel>(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
    
    // Bottom Navigation Bar
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
        ) {
            tabs.forEach { tab ->
                CustomNavBarItem(
                    selected = currentTab == tab.index,
                    onClick = { 
                        currentTab = tab.index
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    icon = tab.icon,
                    label = tab.label
                )
            }
        }
    }
}

data class TabItem(val index: Int, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    val route = when (index) {
        0 -> "home"
        1 -> "logger"
        2 -> "recipes"
        3 -> "progress"
        4 -> "settings"
        else -> "home"
    }
}