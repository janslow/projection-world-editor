package com.jayanslow.projection.world.editor.views;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.vecmath.Vector2f;

import com.jayanslow.projection.world.editor.controller.EditorController;
import com.jayanslow.projection.world.models.FlatScreen;

public class FlatScreenFrame extends AbstractRealObjectFrame<FlatScreen> {

	private static final long	serialVersionUID	= 7133446306109535065L;

	private final JSpinner		spinnerScreenId;
	private final JSpinner		spinnerDimensionsX, spinnerDimensionsY;

	/**
	 * Create the frame.
	 * 
	 * @param controller
	 * 
	 * @wbp.parser.constructor
	 */
	private FlatScreenFrame(boolean create, EditorController controller, FlatScreen screen) {
		super(create, controller, screen, 1, 240);

		// Screen ID
		JLabel lblScreenId = new JLabel("Screen ID");
		panelProperties.add(lblScreenId, "2, 4");
		spinnerScreenId = createIdSpinner();
		panelProperties.add(spinnerScreenId, "4, 4, 3, 1");

		// Dimensions
		JLabel lblDimensions = new JLabel("Dimensions (mm)");
		panelProperties.add(lblDimensions, "2, 10");
		JLabel lblDimensionsX = new JLabel("X");
		panelProperties.add(lblDimensionsX, "4, 10, center, default");
		spinnerDimensionsX = createDimensionSpinner();
		panelProperties.add(spinnerDimensionsX, "6, 10");
		JLabel lblDimensionsY = new JLabel("Y");
		panelProperties.add(lblDimensionsY, "8, 10, center, default");
		spinnerDimensionsY = createDimensionSpinner();
		panelProperties.add(spinnerDimensionsY, "10, 10");

		setCreated(create);
	}

	public FlatScreenFrame(EditorController controller) {
		this(true, controller, null);
	}

	public FlatScreenFrame(EditorController controller, FlatScreen screen) {
		this(false, controller, screen);
	}

	@Override
	protected void bindObject(FlatScreen object) {
		spinnerScreenId.setValue(object.getScreenId());

		Vector2f dimensions = object.getDimensions();
		spinnerDimensionsX.setValue(dimensions.x);
		spinnerDimensionsY.setValue(dimensions.y);
	}

	@Override
	protected FlatScreen createObject(int id) {
		int screenId = getNumber(spinnerScreenId).intValue();
		return new FlatScreen(id, screenId);
	}

	@Override
	protected void saveObject(FlatScreen object) {
		object.setDimensions(new Vector2f(getNumber(spinnerDimensionsX).floatValue(), getNumber(spinnerDimensionsY)
				.floatValue()));
	}

	@Override
	protected void setCreated(boolean create) {
		super.setCreated(create);
		spinnerScreenId.setEnabled(create);
	}
}
