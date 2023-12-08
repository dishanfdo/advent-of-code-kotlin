package aockt.util

infix fun <A, B, C> ((A) -> B).compose(g: (B) -> C): ((A) -> C) = { a: A -> g(this(a)) }