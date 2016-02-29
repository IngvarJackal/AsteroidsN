package asteroids.n.entities.forces

import asteroids.n.engine.Engine
import asteroids.n.engine.forces.EngineForce
import asteroids.n.engine.objects.EngineObject
import asteroids.n.entities.objects.Earth
import asteroids.n.entities.objects.Moon
import asteroids.n.entities.objects.PlayerShip
import asteroids.n.mulScalar
import asteroids.n.subImmut

object EarthGravityForce: EngineForce {
    override fun engineClone(): EngineForce {
        return EarthGravityForce
    }

    val G = 1e1f*2 // gravitational constant

    val MIN_FORCE = 0f*G
    val MAX_FORCE = 50f*G

    override fun applyAndDelete(obj: EngineObject, engine: Engine, timedelta: Float): Boolean {
        var distanceToTheMoon = Float.POSITIVE_INFINITY
        for (gravitySource in engine.staticObjects) {
            if (gravitySource is Moon)
                distanceToTheMoon = gravitySource.position.subImmut(obj.position).len()
        }
        for (gravitySource in engine.staticObjects) {
            if (gravitySource !is Earth)
                continue
            val normal = gravitySource.position.subImmut(obj.position)
            val distance = normal.len()
            var vevMult = 0f
            if (distanceToTheMoon <= MoonGravityForce.GRAVITY_RADIUS)
                vevMult = G * gravitySource.mass / distance / distance / distance // F = G * m2/R^3, m1 removed because a = F/m, R^3 to reduce gravity near the Moon
            else
                vevMult = G * gravitySource.mass / distance / distance // F = G * m2/R^2, m1 removed because a = F/m
            vevMult = Math.max(Math.min(MAX_FORCE, vevMult), MIN_FORCE) // for smooth gameplay let's limit speed
            obj.velocity.add(normal.mulScalar(vevMult/distance*timedelta))
        }
        return false;
    }
}