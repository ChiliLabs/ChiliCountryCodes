package lv.chi.chilicountrycodes

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan

data class Country(
    val countryName: String,
    val phoneCode: String,
    val flagEmoji: String,
    val isoCode: String,
) {

    val combinedName: CharSequence

    init {
        val fullName = "$flagEmoji +$phoneCode $countryName"

        val str = SpannableString(fullName)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            flagEmoji.length + 1,
            flagEmoji.length + phoneCode.length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        combinedName = str
    }


    companion object {
        val UNDEFINED = Country("", "", "", "")
    }
}