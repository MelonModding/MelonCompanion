package meloncompanion;

import meloncompanion.interfaces.PlayerMagnetInterface;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelMagnet extends ItemModelStandard {
	protected IconCoordinate magnet = TextureRegistry.getTexture("meloncompanion:item/magnet");
	protected IconCoordinate magnet_activated = TextureRegistry.getTexture("meloncompanion:item/magnet_activated");
	public static final int MAGNET = 1;

	public ItemModelMagnet(Item item, String namespace) {
		super(item, namespace);
	}

	public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
		if(itemStack.getMetadata() == 1) {
			if(entity instanceof Player){
				if((PlayerMagnetInterface.class.cast(entity).hasMagnet())){
					return this.magnet_activated;
				}
			}
			return this.magnet;
		} else {
			return super.getIcon(entity, itemStack);
		}
	}
}
