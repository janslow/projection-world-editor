package com.jayanslow.projection.world.editor.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector3f;

import com.jayanslow.projection.world.editor.controller.EditorController;
import com.jayanslow.projection.world.models.RealObject;
import com.jayanslow.projection.world.models.Rotation3f;
import com.jayanslow.projection.world.models.Universe;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public abstract class AbstractRealObjectFrame<T extends RealObject> extends JFrame implements ChangeListener,
		ActionListener {

	private static final long			serialVersionUID	= -5931960754984361236L;

	private static final String			COMMAND_SAVE		= "save";
	private static final String			COMMAND_LIVE		= "live";

	protected final JPanel				panelProperties;
	private final JPanel				contentPane;
	protected final EditorController	controller;

	private boolean						create;
	private boolean						live;

	private T							object;

	private final JToggleButton			tglbtnLive;
	private final JButton				btnSave;

	private final JSpinner				spinnerId;
	private final JSpinner				spinnerRotationPan, spinnerRotationTilt, spinnerRotationRoll;
	private final JSpinner				spinnerPositionX, spinnerPositionY, spinnerPositionZ;

	/**
	 * Constructs a new AbstractRealObjectFrame
	 * 
	 * @param create
	 *            True if frame should be in create mode (adding a new object), otherwise false
	 * @param controller
	 *            EditorController to call for changes
	 * @param object
	 *            Object to edit (or null if create == true)
	 * @param extraRows
	 *            Number of extra rows after rotation (excluding dividers)
	 */
	public AbstractRealObjectFrame(boolean create, EditorController controller, T object, int extraRows, int height) {
		this.controller = controller;
		this.object = object;

		setBounds(100, 100, 700, height);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panelProperties = new JPanel();
		panelProperties.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(panelProperties);

		ColumnSpec[] columnSpecs = new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC };
		RowSpec[] rowSpecs = new RowSpec[9 + 2 * extraRows];
		rowSpecs[0] = rowSpecs[2] = rowSpecs[4] = rowSpecs[6] = rowSpecs[8] = FormFactory.RELATED_GAP_ROWSPEC;
		rowSpecs[1] = rowSpecs[3] = rowSpecs[5] = rowSpecs[7] = FormFactory.DEFAULT_ROWSPEC;
		for (int i = 9; i < rowSpecs.length; i += 2) {
			rowSpecs[i] = FormFactory.DEFAULT_ROWSPEC;
			rowSpecs[i + 1] = FormFactory.RELATED_GAP_ROWSPEC;
		}
		rowSpecs[rowSpecs.length - 2] = RowSpec.decode("default:grow");
		panelProperties.setLayout(new FormLayout(columnSpecs, rowSpecs));

		// ID
		createLabel("ID", "2, 2");
		spinnerId = createIdSpinner();
		panelProperties.add(spinnerId, "4, 2, 3, 1");

		// Position
		createLabel("Position (mm)", "2, 6");
		createLabel("X", "4, 6, center, default");
		spinnerPositionX = createDimensionSpinner();
		panelProperties.add(spinnerPositionX, "6, 6");
		createLabel("Y", "8, 6, center, default");
		spinnerPositionY = createDimensionSpinner();
		panelProperties.add(spinnerPositionY, "10, 6");
		createLabel("Z", "12, 6, center, default");
		spinnerPositionZ = createDimensionSpinner();
		panelProperties.add(spinnerPositionZ, "14, 6");

		// Rotation
		createLabel("Rotation (degrees)", "2, 8");
		createLabel("Pan", "4, 8, center, default");
		spinnerRotationPan = createAngleSpinner();
		panelProperties.add(spinnerRotationPan, "6, 8");
		createLabel("Tilt", "8, 8, center, default");
		spinnerRotationTilt = createAngleSpinner();
		panelProperties.add(spinnerRotationTilt, "10, 8");
		createLabel("Roll", "12, 8, center, default");
		spinnerRotationRoll = createAngleSpinner();
		panelProperties.add(spinnerRotationRoll, "14, 8");

		JPanel panelCommands = new JPanel();
		contentPane.add(panelCommands, BorderLayout.SOUTH);

		tglbtnLive = new JToggleButton("Live Save");
		tglbtnLive.setActionCommand(COMMAND_LIVE);
		tglbtnLive.addActionListener(this);
		panelCommands.add(tglbtnLive);

		btnSave = new JButton("Save");
		btnSave.setActionCommand(COMMAND_SAVE);
		btnSave.addActionListener(this);
		panelCommands.add(btnSave);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(COMMAND_LIVE)) {
			live = tglbtnLive.isSelected();
			btnSave.setEnabled(!live);
			if (live)
				save();
		} else if (actionCommand.equals(COMMAND_SAVE)) {
			if (create) {
				int id = getNumber(spinnerId).intValue();
				object = createObject(id);
			}
			save();
			if (create) {
				controller.save(object);
				bind();
				bindObject(object);
				setCreated(false);
			}
		}
	}

	protected void bind() {
		boolean tempLive = this.live;
		this.live = false;

		spinnerId.setValue(object.getId());

		Vector3f position = object.getPosition();
		spinnerPositionX.setValue(position.x);
		spinnerPositionY.setValue(position.y);
		spinnerPositionZ.setValue(position.z);

		Rotation3f rotation = object.getRotation().toDegrees();
		spinnerRotationTilt.setValue(rotation.x);
		spinnerRotationPan.setValue(rotation.y);
		spinnerRotationRoll.setValue(rotation.z);

		bindObject(object);

		this.live = tempLive;
	}

	protected abstract void bindObject(T object);

	protected JSpinner createAngleSpinner() {
		return createSpinner(0, -360, 360, 10);
	}

	protected JSpinner createDimensionSpinner() {
		return createSpinner(0, 0, Universe.MAX_DIMENSION, 100);
	}

	protected JSpinner createIdSpinner() {
		return createSpinner(0, 0, 1000, 1);
	}

	protected JLabel createLabel(String label, String position) {
		JLabel lbl = new JLabel(label);
		panelProperties.add(lbl, position);
		return lbl;
	}

	protected abstract T createObject(int id);

	protected JSpinner createSpinner(double value, double min, double max, double step) {
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
		spinner.addChangeListener(this);
		return spinner;
	}

	protected Number getNumber(JSpinner spinner) {
		return ((SpinnerNumberModel) (spinner.getModel())).getNumber();
	}

	private void save() {
		if (object == null)
			return;
		object.setPosition(new Vector3f(getNumber(spinnerPositionX).floatValue(), getNumber(spinnerPositionY)
				.floatValue(), getNumber(spinnerPositionZ).floatValue()));
		object.setRotation(new Rotation3f(getNumber(spinnerRotationTilt).floatValue(), getNumber(spinnerRotationPan)
				.floatValue(), getNumber(spinnerRotationRoll).floatValue()).toRadians());
		saveObject(object);
		controller.markChanged(object);
	}

	protected abstract void saveObject(T object);

	protected void setCreated(boolean create) {
		this.create = create;
		this.live = !create;
		spinnerId.setEnabled(create);
		tglbtnLive.setSelected(!create);
		tglbtnLive.setEnabled(!create);
		btnSave.setEnabled(create);

		if (!create)
			bind();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (live)
			save();
	}

}
