package kakkoiichris.nazonoshiro

import kakkoiichris.hypergame.view.Window

fun main() {
    val game = NazoNoShiro()

    val window = Window<NazoNoShiro>(1024, 512, 1, 60.0, "Nazo no Shiro")

    window.open(game)
}