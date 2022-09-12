package kakkoiichris.nazonoshiro

import kakkoiichris.kotoba.Console
import kakkoiichris.kotoba.Font
import kakkoiichris.kotoba.Glyph
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
    val console = Console(Console.Config(title = "Nazo no Shiro", font = Font("/font/Fantasque24.bff"), ySpace = -4))
    
    console.open()
    
    console.mainMenu()
    
    console.close()
}

private fun Console.mainMenu() {
    val title = resources.getTXT("tlite").text
    
    //Glyph.Effect.Cycle(0.25, 0xFAF2D6, 0xECDF68, 0xBDA53C, 0x866816, 0x48410A,0x866816,0xBDA53C,0xECDF68)
    effect = Glyph.Effect.Wave(5.0, 1.0, 0.1, true) and Glyph.Effect.Color.white// and Glyph.Effect.Cycle(0.1, 0xFF0000, 0xFF7F00, 0xFFFF00, 0x7FFF00, 0x00FF00, 0x00FF7F, 0x00FFFF, 0x007FFF, 0x0000FF, 0x7F00FF, 0xFF00FF, 0xFF007F)
    
    addRules(
        Glyph.Rule("leaf", "[<>]".toRegex(), Glyph.Effect.Color(0x6B9362), false),
        Glyph.Rule("joint", "[\\[I\\]]".toRegex(), Glyph.Effect.Color(0x4D4B3A), false),
        Glyph.Rule("segment", "=".toRegex(), Glyph.Effect.Color(0x898A74), false),
        Glyph.Rule("subtitle", "\\*.*\\*".toRegex(), Glyph.Effect.Color(0x1D697C), false),
    )
    
    writeLine(title)
    
    pause()
}