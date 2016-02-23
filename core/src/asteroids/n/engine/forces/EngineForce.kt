package asteroids.n.engine.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.objects.EngineObject

interface EngineForce {
    fun apply(obj: EngineObject, engine: Engine, timedelta: Float)
    fun engineClone(): EngineForce
}