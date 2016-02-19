package asteroids.n.utils

import asteroids.n.invertY
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

fun drawLine(shapeRenderer: ShapeRenderer, from: Vector2, to: Vector2, color: Color = Color.WHITE) {
    shapeRenderer.begin()
    shapeRenderer.setColor(color);
    shapeRenderer.line(from.invertY(), to.invertY())
    shapeRenderer.end();
}

fun loadImages(dirname: String):com.badlogic.gdx.utils.Array<TextureRegion> {
    val filenum = Gdx.files.local(dirname+"/").list().size
    val result = com.badlogic.gdx.utils.Array<TextureRegion>(filenum)
    for (i in 1..filenum) {
        result.add(TextureRegion(Texture("${dirname}/${(i).toString().padStart(3, '0')}.png")))
    }
    return result
}