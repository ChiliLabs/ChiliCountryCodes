package lv.chi.chilicountrycodes.providers

import lv.chi.chilicountrycodes.Country

/**
 * This interface may be provided to load list of countries from other sources than assets,
 * for example it might be user to provide hard-coded list of supported countries:
 *
 * ```
 * class SupportedCountryListProvider : CountryListProvider {
 *   override fun getCountries() = listOf(
 *     Country("LT", "370", "Lithuania"),
 *     Country("LV", "371", "Latvia"),
 *     Country("EE", "372", "Estonia")
 *   )
 * }
 * ```
 *
 * Provider is called on background thread (IO coroutine dispatcher).
 */
interface CountryListProvider {

    fun getCountries(): List<Country>
}
