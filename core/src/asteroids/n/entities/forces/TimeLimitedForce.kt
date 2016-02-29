package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils

abstract class TimeLimitedForce(val timeMs: Long) : EngineForce {

    abstract fun actualAction(obj: EngineObject, engine: Engine, timedelta: Float)

    var time: Long = 0
    override fun applyAndDelete(obj: EngineObject, engine: Engine, timedelta: Float): Boolean {
        time += (timedelta*1000).toLong()
        actualAction(obj, engine, timedelta)
        return time >= timeMs
    }
}