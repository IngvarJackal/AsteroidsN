package asteroids.n.logic

import asteroids.n.entities.forces.ThrustForvardForce
import asteroids.n.entities.objects.InputStatus
import asteroids.n.entities.objects.PlayerShip
import asteroids.n.subImmut
import asteroids.n.utils.handlePlayerInput
import com.badlogic.gdx.Gdx

val SPEED_IMPULSE_DELAY = 0.25f // sec
val SPEED_IMPULSE_AMOUNT = 4f
private var curtime = 0f
fun processPlayerInput(ship: PlayerShip) {
    handlePlayerInput()
    ship.rotationAngle = InputStatus.pointVector.subImmut(ship.position).angle() - 90f

    curtime += Gdx.graphics.deltaTime
    if (curtime >= SPEED_IMPULSE_DELAY) {
        curtime = 0f
        if (InputStatus.leftButtonPressed) {
            System.out.println("пиу-пиу!")
        }
        if (InputStatus.rightButtonPressed) {
            ship.forces.add(ThrustForvardForce((SPEED_IMPULSE_DELAY*1000).toLong(), SPEED_IMPULSE_AMOUNT))
        }
    }
}