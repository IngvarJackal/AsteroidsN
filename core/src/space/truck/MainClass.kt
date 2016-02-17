package space.truck

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import space.truck.entities.CelestialBody
import space.truck.entities.SpaceObject

class MainClass : ApplicationAdapter() {
    internal var batch: SpriteBatch? = null
    internal var world: World? = null
    internal var earth: CelestialBody? = null
    internal var moon: SpaceObject? = null;

    override fun create() {
        batch = SpriteBatch()
        world = World(Vector2(0f, 0f), true);

        earth = CelestialBody(Texture("badlogic-small.jpg"), world!!)
        earth!!.setInCenter()

        moon = SpaceObject(Texture("badlogic-very-small.jpg"), world!!)
        moon!!.setPosition(0f, 0f);
    }

    override fun render() {
        world!!.step(Gdx.graphics.getDeltaTime(), 6, 2)

        moveObject(moon!!, arrayOf(earth!!))
        moon!!.updatePositionPhys()

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch!!.begin();

        earth!!.draw(batch!!)
        moon!!.draw(batch!!)

        batch!!.end();
    }

    fun moveObject(obj: SpaceObject, bodies: Array<CelestialBody>) {
        for (body in bodies) {
            var distance = Vector2.dst(obj.body.position.x, obj.body.position.y,
                                       body.body.position.x, body.body.position.y)
            var force = Math.max(Math.min(((obj.body.mass * body.mass) /
                         (Math.pow(distance.toDouble(), 2.0))).toFloat(), 100f), 1e-3f)
            System.out.println("Object: ${obj.body.position}, body: ${body.body.position}")
            System.out.println("Distance: ${distance}, force: ${force}")
            System.out.println("FINAL: ${(body.body.position.x - obj.body.position.x)*force}, ${(body.body.position.y - obj.body.position.y)*force}")
            obj.body.applyForce((body.body.position.x - obj.body.position.x)*force,
                                (body.body.position.y - obj.body.position.y)*force,
                                obj.body.position.x,
                                obj.body.position.y,
                                true)
        }
    }
}
