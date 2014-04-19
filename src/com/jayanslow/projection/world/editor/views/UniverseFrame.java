package com.jayanslow.projection.world.editor.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.jayanslow.projection.world.controllers.HashMapWorldController;
import com.jayanslow.projection.world.controllers.WorldController;
import com.jayanslow.projection.world.editor.controller.EditorController;
import com.jayanslow.projection.world.editor.controller.UniverseListener;
import com.jayanslow.projection.world.editor.models.ChildTableModel;
import com.jayanslow.projection.world.models.CuboidScreen;
import com.jayanslow.projection.world.models.CuboidUniverse;
import com.jayanslow.projection.world.models.FlatScreen;
import com.jayanslow.projection.world.models.RealObject;
import com.jayanslow.projection.world.models.Rotation3f;
import com.jayanslow.projection.world.models.StandardProjector;
import com.jayanslow.projection.world.models.Universe;

public class UniverseFrame extends JFrame implements ActionListener, ChangeListener, UniverseListener {
	private static final long	serialVersionUID	= 2880634970914328031L;

	private static final String	COMMAND_ADD			= "add_screen";
	private static final String	COMMAND_REMOVE		= "remove";
	private static final String	COMMAND_EDIT		= "edit";
	private static final String	COMMAND_REFRESH		= "refresh";

	/**
	 * Launch the application.
	 */
	public static WorldController makeSampleWorld() {
		int id = 0;
		final List<RealObject> objects = new LinkedList<RealObject>();
		final Universe universe = new CuboidUniverse(new Vector3f(5000, 5000, 5000), objects);

		int projector = 0;
		objects.add(new StandardProjector(id++, projector++, new Vector3f(2500, 4000, 0), new Rotation3f(
				(float) Math.PI / 6, 0, 0), new Vector3f(300, 160, 450), 768, 1024, 3));
		objects.add(new StandardProjector(id++, projector++, new Vector3f(0, 5000, 3000), new Rotation3f(
				(float) Math.PI / 4, (float) Math.PI / 4, 0), new Vector3f(300, 160, 450), 768, 1024, 1));

		int screen = 0;
		objects.add(new FlatScreen(id++, screen++, new Vector3f(4500, 500, 5000),
				new Rotation3f(0, (float) Math.PI, 0), new Vector2f(4000, 4000)));
		objects.add(new CuboidScreen(id++, screen++, new Vector3f(2000, 0, 3500), new Rotation3f(0,
				(float) Math.PI / 4, 0), new Vector3f(1000, 1000, 1000)));

		return new HashMapWorldController(universe);
	}

	private final JPanel			contentPane;
	private final JTable			tableChildren;
	private final JLabel			lblHeight;
	private final JSpinner			spinnerHeight;
	private final JLabel			lblWidth;
	private final JSpinner			spinnerWidth;
	private final JButton			btnAdd;
	private final JButton			btnEdit;

	private final JButton			btnRemove;
	private final JLabel			lblDepth;

	private final JSpinner			spinnerDepth;

	private final JButton			btnRefresh;

	private final EditorController	controller;
	private final Universe			universe;

