package asteroids.n

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

fun Vector2.mulScalar(scalar: Float):Vector2 {
    return Vector2(this.x*scalar, this.y*scalar)
}

fun Vector2.invertY():Vector2 {
    return Vector2(this.x, Gdx.graphics.getHeight()-this.y)
}

fun Vector2.subImmut(vector2: Vector2):Vector2 {
    return Vector2(this.x - vector2.x, this.y - vector2.y)
}

fun Vector2.addImmut(vector2: Vector2):Vector2 {
    return Vector2(this.x + vector2.x, this.y + vector2.y)
}