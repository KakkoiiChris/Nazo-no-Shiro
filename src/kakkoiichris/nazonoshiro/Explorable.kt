package kakkoiichris.nazonoshiro

/**
 * Nazo-no-Shiro
 
 * Copyright (C) 2022, KakkoiiChris
 *
 * File:    Explorable.kt
 
 * Created: Friday, September 09, 2022, 23:16:31
 *
 * @author Christian Bryce Alexander
 */
open class Explorable {
    private val explorables = mutableMapOf<String, Explorable>()
    
    private lateinit var currentExplorable: Explorable
}