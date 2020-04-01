package lv.chi.countrycodes

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import lv.chi.chilicountrycodes.CountryRepository


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .penaltyDeath()
                .build()
        )

        setContentView(R.layout.activity_main)


        val d = CountryRepository.fromAssets(this)

        val cbCountries = d.countries {
            Log.wtf("!!!!!!", "---------")
            it.shuffled().take(5).map { c -> Log.wtf("!!!!!!", c.toString()) }
        }


        val cbDetect = d.detectCountry {
            Log.wtf("!!!!!!", "---------")
            Log.wtf("!!!!!!", "detected: $it")
        }
        //cb.cancel()

    }
}
