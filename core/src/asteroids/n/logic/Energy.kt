package asteroids.n.logic

import asteroids.n.entities.objects.Moon
import asteroids.n.entities.objects.PlayerShip
import asteroids.n.subImmut
import com.badlogic.gdx.Gdx

var timePassed = 0f
fun refillEnergy(moon: Moon, ship: PlayerShip) {
    timePassed += Gdx.graphics.deltaTime
    if (timePassed > 0.075) {
        ship.energy = Math.min(500f, ship.energy + 0.05f)
        if (moon.position.subImmut(ship.position).len() <= 50) {
            timePassed = 0f
            ship.energy = Math.min(500f, ship.energy + Gdx.graphics.deltaTime * 100)
        }
    }
}