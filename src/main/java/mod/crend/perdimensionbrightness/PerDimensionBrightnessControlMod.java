package mod.crend.perdimensionbrightness;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class PerDimensionBrightnessControlMod implements ClientModInitializer {
	public static final String MOD_ID = "perdimensionbrightnesscontrol";
	public static final String CONFIG_FILE = MOD_ID + ".json";

	@Override
	public void onInitializeClient() {
		DimensionBrightnessOptions.init(FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE));
	}
}