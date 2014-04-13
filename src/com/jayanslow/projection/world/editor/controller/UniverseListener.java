package com.jayanslow.projection.world.editor.controller;

import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.models.RealObject;

public interface UniverseListener {
	/**
	 * Called when the world is updated
	 * 
	 * @param world
	 *            World which has been updated
	 * @param child
	 *            Child object which has been updated, or null if it is a property of the universe
	 */
	public void onUpdate(WorldController world, RealObject child);
}
