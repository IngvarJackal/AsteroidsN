package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject

class RotationForce(timeMs: Long, val rotation: Float) : TimeLimitedForce(timeMs) {
    override fun engineClone(): EngineForce {
        return RotationForce(this.timeMs, this.rotation)
    }

    override fun actualAction(obj: EngineObject, engine: Engine, timedelta: Float) {
        obj.rotationSpeed = obj.rotationSpeed + rotation*timedelta/obj.mass
    }
}