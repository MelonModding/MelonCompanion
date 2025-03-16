package meloncompanion;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelMagnet extends ItemModelStandard {
	protected IconCoordinate magnet = TextureRegistry.getTexture("meloncompanion:item/magnet");

	public ItemModelMagnet(Item item, String namespace) {
		super(item, namespace);
	}

	public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
		return itemStack.getMetadata() == 1 ? this.magnet : super.getIcon(entity, itemStack);
	}
}
