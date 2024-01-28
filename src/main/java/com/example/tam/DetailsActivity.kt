package com.example.tam

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tam.repository.Country
import com.example.tam.repository.Name


import com.example.tam.repository.UiState
import com.example.tam.ui.theme.TAMTheme

class DetailsActivity : ComponentActivity() {
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("COUNTRY_NAME")
        
        viewModel.getData(name!!)
        setContent {
            TAMTheme {
                val uiState by viewModel.immutableData.observeAsState(UiState())
                when {
                    uiState.isLoading -> {
                        LoadingView()
                    }

                    uiState.error != null -> {
                        ErrorView(uiState.error!!)
                    }

                    uiState.data != null -> {
                        uiState.data?.let {
                            CountryView(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountryView(viewModel: DetailsViewModel) {
    val countries by viewModel.immutableData.observeAsState(UiState())
    if (countries.data!!.isNotEmpty()) {
        CountryData(country = countries.data!![0])

    }
}

@Composable
fun CountryData(country: Country) {
    var languages: String = ""
    for (entry in country.languages)
        languages += entry.value + ", "
    languages = languages.dropLast(2)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()){
        AsyncImage(
            alignment = Alignment.TopCenter,
            modifier = Modifier.size(width = 350.dp, height = 200.dp),
                model = country.flags.values.first(),
                contentDescription = "flag"
            )
        Text(text = country.name.common,
            fontSize = TextUnit(40f, TextUnitType.Sp),
            )
        Text(text = country.capital[0],
            fontSize = TextUnit(30f, TextUnitType.Sp))
        Row(modifier = Modifier.height(30.dp)) {

        }
        Text(text = "Population: ${country.population}",
            fontSize = TextUnit(25f, TextUnitType.Sp))
        Text(text = "Languages: $languages",
            fontSize = TextUnit(25f, TextUnitType.Sp))
    }
}
