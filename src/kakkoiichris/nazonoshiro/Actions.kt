package kakkoiichris.nazonoshiro

/**
 * Nazo-no-Shiro
 
 * Copyright (C) 2022, KakkoiiChris
 *
 * File:    Actions.kt
 
 * Created: Friday, September 09, 2022, 23:13:04
 *
 * @author Christian Bryce Alexander
 */
class TextAction(
    val regex: Regex,
    val description: String,
    val eventIDs: List<String>,
    val secret: Boolean = false,
    val once: Boolean = false,
) {
    var performed = false
    
    fun accepts(input: String): Boolean {
        if (once && performed) {
            return false
        }
        
        val matches = input.matches(regex)
        
        if (matches) {
            performed = true
        }
        
        return matches
    }
}

class MoveAction(
    val eventIDs: List<String>,
    val secret: Boolean = false,
    val once: Boolean = false,
) {
    var performed = false
}