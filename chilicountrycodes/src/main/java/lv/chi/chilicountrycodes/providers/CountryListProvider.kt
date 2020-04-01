package lv.chi.chilicountrycodes.providers

import androidx.annotation.WorkerThread
import lv.chi.chilicountrycodes.Country

interface CountryListProvider {

    @WorkerThread
    fun getRawCountries(): List<String>

    @WorkerThread
    fun mapRawCountry(raw: String): Country
}