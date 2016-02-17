package space.truck.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

class SpaceObject(internal val img: Texture, internal val world: World, density: Float = 1f) {
    val sprite = Sprite(img);

    val body: Body
    init {
        val bodyDef = BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine is 1 pixel
        bodyDef.position.set(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
        body = world.createBody(bodyDef);

        val shape = PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        val fixtureDef = FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;

        val data = body.getMassData();
        data.center.set(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
        body.setMassData(data);

        shape.dispose();
    }

    fun updatePositionPhys() {
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
    }

    fun draw(batch: Batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY());
    }

    fun setPosition(x: Float, y: Float) {
        sprite.setPosition(x, y)
        body.setTransform(x, y, body.angle)
    }

    fun centerMass(): Vector2 {
        return Vector2(sprite.x + sprite.width / 2, sprite.y + sprite.height / 2)
    }

    fun center(): Vector2 {
        return Vector2(sprite.x + sprite.width / 2, 720 - (sprite.y + sprite.height / 2))
    }
}