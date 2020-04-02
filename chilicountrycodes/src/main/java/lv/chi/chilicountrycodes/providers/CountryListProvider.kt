package lv.chi.chilicountrycodes.providers

import lv.chi.chilicountrycodes.Country

/**
 * This interface may be provided to load list of countries from other sources than assets,
 * for example it might be user to provide hard-coded list of supported countries:
 *
 * ```
 * class SupportedCountryListProvider : CountryListProvider {
 *   override fun getRawCountries() = listOf(
 *     "LT;370;Lithuania",
 *     "LV;371;Latvia",
 *     "EE;372;Estonia"
 *   )
 *
 *   override fun mapRawCountry(raw: String): Country {
 *      val (iso, code, name) = raw.split(";")
 *      return Country(iso, code, name)
 *   }
 * }
 * ```
 *
 * Both methods are called on background thread (IO coroutine dispatcher).
 */
interface CountryListProvider {

    fun getRawCountries(): List<String>

    fun mapRawCountry(raw: String): Country
}
