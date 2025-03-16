package meloncompanion;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionBoolean;
import net.minecraft.client.option.OptionRange;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.io.IOException;
import java.net.URISyntaxException;


public class MelonCompanion implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, ClientStartEntrypoint {

	public static final boolean isServer = FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	public static final String MOD_ID = "meloncompanion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static OptionsPage MelonCompanionOptions;

	public static OptionBoolean elevatorsEnabled;
	public static OptionBoolean elevatorAllowObstructions;
	public static OptionRange elevatorCooldown;

	public static OptionBoolean magnetsEnabled;

	public static void registerCommands(){
	}

	@Override
	public void onInitialize() {
		LOGGER.info("MelonCompanion initializing!");
		LOGGER.info("MelonCompanion initialized!");
	}

	public static void initOptions(GameSettings settings){
		elevatorsEnabled = new OptionBoolean(settings, "meloncompanion.category.elevators.enabled", true);
		elevatorAllowObstructions = new OptionBoolean(settings, "meloncompanion.category.elevators.allowObstructions", true);
		elevatorCooldown = new OptionRange(settings, "meloncompanion.category.elevators.cooldown", 8, 0, 20);
		magnetsEnabled = new OptionBoolean(settings, "meloncompanion.category.magnets.enabled", true);
	}

	public static void afterServerStart(){

	}

	public static void info(String s) {
		LOGGER.info(s);
	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		MelonCompanionOptions =
			new OptionsPage("options.meloncompanion.title", new ItemStack(Items.OLIVINE))
				.withComponent(new OptionsCategory("options.meloncompanion.category.elevators")
					.withComponent(new BooleanOptionComponent(elevatorsEnabled))
					.withComponent(new BooleanOptionComponent(elevatorAllowObstructions))
					.withComponent(new ToggleableOptionComponent<>(elevatorCooldown))
				)
				.withComponent(new OptionsCategory("options.meloncompanion.category.magnets")
					.withComponent(new BooleanOptionComponent(magnetsEnabled)
				)
			);

		OptionsPages.register(MelonCompanionOptions);
	}

	@Override
	public void initNamespaces() {
		RecipeBuilder.initNameSpace(MOD_ID);
	}

	@Override
	public void onRecipesReady() {

		ItemStack magnet = new ItemStack(Items.AMMO_FIREBALL, 1, 1);
		magnet.setCustomName("Magnet");

		RecipeBuilder.Shaped(MOD_ID)
			.setShape(
				"ISR",
				"S  ",
				"ISL")
			.addInput('R', Items.DUST_REDSTONE)
			.addInput('L', Items.DYE, DyeColor.BLUE.itemMeta)
			.addInput('S', Items.INGOT_STEEL)
			.addInput('I', Items.INGOT_IRON)
			.create("magnet", magnet);
		RecipeBuilder.Shaped(MOD_ID)
			.setShape(
				"ISL",
				"S  ",
				"ISR")
			.addInput('R', Items.DUST_REDSTONE)
			.addInput('L', Items.DYE, DyeColor.BLUE.itemMeta)
			.addInput('S', Items.INGOT_STEEL)
			.addInput('I', Items.INGOT_IRON)
			.create("magnet", magnet);
		RecipeBuilder.Shaped(MOD_ID)
			.setShape(
				"R L",
				"S S",
				"ISI")
			.addInput('R', Items.DUST_REDSTONE)
			.addInput('L', Items.DYE, DyeColor.BLUE.itemMeta)
			.addInput('S', Items.INGOT_STEEL)
			.addInput('I', Items.INGOT_IRON)
			.create("magnet", magnet);
	}


	@Override
	public void beforeClientStart() {
		try {
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.itemAtlas, true);
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterClientStart() {
		ModelHelper.itemModelDispatcher.addDispatch(new ItemModelMagnet(Items.AMMO_FIREBALL, "minecraft"));
	}
}
