package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject

class LifeForce(timeMs: Long) : TimeLimitedForce(timeMs) {
    override fun engineClone(): EngineForce {
        return LifeForce(timeMs-super.time)
    }

    override fun actualAction(obj: EngineObject, engine: Engine, timedelta: Float) {
        // no action
    }

}