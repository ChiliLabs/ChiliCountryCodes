package lv.chi.countrycodes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.reactivex.disposables.CompositeDisposable
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository
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

        disposable.add(rxRepository.countries().subscribe({
            it.shuffled().take(5).map { c -> Log.d("RxExample", c.toString()) }
        }, {}))
        disposable.add(rxRepository.detectCountry().subscribe({
            Log.d("RxExample", "Detected: $it")
        }, {}))
        disposable.add(rxRepository.countryWithIsoCode("lv").subscribe({
            Log.d("RxExample", "Latvian country code: ${it}")
        }, {}))
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }
}
