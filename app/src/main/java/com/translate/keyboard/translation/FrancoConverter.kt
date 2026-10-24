package com.translate.keyboard.translation

import java.util.Locale

object FrancoConverter {

    private val francoToArMap = mapOf(
        "2" to "أ",
        "3" to "ع",
        "3'" to "غ",
        "4" to "ذ",
        "5" to "خ",
        "6" to "ط",
        "7" to "ح",
        "8" to "ق",
        "9" to "ص",
        "9'" to "ض",
        "sh" to "ش",
        "gh" to "غ",
        "kh" to "خ",
        "th" to "ث",
        "dh" to "ذ",
        "bb" to "ب",
        "tt" to "ت",
        "gg" to "ج",
        "dd" to "د",
        "rr" to "ر",
        "zz" to "ز",
        "ss" to "س",
        "ff" to "ف",
        "kk" to "ك",
        "ll" to "ل",
        "mm" to "م",
        "nn" to "ن",
        "hh" to "ه",
        "ww" to "و",
        "yy" to "ي",
        "a" to "ا",
        "b" to "ب",
        "t" to "ت",
        "g" to "ج",
        "7" to "ح",
        "kh" to "خ",
        "d" to "د",
        "r" to "ر",
        "z" to "ز",
        "s" to "س",
        "sh" to "ش",
        "s" to "س",
        "f" to "ف",
        "k" to "ك",
        "l" to "ل",
        "m" to "م",
        "n" to "ن",
        "h" to "ه",
        "w" to "و",
        "y" to "ي",
        "e" to "ي",
        "o"  to "و"
    )

    private val arToFrancoMap = mapOf(
        'أ' to "2", 'إ' to "2", 'آ' to "2", 'ء' to "2",
        'ب' to "b", 'ت' to "t", 'ث' to "th",
        'ج' to "g", 'ح' to "7", 'خ' to "5",
        'د' to "d", 'ذ' to "4", 'ر' to "r",
        'ز' to "z", 'س' to "s", 'ش' to "sh",
        'ص' to "9", 'ض' to "9'", 'ط' to "6",
        'ظ' to "6'", 'ع' to "3", 'غ' to "3'",
        'ف' to "f", 'ق' to "8", 'ك' to "k",
        'ل' to "l", 'م' to "m", 'ن' to "n",
        'ه' to "h", 'و' to "w", 'ي' to "y",
        'ة' to "a", 'ى' to "a"
    )

    fun francoToArabic(franco: String): String {
        var result = franco.lowercase(Locale.ROOT)
        // Advanced smart replacement
        result = result.replace("sh", "ش")
            .replace("gh", "غ")
            .replace("kh", "خ")
            .replace("th", "ث")
            .replace("dh", "ذ")
            .replace("3'", "غ")
            .replace("9'", "ض")

        val sb = StringBuilder()
        var i = 0
        while (i < result.length) {
            val ch = result[i].toString()
            val mapped = francoToArMap[ch]
            if (mapped != null) {
                sb.append(mapped)
            } else {
                sb.append(result[i])
            }
            i++
        }
        return sb.toString()
    }

    fun arabicToFranco(arabic: String): String {
        val sb = StringBuilder()
        for (ch in arabic) {
            val mapped = arToFrancoMap[ch]
            if (mapped != null) {
                sb.append(mapped)
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun isFranco(text: String): Boolean {
        return text.any { it.isDigit() || it == '3' || it == '7' || it == '5' || it == '9' || it == '2' }
    }
}
