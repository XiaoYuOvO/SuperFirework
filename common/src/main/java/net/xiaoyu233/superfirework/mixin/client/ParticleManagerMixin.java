package net.xiaoyu233.superfirework.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @ModifyExpressionValue(method = "method_18125", at = @At(value = "CONSTANT", args = "intValue=16384"))
    private static int modifyParticleMax(int original){
        return 32768;
    }
}
