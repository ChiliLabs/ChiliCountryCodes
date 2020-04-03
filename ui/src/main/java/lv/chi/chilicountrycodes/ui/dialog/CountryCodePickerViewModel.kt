package lv.chi.chilicountrycodes.ui.dialog

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository


internal class CountryCodePickerViewModel(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val countries = MutableLiveData<List<Country>>().apply { postValue(emptyList()) }
    private var cachedAllCountries = emptyList<Country>()

    fun loadCountries() {
        viewModelScope.launch {
            cachedAllCountries = countryRepository.countries()
            countries.postValue(cachedAllCountries)
        }
    }

    fun getCountries(): LiveData<List<Country>> = countries

    fun setQuery(text: String?) {
        if (text == null) {
            countries.postValue(cachedAllCountries)
        } else {
            viewModelScope.launch(Dispatchers.Default) {
                val filtered = cachedAllCountries.filter { it.combinedName.contains(text, true) }
                countries.postValue(filtered)
            }
        }
    }

    class Factory(private val repo: CountryRepository) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CountryCodePickerViewModel(repo) as (T)
        }
    }
}