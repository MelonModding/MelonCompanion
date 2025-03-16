package meloncompanion.mixins;

import meloncompanion.MelonCompanion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(value = GameSettings.class, remap = false)
public abstract class GameSettingsMixin {

	@Unique
	public OptionBoolean elevatorsEnabled;

	@Unique
	public OptionBoolean elevatorsAllowObstructions;

	@Unique
	public OptionRange elevatorsCooldown;

	@Unique
	public OptionBoolean magnetsEnabled;

	@Inject(method = "<init>", at = @At(value = "NEW", target = "(Ljava/io/File;Ljava/lang/String;)Ljava/io/File;"))
	public void addOptions(Minecraft minecraft, File file, CallbackInfo ci){
		MelonCompanion.initOptions((GameSettings) (Object)this);
		this.elevatorsEnabled = MelonCompanion.elevatorsEnabled;
		this.elevatorsAllowObstructions = MelonCompanion.elevatorAllowObstructions;
		this.elevatorsCooldown = MelonCompanion.elevatorCooldown;
		this.magnetsEnabled = MelonCompanion.magnetsEnabled;
	}
}
