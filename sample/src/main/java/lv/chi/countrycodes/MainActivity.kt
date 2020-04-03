package lv.chi.countrycodes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.reactivex.disposables.CompositeDisposable
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository
import lv.chi.chilicountrycodes.providers.CountryListProvider
import lv.chi.chilicountrycodes.rx.RxCountryRepository
import lv.chi.chilicountrycodes.ui.CountryCodePicker
import lv.chi.countrycodes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), CountryCodePicker.Listener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val r = CountryRepository.fromAssets(this)

        uiWrapperUsageExample(r)

        coroutinesUsageExample(r)

        rxUsageExample(r)
    }

    // UI wrapper usage example
    // ======================================================================================
    // ======================================================================================
    private fun uiWrapperUsageExample(repository: CountryRepository) {
        CountryCodePicker.setCustomRepository(repository)

        binding.sampleButton.setOnClickListener {
            CountryCodePicker.showDialog(supportFragmentManager)
        }
        binding.customSampleButton.setOnClickListener {
            CountryCodePicker.showDialog(supportFragmentManager, R.style.CustomCodePickerTheme)
        }
    }

    override fun onCountryChosen(country: Country) {
        binding.sampleSelected.text = "Selected: ${country.combinedName}"
    }

    // Coroutine usage example
    // ======================================================================================
    // ======================================================================================

    private fun coroutinesUsageExample(repository: CountryRepository) {
        lifecycleScope.launchWhenCreated {
            repository.countries()
                .shuffled()
                .take(5)
                .map { c -> Log.d("CoroutinesExample", c.toString()) }
        }

        lifecycleScope.launchWhenCreated {
            repository.detectCountry()
                .let { Log.d("CoroutinesExample", "Detected: $it") }
        }

        lifecycleScope.launchWhenCreated {
            repository.countryWithIsoCode("lv")
                .let { Log.d("CoroutinesExample", "Latvian country code: ${it.phoneCode}") }
        }
    }

    // Rx2 wrapper usage example
    // ======================================================================================
    // ======================================================================================
    private val disposable = CompositeDisposable()

    private fun rxUsageExample(customRepository: CountryRepository) {
        val rxRepository = RxCountryRepository(customRepository)

        rxRepository.countries()
            .subscribe { list -> list.map { c -> Log.d("RxExample", c.toString()) } }
            .let { disposable.add(it) }

        rxRepository.detectCountry()
            .subscribe { detected -> Log.d("RxExample", "Detected: $detected") }
            .let { disposable.add(it) }

        rxRepository.countryWithIsoCode("lv")
            .subscribe { country -> Log.d("RxExample", "Found: $country") }
            .let { disposable.add(it) }
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

    // Custom repository creation example
    // ======================================================================================
    // ======================================================================================

    private fun customRepositoryExample() {
        val customFileRepository = CountryRepository(
            appContext = applicationContext,
            provider = SupportedCountryListProvider(),
            defaultCountry = Country("LV", "371", "Latvia")
        )
    }

    class SupportedCountryListProvider : CountryListProvider {
        override fun getCountries() = listOf(
            Country("LT", "370", "Lithuania"),
            Country("LV", "371", "Latvia"),
            Country("EE", "372", "Estonia")
        )
    }
}