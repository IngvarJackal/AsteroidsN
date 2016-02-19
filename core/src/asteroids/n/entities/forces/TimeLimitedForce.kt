package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils

abstract class TimeLimitedForce(val timeMs: Long) : EngineForce {

    abstract fun actualAction(obj: EngineObject, engine: Engine)

    var time: Long? = null
    override fun apply(obj: EngineObject, engine: Engine) {
        if (time == null)
            time = TimeUtils.millis()
        else {
            actualAction(obj, engine)
            if (System.currentTimeMillis() - time!! >= timeMs) {
                obj.forces.remove(this)
            }
        }
    }
}