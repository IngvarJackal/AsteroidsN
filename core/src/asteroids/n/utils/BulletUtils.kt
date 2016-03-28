package asteroids.n.utils

import asteroids.n.addImmut
import asteroids.n.engine.Engine
import asteroids.n.entities.forces.LifeForce
import asteroids.n.entities.forces.ThrustForce
import asteroids.n.entities.objects.Bullet
import asteroids.n.entities.objects.InputStatus
import asteroids.n.entities.objects.PlayerShip
import asteroids.n.mulScalar
import asteroids.n.subImmut
import com.badlogic.gdx.math.Vector2
import java.util.*

fun createBullet(target: Vector2, spaceship: PlayerShip, bullets: MutableList<Bullet>, engine: Engine) {
    val bullet = Bullet()
    bullet.position = spaceship.position.addImmut(target.mulScalar(1f/target.len()).mulScalar(20f))
    bullet.forces.add(ThrustForce(1000, target.mulScalar(1f / target.len()).mulScalar(50000f))) // MAX SPEED OF ENGINE
    bullet.forces.add(LifeForce(10000)) // 10 seconds
    bullets.add(bullet)
    bullet.rotationAngle = InputStatus.pointVector.subImmut(spaceship.position).angle() - 90f
    engine.registerObject(bullet)
}

fun checkBullets(bullets: MutableList<Bullet>, engine: Engine) {
    for (bullet: Bullet in ArrayList(bullets))
        if (bullet.forces.isEmpty()) {
            bullets.remove(bullet)
            engine.unregisterObject(bullet)
        }
}