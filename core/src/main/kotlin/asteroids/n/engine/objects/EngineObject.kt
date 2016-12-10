package asteroids.n.engine.objects

import com.badlogic.gdx.math.Vector2
import asteroids.n.engine.forces.EngineForce

interface EngineObject {
    val mass: Float
    var position: Vector2
    var velocity: Vector2
    var rotationAngle: Float
    var rotationSpeed: Float
    val forces: MutableSet<EngineForce>
    val collisions: MutableSet<Pair<Direction, EngineObject>>
    val size: Float
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

interface MovableEngineObject: EngineObject
interface StaticEngineObject: EngineObject