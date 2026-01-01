package net.xiaoyu233.superfirework.particle.explosions;

import net.minecraft.util.math.MathHelper;
import net.xiaoyu233.superfirework.component.explosions.RandomBallComponent;
import net.xiaoyu233.superfirework.particle.ExplosionType;
import net.xiaoyu233.superfirework.particle.ParticleConfig;

public class RandomBallExplosion extends FireworkExplosion<RandomBallComponent> {

    public RandomBallExplosion(ExplosionType<RandomBallComponent, ? extends FireworkExplosion<RandomBallComponent>> type, RandomBallComponent component) {
        super(type, component);
    }

    @Override
    public void spawnExplosionParticles(double speed, int size, double x, double y, double z, ParticleConfig config) {

        for (int i = -size; i <= size; i++) {

            for (int j = -size; j <= size; j++) {

                for (int k = -size; k <= size; k++) {
                    double randSpeed = random.nextInt(6) / 5d * this.component.randomFactor();
                    //The larger the value, the mess the generated particles be
                    double randomizeAmount = 0.5D;
                    double d3 = ((double) j + (this.random.nextDouble() - this.random.nextDouble()) * randomizeAmount);
                    double d4 = ((double) i + (this.random.nextDouble() - this.random.nextDouble()) * randomizeAmount);
                    double d5 = ((double) k + (this.random.nextDouble() - this.random.nextDouble()) * randomizeAmount);
                    double d6 = (double) MathHelper.sqrt((float) (d3 * d3 + d4 * d4 + d5 * d5)) / (speed * randSpeed) + this.random.nextGaussian() * 0.05d;
                    this.createParticle(speed, size, x, y, z, d3 / d6, d4 / d6, d5 / d6, config);
                    if (i != -size && i != size && j != -size && j != size) {
                        k += size * 2 - 1;
                    }
                }
            }
        }
    }
}
