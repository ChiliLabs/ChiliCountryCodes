package lv.chi.countrycodes

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository
import lv.chi.chilicountrycodes.ui.CountryCodePickerDialog
import lv.chi.countrycodes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), CountryCodePickerDialog.Listener {

    private val disposable = CompositeDisposable()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .penaltyDeath()
                .build()
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val r = CountryRepository.fromAssets(this)

        binding.sampleButton.setOnClickListener {

            CountryCodePickerDialog.show(supportFragmentManager, r)

        }


//        val d = RxCountryRepository(r)
//        disposable.add(d.countries().subscribe({
//            it.shuffled().take(5).map { c -> Log.wtf("!!!!!!", c.toString()) }
//        }, {}))

//        lifecycleScope.launchWhenCreated {
//            val cbCountries = d.countries()
//            Log.wtf("!!!!!!", "---------")
//            cbCountries.shuffled().take(5).map { c -> Log.wtf("!!!!!!", c.toString()) }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            val cbCountries = d.detectCountry()
//            Log.wtf("!!!!!!", "---------")
//            Log.wtf("!!!!!!", "detected: $cbCountries")
//        }
//
//        lifecycleScope.launchWhenCreated {
//            val cbCountries = d.countryWithIsoCode("lv")
//            Log.wtf("!!!!!!", "---------")
//            Log.wtf("!!!!!!", "latvia: $cbCountries")
//        }
    }

    override fun onCountryChosen(country: Country) {
        binding.sampleSelected.text = "Selected: ${country.combinedName}"
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }
}
