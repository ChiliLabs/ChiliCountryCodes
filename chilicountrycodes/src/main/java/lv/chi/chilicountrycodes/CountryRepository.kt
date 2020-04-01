package lv.chi.chilicountrycodes

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.chi.chilicountrycodes.providers.AssetCountryProvider
import lv.chi.chilicountrycodes.providers.CountryListProvider
import java.util.*

class CountryRepository(
    private val appContext: Context,
    private val provider: CountryListProvider,
    private val defaultCountry: Country = Country.UNDEFINED
) {

    private var cachedCountriesList: List<Country> = emptyList()

    suspend fun countries(forceReload: Boolean = false) = withContext(Dispatchers.IO) {
        if (forceReload || cachedCountriesList.isEmpty()) {
            Log.i("CountryRepository", "Loading country list from provider")
            val loadedCountries = provider.getRawCountries().map { provider.mapRawCountry(it) }
            cachedCountriesList = loadedCountries
        }
        cachedCountriesList
    }

    suspend fun detectCountry(): Country = simCountryIsoCode(appContext)
        ?.let { findCountry(countries(false), it) }
        ?: defaultCountry

    suspend fun countryWithIsoCode(code: String): Country = findCountry(
        countries(),
        code.toUpperCase(Locale.getDefault())
    ) ?: defaultCountry

    fun defaultCountry() = defaultCountry

    private suspend fun findCountry(countries: List<Country>, code: String): Country? = withContext(Dispatchers.Default) {
        countries.firstOrNull { code == it.isoCode }
    }

    companion object {
        fun fromAssets(
            context: Context,
            countryFileName: String = "countries.txt",
            defaultCountry: Country = Country.UNDEFINED
        ) = CountryRepository(
            context.applicationContext,
            AssetCountryProvider(countryFileName, context.applicationContext.assets),
            defaultCountry
        )
    }
}


