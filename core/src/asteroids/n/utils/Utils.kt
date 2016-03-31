package asteroids.n.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

fun drawRectangle(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float, color: Color = Color.WHITE, filled: Boolean = false) {
    if (!filled)
        shapeRenderer.begin()
    else
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.color = color;
    shapeRenderer.rect(x, y, width, height)
    shapeRenderer.end();
}

fun drawCircle(shapeRenderer: ShapeRenderer, pos: Vector2, radius: Float = 0.4f, color: Color = Color.WHITE) {
    shapeRenderer.begin()
    shapeRenderer.setColor(color);
    shapeRenderer.circle(pos.x, Gdx.graphics.height-pos.y, radius)
    shapeRenderer.end();
}

fun loadImages(dirname: String, filenum: Int):com.badlogic.gdx.utils.Array<TextureRegion> {
    val result = com.badlogic.gdx.utils.Array<TextureRegion>(filenum)
    for (i in 1..filenum) {
        result.add(TextureRegion(Texture(Gdx.files.classpath("${dirname}/${(i).toString().padStart(3, '0')}.png"))))
    }
    return result
}