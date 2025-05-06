package meloncompanion.mixins;

import meloncompanion.ItemModelMagnet;
import meloncompanion.MelonCompanion;
import meloncompanion.interfaces.PlayerMagnetInterface;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixinMagnet implements PlayerMagnetInterface {

	@Shadow
	public ContainerInventory inventory;

	@Shadow
	public abstract ItemStack getHeldItem();

	@Unique
	boolean hasMagnet = false;

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci){
		if (MelonCompanion.magnetsEnabled.value) {
			ItemStack headItem = this.inventory.armorInventory[IArmorItem.PIECE_HEAD];
			ItemStack heldItem = this.getHeldItem();

			hasMagnet =
				//item on head check
				headItem != null &&
				headItem.getItem().equals(Items.AMMO_FIREBALL) &&
				headItem.getMetadata() == ItemModelMagnet.MAGNET ||
				//held item check
				heldItem != null &&
				heldItem.getItem().equals(Items.AMMO_FIREBALL) &&
				heldItem.getMetadata() == ItemModelMagnet.MAGNET;
		}
	}

	@Override
	public boolean hasMagnet() {
		return hasMagnet;
	}
}
