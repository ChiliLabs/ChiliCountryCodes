package lv.chi.countrycodes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import lv.chi.chilicountrycodes.CountryRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val d = CountryRepository.fromAssets(assets)

        d.countries().observe(this) {
            Log.wtf("!!!!!!", "---------")
            it.take(5).map { c -> Log.wtf("!!!!!!", c.toString()) }
        }
    }
}
