package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.mulScalar
import com.badlogic.gdx.math.Vector2

class ThrustForvardForce(timeMs: Long, val thrust: Float): TimeLimitedForce(timeMs) {
    override fun engineClone(): EngineForce {
        return ThrustForvardForce(timeMs, thrust)
    }

    override fun actualAction(obj: EngineObject, engine: Engine, timedelta: Float) {
        obj.velocity = obj.velocity.add(Vector2(0f, 1f).rotate(obj.rotationAngle).mulScalar(timedelta / obj.mass * thrust))
    }
}