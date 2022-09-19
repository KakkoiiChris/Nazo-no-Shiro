package kakkoiichris.nazonoshiro

import kakkoiichris.kotoba.Console
import kakkoiichris.kotoba.Font
import kakkoiichris.kotoba.util.Resources

val resources = Resources("/resources")

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
    val fileName = if (Math.random() < 0.001) "titleFunny" else "titleNormal"
    
    val script = resources.getQuickScript(fileName)
    
    val new = "New Game"
    val load = "Load Game"
    val options = "Options"
    val credits = "Credits"
    val quit = "Quit"
    
    var inMenu = true
    
    while (inMenu) {
        script.run(this)
        
        var choosing = true
        
        while (choosing) {
            val option = readOption(new, load, options, credits, quit)
            
            writeLine()
            
            when (option) {
                new     -> {
                    doNewGame()
                    
                    choosing = false
                }
                
                load    -> {
                    doLoadGame()
                    
                    choosing = false
                }
                
                options -> doOptions()
                
                credits -> doCredits()
                
                quit    -> {
                    choosing = false
                    inMenu = false
                }
            }
        }
        
        clear()
    }
}

private fun Console.doNewGame() {
    writeLine("New Game!")
}

private fun Console.doLoadGame() {
    writeLine("Load Game!")
}

private fun Console.doOptions() {
    writeLine("Options!")
}

private fun Console.doCredits() {
    writeLine("Credits!")
}