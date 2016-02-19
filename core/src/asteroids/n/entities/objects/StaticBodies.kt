package asteroids.n.entities.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

class Earth(resname: String = "earth", msFrameDelay: Long = 1000, mass: Float = 1000f) : SpaceStaticAnimatedObject(resname, msFrameDelay, mass) {
    init {
        position = Vector2(360f, 360f)
    }
}

class Moon(img: Texture = Texture("moon.png"), mass: Float = 75f, val diameter: Float = 240f) : SpaceStaticImageObject(img, mass) {
    init {
        position = Vector2(diameter/2, Gdx.graphics.width.toFloat()/2)
    }
    val step = 0.2f
    var totalSteps = 0f
    var angle = 270.0 // because of initial position
    override fun draw(batch: Batch) {
        totalSteps += Gdx.graphics.deltaTime
        if (totalSteps >= step) {
            totalSteps = 0f

            angle += 0.2
            position = Vector2((Gdx.graphics.width/2f + Math.sin(angle*Math.PI/180)* diameter).toFloat(),
                               (Gdx.graphics.height/2f + Math.cos(angle*Math.PI/180)* diameter).toFloat())

        }
        super.draw(batch)
    }
}

