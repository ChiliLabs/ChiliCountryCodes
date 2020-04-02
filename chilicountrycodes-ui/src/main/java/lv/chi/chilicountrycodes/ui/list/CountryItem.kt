package lv.chi.chilicountrycodes.ui.list

data class CountryItem(
    val fullName: CharSequence,
    val countryCode: String,
    val flagEmoji: String,
    val isoCode: String
)
