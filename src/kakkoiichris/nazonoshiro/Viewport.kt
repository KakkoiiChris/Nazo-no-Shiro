package kakkoiichris.nazonoshiro

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.input.Key
import kakkoiichris.hypergame.media.Colors
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.BasicStroke
import kotlin.math.*

private const val P2 = PI / 2
private const val P3 = 3 * PI / 2
private const val DR = 0.0174533

class Viewport(val width: Int, val height: Int) : Renderable<NazoNoShiro> {
    var px = 300.0
    var py = 200.0
    var pa = 0.0
    var playerSpeed = 1.0
    var playerTurnSpeed = PI / 128

    var mapX = 8
    var mapY = 8

    var map = intArrayOf(
        1, 1, 1, 1, 1, 1, 1, 1,
        1, 0, 1, 0, 0, 0, 0, 1,
        1, 0, 1, 0, 0, 0, 0, 1,
        1, 0, 1, 0, 0, 0, 0, 1,
        1, 0, 0, 0, 0, 0, 0, 1,
        1, 0, 0, 0, 0, 1, 0, 1,
        1, 0, 0, 0, 0, 0, 0, 1,
        1, 1, 1, 1, 1, 1, 1, 1,
    )
    var tileSize = 64

    override fun update(
        view: View<*>,
        game: NazoNoShiro,
        time: Time,
        input: Input
    ) {
        if (input.keyHeld(Key.W)) {
            px += cos(pa) * playerSpeed * time.delta
            py += sin(pa) * playerSpeed * time.delta
        }

        if (input.keyHeld(Key.S)) {
            px -= cos(pa) * playerSpeed * time.delta
            py -= sin(pa) * playerSpeed * time.delta
        }

        if (input.keyHeld(Key.A)) {
            pa -= playerTurnSpeed * time.delta

            if (pa < 0) {
                pa += PI * 2
            }
        }

        if (input.keyHeld(Key.D)) {
            pa += playerTurnSpeed * time.delta

            if (pa >= PI * 2) {
                pa -= PI * 2
            }
        }
    }

    override fun render(
        view: View<*>,
        game: NazoNoShiro,
        renderer: Renderer
    ) {
        renderer.clearRect(view.bounds)

        for (y in 0..<mapY) {
            for (x in 0..<mapX) {
                val tile = map[y * mapX + x]

                renderer.color = when (tile) {
                    0    -> Colors.black
                    1    -> Colors.white
                    else -> Colors.red
                }

                renderer.fillRect(x * tileSize + 1, y * tileSize + 1, tileSize - 2, tileSize - 2)
            }
        }

        renderer.color = Colors.yellow
        renderer.fillOval(px.toInt() - 4, py.toInt() - 4, 8, 8)

        renderer.stroke = BasicStroke(2F)
        renderer.drawLine(
            px.toInt(),
            py.toInt(),
            (px + cos(pa) * 10).toInt(),
            (py + sin(pa) * 10).toInt()
        )

        drawRays(renderer)
    }

    private fun dist(ax: Double, ay: Double, bx: Double, by: Double, ang: Double) =
        sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay))

    private fun drawRays(renderer: Renderer) {
        var ra = pa - DR * 30
        var rx = 0.0
        var ry = 0.0
        var xo = 0.0
        var yo = 0.0

        for (r in 0..<60) {
            if (ra < 0) {
                ra += PI * 2
            }

            if (ra >= PI * 2) {
                ra -= PI * 2
            }

            var distH = 1000000.0
            var hx = px
            var hy = py
            var dof = 0

            var aTan = -1 / tan(ra)

            if (ra > PI) {
                ry = ((py.toInt() shr 6) shl 6) - 0.0001
                rx = (py - ry) * aTan + px

                yo = -64.0
                xo = -yo * aTan
            }

            if (ra < PI) {
                ry = ((py.toInt() shr 6) shl 6) + 64.0
                rx = (py - ry) * aTan + px

                yo = 64.0
                xo = -yo * aTan
            }

            if (ra == 0.0 || ra == PI) {
                rx = px
                ry = py
                dof = 8
            }

            while (dof < 8) {
                var mx = rx.toInt() shr 6
                var my = ry.toInt() shr 6
                var mp = my * mapX + mx

                if (mp >= 0 && mp < mapX * mapY && map[mp] == 1) {
                    hx = rx
                    hy = ry
                    distH = dist(px, py, hx, hy, ra)
                    dof = 8
                }
                else {
                    rx += xo
                    ry += yo
                    dof += 1
                }
            }

            var distV = 1000000.0
            var vx = px
            var vy = py
            dof = 0

            var nTan = -tan(ra)

            if (ra > P2 && ra < P3) {
                rx = ((px.toInt() shr 6) shl 6) - 0.0001
                ry = (px - rx) * nTan + py

                xo = -64.0
                yo = -xo * nTan
            }

            if (ra < P2 || ra > P3) {
                rx = ((px.toInt() shr 6) shl 6) + 64.0
                ry = (px - rx) * nTan + py

                xo = 64.0
                yo = -xo * nTan
            }

            if (ra == P2 || ra == P3) {
                rx = px
                ry = py
                dof = 8
            }

            while (dof < 8) {
                var mx = rx.toInt() shr 6
                var my = ry.toInt() shr 6
                var mp = my * mapX + mx

                if (mp >= 0 && mp < mapX * mapY && map[mp] == 1) {
                    vx = rx
                    vy = ry
                    distV = dist(px, py, vx, vy, ra)
                    dof = 8
                }
                else {
                    rx += xo
                    ry += yo
                    dof += 1
                }
            }

            if (distH < distV) {
                rx = hx
                ry = hy
            }
            else {
                rx = vx
                ry = vy
            }

            renderer.color = Colors.red
            renderer.stroke = BasicStroke(1F)
            renderer.drawLine(px.toInt(), py.toInt(), rx.toInt(), ry.toInt())

            ra += DR
        }
    }
}