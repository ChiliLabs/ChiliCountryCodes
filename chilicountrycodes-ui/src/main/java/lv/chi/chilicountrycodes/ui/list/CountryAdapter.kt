package lv.chi.chilicountrycodes.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lv.chi.chilicountrycodes.Country
import lv.chi.chilicountrycodes.ui.R


class CountryAdapter(
    private val onCountryClicked: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private var countries: List<Country> = emptyList()

    fun setCountries(countries: List<Country>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.country_item, parent, false)
        .let { CountryViewHolder(it) }
        .also { holder -> holder.itemView.setOnClickListener { holder.item?.let { onCountryClicked(it) } } }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.item = countries[position]
    }

    override fun getItemCount() = countries.size

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val countryName = view as TextView

        var item: Country? = null
            set(value) {
                field = value
                countryName.text = value?.combinedName ?: ""
            }
    }
}
