package com.obqy;

import com.obqy.biomes.BiomeNotification;
import com.obqy.biomes.BiomeTracker;
import com.obqy.biomes.effects.BiomeParticleEffect;
import com.obqy.config.BiomeConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravellersTitles implements ClientModInitializer {
	public static final String MOD_ID = "travellers-titles";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initialising!");

		BiomeConfig.load();

		BiomeTracker.register();
		BiomeNotification.register();
		BiomeParticleEffect.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}