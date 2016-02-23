package asteroids.n

import asteroids.n.engine.Engine
import asteroids.n.entities.forces.*
import asteroids.n.entities.objects.Asteroid
import asteroids.n.entities.objects.Earth
import asteroids.n.entities.objects.Moon
import asteroids.n.entities.objects.PlayerShip
import asteroids.n.logic.processPlayerInput
import asteroids.n.utils.createAsteroid
import asteroids.n.utils.drawCircle
import asteroids.n.utils.drawLine
import asteroids.n.utils.handlePlayerInput
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
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
        spaceship!!.position = Vector2(120f, 350f)
        spaceship!!.forces.add(ThrustForce(200, Vector2(0f, 300f)))
        spaceship!!.forces.add(MoonGravityForce)
        spaceship!!.forces.add(EarthGravityForce)

        asteroid = createAsteroid(4f, 1f)
        asteroid!!.position = Vector2(260f, 260f)
        asteroid!!.forces.add(ThrustForce(200, Vector2(0f, 30f)))
        asteroid!!.forces.add(RotationForce(200, 25f))
        asteroid!!.forces.add(MoonGravityForce)
        asteroid!!.forces.add(EarthGravityForce)

        physEngine.registerObject(earth!!)
        physEngine.registerObject(moon!!)
        physEngine.registerObject(spaceship!!)
        physEngine.registerObject(asteroid!!)

        cam = OrthographicCamera()
        cam!!.setToOrtho(true, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat());
        cam!!.update()
        shapeRenderer = ShapeRenderer()
        shapeRenderer!!.setProjectionMatrix(cam!!.combined)
        shapeRenderer!!.setAutoShapeType(true)
    }

    var timePassed: Float = Float.POSITIVE_INFINITY
    var tracedTrajectory: MutableList<Vector2>? = null
    fun renderTrajectory(spaceship: PlayerShip, shapeRenderer: ShapeRenderer) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        timePassed += Gdx.graphics.deltaTime
        if (timePassed >= 1/1f) {
            timePassed = 0f
            tracedTrajectory = physEngine.tracePlayerPath(spaceship)
        }
        for (pointNum in 0..tracedTrajectory!!.size-1) {

            drawCircle(shapeRenderer, tracedTrajectory!!.get(pointNum),
                    color=Color(0f,
                                0.9f,
                                0.9f,
                                Math.sqrt((0.9*tracedTrajectory!!.size - pointNum)/tracedTrajectory!!.size).toFloat()))
        }
    }
    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        physEngine.step()

        processPlayerInput(spaceship!!)

        renderTrajectory(spaceship!!, shapeRenderer!!)


        batch!!.begin()

        earth!!.draw(batch!!)
        moon!!.draw(batch!!)
        spaceship!!.draw(batch!!)
        asteroid!!.draw(batch!!)

        batch!!.end()
    }
}
