package asteroids.n.utils

import asteroids.n.entities.objects.Asteroid
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils

fun createAsteroid(mass: Float, massVariance: Float):Asteroid {
    return createAsteroidFromResource(mass, massVariance, "assets/asteroids", 5)
}

fun createSmallAsteroid(mass: Float, massVariance: Float):Asteroid {
    return createAsteroidFromResource(mass, massVariance, "assets/asteroids-small", 3)
}

fun createAsteroidFromResource(mass: Float, massVariance: Float, resource: String, filenum: Int):Asteroid {
    val asteroids = loadImages(resource, filenum)
    asteroids.shuffle()
    var resAsteroids = com.badlogic.gdx.utils.Array<TextureRegion>(3)
    for (i in 0..1) {
        asteroids[i].flip(MathUtils.random(1) == 0, MathUtils.random(1) == 0)
        resAsteroids.add(asteroids[i])
    }
    return Asteroid(mass + MathUtils.random(massVariance)/2 - massVariance, resAsteroids)
}