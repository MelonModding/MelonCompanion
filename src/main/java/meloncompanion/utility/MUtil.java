package meloncompanion.utility;

import meloncompanion.MelonCompanion;
import meloncompanion.interfaces.PlayerMagnetInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.achievement.Achievements;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class MUtil {

	public static @Nullable Player closestPlayerToEntity(World world, Entity entity){
		Player closestPlayer = null;
		float closestDistance = Float.MAX_VALUE;
		for(Player player : world.players){
			float distanceTo = player.distanceTo(entity);
			if(distanceTo < closestDistance){
				closestPlayer = player;
				closestDistance = distanceTo;
			}
		}
		return closestPlayer;
	}

	public static @Nullable Player closestPlayerWithMagnetToItem(World world, Entity entity){
		Player closestPlayer = null;
		float closestDistance = Float.MAX_VALUE;
		for(Player player : world.players){
			if(!(PlayerMagnetInterface.class.cast(player).hasMagnet())){
				continue;
			}
			float distanceTo = player.distanceTo(entity);
			if(distanceTo < closestDistance){
				closestPlayer = player;
				closestDistance = distanceTo;
			}
		}
		return closestPlayer;
	}

	public static String hmsConversion(long systemTimeMillis) {

		Duration duration = Duration.ofMillis(systemTimeMillis);

		long h = duration.toHours();
		long m = duration.toMinutes() % 60;
		long s = duration.getSeconds() % 60;

		return String.format("%02d:%02d:%02d [h:m:s]", h, m, s);
	}

	//TODO simplify
	public static String formatHexString(String dirtyHex){
		StringBuilder output = new StringBuilder();
		output.append("§<");
		char[] charArray = dirtyHex.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			if (i < 6 && Character.isDigit(c)) {
				output.append(c);
			} else {
				break;
			}
		}
		output.append(">");
		return output.toString();
	}

	public static void teleport(double x, double y, double z, @NotNull Player player, @NotNull Dimension dimension){

		assert player.world != null;
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
		if (MelonCompanion.isServer){
			if(player.dimension != dimension.id){
				MinecraftServer mc = MinecraftServer.getInstance();
				mc.playerList.sendPlayerToOtherDimension((PlayerServer) player, dimension.id, DyeColor.WHITE, false);
			}
			((PlayerServer) player).playerNetServerHandler.teleport(x, y + 0.2, z);
		} else {
			if(player.dimension != dimension.id){
				teleportToDimension(dimension);
			}
			player.setPos(x, y + player.bbHeight + 0.2, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);

	}

	public static void teleport(double x, double y, double z, @NotNull Player player){
		assert player.world != null;
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
		if (MelonCompanion.isServer){
			((PlayerServer) player).playerNetServerHandler.teleport(x, y + 0.2, z);
		} else {
			player.setPos(x, y + player.bbHeight + 0.2, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 2f);
	}

	public static void teleportToDimension(Dimension dimension) {
		Minecraft mc = Minecraft.getMinecraft();
		Dimension lastDim = Dimension.getDimensionList().get(mc.thePlayer.dimension);
		Minecraft.LOGGER.info("Switching to dimension \"{}\"!!", dimension.getTranslatedName());
		mc.thePlayer.dimension = dimension.id;
		mc.currentWorld.setEntityDead(mc.thePlayer);
		mc.thePlayer.removed = false;
		double x = mc.thePlayer.x;
		double z = mc.thePlayer.z;
		double y = mc.thePlayer.y;
		x *= Dimension.getCoordScale(lastDim, dimension);
		z *= Dimension.getCoordScale(lastDim, dimension);
		mc.thePlayer.moveTo(x, y, z, mc.thePlayer.yRot, mc.thePlayer.xRot);
		ChunkCoordinates newCoordinates = new ChunkCoordinates(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
		if (mc.thePlayer.isAlive() && mc.thePlayer.dimensionEnterCoordinate != null) {
			double dx = (double)mc.thePlayer.dimensionEnterCoordinate.x - x;
			double dy = (double)mc.thePlayer.dimensionEnterCoordinate.y - y;
			double dz = (double)mc.thePlayer.dimensionEnterCoordinate.z - z;
			double distSqr = dx * dx + dy * dy + dz * dz;
			if (distSqr > 6.4E7) {
				mc.thePlayer.addStat(Achievements.FAST_TRAVEL, 1);
			}
		}

		mc.thePlayer.dimensionEnterCoordinate = newCoordinates;
		if (mc.thePlayer.isAlive()) {
			mc.currentWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
		}

		WorldClient world = new WorldClient(mc.currentWorld, dimension);
		I18n i18n = I18n.getInstance();
		if (dimension == lastDim.homeDim) {
			mc.changeWorld(world, i18n.translateKeyAndFormat("gui.loading.label.leaving", lastDim.getTranslatedName()), mc.thePlayer);
		} else {
			mc.changeWorld(world, i18n.translateKeyAndFormat("gui.loading.label.entering", dimension.getTranslatedName()), mc.thePlayer);
		}

		mc.thePlayer.world = mc.currentWorld;
		if (mc.thePlayer.isAlive()) {
			mc.thePlayer.moveTo(x, y, z, mc.thePlayer.yRot, mc.thePlayer.xRot);
			mc.currentWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
		}
	}

	public static boolean jumpOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y+1; y2 < 255; y2++){
			if(world.getBlock(x, y2, z) == Blocks.BLOCK_STEEL && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !MelonCompanion.elevatorAllowObstructions.value) {
				break;
			}
		}
		return false;
	}

	// returns true if we teleported
	public static boolean sneakOnElevator(World world, int x, int y, int z, Player player){
		for(int y2 = y-1; y2 > 0; y2--){
			if(world.getBlock(x, y2, z) == Blocks.BLOCK_STEEL && !Blocks.solid[world.getBlockId(x, y2+1, z)] && !Blocks.solid[world.getBlockId(x, y2+2, z)]){
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !MelonCompanion.elevatorAllowObstructions.value) {
				break;
			}
		}
		return false;
	}

}
