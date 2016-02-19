package asteroids.n.engine

import asteroids.n.addImmut
import asteroids.n.engine.objects.EngineObject
import asteroids.n.engine.objects.MovableEngineObject
import asteroids.n.engine.objects.StaticEngineObject
import asteroids.n.mulScalar
import com.badlogic.gdx.math.Vector2
import java.util.*

class Engine(val msDelay: Float) {
    val REGULARIZATION = 1e3f // to slow down because of ms
    val MAX_SPEED = 2000/REGULARIZATION
    val MAX_ROTATION = 360*3f/msDelay

    internal val movableObjects: MutableSet<MovableEngineObject> = HashSet()
    internal val staticObjects: MutableSet<StaticEngineObject> = HashSet()

    fun registerObject(obj: EngineObject) {
        when (obj) {
            is MovableEngineObject -> {movableObjects.add(obj)}
            is StaticEngineObject -> {staticObjects.add(obj)}
            else -> {throw IllegalArgumentException("Must be StaticEngineObject or MovableEngineObject!")}
        }
    }

    internal var lastRunTime: Long = 0L
    fun step() {
        if (System.currentTimeMillis() - lastRunTime >= msDelay) {
            makeStep()
            lastRunTime = System.currentTimeMillis()
        }
        else
            return // just pass
    }

    internal fun makeStep() {
        for (movableObject in movableObjects) {
            for (force in HashSet(movableObject.forces)) {
                force.apply(movableObject, this)
            }
            movableObject.position = movableObject.position.addImmut(normalizeSpeed(movableObject.velocity.mulScalar(msDelay/REGULARIZATION)))
            movableObject.rotationAngle = (movableObject.rotationAngle + normalizeRotation(movableObject.rotationSpeed*msDelay/REGULARIZATION)) % 360
        }
    }

    internal fun normalizeSpeed(speed: Vector2): Vector2 {
        if (speed.len() >= MAX_SPEED)
            return speed.mulScalar(MAX_SPEED/speed.len())
        else
            return speed
    }

    internal fun normalizeRotation(rotation: Float): Float {
        return Math.min(MAX_ROTATION, rotation)
    }
}