package asteroids.n

import asteroids.n.engine.Engine
import asteroids.n.entities.forces.EarthGravityForce
import asteroids.n.entities.forces.MoonGravityForce
import asteroids.n.entities.forces.RotationForce
import asteroids.n.entities.forces.ThrustForce
import asteroids.n.entities.objects.*
import asteroids.n.logic.collideAsteroids
import asteroids.n.logic.collideBullets
import asteroids.n.logic.processPlayerInput
import asteroids.n.logic.refillEnergy
import asteroids.n.utils.*
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import java.util.*

object Constants {
    val EARTH_HEALTH_MAX = 20;
    val PLAYER_ENERGY_MAX = 500f;
}

class MainClass : ApplicationAdapter() {
    internal var batch: SpriteBatch? = null

    internal var earth: Earth? = null
    internal var moon: Moon? = null
    internal var spaceship: PlayerShip? = null

    internal var cam: OrthographicCamera? = null
    internal var shapeRenderer: ShapeRenderer? = null

    internal var asteroids: MutableList<Asteroid> = ArrayList()

    internal var bullets: MutableList<Bullet> = ArrayList()

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
        if (!maybeKillPlayer(spaceship!!)) {

            Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            createRandomAsteroid()
            collideAsteroids(asteroids, physEngine, bullets)
            collideBullets(physEngine, bullets)

            refillEnergy(moon!!, spaceship!!)

            physEngine.step()

            checkBullets(bullets, physEngine)

            processPlayerInput(spaceship!!, bullets, physEngine)

            renderTrajectory(spaceship!!, shapeRenderer!!)

            displayEnergy(spaceship!!)
            displayEarthHealth(earth!!)

            batch!!.begin()

            earth!!.draw(batch!!)
            moon!!.draw(batch!!)
            spaceship!!.draw(batch!!)

            for (asteroid: Asteroid in asteroids)
                asteroid.draw(batch!!)

            for (bullet: Bullet in bullets)
                bullet.draw(batch!!)

            batch!!.end()
        }
    }

    var timePassed2: Float = Float.POSITIVE_INFINITY
    fun createRandomAsteroid() {
        timePassed2 += Gdx.graphics.deltaTime
        if (timePassed2 >= 2f) {
            timePassed2 = 0f

            var asteroid: Asteroid? = null

            if (MathUtils.random(3) == 0)
                asteroid = createAsteroid(mass = 4.5f, massVariance = 1f)
            else
                asteroid = createSmallAsteroid(mass = 2f, massVariance = 0.5f)
            asteroid.position = Vector2(MathUtils.random(0f, Gdx.graphics.width / 2f - 150) + MathUtils.random(0, 2) * (Gdx.graphics.width / 2f + 150), MathUtils.random(-5f, -25f))
            asteroid.forces.add(ThrustForce(200, Vector2(MathUtils.random(0, Gdx.graphics.width) / 4f, MathUtils.random(0, Gdx.graphics.width) / 4f)))
            asteroid.forces.add(RotationForce(200, MathUtils.random(-40f, 40f)))
            asteroid.forces.add(MoonGravityForce)
            asteroid.forces.add(EarthGravityForce)
            asteroids.add(asteroid)
            physEngine.registerObject(asteroid)
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
                    color = Color(0f,
                                0.9f,
                                0.9f,
                                Math.sqrt((0.9*tracedTrajectory!!.size - pointNum)/tracedTrajectory!!.size).toFloat()))
        }
    }

    fun maybeKillPlayer(spaceship: PlayerShip): Boolean {
        if (spaceship.collisions.isNotEmpty() || earth!!.health <= 0) {
            batch!!.begin()
            batch!!.draw(Texture("game_over.png"), Gdx.graphics.width / 2f - 300f, Gdx.graphics.height / 2f);
            batch!!.end()
            return true;
        } else {
            return false
        }
    }

    fun displayEnergy(spaceship: PlayerShip) {
        drawRectangle(shapeRenderer!!, 10f, 10f, Gdx.graphics.width.toFloat()-20, 20f)
        drawRectangle(shapeRenderer!!, 10f, 10f, spaceship.energy*1f/Constants.PLAYER_ENERGY_MAX*(Gdx.graphics.width.toFloat()-21), 19f, Color(
                                                                                             1f-spaceship.energy*1f/Constants.PLAYER_ENERGY_MAX,
                                                                                             Math.sqrt(spaceship.energy*1f/Constants.PLAYER_ENERGY_MAX.toDouble()).toFloat(),
                                                                                             0f,
                                                                                             1f), filled=true)
    }

    fun displayEarthHealth(earth: Earth) {
        drawRectangle(shapeRenderer!!, Gdx.graphics.width/2f - 40f, Gdx.graphics.height / 2f - 48f, 80f, 6f)
        drawRectangle(shapeRenderer!!, Gdx.graphics.width/2f - 40f, Gdx.graphics.height / 2f - 48f, earth.health*1f/Constants.EARTH_HEALTH_MAX*80f-1, 5f, Color(
                1f-earth.health*1f/Constants.EARTH_HEALTH_MAX,
                earth.health*1f/Constants.EARTH_HEALTH_MAX,
                0f,
                1f), filled=true)
    }
}
