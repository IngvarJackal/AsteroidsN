package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.mulScalar
import com.badlogic.gdx.math.Vector2

class ThrustForce(timeMs: Long, val thrust: Vector2): TimeLimitedForce(timeMs) {
    override fun engineClone(): EngineForce {
        return ThrustForce(this.timeMs, this.thrust)
    }

    override fun actualAction(obj: EngineObject, engine: Engine, timedelta: Float) {
        obj.velocity = obj.velocity.add(thrust.mulScalar(timedelta/obj.mass))
    }
}