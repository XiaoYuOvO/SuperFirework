package net.xiaoyu233.superfirework;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.util.Identifier;
import net.xiaoyu233.superfirework.component.SFComponents;
import net.xiaoyu233.superfirework.entity.SFEntityTypes;
import net.xiaoyu233.superfirework.item.SFItems;
import net.xiaoyu233.superfirework.particle.SFParticleTypes;
import org.slf4j.Logger;

import java.util.HashMap;

public final class Superfirework {

    public static final String MOD_ID = "superfirework";
    public static final RegistrarManager REGISTRAR_MANAGER = RegistrarManager.get(MOD_ID);
    public static void init() {
        // Write common init code here.
        SFComponents.registerComponents();
        SFEntityTypes.registerEntities();
        SFItems.registerItems();
        SFParticleTypes.registerParticle();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
