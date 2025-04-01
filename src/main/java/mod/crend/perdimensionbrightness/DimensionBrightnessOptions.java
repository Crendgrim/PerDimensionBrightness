package mod.crend.perdimensionbrightness;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.client.option.GameOptions.getGenericValueText;

public class DimensionBrightnessOptions {

	public static Map<RegistryKey<World>, SimpleOption<Double>> BRIGHTNESS_PER_DIMENSION = new HashMap<>();

	static Path CONFIG_FILE;

	public static void init(Path configFile) {
		CONFIG_FILE = configFile;
		register(World.OVERWORLD);
		register(World.NETHER);
		register(World.END);
		load();
		save();
	}

	public static void register(RegistryKey<World> world) {
		BRIGHTNESS_PER_DIMENSION.put(world, new SimpleOption<>("options.gamma", SimpleOption.emptyTooltip(), (optionText, value) -> {
			int i = (int)(value * 100.0);
			if (i == 0) {
				return getGenericValueText(optionText, Text.translatable("options.gamma.min"));
			} else if (i == 50) {
				return getGenericValueText(optionText, Text.translatable("options.gamma.default"));
			} else {
				return i == 100 ? getGenericValueText(optionText, Text.translatable("options.gamma.max")) : getGenericValueText(optionText, i);
			}
		}, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.5, value -> {
			save();
		}));
	}

	public static SimpleOption<Double> getCurrent() {
		if (MinecraftClient.getInstance().world == null) return BRIGHTNESS_PER_DIMENSION.get(World.OVERWORLD);
		return BRIGHTNESS_PER_DIMENSION.get(MinecraftClient.getInstance().world.getRegistryKey());
	}

	public static void load() {
		try {
			Type type = new TypeToken<Map<String, Double>>() {}.getType();
			Map<String, Double> config = new GsonBuilder().create().fromJson(Files.readString(CONFIG_FILE), type);
			for (String identifier : config.keySet()) {
				RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(identifier));
				BRIGHTNESS_PER_DIMENSION.get(registryKey).setValue(config.get(identifier));
			}
		} catch (IOException ignored) {
		}
	}
	public static void save() {
		Map<String, Double> config = new HashMap<>();
		for (RegistryKey<World> registryKey : BRIGHTNESS_PER_DIMENSION.keySet()) {
			config.put(registryKey.getValue().toString(), BRIGHTNESS_PER_DIMENSION.get(registryKey).getValue());
		}
		try {
			Files.writeString(CONFIG_FILE, new GsonBuilder().setPrettyPrinting().create().toJson(config));
		} catch (IOException ignored) {
		}
	}
}
