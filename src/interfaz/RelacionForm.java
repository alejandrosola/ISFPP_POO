package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import aplicacion.Constantes;
import aplicacion.Coordinador;
import modelo.Relacion;
import modelo.Usuario;
import util.RelacionRepetidaException;
import util.Validation;

public class RelacionForm extends JDialog {

	private Coordinador coordinador;

	private JPanel contentPane;
	private JTextField jtfTiempo;
	private JTextField jtfInteraccion;
	private JTextField jtfLikes;

	private JLabel lblErrorTiempo;
	private JLabel lblErrorInteraccion;
	private JLabel lblErrorLikes;
	private JLabel lblErrorRelacion;

	private JButton btnInsertar;
	private JButton btnModificar;
	private JButton btnBorrar;
	private JButton btnCancelar;

	private DefaultComboBoxModel<String> modelo1;
	private DefaultComboBoxModel<String> modelo2;
	private JComboBox<String> comboBoxUsuario1;
	private JComboBox<String> comboBoxUsuario2;

	int width;
	int height;

	/**
	 * Create the frame.
	 */
	public RelacionForm(Coordinador coordinador) {
		this.coordinador = coordinador;
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Icono
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/resources/iconoRelaciones.png")));

		width = 200;
		height = 20;

		String[] usuarios = coordinador.getIds();

		ordenarIds(usuarios);

		modelo1 = new DefaultComboBoxModel<String>(usuarios);
		modelo2 = new DefaultComboBoxModel<String>(usuarios);

		comboBoxUsuario1 = new JComboBox(modelo1);
		comboBoxUsuario2 = new JComboBox(modelo2);

		comboBoxUsuario1.setBounds(159, 24, width, height);
		contentPane.add(comboBoxUsuario1);
		comboBoxUsuario2.setBounds(159, 55, width, height);
		contentPane.add(comboBoxUsuario2);

		JLabel lblTiempo = new JLabel("Tiempo:");
		lblTiempo.setBounds(42, 151, 107, 14);
		contentPane.add(lblTiempo);
		

		jtfTiempo = new JTextField();
		jtfTiempo.setColumns(10);
		jtfTiempo.setBounds(159, 148, width, height);
		contentPane.add(jtfTiempo);

		JLabel lblAmigo_Destino = new JLabel("Id Usuario Destino:");
		lblAmigo_Destino.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAmigo_Destino.setBounds(42, 24, 107, 14);
		contentPane.add(lblAmigo_Destino);

		JLabel lblAmigo_Origen = new JLabel("Id Usuario Origen :");
		lblAmigo_Origen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAmigo_Origen.setBounds(42, 55, 107, 14);
		contentPane.add(lblAmigo_Origen);

		JLabel lblInteraccion = new JLabel("Interaccion:");
		lblInteraccion.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblInteraccion.setBounds(42, 89, 107, 14);
		contentPane.add(lblInteraccion);

		jtfInteraccion = new JTextField();
		jtfInteraccion.setBounds(159, 86, width, height);
		contentPane.add(jtfInteraccion);
		jtfInteraccion.setColumns(10);

		JLabel lblLikes = new JLabel("Likes:");
		lblLikes.setBounds(42, 120, 107, 14);
		contentPane.add(lblLikes);

		jtfLikes = new JTextField();
		jtfLikes.setColumns(10);
		jtfLikes.setBounds(159, 117, width, height);
		contentPane.add(jtfLikes);

		lblErrorTiempo = new JLabel("");
		lblErrorTiempo.setForeground(Color.RED);
		lblErrorTiempo.setBounds(159+width + 2, 148, 300, 14);
		contentPane.add(lblErrorTiempo);


		lblErrorInteraccion = new JLabel("");
		lblErrorInteraccion.setForeground(Color.RED);
		lblErrorInteraccion.setBounds(159+width+2, 86, 300, 14);
		contentPane.add(lblErrorInteraccion);

		lblErrorLikes = new JLabel("");
		lblErrorLikes.setForeground(Color.RED);
		lblErrorLikes.setBounds(159+width+2, 117, 300, 14);
		contentPane.add(lblErrorLikes);

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
		
		lblErrorRelacion = new JLabel("");
		lblErrorRelacion.setForeground(Color.RED);
		lblErrorRelacion.setBounds(42, 178, 292, 13);
		contentPane.add(lblErrorRelacion);
		btnCancelar.addActionListener(handler);

		setModal(true);
	}

