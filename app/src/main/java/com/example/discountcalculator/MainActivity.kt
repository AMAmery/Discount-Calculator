package com.example.discountcalculator

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.discountcalculator.ui.theme.DiscountCalculatorTheme

val blackGrayColorId = R.color.BlackGray
val cyanAquaColorId = R.color.CyanAqua
val extraLightBlueColorId = R.color.ExtraLightBlue
val tealColorId = R.color.Teal

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiscountCalculatorTheme {
                GreetingPreview()

            }
        }
    }
}


fun apostropheRemover(input: String): String {
    var result = ""
    input.forEach { digit -> if (digit != ',') result += digit }

    return result

}

fun PersianToEnglish(persianStr: String): String {
    var result = ""
    var en = '0'
    for (ch in persianStr) {
        en = ch
        when (ch) {
            '۰' -> en = '0'
            '۱' -> en = '1'
            '۲' -> en = '2'
            '۳' -> en = '3'
            '۴' -> en = '4'
            '۵' -> en = '5'
            '۶' -> en = '6'
            '۷' -> en = '7'
            '۸' -> en = '8'
            '۹' -> en = '9'
        }
        result = "${result}$en"
    }
    return result
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CustomText(value: String, modifier: Modifier, colorId: Int) {

    Text(
        modifier = modifier,
        text = value,
        fontFamily = FontFamily(Font(R.font.mikhak_medium)),
        fontWeight = FontWeight.ExtraBold,
        color = colorResource(id = colorId),
        fontSize = 20.sp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiscountCalculatorTheme {

        // common attributes for textField
        var textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp)

        var priceState by remember { mutableStateOf("") }
        var discountState by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("") }
        var gainState by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = blackGrayColorId)),
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {

                CustomText(
                    value = "قیمت اصلی",
                    Modifier.padding(start = 25.dp, end = 25.dp),
                    cyanAquaColorId
                )

                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = priceState,
                    onValueChange = {
                        try {
                            priceState = it
                            if (priceState != "") {
                                priceState = apostropheRemover(priceState)
                                priceState = PersianToEnglish(priceState)
                                priceState = "%,d".format(priceState.toLong())
                                priceState = priceState.replace("٬", ",")
                            }
                        } catch (e: Exception) {
                            Log.d("error__", "${e.message} : $priceState")
                        }
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.money),
                            contentDescription = "money"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        color = colorResource(id = extraLightBlueColorId),
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.gandom_fd))
                    )
                )

            }
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                CustomText(
                    value = "تخفیف",
                    Modifier.padding(start = 25.dp, end = 25.dp),
                    blackGrayColorId
                )
                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = discountState,
                    onValueChange = {
                        if (it.isDigitsOnly() && "0$it".toInt() <= 100 && (it.length in 0..3)) {
                            discountState = it
                        }
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.money),
                            contentDescription = "money"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        color = colorResource(id = extraLightBlueColorId),
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.gandom_fd))
                    ),
                    singleLine = true
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomText(value = result, Modifier.padding(end = 5.dp), tealColorId)
                    CustomText(value = ": نتیجه", Modifier.padding(end = 25.dp), cyanAquaColorId)
                }
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomText(value = gainState, Modifier.padding(end = 5.dp), tealColorId)
                    CustomText(value = ":  سود شما", Modifier.padding(end = 25.dp), cyanAquaColorId)
                }

            }

        }

    }
}