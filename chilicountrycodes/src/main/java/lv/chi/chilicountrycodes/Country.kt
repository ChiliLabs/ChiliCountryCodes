package lv.chi.chilicountrycodes

data class Country(
    val fullName: CharSequence,
    val phoneCode: String,
    val flagEmoji: String,
    val isoCode: String
) {

    companion object {
        val UNDEFINED = Country("", "", "", "")
    }
}