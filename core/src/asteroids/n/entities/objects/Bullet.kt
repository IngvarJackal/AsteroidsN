package asteroids.n.entities.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class Bullet() : SpaceMovableImageObject(Texture(Gdx.files.classpath("assets/bullet.png")), 1f) {
}