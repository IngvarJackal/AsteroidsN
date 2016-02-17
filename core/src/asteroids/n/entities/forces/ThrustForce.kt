package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.mulScalar
import com.badlogic.gdx.math.Vector2

class ThrustForce(val timeMs: Long, val thrust: Vector2): EngineForce {
    val REGULARIZATION = 1e2f // to slow down because of ms

    var time: Long? = null
    override fun apply(obj: EngineObject, engine: Engine) {
        if (time == null)
            time = System.currentTimeMillis()
        else {
            obj.velocity = obj.velocity.add(thrust.mulScalar(engine.msDelay/REGULARIZATION/obj.mass))
            if (System.currentTimeMillis() - time!! >= timeMs) {
                obj.forces.remove(this)
            }
        }
    }
}