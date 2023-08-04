package com.example.discountcalculator

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import java.util.Locale

class DeviceLanguage {

    private var deviceLanguage = ""
    var deviceLanguageGS: String
        get() = deviceLanguage
        set(value) {
            deviceLanguage = value
        }

    init {
        deviceLanguage()
    }

    fun deviceLanguage() {
        deviceLanguage = Locale.getDefault().language.toString()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun whatFont(): FontFamily {

        return when (deviceLanguage) {

            "en" -> FontFamily(Font(R.font.slabo_regular))
            "fa" -> FontFamily(Font(R.font.mikhak_medium))
            "ar" -> FontFamily(Font(R.font.alkhasheiy))

            else -> FontFamily(Font(R.font.slabo_regular))
        }

    }

}