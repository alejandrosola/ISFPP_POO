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
import modelo.Usuario;

public class UsuarioList extends JDialog {

	private Coordinador coordinador;
	private int accion;
	private Usuario usuario;

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable tableUsuario;
	private JButton btnInsertar;
	private DefaultTableModel modeloTable;
	/**
	 * Create the frame.
	 */
	public UsuarioList(Coordinador coordinador) {
		
		this.coordinador = coordinador;
		setBounds(100, 100, 756, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Icono y titulo
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/resources/iconoUsuarios.png")));
		this.setTitle("Usuarios");

		btnInsertar = new JButton("Insertar");
		btnInsertar.setBounds(38, 280, 114, 32);
		contentPane.add(btnInsertar);

		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				UsuarioForm usuarioForm = null;

				coordinador.insertarUsuarioForm();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 25, 673, 244);
		contentPane.add(scrollPane);
		
		tableUsuario = new JTable();
		tableUsuario.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Id", "Nombre", "Fecha Nac.", "Genero", "Ciudad", "Modificar", "Borrar" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		modeloTable = (DefaultTableModel) tableUsuario.getModel();
		
		tableUsuario.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
		tableUsuario.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));
		tableUsuario.getColumn("Borrar").setCellRenderer(new ButtonRenderer());
		tableUsuario.getColumn("Borrar").setCellEditor(new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(tableUsuario);

		setModal(true);
	}


	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableUsuario.getModel()).setRowCount(0);
		for (Usuario u : coordinador.getUsuarios())
			addRow(u);
	}

	public void addRow(Usuario emp) {
		Object[] row = new Object[tableUsuario.getModel().getColumnCount()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		row[0] = emp.getId();
		row[1] = emp.getNombre();
		row[2] = emp.getFechaNac();
		row[3] = emp.getGenero();
		row[4] = emp.getCiudad();
		row[5] = "edit";
		row[6] = "drop";

		((DefaultTableModel) tableUsuario.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		tableUsuario.setValueAt(usuario.getId(), row, 0);
		tableUsuario.setValueAt(usuario.getNombre(), row, 1);
		tableUsuario.setValueAt(usuario.getFechaNac().toString(), row, 2);
		tableUsuario.setValueAt(usuario.getGenero(), row, 3);
		tableUsuario.setValueAt(usuario.getCiudad(), row, 4);
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
			String id = null;
			if (isPushed) {
				id = tableUsuario.getValueAt(tableUsuario.getSelectedRow(), 0).toString();
				Usuario usuario = (Usuario) coordinador.getUsuario(id);

				
				if (label.equals("drop")) {
					int resp = JOptionPane.showConfirmDialog(null, "Está seguro que borra este registro?", "Confirmar",
							JOptionPane.YES_NO_OPTION);
					
					if (resp == JOptionPane.OK_OPTION) {
						coordinador.borrarUsuario(usuario);
						coordinador.actualizarModeloBorrar(id);
						isDeleteRow = true;
					}	
				} else if (label.equals("edit")) {
					coordinador.modificarUsuarioForm(usuario);
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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void actualizarAgregar(Usuario usuario) {		
		this.addRow(usuario);
	}

	
}
