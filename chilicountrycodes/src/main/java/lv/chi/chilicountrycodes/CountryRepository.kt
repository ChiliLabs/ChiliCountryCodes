package lv.chi.chilicountrycodes

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.chi.chilicountrycodes.providers.AssetCountryProvider
import lv.chi.chilicountrycodes.providers.CountryListProvider
import java.util.*

/**
 * CountryRepository handles country list loading from provider and caching as well as
 * country lookup based ISO code or device SIM settings (if available).
 *
 * @param appContext - application context for telephony services.
 * @param provider - source of country information. Uses csv asset file by default.
 * @param defaultCountry - country that will be returned in case of lookup failure.
 */
class CountryRepository(
    private val appContext: Context,
    private val provider: CountryListProvider,
    private val defaultCountry: Country = Country.UNDEFINED
) {

    private var cachedCountriesList: List<Country> = emptyList()

    /**
     * Lazily loads country information from provider and returns whole list.
     * Values are cached on first call, use `forceReload` parameter to invalidate cache.
     */
    suspend fun countries(forceReload: Boolean = false) = withContext(Dispatchers.IO) {
        if (forceReload || cachedCountriesList.isEmpty()) {
            Log.i("CountryRepository", "Loading country list from provider")
            val loadedCountries = provider.getRawCountries().map { provider.mapRawCountry(it) }
            cachedCountriesList = loadedCountries
        }
        cachedCountriesList
    }

    /**
     * Returns country information based on devices telephony services.
     * Detection does not require extra permissions.
     */
    suspend fun detectCountry(): Country = simCountryIsoCode(appContext)
        ?.let { findCountry(countries(false), it) }
        ?: defaultCountry

    /**
     * Returns information about country with provided ISO code or default
     * value if no country with such code.
     */
    suspend fun countryWithIsoCode(code: String): Country = findCountry(
        countries(),
        code.toUpperCase(Locale.getDefault())
    ) ?: defaultCountry

    /**
     * Current default country in repository.
     */
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


