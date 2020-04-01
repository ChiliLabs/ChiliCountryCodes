package lv.chi.chilicountrycodes

import android.content.Context
import android.telephony.TelephonyManager
import java.util.*


// https://stackoverflow.com/a/35849652
internal fun isoCodeToEmoji(countryIsoCode: String): String {
    val firstLetter = Character.codePointAt(countryIsoCode, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryIsoCode, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}


internal fun simCountryIsoCode(appContext: Context): String? {
    val tm = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val simCountry = tm.simCountryIso
    return if (simCountry?.length == 2) {
        simCountry.toUpperCase(Locale.US)
    } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
        val networkCountry = tm.networkCountryIso
        if (networkCountry?.length == 2) {
            networkCountry.toUpperCase(Locale.US)
        } else {
            null
        }
    } else {
        null
    }
}