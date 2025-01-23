package com.ait.aitweatherapp.ui.screen.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.ait.aitweatherapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel(),
    onWeatherDetailsAPISelected: (cityName: String) -> Unit = {}
) {

    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    val cities by viewModel.cities.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.weather_app_text)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_text),
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (cities.isEmpty()) {
                Text(
                    stringResource(R.string.no_cities_text),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(cities.size) { index ->
                        Button(
                            onClick = {
                                val cityName = cities[index]
                                onWeatherDetailsAPISelected(cityName)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(64.dp),
                            shape = RoundedCornerShape(4.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cities[index],
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(1f),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.delete_text),
                                    modifier = Modifier.clickable {
                                        viewModel.removeCity(cities[index])
                                    },
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        CityDialog(viewModel,
            onCancel = {
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CityDialog(
    viewModel: CitiesViewModel,
    onCancel: () -> Unit,
) {
    var cityName by rememberSaveable { mutableStateOf("") }

    val invalidErrorString = stringResource(R.string.invalid_error_text)

    var nameErrorState by rememberSaveable { mutableStateOf(true) }
    val nameErrorText by rememberSaveable { mutableStateOf(invalidErrorString) }

    LaunchedEffect(cityName) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            nameErrorState = !viewModel.validateName(cityName)
        }
    }

    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(stringResource(R.string.add_new_city_text), style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.city_name_text)) },
                    value = cityName,
                    onValueChange = {
                        cityName = it
                    },
                    isError = nameErrorState,
                    supportingText = {
                        if (nameErrorState) {
                            Text(nameErrorText)
                        }
                    },
                    trailingIcon = {
                        if (nameErrorState) {
                            Icon(
                                Icons.Filled.Warning, stringResource(R.string.error_icon_text),
                                tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (!nameErrorState) {
                            val formattedName = viewModel.toCapitalizedString(cityName)
                            viewModel.addCity(formattedName)
                        } else {
                            return@TextButton
                        }
                        onCancel()
                    }) {
                        Text(stringResource(R.string.save_text))
                    }
                }
            }
        }
    }
}