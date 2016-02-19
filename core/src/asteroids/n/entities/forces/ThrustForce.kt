package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.objects.EngineObject
import asteroids.n.mulScalar
import com.badlogic.gdx.math.Vector2

class ThrustForce(timeMs: Long, val thrust: Vector2): TimeLimitedForce(timeMs) {
    val REGULARIZATION = 1e2f // to slow down because of ms
    override fun actualAction(obj: EngineObject, engine: Engine) {
        obj.velocity = obj.velocity.add(thrust.mulScalar(engine.msDelay/REGULARIZATION/obj.mass))
    }
}