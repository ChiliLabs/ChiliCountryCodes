package lv.chi.chilicountrycodes

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.chi.chilicountrycodes.providers.AssetCountryProvider
import lv.chi.chilicountrycodes.providers.CountryListProvider

class CountryRepository(
    private val appContext: Context,
    private val provider: CountryListProvider,
    val defaultCountry: Country = Country.UNDEFINED
) {

    private var cachedCountriesList: List<Country> = emptyList()

    fun countries(forceReload: Boolean = false, onResult: (List<Country>) -> Unit) = launchAsCancellable(Dispatchers.Main) {
        onResult(getCountries(forceReload))
    }

    fun detectCountry(onResult: (Country) -> Unit) = launchAsCancellable(Dispatchers.Default) {
        var result = defaultCountry

        simCountryIsoCode(appContext)
            ?.let { findCountry(getCountries(false), it) }
            ?.also { result = it }

        withContext(Dispatchers.Main) {
            onResult(result)
        }
    }

    fun countryWithIsoCode(code: String, onResult: (Country) -> Unit) = launchAsCancellable(Dispatchers.Main) {
        var result = defaultCountry
        findCountry(getCountries(false), code)?.let { result = it }
        onResult(result)
    }

    private suspend fun getCountries(forceReload: Boolean): List<Country> = withContext(Dispatchers.IO) {
        if (forceReload || cachedCountriesList.isEmpty()) {
            val loadedCountries = provider.getRawCountries().map { provider.mapRawCountry(it) }
            cachedCountriesList = loadedCountries
        }
        cachedCountriesList
    }

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


