package asteroids.n.entities.objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.engine.objects.MovableEngineObject
import asteroids.n.engine.objects.StaticEngineObject
import java.util.*

open class SpaceObject(internal val img: Texture, override val mass: Float): EngineObject {
    override var position: Vector2
        get() = Vector2(sprite.x + sprite.width / 2, sprite.y + sprite.height / 2)
        set(value: Vector2) {
            position.x = value.x
            position.y = value.y
            sprite.setPosition(value.x - sprite.width / 2, value.y - sprite.height / 2)
        }
    override var velocity: Vector2 = Vector2(0f, 0f)

    override var forces: MutableSet<EngineForce> = HashSet()

    val sprite = Sprite(img);

    fun draw(batch: Batch) {
        batch.draw(sprite, sprite.x, sprite.y);
    }
}

class SpaceMovableObject(img: Texture, mass: Float) : SpaceObject(img, mass), MovableEngineObject
class SpaceStaticObject(img: Texture, mass: Float) : SpaceObject(img, mass), StaticEngineObject