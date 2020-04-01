package lv.chi.chilicountrycodes

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lv.chi.chilicountrycodes.providers.AssetCountryProvider
import lv.chi.chilicountrycodes.providers.CountryListProvider

class CountryRepository(
    private val defaultCountry: Country = Country.UNDEFINED,
    private val provider: CountryListProvider
) {

    private var cachedList: List<Country> = emptyList()

    fun countries() = liveData {
        val result = if (cachedList.isNotEmpty()) {
            Log.wtf("!!!!!", "Using cached countries")
            cachedList
        } else {
            Log.wtf("!!!!!", "Loading countries")
            withContext(Dispatchers.IO) {
                provider.getRawCountries().map { provider.mapRawCountry(it) }
            }
        }
        emit(result)
    }

    fun detectCountry(defaultIso: String) = liveData {
        emit(defaultCountry)
    }

    companion object {

        fun fromAssets(
            assetManager: AssetManager,
            countryFileName: String = "countries.txt",
            defaultCountry: Country = Country.UNDEFINED
        ) = CountryRepository(
            defaultCountry,
            AssetCountryProvider(countryFileName, assetManager)
        )
    }
}


