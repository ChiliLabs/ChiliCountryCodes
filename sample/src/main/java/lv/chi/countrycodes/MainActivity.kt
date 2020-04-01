package lv.chi.countrycodes

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import lv.chi.chilicountrycodes.CountryRepository
import lv.chi.chilicountrycodes.rx.RxCountryRepository


class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .penaltyDeath()
                .build()
        )

        setContentView(R.layout.activity_main)

        val r = CountryRepository.fromAssets(this)
        val d = RxCountryRepository(r)


        disposable.add(d.countries().subscribe({
            it.shuffled().take(5).map { c -> Log.wtf("!!!!!!", c.toString()) }
        }, {}))

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

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }
}
