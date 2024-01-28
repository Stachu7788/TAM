package com.example.tam.repository

import retrofit2.Response

class CountryRepository {
    suspend fun getCountryResponse(code: String): Response<List<Country>> =
        CountryService.countryService.getCountry(code)
    suspend fun getCountriesResponse(): Response<List<Country>> =
        CountryService.countryService.getCountries()

}