	public void accion(int accion, Relacion relacion) {
		btnInsertar.setVisible(false);
		btnModificar.setVisible(false);
		if (accion == Constantes.INSERTAR) {
			this.setTitle("Insertar una relacion");
			comboBoxUsuario1.setSelectedIndex(0);
			comboBoxUsuario1.setEnabled(true);
			comboBoxUsuario2.setSelectedIndex(0);
			comboBoxUsuario2.setEnabled(true);
			btnInsertar.setVisible(true);
			btnBorrar.setVisible(false);
			jtfTiempo.setEditable(true);
			jtfInteraccion.setEditable(true);
			jtfLikes.setEditable(true);
			btnInsertar.setVisible(true);
			limpiar();
		}
		if (accion == Constantes.MODIFICAR) {
			this.setTitle("Modificar una relacion");
			btnModificar.setVisible(true);
			comboBoxUsuario1.setSelectedItem(relacion.getAmigo_destino().getId());
			comboBoxUsuario1.setEnabled(false);
			comboBoxUsuario2.setSelectedItem(relacion.getAmigo_origen().getId());
			comboBoxUsuario2.setEnabled(false);
			btnBorrar.setVisible(false);
			jtfTiempo.setEditable(false);
			jtfInteraccion.setEditable(true);
			jtfLikes.setEditable(true);
			mostrar(relacion);
		}

	}

	private void mostrar(Relacion relacion) {
		jtfTiempo.setText(relacion.getTiempo().toString());
		jtfInteraccion.setText(String.valueOf(relacion.getInteraccion()));
		jtfLikes.setText(String.valueOf(relacion.getLikes()));
	}

	private void limpiar() {
		jtfTiempo.setText("");
		jtfInteraccion.setText("");
		jtfLikes.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == btnCancelar) {
				coordinador.cancelarRelacion();
				return;
			}

			boolean valido = true;

			lblErrorTiempo.setText("");
			lblErrorInteraccion.setText("");
			lblErrorLikes.setText("");

			LocalDate tiempo = Validation.isDate(jtfTiempo.getText());
			if (tiempo == null) {
				lblErrorTiempo.setText("Formato de fecha AAAA-MM-DD");
				valido = false;
			}
			Usuario amigo_Destino = coordinador.getUsuario((String) comboBoxUsuario1.getSelectedItem());

			Usuario amigo_Origen = coordinador.getUsuario((String) comboBoxUsuario2.getSelectedItem());

			int interaccion = 0;
			
			int likes = 0;
			try {
				interaccion = Integer.parseInt(jtfInteraccion.getText());
			} catch (NumberFormatException e) {
				lblErrorInteraccion.setText("Solo numeros.");
				valido = false;
			}
			if (jtfInteraccion.getText().isEmpty()) { 
				lblErrorInteraccion.setText("Campo obligatorio");
				valido = false;
			}
			
			
			try {
				likes = Integer.parseInt(jtfLikes.getText());
			} catch (NumberFormatException e) {
				lblErrorInteraccion.setText("Solo numeros.");
				valido = false;
			}
			if (jtfLikes.getText().isEmpty()) {
				lblErrorLikes.setText("Campo obligatorio");
				valido = false;
			}
			
			if (amigo_Destino.getId().equals(amigo_Origen.getId())) {
				lblErrorRelacion.setText(Constantes.RELACIONUSUARIOREPETIDO);
				valido = false;
			}

			if (!valido)
				return;

			Relacion relacion = new Relacion(tiempo, amigo_Destino, amigo_Origen, interaccion, likes);
			if (event.getSource() == btnInsertar) {
				try {
					coordinador.insertarRelacion(relacion);					
				} catch(RelacionRepetidaException e) {
					lblErrorRelacion.setText(e.getMessage());
				}
			}
			if (event.getSource() == btnModificar) {
				coordinador.modificarRelacion(relacion);
			}

		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public String[] ordenarIds(String[] usuarios) {
		int[] usuariosInt = new int[usuarios.length];
		for (int i = 0; i < usuarios.length; i++) {
			usuariosInt[i] = Integer.parseInt(usuarios[i]);
		}
		Arrays.sort(usuariosInt);

		for (int i = 0; i < usuarios.length; i++)
			usuarios[i] = String.valueOf(usuariosInt[i]);

		return usuarios;
	}

	
	public void actualizarModeloBorrar(String id) {
		modelo1.removeElement(id);
		modelo2.removeElement(id);
	}
	
	public void actualizarModeloAgregar(String id) {
		modelo1.addElement(id);
		modelo2.addElement(id);
	}
}