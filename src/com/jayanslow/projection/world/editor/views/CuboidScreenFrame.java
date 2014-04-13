package com.jayanslow.projection.world.editor.views;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.vecmath.Vector3f;

import com.jayanslow.projection.world.editor.controller.EditorController;
import com.jayanslow.projection.world.models.CuboidScreen;

public class CuboidScreenFrame extends AbstractRealObjectFrame<CuboidScreen> {

	private static final long	serialVersionUID	= 7133446306109535065L;

	private final JSpinner		spinnerScreenId;
	private final JSpinner		spinnerDimensionsX, spinnerDimensionsY, spinnerDimensionsZ;

	/**
	 * Create the frame.
	 * 
	 * @param controller
	 * 
	 * @wbp.parser.constructor
	 */
	private CuboidScreenFrame(boolean create, EditorController controller, CuboidScreen screen) {
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
		JLabel lblDimensionsZ = new JLabel("Z");
		panelProperties.add(lblDimensionsZ, "12, 10, center, default");
		spinnerDimensionsZ = createDimensionSpinner();
		panelProperties.add(spinnerDimensionsZ, "14, 10");

		setCreated(create);
	}

	public CuboidScreenFrame(EditorController controller) {
		this(true, controller, null);
	}

	public CuboidScreenFrame(EditorController controller, CuboidScreen screen) {
		this(false, controller, screen);
	}

	@Override
	protected void bindObject(CuboidScreen object) {
		spinnerScreenId.setValue(object.getScreenId());

		Vector3f dimensions = object.getDimensions();
		spinnerDimensionsX.setValue(dimensions.x);
		spinnerDimensionsY.setValue(dimensions.y);
		spinnerDimensionsZ.setValue(dimensions.z);
	}

	@Override
	protected CuboidScreen createObject(int id) {
		int screenId = getNumber(spinnerScreenId).intValue();
		return new CuboidScreen(id, screenId);
	}

	@Override
	protected void saveObject(CuboidScreen object) {
		object.setDimensions(new Vector3f(getNumber(spinnerDimensionsX).floatValue(), getNumber(spinnerDimensionsY)
				.floatValue(), getNumber(spinnerDimensionsZ).floatValue()));
	}

	@Override
	protected void setCreated(boolean create) {
		super.setCreated(create);
		spinnerScreenId.setEnabled(create);
	}
}
