package com.jayanslow.projection.world.editor.controller;

import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.editor.views.CuboidScreenFrame;
import com.jayanslow.projection.world.editor.views.FlatScreenFrame;
import com.jayanslow.projection.world.editor.views.StandardProjectorFrame;
import com.jayanslow.projection.world.editor.views.UniverseFrame;
import com.jayanslow.projection.world.models.CuboidScreen;
import com.jayanslow.projection.world.models.FlatScreen;
import com.jayanslow.projection.world.models.RealObject;
import com.jayanslow.projection.world.models.StandardProjector;

public class StandardWorldEditorController implements WorldEditorController {
	private final List<UniverseListener>	listeners	= new LinkedList<>();
	private final WorldController			world;

	public StandardWorldEditorController(WorldController world) {
		super();
		this.world = world;
	}

	@Override
	public void addUniverseListener(UniverseListener l) {
		listeners.add(l);
	}

	@Override
	public <T extends RealObject> void create(Class<T> type) {
		if (type.equals(StandardProjector.class))
			showFrame(new StandardProjectorFrame(this));
		else if (type.equals(FlatScreen.class))
			showFrame(new FlatScreenFrame(this));
		else if (type.equals(CuboidScreen.class))
			showFrame(new CuboidScreenFrame(this));
	}

	@Override
	public void edit(RealObject o) {
		if (o.getClass().equals(StandardProjector.class))
			showFrame(new StandardProjectorFrame(this, (StandardProjector) o));
		else if (o.getClass().equals(FlatScreen.class))
			showFrame(new FlatScreenFrame(this, (FlatScreen) o));
		else if (o.getClass().equals(CuboidScreen.class))
			showFrame(new CuboidScreenFrame(this, (CuboidScreen) o));

	}

	@Override
	public void editUniverse() {
		UniverseFrame frame = new UniverseFrame(this, world.getUniverse());
		showFrame(frame);
		addUniverseListener(frame);
	}

	@Override
	public WorldController getWorld() {
		return world;
	}

	@Override
	public void markChanged(RealObject o) {
		for (UniverseListener l : listeners)
			if (l != null)
				l.onUpdate(world, o);
	}

	@Override
	public void remove(RealObject o) {
		world.remove(o);
		markChanged(o);
	}

	@Override
	public void save(RealObject n) {
		world.add(n);
		markChanged(n);
	}

	private void showFrame(Frame frame) {
		frame.setVisible(true);
	}

}
