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

    internal var cam: OrthographicCamera? = null
    internal var shapeRenderer: ShapeRenderer? = null

    internal var asteroids: MutableList<Asteroid> = ArrayList()

    internal val physEngine = Engine(1000/60f); // 60 Hz interval in ms

    override fun create() {
        batch = SpriteBatch()

        earth = Earth()

        moon = Moon()

        spaceship = PlayerShip()
        spaceship!!.position = Vector2(120f, 350f)
        spaceship!!.forces.add(ThrustForce(200, Vector2(50f, 275f)))
        spaceship!!.forces.add(MoonGravityForce)
        spaceship!!.forces.add(EarthGravityForce)

        physEngine.registerObject(earth!!)
        physEngine.registerObject(moon!!)
        physEngine.registerObject(spaceship!!)

        cam = OrthographicCamera()
        cam!!.setToOrtho(true, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat());
        cam!!.update()
        shapeRenderer = ShapeRenderer()
        shapeRenderer!!.setProjectionMatrix(cam!!.combined)
        shapeRenderer!!.setAutoShapeType(true)

    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        createRandomAsteroid()

        physEngine.step()

        processPlayerInput(spaceship!!)

        renderTrajectory(spaceship!!, shapeRenderer!!)

        batch!!.begin()

        earth!!.draw(batch!!)
        moon!!.draw(batch!!)
        spaceship!!.draw(batch!!)

        for (asteroid: Asteroid in asteroids)
            asteroid.draw(batch!!)

        batch!!.end()
    }

    var timePassed2: Float = Float.POSITIVE_INFINITY
    fun createRandomAsteroid() {
        timePassed2 += Gdx.graphics.deltaTime
        if (timePassed2 >= 10f) {
            timePassed2 = 0f

            for (i in 0..1) {
                val asteroid = createAsteroid(mass = 4f, massVariance = 1f)
                asteroid.position = Vector2(MathUtils.random(0f, Gdx.graphics.width / 2f - 150) + MathUtils.random(0, 2) * (Gdx.graphics.width / 2f + 150), MathUtils.random(-5f, -25f))
                asteroid.forces.add(ThrustForce(200, Vector2(MathUtils.random(0, Gdx.graphics.width) / 4f, MathUtils.random(0, Gdx.graphics.width) / 4f)))
                asteroid.forces.add(RotationForce(200, MathUtils.random(-40f, 40f)))
                asteroid.forces.add(MoonGravityForce)
                asteroid.forces.add(EarthGravityForce)
                asteroids.add(asteroid)
                physEngine.registerObject(asteroid)
            }
        }
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
}
