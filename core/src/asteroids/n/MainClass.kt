package asteroids.n

import asteroids.n.engine.Engine
import asteroids.n.entities.forces.GravityForce
import asteroids.n.entities.forces.RotationForce
import asteroids.n.entities.forces.ThrustForce
import asteroids.n.entities.objects.*
import asteroids.n.utils.createAsteroid
import asteroids.n.utils.drawLine
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.TimeUtils
import java.util.*

class MainClass : ApplicationAdapter() {
    internal var batch: SpriteBatch? = null

    internal var earth: Earth? = null
    internal var moon: Moon? = null
    internal var spaceship: PlayerShip? = null

    internal var asteroid: Asteroid? = null

    internal var cam: OrthographicCamera? = null
    internal var shapeRenderer: ShapeRenderer? = null

    internal val physEngine = Engine(1000/60f); // 60 Hz interval in ms

    override fun create() {
        batch = SpriteBatch()

        earth = Earth()
        moon = Moon()
        spaceship = PlayerShip()
        spaceship!!.position = Vector2(160f, 360f)
        spaceship!!.forces.add(ThrustForce(200, Vector2(-35f, 55f)))
        spaceship!!.forces.add(GravityForce)
        asteroid = createAsteroid(4f, 1f)
        asteroid!!.position = Vector2(260f, 260f)
        asteroid!!.forces.add(ThrustForce(200, Vector2(0f, -7f)))
        asteroid!!.forces.add(RotationForce(200, 25f))
        asteroid!!.forces.add(GravityForce)

        physEngine.registerObject(earth!!)
        physEngine.registerObject(moon!!)
        physEngine.registerObject(spaceship!!)
        physEngine.registerObject(asteroid!!)

        cam = OrthographicCamera()
        cam!!.setToOrtho(true, 720f, 720f);
        cam!!.update()
        shapeRenderer = ShapeRenderer()
        shapeRenderer!!.setProjectionMatrix(cam!!.combined)
        shapeRenderer!!.setAutoShapeType(true)
    }

    var lastPosition: MutableList<Vector2> = ArrayList()
    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        physEngine.step()

        lastPosition.add(asteroid!!.position)
        for (i in 0..lastPosition.size-2) {
            drawLine(shapeRenderer!!, lastPosition[i], lastPosition[i+1])
        }

        batch!!.begin();

        earth!!.draw(batch!!)
        moon!!.draw(batch!!)
        spaceship!!.draw(batch!!)
        asteroid!!.draw(batch!!)

        batch!!.end();
    }
}
