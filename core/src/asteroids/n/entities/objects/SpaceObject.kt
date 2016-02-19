package asteroids.n.entities.objects

import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.engine.objects.MovableEngineObject
import asteroids.n.engine.objects.StaticEngineObject
import asteroids.n.utils.loadImages
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Vector2
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite
import java.util.*

abstract class SpaceObject(override val mass: Float): EngineObject {

    abstract val sprite: Sprite

    override var position: Vector2
        get() = Vector2(sprite.x + sprite.width / 2, sprite.y + sprite.height / 2)
        set(value: Vector2) {
            position.x = value.x
            position.y = value.y
            sprite.setPosition(value.x - sprite.width / 2, value.y - sprite.height / 2)
        }
    override var velocity: Vector2 = Vector2(0f, 0f)

    override var rotationAngle: Float
        get() = sprite.rotation
        set(value) {
            sprite.setRotation(value)
        }
    override var rotationSpeed: Float = 0f

    override var forces: MutableSet<EngineForce> = HashSet()

    abstract fun draw(batch: Batch)
}

open class SpaceImageObject(internal val img: Texture, mass: Float): SpaceObject(mass) {
    override val sprite = Sprite(img);

    override fun draw(batch: Batch) {
        batch.draw(sprite, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
    }
}
open class SpaceMovableImageObject(img: Texture, mass: Float) : SpaceImageObject(img, mass), MovableEngineObject
open class SpaceStaticImageObject(img: Texture, mass: Float) : SpaceImageObject(img, mass), StaticEngineObject

open class SpaceAnimatedObject(internal val resname: String, val msFrameDelay: Long, mass: Float): SpaceObject(mass) {
    override val sprite = AnimatedSprite(Animation(msFrameDelay/1000f, loadImages(resname), Animation.PlayMode.LOOP));

    override fun draw(batch: Batch) {
        batch.draw(sprite, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
        sprite.update()
    }
}
open class SpaceMovableAnimatedObject(resname: String, msFrameDelay: Long, mass: Float) : SpaceAnimatedObject(resname, msFrameDelay, mass), MovableEngineObject
open class SpaceStaticAnimatedObject(resname: String, msFrameDelay: Long, mass: Float) : SpaceAnimatedObject(resname, msFrameDelay, mass), StaticEngineObject