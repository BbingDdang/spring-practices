package com.poscodx.container.config.videosystem.mixing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poscodx.container.videosystem.Avengers;
import com.poscodx.container.videosystem.BlankDisc;
import com.poscodx.container.videosystem.DigitalVideoDisc;

@Configuration
public class DVDConfig {
	@Bean
	public DigitalVideoDisc avengers() {
		return new Avengers();
	}
	
	@Bean
	public DigitalVideoDisc avengersInfinityWar)) {
		BlankDisc dvd = new BlankDisc();
		dvd.setTitle("Avengers Infinity war");
		dvd.setStudio("Marvel");
		return dvd;
	}
	
}
