package com.jayanslow.projection.world.editor.views;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.vecmath.Vector3f;

import com.jayanslow.projection.world.editor.controller.EditorController;
import com.jayanslow.projection.world.models.StandardProjector;

public class StandardProjectorFrame extends AbstractRealObjectFrame<StandardProjector> {

	private static final long	serialVersionUID	= 7133446306109535065L;

	private final JSpinner		spinnerProjectorId;
	private final JSpinner		spinnerDimensionsX, spinnerDimensionsY, spinnerDimensionsZ;
	private final JSpinner		spinnerResolutionWidth, spinnerResolutionHeight;
	private final JSpinner		spinnerThrowRatio;

	private StandardProjectorFrame(boolean create, EditorController controller, StandardProjector screen) {
		super(create, controller, screen, 3, 300);

		// Screen ID
		JLabel lblScreenId = new JLabel("Projector ID");
		panelProperties.add(lblScreenId, "2, 4");
		spinnerProjectorId = createIdSpinner();
		panelProperties.add(spinnerProjectorId, "4, 4, 3, 1");

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

		JLabel lblResolution = new JLabel("Resolution (pixels)");
		panelProperties.add(lblResolution, "2, 12");
		JLabel lblResolutionHeight = new JLabel("Height");
		panelProperties.add(lblResolutionHeight, "4, 12, center, default");
		spinnerResolutionHeight = createResolutionSpinner();
		panelProperties.add(spinnerResolutionHeight, "6, 12");
		JLabel lblResolutionWidth = new JLabel("Width");
		panelProperties.add(lblResolutionWidth, "8, 12, center, default");
		spinnerResolutionWidth = createResolutionSpinner();
		panelProperties.add(spinnerResolutionWidth, "10, 12");

		JLabel lblThrowRatio = new JLabel("Throw Ratio");
		panelProperties.add(lblThrowRatio, "2, 14");
		spinnerThrowRatio = createSpinner(1, 0.1, 100, 0.1);
		panelProperties.add(spinnerThrowRatio, "4, 14, 3, 1");

		setCreated(create);
	}

	public StandardProjectorFrame(EditorController controller) {
		this(true, controller, null);
	}

	public StandardProjectorFrame(EditorController controller, StandardProjector screen) {
		this(false, controller, screen);
	}

	@Override
	protected void bindObject(StandardProjector object) {
		spinnerProjectorId.setValue(object.getProjectorId());

		Vector3f dimensions = object.getDimensions();
		spinnerDimensionsX.setValue(dimensions.x);
		spinnerDimensionsY.setValue(dimensions.y);
		spinnerDimensionsZ.setValue(dimensions.z);

		spinnerResolutionHeight.setValue(object.getResolutionHeight());
		spinnerResolutionWidth.setValue(object.getResolutionWidth());

		spinnerThrowRatio.setValue(object.getThrowRatio());
	}

	@Override
	protected StandardProjector createObject(int id) {
		int projectorId = getNumber(spinnerProjectorId).intValue();
		return new StandardProjector(id, projectorId);
	}

	protected JSpinner createResolutionSpinner() {
		return createSpinner(1, 1, 10000, 1);
	}

	@Override
	protected void saveObject(StandardProjector object) {
		object.setDimensions(new Vector3f(getNumber(spinnerDimensionsX).floatValue(), getNumber(spinnerDimensionsY)
				.floatValue(), getNumber(spinnerDimensionsZ).floatValue()));
		object.setResolution(getNumber(spinnerResolutionHeight).intValue(), getNumber(spinnerResolutionWidth)
				.intValue());
		object.setThrowRatio(getNumber(spinnerThrowRatio).floatValue());
	}

	@Override
	protected void setCreated(boolean create) {
		super.setCreated(create);
		spinnerProjectorId.setEnabled(create);
	}
}
