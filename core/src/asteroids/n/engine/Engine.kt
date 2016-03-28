package asteroids.n.engine

import asteroids.n.addImmut
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.Direction
import asteroids.n.engine.objects.EngineObject
import asteroids.n.engine.objects.MovableEngineObject
import asteroids.n.engine.objects.StaticEngineObject
import asteroids.n.entities.objects.PlayerShip
import asteroids.n.mulScalar
import asteroids.n.subImmut
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

    fun unregisterObject(obj: EngineObject) {
        when (obj) {
            is MovableEngineObject -> {movableObjects.remove(obj)}
            is StaticEngineObject -> {staticObjects.remove(obj)}
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

    fun tracePlayerPath(ship: PlayerShip, steps: Int = 2000, steplen: Int = (msDelay*2).toInt()): MutableList<Vector2> {
        val resultArray:MutableList<Vector2> = ArrayList(steps)
        val shipCopy = ship.partialEngineClone()
        for (i in 1..steps) {
            val iterator = shipCopy.forces.iterator()
            while (iterator.hasNext())
                if (iterator.next().applyAndDelete(shipCopy, this, steplen/1000f))
                    iterator.remove()
            shipCopy.position = shipCopy.position.addImmut(normalizeSpeed(shipCopy.velocity.mulScalar(steplen/REGULARIZATION)))
            resultArray.add(shipCopy.position)
        }
        return resultArray
    }

    internal fun makeStep() {
        for (movableObject in movableObjects) {
            val iterator = movableObject.forces.iterator()
            while (iterator.hasNext())
                if (iterator.next().applyAndDelete(movableObject, this, msDelay/1000))
                    iterator.remove()

            movableObject.position = movableObject.position.addImmut(normalizeSpeed(movableObject.velocity.mulScalar(msDelay/REGULARIZATION)))
            movableObject.rotationAngle = (movableObject.rotationAngle + normalizeRotation(movableObject.rotationSpeed*msDelay/REGULARIZATION)) % 360

            val allObjects = HashSet<EngineObject>(movableObjects)
            allObjects.addAll(staticObjects)
            allObjects.remove(movableObject)
            for (anotherObject in allObjects) {
                val collision = checkCollision(movableObject, anotherObject)
                if (collision != null) {
                    movableObject.collisions.add(Pair(collision, anotherObject))
                }
            }
        }
    }

    internal fun checkCollision(obj1: EngineObject, obj2: EngineObject):Direction? {
        val diff = obj2.position.subImmut(obj1.position)
        val totSize = obj1.size + obj2.size
        if (diff.len() <= totSize) {
            if (diff.y > 0 && diff.y > Math.abs(diff.x))
                return Direction.UP
            if (diff.y < 0 && diff.y < Math.abs(diff.x))
                return Direction.DOWN
            if (diff.x > 0 && diff.x > Math.abs(diff.y))
                return Direction.RIGHT
            if (diff.x < 0 && diff.x < Math.abs(diff.y))
                return Direction.LEFT
            throw AssertionError("WTF??? diff = ${diff}, totsize = ${totSize}}")
        }
        return null
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