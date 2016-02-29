package asteroids.n.engine.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.objects.EngineObject

interface EngineForce {
    fun applyAndDelete(obj: EngineObject, engine: Engine, timedelta: Float): Boolean
    fun engineClone(): EngineForce
}