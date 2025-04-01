package mod.crend.perdimensionbrightness.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mod.crend.perdimensionbrightness.DimensionBrightnessOptions;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
	@ModifyReturnValue(method = "getGamma", at = @At("RETURN"))
	public SimpleOption<Double> getPerDimensionGamma(SimpleOption<Double> original) {
		return DimensionBrightnessOptions.getCurrent();
	}
}