package lv.chi.chilicountrycodes

interface CountryRepository {
    fun countries(onLoaded: (List<Country>) -> Unit)
    fun detectCountry(defaultIso: String, onLoaded: (Country) -> Unit)
}

class DummyCountryRepository: CountryRepository {

    override fun countries(onLoaded: (List<Country>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun detectCountry(defaultIso: String, onLoaded: (Country) -> Unit) {
        TODO("Not yet implemented")
    }
}