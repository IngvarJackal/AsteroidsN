package asteroids.n.logic

import asteroids.n.addImmut
import asteroids.n.engine.Engine
import asteroids.n.entities.objects.Asteroid
import asteroids.n.entities.objects.Bullet
import asteroids.n.entities.objects.Earth
import asteroids.n.entities.objects.Moon
import asteroids.n.utils.createAsteroid
import asteroids.n.entities.forces.*
import asteroids.n.mulScalar
import asteroids.n.utils.createSmallAsteroid
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import java.util.*


fun collideBullets(gameEngine: Engine, bullets: MutableList<Bullet>) {
    for (bullet in ArrayList(bullets))
        for (pair in bullet.collisions.iterator()) {
            when (pair.second) {
                is Earth -> destroyBullet(bullet, bullets, gameEngine)
                is Moon -> destroyBullet(bullet, bullets, gameEngine)
            }
        }
}


fun collideAsteroids(asteroids: MutableList<Asteroid>, gameEngine: Engine, bullets: MutableList<Bullet>) {
    for (asteroid in ArrayList(asteroids))
        maybeDestroyAsteroid(asteroid, asteroids, gameEngine, bullets)
}

fun maybeDestroyAsteroid(asteroid: Asteroid, asteroids: MutableList<Asteroid>, gameEngine: Engine, bullets: MutableList<Bullet>) {
    for (pair in asteroid.collisions.iterator()) {
        when (pair.second) {
            is Earth -> {(pair.second).health -= StrictMath.round(asteroid.mass); destroyAsteroid(asteroid, asteroids, gameEngine)}
            is Moon -> {destroyAsteroid(asteroid, asteroids, gameEngine)}
            is Bullet -> {shootAsteroid(asteroid, asteroids, gameEngine); destroyBullet(pair.second, bullets, gameEngine)}
        }
    }
}

fun shootAsteroid(asteroid: Asteroid, asteroids: MutableList<Asteroid>, gameEngine: Engine) {
    splitNdestroy(asteroid, 2f, asteroids, gameEngine)
}

fun splitNdestroy(asteroid: Asteroid, factor: Float, asteroids: MutableList<Asteroid>, gameEngine: Engine) {
    destroyAsteroid(asteroid, asteroids, gameEngine)
    val newMass = Math.floor(asteroid.mass/factor.toDouble()).toFloat()
    if (newMass != 0f) {
        for (i in -1..1) {
            val newAsteroid = createSmallAsteroid(mass = newMass, massVariance = 0f)
            newAsteroid.position = asteroid.position.addImmut(Vector2(i*2f, i*i*2f))
            newAsteroid.forces = HashSet(asteroid.forces)

            newAsteroid.rotationSpeed = asteroid.rotationSpeed + MathUtils.random(-15f, 15f)

            newAsteroid.velocity = asteroid.velocity.cpy()
            newAsteroid.velocity.add(newAsteroid.velocity.mulScalar(1f/newAsteroid.velocity.len() * 15).rotate(45f*i))
            asteroids.add(newAsteroid)
            gameEngine.registerObject(newAsteroid)
        }
    }
}

fun destroyAsteroid(asteroid: Asteroid, asteroids: MutableList<Asteroid>, gameEngine: Engine) {
    asteroids.remove(asteroid)
    gameEngine.unregisterObject(asteroid)
}

fun destroyBullet(bullet: Bullet, bullets: MutableList<Bullet>, gameEngine: Engine) {
    bullets.remove(bullet)
    gameEngine.unregisterObject(bullet)
}