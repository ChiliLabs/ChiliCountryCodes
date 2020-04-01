package lv.chi.chilicountrycodes.rx

import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository

class RxCountryRepository(
    private val internalRepo: CountryRepository
) {

    fun countries(forceReload: Boolean = false): Single<List<Country>> = rxSingle {
        internalRepo.countries(forceReload)
    }

    fun detectCountry(): Single<Country> = rxSingle {
        internalRepo.detectCountry()
    }

    fun countryWithIsoCode(code: String): Single<Country> = rxSingle {
        internalRepo.countryWithIsoCode(code)
    }

    fun defaultCountry() = internalRepo.defaultCountry()
}