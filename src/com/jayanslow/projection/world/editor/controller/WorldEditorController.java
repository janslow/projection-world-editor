package com.jayanslow.projection.world.editor.controller;

import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.models.RealObject;

public interface WorldEditorController {
	public void addUniverseListener(UniverseListener l);

	/**
	 * Creates a new object
	 * 
	 * @param type
	 */
	public <T extends RealObject> void create(Class<T> type);

	/**
	 * Edits an existing object
	 * 
	 * @param o
	 *            Existing object
	 */
	public void edit(RealObject o);

	/**
	 * Edits the universe
	 */
	public void editUniverse();

	public WorldController getWorld();

	/**
	 * Call to identify all listeners that the universe has changed
	 */
	public void markChanged(RealObject o);

	/**
	 * Removes an existing object from the world
	 * 
	 * @param o
	 *            Object to remove
	 */
	public void remove(RealObject o);

	/**
	 * Saves a newly created object to the world
	 * 
	 * @param n
	 *            Object to save New Object
	 */
	public void save(RealObject n);
}