	/**
	 * Create the frame.
	 */
	public UniverseFrame(EditorController controller, Universe universe) {
		this.controller = controller;
		this.universe = universe;

		setBounds(100, 100, 450, 494);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelProperties = new JPanel();
		contentPane.add(panelProperties, BorderLayout.NORTH);
		panelProperties.setLayout(new GridLayout(3, 2, 0, 0));

		JPanel panelControls = new JPanel();
		contentPane.add(panelControls, BorderLayout.SOUTH);

		// Property Labels
		lblHeight = new JLabel("Height (mm)");
		panelProperties.add(lblHeight);

		spinnerHeight = new JSpinner();
		spinnerHeight.addChangeListener(this);
		panelProperties.add(spinnerHeight);

		lblWidth = new JLabel("Width (mm)");
		panelProperties.add(lblWidth);

		spinnerWidth = new JSpinner();
		spinnerWidth.addChangeListener(this);
		panelProperties.add(spinnerWidth);

		lblDepth = new JLabel("Depth (mm)");
		panelProperties.add(lblDepth);

		spinnerDepth = new JSpinner();
		spinnerDepth.addChangeListener(this);
		panelProperties.add(spinnerDepth);

		btnAdd = createButton("Add", COMMAND_ADD);
		panelControls.add(btnAdd);

		btnEdit = createButton("Edit", COMMAND_EDIT);
		panelControls.add(btnEdit);

		btnRemove = createButton("Remove", COMMAND_REMOVE);
		panelControls.add(btnRemove);

		btnRefresh = createButton("Refresh", COMMAND_REFRESH);
		panelControls.add(btnRefresh);

		// Table
		tableChildren = new JTable();
		tableChildren.addMouseListener(new MouseAdapter() {
			private static final long	DOUBLE_CLICK_TIME	= 500;

			private long				lastTime;
			private int					lastRow				= -1;

			@Override
			public void mouseClicked(MouseEvent e) {
				int currentRow = tableChildren.rowAtPoint(e.getPoint());
				if (currentRow < 0)
					return;

				long currentTime = System.currentTimeMillis();

				if (lastRow == currentRow && (currentTime - lastTime) < DOUBLE_CLICK_TIME) {
					RealObject o = ((ChildTableModel) tableChildren.getModel()).getRealObject(currentRow);
					UniverseFrame.this.controller.edit(o);
				}
				lastRow = currentRow;
				lastTime = currentTime;
			}
		});

		contentPane.add(new JScrollPane(tableChildren), BorderLayout.CENTER);

		bind();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand() == COMMAND_ADD) {
			Object[] options = { "Standard Projector", "Flat Screen", "Cuboid Screen" };
			int result = JOptionPane.showOptionDialog(UniverseFrame.this, "What type of object should be added?",
					"Add Object", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
					"Standard Projector");
			switch (result) {
			case 0:
				controller.create(StandardProjector.class);
				break;
			case 1:
				controller.create(FlatScreen.class);
				break;
			case 2:
				controller.create(CuboidScreen.class);
				break;
			default:
				return;
			}
		} else if (e.getActionCommand().equals(COMMAND_EDIT) || e.getActionCommand().equals(COMMAND_REMOVE)) {
			int index = tableChildren.getSelectedRow();
			RealObject o = ((ChildTableModel) tableChildren.getModel()).getRealObject(index);
			if (e.getActionCommand().equals(COMMAND_EDIT))
				controller.edit(o);
			else {
				boolean confirmed = JOptionPane.showConfirmDialog(this,
						String.format("Do you want to remove %s object with ID %d?", o.getType(), o.getId()),
						"Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
				if (confirmed)
					controller.remove(o);
			}

		} else if (e.getActionCommand().equals(COMMAND_REFRESH))
			bind();
	}

	private void bind() {
		Vector3f dimensions = universe.getDimensions();
		spinnerWidth.setModel(new SpinnerNumberModel(dimensions.x, 1, Universe.MAX_DIMENSION, 1));
		spinnerHeight.setModel(new SpinnerNumberModel(dimensions.y, 1, Universe.MAX_DIMENSION, 1));
		spinnerDepth.setModel(new SpinnerNumberModel(dimensions.z, 1, Universe.MAX_DIMENSION, 1));

		ChildTableModel.useModel(tableChildren, universe.getChildren());
	}

	private JButton createButton(String text, String command) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.addActionListener(this);
		return button;
	}

	@Override
	public void onUpdate(WorldController world, RealObject child) {
		bind();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Vector3f dimensions = universe.getDimensions();
		float x = dimensions.x, y = dimensions.y, z = dimensions.z;
		if (e.getSource() == spinnerHeight)
			y = ((SpinnerNumberModel) (spinnerHeight.getModel())).getNumber().floatValue();
		if (e.getSource() == spinnerWidth)
			x = ((SpinnerNumberModel) (spinnerWidth.getModel())).getNumber().floatValue();
		if (e.getSource() == spinnerDepth)
			z = ((SpinnerNumberModel) (spinnerDepth.getModel())).getNumber().floatValue();

		universe.setDimensions(new Vector3f(x, y, z));
	}
}
