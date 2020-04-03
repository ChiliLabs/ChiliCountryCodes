package lv.chi.chilicountrycodes.providers

import android.content.res.AssetManager
import lv.chi.chilicountrycodes.Country

internal class AssetCountryProvider(
    private val countryFileName: String,
    private val assetManager: AssetManager
) : CountryListProvider {

    override fun getCountries() = assetManager
        .open(countryFileName)
        .bufferedReader()
        .useLines {
            it.map { raw ->
                val (phoneCode, isoCode, countryName) = raw.split(";")
                Country(
                    isoCode = isoCode,
                    phoneCode = phoneCode,
                    countryName = countryName
                )
            }.toList()
        }
}