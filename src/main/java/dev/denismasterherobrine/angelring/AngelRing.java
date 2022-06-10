package dev.denismasterherobrine.angelring;

import dev.denismasterherobrine.angelring.item.thermal.*;
import dev.denismasterherobrine.angelring.item.vanilla.*;
import dev.denismasterherobrine.angelring.utils.ExternalMods;
import dev.denismasterherobrine.angelring.config.Configuration;
import dev.denismasterherobrine.angelring.register.ItemRegistry;
import dev.denismasterherobrine.angelring.item.vanilla.ItemDiamondRing;
import dev.denismasterherobrine.angelring.item.vanilla.ItemRing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("angelring")
public class AngelRing {
    public static final String MODID = "angelring";
    private static final Logger LOGGER = LogManager.getLogger();

    public AngelRing() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        Configuration.loadConfig(Configuration.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("angelring2.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        if (ExternalMods.TINKERSCONSTRUCT.isLoaded()) {
            LOGGER.warn("Tinkers' Construct is loaded! Slime Boots doesn't work with Angel Ring at the moment.");
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void RegisterItems(final RegistryEvent.Register<Item> event) {
            // Vanilla Angel Ring Tiers
            event.getRegistry().register(ItemRegistry.ItemRing = new ItemRing().setRegistryName(location("itemring")));
            event.getRegistry().register(ItemRegistry.ItemDiamondRing = new ItemDiamondRing().setRegistryName(location("itemdiamondring")));
            event.getRegistry().register(ItemRegistry.EnergeticAngelRing = new EnergeticAngelRing().setRegistryName(location("energetic_angel_ring")));

            // Thermal Expansion's Angel Ring Tiers
            event.getRegistry().register(ItemRegistry.LeadstoneAngelRing = new LeadstoneAngelRing().setRegistryName(location("leadstone_angel_ring")));
            event.getRegistry().register(ItemRegistry.HardenedAngelRing = new HardenedAngelRing().setRegistryName(location("hardened_angel_ring")));
            event.getRegistry().register(ItemRegistry.ReinforcedAngelRing = new ReinforcedAngelRing().setRegistryName(location("reinforced_angel_ring")));
            event.getRegistry().register(ItemRegistry.ResonantAngelRing = new ResonantAngelRing().setRegistryName(location("resonant_angel_ring")));
        }

        private static ResourceLocation location(String name) {
            return new ResourceLocation(MODID, name);
        }
    }
}
