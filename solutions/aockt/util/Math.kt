@file:Suppress("unused")

package aockt.util

import kotlin.math.pow

fun Int.pow(x: Int): Int = this.toDouble().pow(x).toInt()

fun gcd(a: Long, b: Long): Long {
    var r1 = a
    var r2 = b
    while (r2 > 0) {
        val temp = r2
        r2 = r1 % r2
        r1 = temp
    }

    return r1
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

fun gcd(vararg nums: Long): Long {
    var result: Long = nums[0]
    for (num in nums) result = gcd(result, num)
    return result
}

fun lcm(vararg nums: Long): Long {
    var result = nums[0]
    for (num in nums) result = lcm(result, num)
    return result
}