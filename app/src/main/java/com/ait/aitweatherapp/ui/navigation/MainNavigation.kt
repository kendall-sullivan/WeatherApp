package com.ait.aitweatherapp.ui.navigation

sealed class Screen(val route: String) {
    object CitiesList : Screen("citieslist")
    object WeatherDetailsAPI : Screen("weatherdetails?city={cityName}") {
        fun createRoute(cityName: String) : String {
            return "weatherdetails?city=$cityName"
        }
    }
}