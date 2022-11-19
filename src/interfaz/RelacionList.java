package interfaz;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import aplicacion.Constantes;
import aplicacion.Coordinador;
import modelo.Relacion;

public class RelacionList extends JDialog {

	private Coordinador coordinador;
	private int accion;
	private Relacion relacion;

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable tableRelacion;
	private JButton btnInsertar;
	private DefaultTableModel modeloTable;
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public RelacionList(Coordinador coordinador) {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Icono y titulo
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/resources/iconoRelaciones.png")));
		this.setTitle("Relaciones");

		this.coordinador = coordinador;
		setBounds(100, 100, 756, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnInsertar = new JButton("Insertar");
		btnInsertar.setBounds(38, 280, 114, 32);
		contentPane.add(btnInsertar);

		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				RelacionForm relacionForm = null;

				coordinador.insertarRelacionForm();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 25, 673, 244);
		contentPane.add(scrollPane);
		
		tableRelacion = new JTable();
		tableRelacion.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Tiempo", "Amigo Destino", "Amigo Origen.", "Interaccion", "Likes", "Modificar", "Borrar" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		modeloTable = (DefaultTableModel) tableRelacion.getModel();
		
		tableRelacion.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
		tableRelacion.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));
		tableRelacion.getColumn("Borrar").setCellRenderer(new ButtonRenderer());
		tableRelacion.getColumn("Borrar").setCellEditor(new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(tableRelacion);

		setModal(true);
	}


	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableRelacion.getModel()).setRowCount(0);
		for (Relacion r : coordinador.getRelaciones())
			addRow(r);
	}

	public void addRow(Relacion relac) {
		Object[] row = new Object[tableRelacion.getModel().getColumnCount()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		row[0] = relac.getTiempo();
		row[1] = relac.getAmigo_destino().getId();
		row[2] = relac.getAmigo_origen().getId();
		row[3] = relac.getInteraccion();
		row[4] = relac.getLikes();
		row[5] = "edit";
		row[6] = "drop";

		((DefaultTableModel) tableRelacion.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		tableRelacion.setValueAt(relacion.getTiempo().toString(), row, 0);
		tableRelacion.setValueAt(relacion.getAmigo_destino().getId(), row, 1);
		tableRelacion.setValueAt(relacion.getAmigo_origen().getId(),row, 2);
		tableRelacion.setValueAt(relacion.getInteraccion(), row, 3);
		tableRelacion.setValueAt(relacion.getLikes(), row, 4);
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
//			 setText((value == null) ? "" : value.toString());
			Icon icon = null;
			
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/resources/modificar.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/resources/eliminar.png"));
			setIcon(icon);

			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable table;
		private boolean isDeleteRow = false;
		private boolean isUpdateRow = false;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}

			label = (value == null) ? "" : value.toString();
			Icon icon = null;
			
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/resources/modificar.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/resources/eliminar.png"));
			button.setIcon(icon);
			isPushed = true;
			this.table = table;
			isDeleteRow = false;
			isUpdateRow = false;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			String idUserD = null;
			String idUserO = null;
			if (isPushed) {
				idUserD = tableRelacion.getValueAt(tableRelacion.getSelectedRow(), 1).toString();
				idUserO = tableRelacion.getValueAt(tableRelacion.getSelectedRow(), 2).toString();
				
				Relacion relacion = coordinador.getRelacion(idUserD, idUserO);

				
				if (label.equals("drop")) {
					int resp = JOptionPane.showConfirmDialog(null, "Está seguro que borra este registro?", "Confirmar",
							JOptionPane.YES_NO_OPTION);
					
					if (resp == JOptionPane.OK_OPTION) {
						coordinador.borrarRelacion(relacion);
						isDeleteRow = true;
					}	
				} else if (label.equals("edit")) {
					coordinador.modificarRelacionForm(relacion);
					isUpdateRow = false;
				}
				
				if (accion == Constantes.MODIFICAR) {
					isUpdateRow = true;
				}
				
				isPushed = false;
			}
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();

			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			if (isDeleteRow)
				tableModel.removeRow(table.getSelectedRow());

			if (isUpdateRow) {
				updateRow(table.getSelectedRow());
			}

		}

	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}
	
	public void actualizarAgregar(Relacion relacion) {		
		this.addRow(relacion);
	}

	
}