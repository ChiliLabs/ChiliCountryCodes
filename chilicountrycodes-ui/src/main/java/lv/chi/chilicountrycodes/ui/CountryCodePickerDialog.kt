package lv.chi.chilicountrycodes.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.CountryRepository
import lv.chi.chilicountrycodes.ui.databinding.CountryPhonePickerDialogBinding
import lv.chi.chilicountrycodes.ui.list.CountryAdapter

class CountryCodePickerDialog : DialogFragment() {

    private lateinit var listener: Listener
    private lateinit var adapter: CountryAdapter

    private lateinit var vmFactory: CountryCodePickerViewModel.Factory
    private lateinit var vm: CountryCodePickerViewModel

    private var viewBinding: CountryPhonePickerDialogBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachParentAsListener()
    }

    private fun attachParentAsListener() {
        val parent = parentFragment ?: activity
        listener = parent as? Listener ?: throw ClassCastException("$parent must implement CountryCodePickerDialog.Listener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CountryPhonePickerDialogBinding.inflate(inflater, container, false)
        .apply {
            if (savedInstanceState != null) {
                // TODO handle screen rotation properly
                // For now it just closes when rotated. Cool hacks!
                dismissAllowingStateLoss()
            } else {
                vm = getViewModel()

                // Dismiss on press outside of dialog
                countryPickerBackground.setOnClickListener { dismissAllowingStateLoss() }

                setupListAdapter()

                countryPickerSearch.doOnTextChanged { text, _, _, _ ->
                    vm.setQuery(text.toString().ifBlank { null })
                }

                subscribeCountryList(vm.getCountries())
                vm.loadCountries()
            }
        }
        .also { viewBinding = it }
        .root

    private fun getViewModel() = ViewModelProvider(this@CountryCodePickerDialog, vmFactory)[CountryCodePickerViewModel::class.java]

    private fun subscribeCountryList(data: LiveData<List<Country>>) {
        data.observe(viewLifecycleOwner) { countries ->
            countries?.let { adapter.setCountries(it) }
        }
    }

    private fun CountryPhonePickerDialogBinding.setupListAdapter() {
        adapter = CountryAdapter {
            listener.onCountryChosen(it)
            dismiss()
        }
        countryPickerList.adapter = adapter
    }

    override fun onResume() {
        // making sure that status bar is translucent
        dialog?.window?.apply {
            setBackgroundDrawable(null)
            val params = attributes.also {
                it.width = WindowManager.LayoutParams.MATCH_PARENT
                it.height = WindowManager.LayoutParams.MATCH_PARENT
            }
            attributes = params
        }
        super.onResume()
    }

    override fun onDestroyView() {
        // Prevents accidental memory leaks in fragments
        viewBinding?.countryPickerList?.adapter = null
        viewBinding = null
        super.onDestroyView()
    }

    interface Listener {
        fun onCountryChosen(country: Country)
    }

    companion object {
        private const val TAG = "lv.chi.chilicountrycodes.ui.dialog"

        fun showDefault(
            context: Context,
            childFragmentManager: FragmentManager
        ) = show(childFragmentManager, CountryRepository.fromAssets(context))

        fun show(
            childFragmentManager: FragmentManager,
            repository: CountryRepository
        ) {
            CountryCodePickerDialog().apply {
                vmFactory = CountryCodePickerViewModel.Factory(repository)
            }.show(childFragmentManager, TAG)
        }

    }
}