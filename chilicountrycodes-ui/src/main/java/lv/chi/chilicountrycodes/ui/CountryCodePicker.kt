package lv.chi.chilicountrycodes.ui

import android.content.Context
import androidx.annotation.StyleRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository
import lv.chi.chilicountrycodes.ui.dialog.CountryCodePickerDialog

/**
 * Convenience wrapper for country picker dialog that allows to inject
 * custom country repository instances to use in the dialog.
 */
object CountryCodePicker {

    /**
     * Callback that will be triggered when country is selected in dialog.
     */
    interface Listener {
        fun onCountryChosen(country: Country)
    }

    /**
     * Shows country picker dialog fragment using provided fragment manager.
     * Class that calls this method *must* implement [CountryCodePicker.Listener] interface.
     *
     * @param fragmentManager - will be used to display picker dialog.
     * @param theme - custom dialog theme. Must be descendant of `CountryCodePicker.Base` style.
     */
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

    /**
     * Sets specific instance of country repository to be used in dialog fragment.
     * Most useful in combination with any DI framework.
     */
    fun setCustomRepository(repository: CountryRepository) {
        customRepository = repository
    }

    private var customRepository: CountryRepository? = null

    internal fun getCountryRepository(context: Context) = customRepository
        ?: CountryRepository.fromAssets(context)
}