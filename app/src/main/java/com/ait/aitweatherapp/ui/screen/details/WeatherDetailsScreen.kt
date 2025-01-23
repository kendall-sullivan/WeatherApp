package com.ait.aitweatherapp.ui.screen.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ait.aitweatherapp.R
import com.ait.aitweatherapp.data.weather.WeatherResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.cityName) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            Button(onClick = {
                viewModel.getWeatherData(
                    viewModel.cityName,
                    "metric",
                    "e5f13ed33afee3f236b10358f5444d25"
                )
            }) {
                Text(text = stringResource(R.string.refresh_text))
            }

            when (viewModel.weatherUiState) {
                is WeatherUiState.Init -> Text(text = stringResource(R.string.press_refresh_text))
                is WeatherUiState.Loading -> CircularProgressIndicator()
                is WeatherUiState.Success -> WeatherResultScreen(
                    (viewModel.weatherUiState as WeatherUiState.Success).weatherData
                )
                is WeatherUiState.Error -> Text(text = stringResource(R.string.error_text))
            }
        }
    }
}

@Composable
fun WeatherResultScreen(weatherResult: WeatherResult) {
    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://openweathermap.org/img/w/${
                    weatherResult.weather?.get(0)?.icon
                }.png")
                .crossfade(true)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Column() {
            Text(text = stringResource(R.string.weather_data_for) + weatherResult.name + stringResource(R.string.colon),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = stringResource(R.string.description) + weatherResult.weather?.get(0)?.description)
            Text(text = stringResource(R.string.current_temperature) + weatherResult.main?.temp + stringResource(R.string.celsius))
            Text(text = stringResource(R.string.minimum_temperature) + weatherResult.main?.tempMin + stringResource(R.string.celsius))
            Text(text = stringResource(R.string.maximum_temperature) + weatherResult.main?.tempMax + stringResource(R.string.celsius))
            Text(text = stringResource(R.string.feels_like) + weatherResult.main?.feelsLike + stringResource(R.string.celsius))
        }
    }
}