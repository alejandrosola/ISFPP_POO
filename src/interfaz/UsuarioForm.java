package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import aplicacion.Constantes;
import aplicacion.Coordinador;
import modelo.Usuario;
import util.Validation;

public class UsuarioForm extends JDialog {

	private Coordinador coordinador;

	private JPanel contentPane;
	private JTextField jtfNombre;
	private JTextField jtfGenero;
	private JTextField jtfCiudad;
	private JTextField jtfFechaNacimiento;
	private JTextField jtfId;

	private JLabel lblErrorNombre;
	private JLabel lblErrorGenero;
	private JLabel lblErrorCiudad;
	private JLabel lblErrorFechaNacimiento;
	private JLabel lblId;

	private JButton btnInsertar;
	private JButton btnModificar;
	private JButton btnBorrar;
	private JButton btnCancelar;
	
	int width;
	int height;

	/**
	 * Create the frame.
	 */
	public UsuarioForm() {
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Icono
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/resources/iconoUsuarios.png")));
		
		width = 200;
		height = 20;
		
		lblId = new JLabel("ID:");
        lblId.setBounds(42, 151, 107, 14);
        contentPane.add(lblId);

        jtfId = new JTextField();
        jtfId.setColumns(10);
        jtfId.setBounds(159, 148, width, height);
        contentPane.add(jtfId);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombre.setBounds(42, 24, 107, 14);
		contentPane.add(lblNombre);

		jtfNombre = new JTextField();
		jtfNombre.setBounds(159, 24, width, height);
		contentPane.add(jtfNombre);
		jtfNombre.setColumns(10);

		JLabel lblGenero = new JLabel("Genero:");
		lblGenero.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGenero.setBounds(42, 55, 107, 14);
		contentPane.add(lblGenero);

		jtfGenero = new JTextField();
		jtfGenero.setBounds(159, 55, width, height);
		contentPane.add(jtfGenero);
		jtfGenero.setColumns(10);

		JLabel lblCiudad = new JLabel("Ciudad:");
		lblCiudad.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCiudad.setBounds(42, 89, 107, 14);
		contentPane.add(lblCiudad);

		jtfCiudad = new JTextField();
		jtfCiudad.setBounds(159, 86, width, height);
		contentPane.add(jtfCiudad);
		jtfCiudad.setColumns(10);

		JLabel lblFechaNacimiento = new JLabel("Fecha Nacimiento:");
		lblFechaNacimiento.setBounds(42, 120, 107, 14);
		contentPane.add(lblFechaNacimiento);

		jtfFechaNacimiento = new JTextField();
		jtfFechaNacimiento.setColumns(10);
		jtfFechaNacimiento.setBounds(159, 117, width, height);
		contentPane.add(jtfFechaNacimiento);

		lblErrorNombre = new JLabel("");
		lblErrorNombre.setForeground(Color.RED);
		lblErrorNombre.setBounds(159 + width + 2, 24, 300, 14);
		contentPane.add(lblErrorNombre);

		lblErrorGenero = new JLabel("");
		lblErrorGenero.setForeground(Color.RED);
		lblErrorGenero.setBounds(159 + width + 2, 55, 300, 14);
		contentPane.add(lblErrorGenero);

		lblErrorCiudad = new JLabel("");
		lblErrorCiudad.setForeground(Color.RED);
		lblErrorCiudad.setBounds(159 + width + 2, 86, 300, 14);
		contentPane.add(lblErrorCiudad);

		lblErrorFechaNacimiento = new JLabel("");
		lblErrorFechaNacimiento.setForeground(Color.RED);
		lblErrorFechaNacimiento.setBounds(159 + width + 2, 117, 300, 14);
		contentPane.add(lblErrorFechaNacimiento);

		Handler handler = new Handler();

		btnInsertar = new JButton("Insertar");
		btnInsertar.setBounds(85, 202, 114, 32);
		contentPane.add(btnInsertar);
		btnInsertar.addActionListener(handler);

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(85, 202, 114, 32);
		contentPane.add(btnModificar);
		btnModificar.addActionListener(handler);

		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(85, 202, 114, 32);
		contentPane.add(btnBorrar);
		btnBorrar.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(225, 202, 114, 32);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		setModal(true);
	}

	public void accion(int accion, Usuario usuario) {
		btnInsertar.setVisible(false);
		btnModificar.setVisible(false);
		btnBorrar.setVisible(false);
		jtfNombre.setEditable(true);
		jtfGenero.setEditable(true);
		jtfCiudad.setEditable(true);
		jtfFechaNacimiento.setEditable(true);
		jtfId.setVisible(true);

		if (accion == Constantes.INSERTAR) {
			this.setTitle("Insertar un usuario");
			btnInsertar.setVisible(true);
			jtfId.setVisible(false);
			lblId.setText("");
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			this.setTitle("Modificar un usuario");
			btnModificar.setVisible(true);
			jtfId.setEditable(false);
			lblId.setText("ID: ");
			mostrar(usuario);
		}

	}

	private void mostrar(Usuario usuario) {
		jtfNombre.setText(usuario.getNombre());
		jtfCiudad.setText(usuario.getCiudad());
		jtfGenero.setText(usuario.getGenero());
		jtfFechaNacimiento.setText(usuario.getFechaNac().toString());
		jtfId.setText(usuario.getId());
	}

	private void limpiar() {
		jtfNombre.setText("");
		jtfGenero.setText("");
		jtfCiudad.setText("");
		jtfFechaNacimiento.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnCancelar) {
				coordinador.cancelarUsuario();
				return;
			}

			boolean valido = true;

			lblErrorNombre.setText("");
			lblErrorCiudad.setText("");
			lblErrorGenero.setText("");
			lblErrorFechaNacimiento.setText("");

			String nombre = jtfNombre.getText().trim();
			if (nombre.isEmpty()) {
				lblErrorNombre.setText("Campo obligatorio");
				valido = false;
			} else if (nombre.matches("[A-Z][a-zA-Z].*") != true) {
				lblErrorNombre.setText("Solo letras. Primera con may�scula");
				valido = false;
			}

			String ciudad = jtfCiudad.getText().trim();
			if (ciudad.isEmpty()) {
				lblErrorCiudad.setText("Campo obligatorio");
				valido = false;
			} else if (ciudad.matches("[A-Z][a-zA-Z].*") != true) {
				lblErrorCiudad.setText("Solo letras. Primera con may�scula");
				valido = false;
			}

			String genero = jtfGenero.getText().trim();
			if (genero.isEmpty()) {
				lblErrorGenero.setText("Campo obligatorio");
				valido = false;
			} else if (!genero.matches("[H]") && !genero.matches("[M]")) {
				lblErrorGenero.setText("H o M");
				valido = false;
			}

			LocalDate fechaNacimiento = Validation.isDate(jtfFechaNacimiento.getText());
			if (fechaNacimiento == null) {
				lblErrorFechaNacimiento.setText("Formato de fecha AAAA-MM-DD");
				valido = false;
			}
			
			String id = jtfId.getText();
			
			if (!valido)
				return;

			Usuario usuario = new Usuario(id, nombre, fechaNacimiento,
					genero, ciudad);
			if (event.getSource() == btnInsertar) {
				coordinador.insertarUsuario(usuario);
				coordinador.actualizarModeloAgregar(usuario.getId());
			}
			if (event.getSource() == btnModificar) {
				coordinador.modificarUsuario(id,usuario);
			}
				
		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
	
	
}
