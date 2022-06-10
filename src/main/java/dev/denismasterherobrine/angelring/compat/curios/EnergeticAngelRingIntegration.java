package dev.denismasterherobrine.angelring.compat.curios;

import dev.denismasterherobrine.angelring.config.Configuration;
import dev.denismasterherobrine.angelring.register.ItemRegistry;
import dev.denismasterherobrine.angelring.item.utils.EnergyItem;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergeticAngelRingIntegration {
    public static ICapabilityProvider initCapabilities(ItemStack stack) {
        ICurio curio = new AbstractRingCurio(ItemRegistry.EnergeticAngelRing) {
            private final int fePerTick = Configuration.EnergeticFEPerTick.get();

            @Override
            public ItemStack getStack() {
                return stack;
            }

            private IEnergyStorage getEnergyStorage(ItemStack stack) {
                return stack.getCapability(CapabilityEnergy.ENERGY).resolve().get();
            }

            @Override
            protected boolean checkIfAllowedToFly(Player player, ItemStack stack) {
                return getEnergyStorage(stack).getEnergyStored() > 1;
            }

            @Override
            protected TranslatableComponent getNotAbleToFlyMessage() {
                return new TranslatableComponent("item.angelring.energetic_angel_ring.not_enough_fe");
            }

            @Override
            protected void payForFlight(Player player, ItemStack stack) {
                getEnergyStorage(stack).extractEnergy(fePerTick, false);
            }
        };

        EnergyItem energyItem = new EnergyItem(stack, Configuration.EnergeticFECapacity.get());

        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);
            private final LazyOptional<IEnergyStorage> energyStorageOpt = LazyOptional.of(() -> energyItem);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                                     @Nullable Direction side) {
                if (cap == CapabilityEnergy.ENERGY) {
                    return energyStorageOpt.cast();
                } else {
                    return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
                }
            }
        };
    }

    public static boolean isRingInCuriosSlot(ItemStack angelRing, LivingEntity player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(angelRing.getItem(), player).isPresent();
    }
}
