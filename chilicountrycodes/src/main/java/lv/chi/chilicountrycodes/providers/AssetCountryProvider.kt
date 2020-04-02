package lv.chi.chilicountrycodes.providers

import android.content.res.AssetManager
import lv.chi.chilicountrycodes.Country

internal class AssetCountryProvider(
    private val countryFileName: String,
    private val assetManager: AssetManager
) : CountryListProvider {

    override fun getRawCountries() = assetManager
        .open(countryFileName)
        .bufferedReader()
        .useLines { it.toList() }

    override fun mapRawCountry(raw: String): Country {
        val (countryCode, isoCode, countryName) = raw.split(";")
        return Country(
            isoCode = isoCode,
            phoneCode = countryCode,
            countryName = countryName
        )
    }
}