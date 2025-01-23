package com.ait.aitweatherapp.ui.screen.cities

import androidx.lifecycle.ViewModel
import com.ait.aitweatherapp.network.WeatherAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(val weatherAPI: WeatherAPI): ViewModel() {
    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities.asStateFlow()

    fun addCity(city: String) {
        val formattedCity = toCapitalizedString(city)
        _cities.value += formattedCity
    }

    fun removeCity(city: String) {
        _cities.value -= city
    }

    suspend fun validateName(cityName: String): Boolean {
        return try {
                if (cityName.isBlank()) {
                    false
                } else {
                    val name = toCapitalizedString(cityName)
                    weatherAPI.getWeatherData(
                        name,
                        "metric",
                        "e5f13ed33afee3f236b10358f5444d25"
                    )
                    true
                }
            } catch (e: IOException) {
                false
            } catch (e: Exception) {
                false
            } catch (e: HttpException) {
                false
            }
    }

    fun toCapitalizedString(name: String): String {
        return name.trim().lowercase().split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { char -> char.titlecase() }
        }
    }
}