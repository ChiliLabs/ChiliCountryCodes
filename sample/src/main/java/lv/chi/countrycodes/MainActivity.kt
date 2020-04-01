package lv.chi.countrycodes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lv.chi.chilicountrycodes.DummyCountryRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val d = DummyCountryRepository()
    }
}
