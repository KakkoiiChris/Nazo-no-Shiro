package kakkoiichris.nazonoshiro

import kakkoiichris.kotoba.util.json.JSON

/**
 * Nazo-no-Shiro
 
 * Copyright (C) 2022, KakkoiiChris
 *
 * File:    World.kt
 
 * Created: Friday, September 09, 2022, 23:15:20
 *
 * @author Christian Bryce Alexander
 */
class World(data: JSON) : Explorable() {
    init {
        val root = data.root
    }
}