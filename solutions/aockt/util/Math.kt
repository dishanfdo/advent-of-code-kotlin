package aockt.util

import kotlin.math.pow

fun Int.pow(x: Int): Int = this.toDouble().pow(x).toInt()