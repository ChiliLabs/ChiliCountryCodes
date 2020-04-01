package lv.chi.chilicountrycodes


// https://stackoverflow.com/a/35849652
internal fun isoCodeToEmoji(countryIsoCode: String): String {
    val firstLetter = Character.codePointAt(countryIsoCode, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryIsoCode, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}
