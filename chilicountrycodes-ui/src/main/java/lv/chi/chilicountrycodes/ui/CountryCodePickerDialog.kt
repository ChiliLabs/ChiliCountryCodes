package lv.chi.chilicountrycodes.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import lv.chi.chilicountrycodes.ui.databinding.CountryPhonePickerDialogBinding
import lv.chi.chilicountrycodes.ui.list.CountryAdapter

internal class CountryCodePickerDialog : DialogFragment() {

    private lateinit var listener: CountryCodePicker.Listener
    private lateinit var adapter: CountryAdapter

    private lateinit var vm: CountryCodePickerViewModel

    private var viewBinding: CountryPhonePickerDialogBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachParentAsListener()
    }

    private fun attachParentAsListener() {
        val parent = parentFragment ?: activity
        listener = parent as? CountryCodePicker.Listener
            ?: throw ClassCastException("$parent must implement CountryCodePickerDialog.Listener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contextWrapper = ContextThemeWrapper(requireContext(), themeArg())
        viewBinding = CountryPhonePickerDialogBinding
            .inflate(LayoutInflater.from(contextWrapper), container, false)
            .apply {
                // Dismiss on press outside of dialog
                countryPickerBackground.setOnClickListener { dismissAllowingStateLoss() }
                adapter = CountryAdapter {
                    listener.onCountryChosen(it)
                    dismiss()
                }
                countryPickerList.adapter = adapter
            }

        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = getViewModel()

        viewBinding?.countryPickerSearch?.doOnTextChanged { text, _, _, _ ->
            vm.setQuery(text.toString().ifBlank { null })
        }
        vm.getCountries().observe(viewLifecycleOwner) { countries ->
            countries?.let { adapter.setCountries(it) }
        }
        vm.loadCountries()
    }

    private fun getViewModel(): CountryCodePickerViewModel {
        val vmFactory = CountryCodePickerViewModel.Factory(
            CountryCodePicker.getCountryRepository(requireContext())
        )
        return ViewModelProvider(this, vmFactory)[CountryCodePickerViewModel::class.java]
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

    private fun themeArg() = requireArguments().getInt(ARG_THEME, R.style.CountryCodePicker_Base)

    companion object {
        internal const val TAG = "lv.chi.chilicountrycodes.ui.dialog"
        internal const val ARG_THEME = "lv.chi.chilicountrycodes.ui.dialogTheme"
    }
}