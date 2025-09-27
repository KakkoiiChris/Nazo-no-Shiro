package kakkoiichris.nazonoshiro

import kakkoiichris.hypergame.Game
import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View

class NazoNoShiro : Game {
    private lateinit var viewport: Viewport

    override fun init(view: View<*>) {
        viewport = Viewport(view.width, view.height)
    }

    override fun update(
        view: View<*>,
        time: Time,
        input: Input
    ) {
        viewport.update(view, this, time, input)
    }

    override fun render(view: View<*>, renderer: Renderer) {
        viewport.render(view, this, renderer)
    }

    override fun halt(view: View<*>) {
    }
}