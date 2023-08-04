package com.example.discountcalculator

import android.os.Build
import android.os.Bundle
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.discountcalculator.ui.theme.DiscountCalculatorTheme
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

val yellowColorId = R.color.yellow
val blackColorId = R.color.Black
lateinit var calculator: Calculator
var languageFont: FontFamily = FontFamily(Font(R.font.slabo_regular))
lateinit var deviceLanguage: DeviceLanguage
var horizontalAlignment = Alignment.Start
val nf: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
val formatter = nf as DecimalFormat

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiscountCalculatorTheme {
                calculator = Calculator()
                deviceLanguage = DeviceLanguage()
                languageFont = deviceLanguage.whatFont()
                formatter.applyPattern("#,###,###")
                horizontalAlignment = if (deviceLanguage.deviceLanguageGS=="ar"|| deviceLanguage.deviceLanguageGS=="fa")
                    Alignment.Start
                else
                    Alignment.End

                    GreetingPreview()

            }
        }
    }
}

fun formatNumber(textState: String): String = formatter.format(textState.toLong())

fun apostropheRemover(input: String): String = input.filterNot { it == ',' }

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CustomText(
    value: String,
    modifier: Modifier,
    colorId: Int
) {

    Text(
        modifier = modifier,
        text = value,
        fontFamily = languageFont,
        fontWeight = FontWeight.Normal,
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
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp, top = 15.dp)

        var priceState by remember { mutableStateOf("") }
        var discountState by remember { mutableStateOf("") }
        var resultState by remember { mutableStateOf("") }
        var gainState by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = blackColorId)),
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp)
            ) {

                CustomText(
                    value = stringResource(id = R.string.originalPriceText),
                    Modifier.padding(start = 25.dp, end = 25.dp),
                    yellowColorId
                )

                OutlinedTextField(modifier = textFieldModifier,
                    value = priceState,
                    onValueChange = {
                        priceState = it
                        if (priceState != "") {
                            priceState = apostropheRemover(priceState)
                            resultState =
                                calculator.discountPriceCalculator(priceState, discountState)
                            gainState = calculator.gainCalculator(priceState, discountState)
                            priceState = formatNumber(priceState)
                            gainState = formatNumber(gainState)
                            resultState = formatNumber(resultState)
                        }
                    }, colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = yellowColorId),
                        unfocusedBorderColor = Color.Gray
                    ),
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.money),
                            contentDescription = "money"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        color = colorResource(id = yellowColorId),
                        fontSize = 25.sp,
                    ),
                    placeholder = {
                        Text(
                            text = "350,000", color = Color.Gray,
                            fontSize = 18.sp,
                        )
                    })


            }
            Column(
                Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center
            ) {
                CustomText(
                    value = stringResource(id = R.string.discountText),
                    Modifier.padding(start = 25.dp, end = 25.dp, top = 15.dp),
                    yellowColorId
                )
                OutlinedTextField(modifier = textFieldModifier,
                    value = discountState,
                    onValueChange = {
                        if (it.isDigitsOnly() && "0$it".toInt() <= 100 && (it.length <= 3)) {
                            discountState = it
                            priceState = apostropheRemover(priceState)
                            resultState =
                                calculator.discountPriceCalculator(priceState, discountState)
                            gainState = calculator.gainCalculator(priceState, discountState)
                            priceState = formatNumber(priceState)
                            gainState = formatNumber(gainState)
                            resultState = formatNumber(resultState)
                        }
                    }, colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = yellowColorId),
                        unfocusedBorderColor = Color.Gray
                    ),
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.discount),
                            contentDescription = "discount"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        color = colorResource(id = yellowColorId),
                        fontSize = 25.sp,
                    ),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "25", color = Color.Gray,
                            fontSize = 18.sp,
                        )
                    })
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = horizontalAlignment
            ) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp, start = 25.dp),
                ) {

                    Text(
                        text = buildAnnotatedString {

                            append(stringResource(id = R.string.result))

                            withStyle(
                                SpanStyle(
                                    fontFamily = languageFont,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray,
                                    fontSize = 20.sp
                                )
                            ) {
                                append(" $resultState")
                            }

                        },
                        fontFamily = languageFont,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = yellowColorId),
                        fontSize = 20.sp
                    )

                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp, start = 25.dp),
                ) {
                    Text(
                        text = buildAnnotatedString {

                            append(stringResource(id = R.string.profitFromThePurchase))

                            withStyle(
                                SpanStyle(
                                    fontFamily = languageFont,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray,
                                    fontSize = 20.sp
                                )
                            ) {
                                append(" $gainState")
                            }

                        },
                        fontFamily = languageFont,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = yellowColorId),
                        fontSize = 20.sp
                    )
                }

            }

        }

    }
}