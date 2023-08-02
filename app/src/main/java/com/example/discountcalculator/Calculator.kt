package com.example.discountcalculator

import kotlin.time.times

class Calculator {

    fun filter(number: String): Boolean = number.isNotEmpty() && number.toInt() > 0

    fun discountPriceCalculator(
        price: String,
        discount: String
    ) :String = if (filter(price)&&filter(discount)) (price.toDouble()-(price.toDouble()*(discount.toDouble()/100))).toInt().toString() else "0"

    fun gainCalculator(price: String,discount: String):String = if (filter(price)&&filter(discount)) (price.toDouble()*(discount.toDouble()/100)).toInt().toString() else "0"

}