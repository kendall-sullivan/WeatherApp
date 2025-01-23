package com.ait.aitweatherapp.ui.screen.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.aitweatherapp.data.weather.WeatherResult
import com.ait.aitweatherapp.network.WeatherAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val weatherAPI: WeatherAPI,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var cityName = "Budapest"

    init {
        cityName = savedStateHandle.get<String>("cityName") ?: "Budapest"
    }

    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Init)

    fun getWeatherData(cityName: String, units: String, appId: String) {
        weatherUiState = WeatherUiState.Loading
        viewModelScope.launch {
            weatherUiState = try {
                val result = weatherAPI.getWeatherData(cityName, units, appId)
                WeatherUiState.Success(result)
            } catch (e: IOException) {
                WeatherUiState.Error
            } catch (e: HttpException) {
                WeatherUiState.Error
            } catch (e: Exception) {
                WeatherUiState.Error
            }
        }
    }
}

sealed interface WeatherUiState {
    object Init : WeatherUiState
    data class Success(val weatherData: WeatherResult) : WeatherUiState
    object Error : WeatherUiState
    object Loading : WeatherUiState
}