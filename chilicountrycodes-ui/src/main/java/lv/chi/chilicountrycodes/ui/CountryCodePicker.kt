package lv.chi.chilicountrycodes.ui

import android.content.Context
import androidx.annotation.StyleRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository

object CountryCodePicker {

    interface Listener {
        fun onCountryChosen(country: Country)
    }

    fun showDialog(
        fragmentManager: FragmentManager,
        @StyleRes theme: Int = R.style.CountryCodePicker_Base
    ) {
        CountryCodePickerDialog().apply {
            arguments = bundleOf(
                CountryCodePickerDialog.ARG_THEME to theme
            )
        }.show(fragmentManager, CountryCodePickerDialog.TAG)
    }

    fun setCustomRepository(repository: CountryRepository) {
        customRepository = repository
    }

    private var customRepository: CountryRepository? = null

    internal fun getCountryRepository(context: Context) = customRepository
        ?: CountryRepository.fromAssets(context)


}