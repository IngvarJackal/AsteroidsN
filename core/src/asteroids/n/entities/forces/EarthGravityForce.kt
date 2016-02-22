package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.entities.objects.Earth
import asteroids.n.mulScalar
import asteroids.n.subImmut

object EarthGravityForce: EngineForce {
    val G = 1e-2f*2 // gravitational constant

    val MIN_FORCE = 0f*G
    val MAX_FORCE = 50f*G

    val GRAVITY_RADIUS = 270 // px

    override fun apply(obj: EngineObject, engine: Engine) {
        for (gravitySource in engine.staticObjects) {
            if (gravitySource !is Earth)
                continue
            val normal = gravitySource.position.subImmut(obj.position)
            val distance = normal.len()
            var vevMult = 0f
            if (distance <= GRAVITY_RADIUS)
                vevMult = G * gravitySource.mass / distance / distance // F = G * m2/R^2, m1 removed because a = F/m
            else
                vevMult = G * gravitySource.mass / distance / distance / distance // F = G * m2/R^3, m1 removed because a = F/m, R^3 to reduce gravity near the Moon
            vevMult = Math.max(Math.min(MAX_FORCE, vevMult), MIN_FORCE) // for smooth gameplay let's limit speed
            obj.velocity.add(normal.mulScalar(vevMult/distance*engine.msDelay))
        }
    }
}