package com.ait.aitweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ait.aitweatherapp.data.weather.WeatherResult
import com.ait.aitweatherapp.ui.navigation.Screen
import com.ait.aitweatherapp.ui.screen.cities.CitiesListScreen
import com.ait.aitweatherapp.ui.screen.details.WeatherDetailsScreen
import com.ait.aitweatherapp.ui.theme.AITWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AITWeatherAppTheme {
                NavGraph(
                    modifier = Modifier.padding()
                )
            }
        }
    }
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.CitiesList.route
    ) {
        composable(Screen.CitiesList.route) {
            CitiesListScreen(
                onWeatherDetailsAPISelected = { cityName -> navController.navigate(Screen.WeatherDetailsAPI.createRoute(cityName)) },
            )
        }
        composable(Screen.WeatherDetailsAPI.route) {
            WeatherDetailsScreen()
        }
    }
}
