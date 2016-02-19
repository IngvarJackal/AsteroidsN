package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.objects.EngineObject

class RotationForce(timeMs: Long, val rotation: Float) : TimeLimitedForce(timeMs) {
    val REGULARIZATION = 1e2f // to slow down because of ms
    override fun actualAction(obj: EngineObject, engine: Engine) {
        obj.rotationSpeed = obj.rotationSpeed + rotation*engine.msDelay/REGULARIZATION/obj.mass
    }
}