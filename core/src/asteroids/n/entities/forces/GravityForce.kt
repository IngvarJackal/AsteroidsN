package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.mulScalar
import asteroids.n.subImmut

object GravityForce: EngineForce {
    val G = 1e-2f*2 // gravitational constant

    val MIN_FORCE = 0.001f*G
    val MAX_FORCE = 50f*G

    override fun apply(obj: EngineObject, engine: Engine) {
        for (gravitySource in engine.staticObjects) {
            val normal = gravitySource.position.subImmut(obj.position)
            val distance = normal.len()
            var vevMult = G * gravitySource.mass / distance / distance // F = G * m2/R^2, m1 removed because a = F/m
            vevMult = Math.max(Math.min(MAX_FORCE, vevMult), MIN_FORCE) // for smooth gameplay let's limit speed
            obj.velocity.add(normal.mulScalar(vevMult/distance*engine.msDelay))
        }
    }
}