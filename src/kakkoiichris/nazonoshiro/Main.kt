package kakkoiichris.nazonoshiro

import kakkoiichris.kotoba.Console
import kakkoiichris.kotoba.Font
import kakkoiichris.kotoba.QuickScript
import kakkoiichris.kotoba.util.Resources

val resources = Resources("/resources");

/**
 * Nazo-no-Shiro
 
 * Copyright (C) 2022, KakkoiiChris
 *
 * File:    Main.kt
 
 * Created: Thursday, September 08, 2022, 16:29:39
 *
 * @author Christian Bryce Alexander
 */
fun main() {
    val console = Console(
        Console.Config(
            title = "Nazo no Shiro",
            font = Font("/font/Fantasque24.bff"),
            ySpace = -4,
            background = 0x111111
        )
    )
    
    console.open()
    
    console.mainMenu()
    
    console.close()
}

private fun Console.mainMenu() {
    val title = resources.getTXT("tlite").lines
    
    val script = QuickScript(title)
    
    script.run(this)
    
    pause()
}