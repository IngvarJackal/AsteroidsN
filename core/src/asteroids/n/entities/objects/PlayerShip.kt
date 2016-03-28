package asteroids.n.entities.objects

import com.badlogic.gdx.math.Vector2
import java.util.*

class PlayerShip(resname: String = "spaseship", msFrameDelay: Long = 250, mass: Float = 4f) : SpaceMovableAnimatedObject(resname, msFrameDelay, mass) {
    public fun partialEngineClone(): PlayerShip {
        val newShip = PlayerShip(resname=this.resname, msFrameDelay=this.msFrameDelay, mass=this.mass)
        newShip.position = Vector2(this.position)
        newShip.velocity = Vector2(this.velocity)
        newShip.rotationAngle = this.rotationAngle
        newShip.rotationSpeed = this.rotationSpeed

        newShip.forces = HashSet()
        for (force in this.forces) {
            newShip.forces.add(force.engineClone())
        }

        return newShip
    }
}

public object InputStatus {
    var pointVector = Vector2()
    var leftButtonPressed = false
    var rightButtonPressed = false

    override fun toString(): String {
        return "pointVector=${pointVector}, leftButtonPressed=${leftButtonPressed}, rightButtonPressed=${rightButtonPressed}"
    }
}