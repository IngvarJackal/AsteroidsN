package asteroids.n.entities.objects

import asteroids.n.engine.objects.MovableEngineObject
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Asteroid(mass: Float, val asteroidTextures: com.badlogic.gdx.utils.Array<TextureRegion>) : SpaceObject(mass), MovableEngineObject {
    override val sprite: Sprite = Sprite(asteroidTextures[0])

    override fun draw(batch: Batch) {
        for (texture in asteroidTextures)
            batch.draw(texture, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
    }

}