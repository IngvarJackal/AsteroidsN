package space.truck.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*

class CelestialBody(internal val img: Texture, internal val world: World, mass: Float = 100f) {
    val mass = mass
    val sprite = Sprite(img);
    val body: Body
    init {
        val bodyDef = BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine is 1 pixel
        bodyDef.position.set(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
        body = world.createBody(bodyDef);

        val shape = PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        val fixtureDef = FixtureDef();
        fixtureDef.shape = shape;

        shape.dispose();
    }

    fun setInCenter() {
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        body.setTransform(sprite.x, sprite.y, body.angle)
    }

    fun draw(batch: Batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY());
    }
}