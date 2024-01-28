package com.example.tam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tam.repository.Country
import com.example.tam.repository.UiState
import com.example.tam.ui.theme.TAMTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()
        setContent {
            TAMTheme {
                val uiState by viewModel.immutableData.observeAsState(UiState())
                Surface (modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Scaffold(
                        bottomBar = { MyTopView(viewModel = viewModel)}
                    ) { scaffoldPaddings ->
                        when {
                            uiState.isLoading -> {
                                LoadingView()
                            }

                            uiState.error != null -> {
                                ErrorView(uiState.error!!)
                            }

                            uiState.data != null -> {
                                uiState.data?.let {
                                    MainView(
                                        modifier = Modifier.padding(scaffoldPaddings),
                                        viewModel = viewModel,
                                        onClick = { name -> navigateToDetails(name) })
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun navigateToDetails(name: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("COUNTRY_NAME", name)
        startActivity(intent)
    }
}
@Composable
fun ErrorView(string: String) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Snackbar {
            Text(text = string)
        }
    }
}

@Composable
fun LoadingView() {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopView(viewModel: MainViewModel) {
    var searchText by remember { mutableStateOf("") }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = searchText,
        onQueryChange = { wpisywanyTekst -> searchText = wpisywanyTekst },
        onSearch = { viewModel.updateFilterQuery(it) },
        placeholder = { Text(text = "Wyszukaj...") },
        active = false,
        onActiveChange = { },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Image(
                modifier = Modifier.clickable {
                    searchText = ""
                    viewModel.updateFilterQuery("")
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear"
            )
        }
    ) {

    }
}
@Composable
fun MainView(modifier: Modifier, viewModel: MainViewModel, onClick: (String) -> Unit) {
    val countries by viewModel.immutableData.observeAsState(UiState())
    val query by viewModel.filterQuery.observeAsState("")

    val countryList = countries.data?.filter { it.name.common.contains(query, ignoreCase = true) }
    Log.d("MainViewQuery", "$query, ${countryList?.size}")
    if (countryList != null) {
        CountryList(countries = countryList, onClick = onClick)
    }
}


@Composable
fun CountryList(countries: List<Country>, onClick: (String) -> Unit) {
    LazyColumn() {
        if (countries.isNotEmpty()) {
            countries.forEach { country -> item {
                    Row (modifier = Modifier.clickable { onClick.invoke(country.name.common) }) {
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(70.dp)
                                .padding(5.dp)
                        )
                        {
                            AsyncImage(
                                model = country.flags.values.first(),
                                contentDescription = "flag"
                            )
                        }
                        Column {
                            Text(
                                text = country.name.common,
                                fontSize = 20.sp
                            )
                            Text(
                                text = country.capital.first(),
                                fontSize = 15.sp
                            )

                        }
                    }
                }
            }
        }
    }
}