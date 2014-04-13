package com.jayanslow.projection.world.editor.models;

import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.vecmath.Vector3f;

import com.jayanslow.projection.world.models.Projector;
import com.jayanslow.projection.world.models.RealObject;
import com.jayanslow.projection.world.models.RealObjectType;
import com.jayanslow.projection.world.models.Screen;

public class ChildTableModel extends AbstractTableModel {

	private static final long	serialVersionUID	= 2674047028387318232L;

	private static final int	COLUMN_ID			= 0;
	private static final int	COLUMN_SUB_ID		= 1;
	private static final int	COLUMN_TYPE			= 2;
	private static final int	COLUMN_SUB_TYPE		= 3;
	private static final int	COLUMN_POSITION		= 4;

	public static void useModel(JTable table, List<RealObject> children) {
		ChildTableModel model = new ChildTableModel(children);
		table.setModel(model);

		for (int i = 0; i < model.getColumnCount(); i++) {
			TableColumn c = table.getColumnModel().getColumn(i);
			switch (i) {
			case COLUMN_ID:
			case COLUMN_SUB_ID:
				c.setPreferredWidth(50);
				break;
			case COLUMN_POSITION:
				c.setPreferredWidth(200);
			}
		}

		ListSelectionModel selection = table.getSelectionModel();
		selection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (model.getRowCount() > 0)
			selection.setSelectionInterval(0, 0);
	}

	private final List<RealObject>	children;

	/**
	 * @param universeFrame
	 */
	ChildTableModel(List<RealObject> children) {
		this.children = children;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case COLUMN_ID:
		case COLUMN_SUB_ID:
			return Integer.class;
		case COLUMN_TYPE:
			return RealObjectType.class;
		case COLUMN_POSITION:
			return Vector3f.class;
		case COLUMN_SUB_TYPE:
			return Object.class;
		default:
			throw new IllegalArgumentException("Column out of range");
		}
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case COLUMN_ID:
			return "ID";
		case COLUMN_SUB_ID:
			return "Sub-ID";
		case COLUMN_TYPE:
			return "Type";
		case COLUMN_SUB_TYPE:
			return "Subtype";
		case COLUMN_POSITION:
			return "Position";
		default:
			throw new IllegalArgumentException("Column out of range");
		}
	}

	public RealObject getRealObject(int rowIndex) {
		return children.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return children.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RealObject o = children.get(rowIndex);
		switch (columnIndex) {
		case COLUMN_ID:
			return o.getId();
		case COLUMN_TYPE:
			return o.getType();
		case COLUMN_SUB_ID:
			switch (o.getType()) {
			case PROJECTOR:
				return ((Projector) o).getProjectorId();
			case SCREEN:
				return ((Screen) o).getScreenId();
			default:
				throw new RuntimeException("Unknown RealObjectType in UniverseFrame");
			}
		case COLUMN_POSITION:
			return o.getPosition();
		case COLUMN_SUB_TYPE:
			switch (o.getType()) {
			case PROJECTOR:
				return null;
			case SCREEN:
				return ((Screen) o).getScreenType();
			default:
				throw new RuntimeException("Unknown RealObjectType in UniverseFrame");
			}
		default:
			throw new IllegalArgumentException("Column out of range");
		}
	}
}
