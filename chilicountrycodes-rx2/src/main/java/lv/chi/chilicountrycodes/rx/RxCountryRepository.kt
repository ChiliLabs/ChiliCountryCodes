package lv.chi.chilicountrycodes.rx

import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository

/**
 * Thin wrapper to suspend function calls into rx.Single instances.
 *
 * @param internalRepo - actual repository that will do real work.
 */
class RxCountryRepository(
    private val internalRepo: CountryRepository
) {

    /**
     * See [CountryRepository.countries]
     */
    fun countries(forceReload: Boolean = false): Single<List<Country>> = rxSingle {
        internalRepo.countries(forceReload)
    }

    /**
     * See [CountryRepository.detectCountry]
     */
    fun detectCountry(): Single<Country> = rxSingle {
        internalRepo.detectCountry()
    }

    /**
     * See [CountryRepository.countryWithIsoCode]
     */
    fun countryWithIsoCode(code: String): Single<Country> = rxSingle {
        internalRepo.countryWithIsoCode(code)
    }

    /**
     * See [CountryRepository.defaultCountry]
     */
    fun defaultCountry() = internalRepo.defaultCountry()
}