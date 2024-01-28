package com.example.tam

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tam.repository.Country
import com.example.tam.repository.CountryRepository
import com.example.tam.repository.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val countryRepository = CountryRepository()

    private val mutableData = MutableLiveData<UiState<List<Country>>>()
    val immutableData: LiveData<UiState<List<Country>>> = mutableData

    val filterQuery = MutableLiveData("")
    fun updateFilterQuery(text: String) {
        filterQuery.postValue(text)
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mutableData.postValue(UiState(isLoading = true, error = null))
                val request = countryRepository.getCountriesResponse()
                val countries = request.body()
                mutableData.postValue(UiState(data =  countries, isLoading = false, error = null))
            } catch (e: Exception) {
                Log.e("MainViewModel", "error:", e)
            }
        }
    }
}