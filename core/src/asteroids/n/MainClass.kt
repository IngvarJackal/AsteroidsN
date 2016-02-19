package asteroids.n

import asteroids.n.engine.Engine
import asteroids.n.entities.forces.GravityForce
import asteroids.n.entities.forces.ThrustForce
import asteroids.n.entities.objects.SpaceMovableImageObject
import asteroids.n.entities.objects.SpaceStaticAnimatedObject
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import asteroids.n.entities.objects.SpaceStaticImageObject
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.TimeUtils
import java.util.*

class MainClass : ApplicationAdapter() {
    internal var batch: SpriteBatch? = null
    internal var earth: SpaceStaticAnimatedObject? = null
    internal var moon: SpaceMovableImageObject? = null

    internal var cam: OrthographicCamera? = null
    internal var shapeRenderer: ShapeRenderer? = null

    internal val physEngine = Engine(1000/60f); // 60 Hz interval in ms

    override fun create() {
        batch = SpriteBatch()

        earth = SpaceStaticAnimatedObject("earth", msFrameDelay=1000, mass = 100000f)
        earth!!.position = Vector2(360f, 360f)
        earth!!.sprite.rotation = 45f

        moon = SpaceMovableImageObject(Texture("badlogic-very-small.png"), mass = 1f)
        moon!!.position = Vector2(140f, 370f)
        moon!!.forces.add(GravityForce)
        //moon!!.forces.add(ThrustForce(200, Vector2(0f,2000f)))
        moon!!.forces.add(ThrustForce(200, Vector2(0f,50f)))

        cam = OrthographicCamera()
        cam!!.setToOrtho(true, 720f, 720f);
        cam!!.update()
        shapeRenderer = ShapeRenderer()
        shapeRenderer!!.setProjectionMatrix(cam!!.combined)
        shapeRenderer!!.setAutoShapeType(true)

        physEngine.registerObject(earth!!)
        physEngine.registerObject(moon!!)
    }

    var lastPosition: MutableList<Vector2> = ArrayList()
    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        physEngine.step()

        lastPosition.add(moon!!.position)
        for (i in 0..lastPosition.size-2) {
            drawLine(lastPosition[i], lastPosition[i+1])
        }

        batch!!.begin();

        earth!!.draw(batch!!)
        moon!!.draw(batch!!)

        System.out.println(moon!!.position)

        batch!!.end();
    }

    fun drawLine(from: Vector2, to: Vector2, color: Color = Color.BLACK) {
        shapeRenderer!!.begin()
        shapeRenderer!!.setColor(color);
        shapeRenderer!!.line(from.invertY(), to.invertY())
        shapeRenderer!!.end();
    }
}
