package space.truck

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import space.truck.entities.CelestialBody
import space.truck.entities.SpaceObject
import java.util.*

class MainClass : ApplicationAdapter() {
    internal var batch: SpriteBatch? = null
    internal var world: World? = null
    internal var earth: CelestialBody? = null
    internal var moon: SpaceObject? = null

    internal var cam: OrthographicCamera? = null
    internal var shapeRenderer: ShapeRenderer? = null

    override fun create() {
        batch = SpriteBatch()
        world = World(Vector2(0f, 0f), true);

        earth = CelestialBody(Texture("badlogic-small.png"), world!!, mass = 100000f)
        earth!!.setInCenter()

        moon = SpaceObject(Texture("badlogic-very-small.png"), world!!, density = 0.01f)
        moon!!.setPosition(100f, 328f);
        moon!!.body.applyForce(moon!!.body.position.x, moon!!.body.position.y + 600, // 900
                               moon!!.body.position.x, moon!!.body.position.y,
                               true)

        cam = OrthographicCamera()
        cam!!.setToOrtho(true, 720f, 720f);
        cam!!.update()
        shapeRenderer = ShapeRenderer()
        shapeRenderer!!.setProjectionMatrix(cam!!.combined)
        shapeRenderer!!.setAutoShapeType(true)
    }

    var lastPosition: MutableList<Vector2> = ArrayList<Vector2>()

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world!!.step(1/60f, 36, 24)

        moveObject(moon!!, arrayOf(earth!!))
        moon!!.updatePositionPhys()

        lastPosition.add(moon!!.center())
        for (i in 0..lastPosition.size-2) {
            drawLine(lastPosition[i], lastPosition[i+1])
        }

        batch!!.begin();

        earth!!.draw(batch!!)
        moon!!.draw(batch!!)

        batch!!.end();
    }

    fun moveObject(obj: SpaceObject, bodies: Array<CelestialBody>, speedDecrease: Float = 0.99f) {
        obj.body.applyForce(Vector2(-obj.body.linVelLoc.x*speedDecrease,
                                    -obj.body.linVelLoc.y*speedDecrease),
                            Vector2(obj.centerMass().x,
                                    obj.centerMass().y),
                            true);

        for (body in bodies) {
            var distance = Vector2.dst(obj.centerMass().x, obj.centerMass().y,
                                       body.centerMass().x, body.centerMass().y)
            var force = Math.max(Math.min(((obj.body.mass * body.mass) /
                         (Math.pow(distance.toDouble(), 2.0))).toFloat(), 20f), 0.5f)
            System.out.println("Object: ${obj.centerMass()}, body: ${body.centerMass()}")
            System.out.println("Distance: ${distance}, force: ${force}")
            obj.body.applyForce((body.centerMass().x - obj.centerMass().x)*force/distance,
                                (body.centerMass().y - obj.centerMass().y)*force/distance,
                                obj.centerMass().x,
                                obj.centerMass().y,
                                true)
            drawLine(Vector2(obj.centerMass().x, 720-obj.centerMass().y),
                    Vector2(obj.centerMass().x+(body.centerMass().x - obj.centerMass().x)*force/distance*4,
                            720-obj.centerMass().y - (body.centerMass().y - obj.centerMass().y)*force/distance*4))
        }
    }

    fun drawLine(from: Vector2, to: Vector2, color: Color = Color.BLACK) {
        shapeRenderer!!.begin()
        shapeRenderer!!.setColor(color);
        shapeRenderer!!.line(from, to)
        shapeRenderer!!.end();
    }
}
