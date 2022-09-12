package kakkoiichris.nazonoshiro

/**
 * Nazo-no-Shiro
 
 * Copyright (C) 2022, KakkoiiChris
 *
 * File:    Event.kt
 
 * Created: Friday, September 09, 2022, 23:26:35
 *
 * @author Christian Bryce Alexander
 */
interface Event {
    val id: String
    
    val isLocal get() = id[0] != '@'
    
    fun occur()
}