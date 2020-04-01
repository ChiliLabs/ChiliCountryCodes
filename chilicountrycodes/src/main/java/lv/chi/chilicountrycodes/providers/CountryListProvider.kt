package lv.chi.chilicountrycodes.providers

import lv.chi.chilicountrycodes.Country

interface CountryListProvider {

    fun getRawCountries(): List<String>

    fun mapRawCountry(raw: String): Country
}