package interfaz;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import aplicacion.Coordinador;
import logica.AmistadesHilo;
import logica.MasInfluyentesHilo;
import logica.PromedioHilo;
import logica.RapidoHilo;
import logica.SugerenciasHilo;
import modelo.Usuario;

public class Interfaz {

	private JFrame frmRedSocial;
	private Coordinador coordinador;
	private JLabel lblUsuario1;
	private JLabel lblUsuario2;
	private JSpinner spinnerCantidad;
	private DefaultComboBoxModel<String> modelo1;
	private DefaultComboBoxModel<String> modelo2;
	private SpinnerNumberModel cantidadUsuarios;
	private JScrollPane scrollPane;
	private JTextArea textAreaResultado;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private JComboBox<String> comboBoxOpcion;
	private JProgressBar progressBar;

	public Interfaz(Coordinador coordinador) {
		this.coordinador = coordinador;
		initialize();
	}

	public Image getIconImage() {
		Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("resources/icono.jpg"));
		return retValue;
	}

	private void initialize() {
		frmRedSocial = new JFrame();
		frmRedSocial.setBounds(100, 100, 767, 527);
		frmRedSocial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 52, 115, 0, 0, 124, 53, 43, 0, 157, 35, 0 };
		gridBagLayout.rowHeights = new int[] { 49, 0, 0, 0, 0, 31, 0, 0, 170, 77, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		frmRedSocial.getContentPane().setLayout(gridBagLayout);

		frmRedSocial.setIconImage(
				Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/resources/icono2.png")));
		frmRedSocial.setTitle("Red social");

		JMenuBar menuBar = new JMenuBar();
		getFrame().setJMenuBar(menuBar);

		JMenuItem mntmNewMenuItemUsuarios = new JMenuItem("Usuarios");
		mntmNewMenuItemUsuarios.setHorizontalAlignment(SwingConstants.LEFT);
		mntmNewMenuItemUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				coordinador.mostrarUsuarioList();
			}
		});

		menuBar.add(mntmNewMenuItemUsuarios);

		JMenuItem mntmNewMenuItemRelaciones = new JMenuItem("Relaciones");
		mntmNewMenuItemRelaciones.setHorizontalAlignment(SwingConstants.LEFT);
		mntmNewMenuItemRelaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				coordinador.mostrarRelacionList();
			}
		});

		menuBar.add(mntmNewMenuItemRelaciones);

		String[] lista = { "...", "Promedio de amistades", "N usuarios mas influyentes",
				"Camino mas corto entre dos usuarios por tiempo de amistad", "Amistades de un usuario",
				"Sugerencia de amigos de un usuario" };

		JLabel lblElegi = new JLabel("Elija una consulta");
		lblElegi.setFont(new Font("Tahoma", Font.PLAIN, 18));

		String[] usuarios = coordinador.getIds();

		ordenarIds(usuarios);

		modelo1 = new DefaultComboBoxModel<String>(usuarios);
		modelo2 = new DefaultComboBoxModel<String>(usuarios);

		JComboBox<String> comboBoxUsuario1 = new JComboBox(modelo1);
		JComboBox<String> comboBoxUsuario2 = new JComboBox(modelo2);

		lblUsuario1 = new JLabel("Usuario 1:");
		lblUsuario1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsuario1.setVisible(false);

		lblUsuario1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblUsuario1 = new GridBagConstraints();
		gbc_lblUsuario1.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsuario1.gridx = 8;
		gbc_lblUsuario1.gridy = 2;
		frmRedSocial.getContentPane().add(lblUsuario1, gbc_lblUsuario1);

		comboBoxOpcion = new JComboBox(lista);
		GridBagConstraints gbc_comboBoxOpcion = new GridBagConstraints();
		gbc_comboBoxOpcion.gridwidth = 4;
		gbc_comboBoxOpcion.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxOpcion.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxOpcion.gridx = 1;
		gbc_comboBoxOpcion.gridy = 3;
		frmRedSocial.getContentPane().add(comboBoxOpcion, gbc_comboBoxOpcion);

		lblUsuario2 = new JLabel("Usuario 2:");
		lblUsuario2.setFont(new Font("Tahoma", Font.PLAIN, 18));

		if (coordinador.getIds().length >= 1) {
			cantidadUsuarios = new SpinnerNumberModel(1, 1, coordinador.getIds().length, 1);
		} else {
			cantidadUsuarios = new SpinnerNumberModel(0, 0, 0, 1);
		}
		spinnerCantidad = new JSpinner(cantidadUsuarios);

		comboBoxOpcion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxOpcion.getSelectedItem().equals(lista[1])) {
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					lblUsuario1.setVisible(false);
					lblUsuario2.setVisible(false);
					comboBoxUsuario1.setVisible(false);
					comboBoxUsuario2.setVisible(false);
					spinnerCantidad.setVisible(false);
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[2])) {
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					lblUsuario1.setVisible(false);
					lblUsuario2.setVisible(false);
					comboBoxUsuario1.setVisible(false);
					comboBoxUsuario2.setVisible(false);
					spinnerCantidad.setVisible(true);
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[3])) {
					lblUsuario1.setText("Usuario 1:");
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					spinnerCantidad.setVisible(false);
					lblUsuario1.setVisible(true);
					lblUsuario2.setVisible(true);
					comboBoxUsuario1.setVisible(true);
					comboBoxUsuario2.setVisible(true);
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[4])) {
					lblUsuario1.setText("Usuario:");
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					spinnerCantidad.setVisible(false);
					lblUsuario1.setVisible(true);
					lblUsuario2.setVisible(false);
					comboBoxUsuario1.setVisible(true);
					comboBoxUsuario2.setVisible(false);
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[5])) {
					lblUsuario1.setText("Usuario:");
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					spinnerCantidad.setVisible(false);
					lblUsuario1.setVisible(true);
					lblUsuario2.setVisible(false);
					comboBoxUsuario1.setVisible(true);
					comboBoxUsuario2.setVisible(false);
				}

			}
		});

		textAreaResultado = new JTextArea();
		textAreaResultado.setLineWrap(true);
		textAreaResultado.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));

		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 4;
		gbc_scrollPane_1.gridheight = 4;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 5;
		frmRedSocial.getContentPane().add(scrollPane, gbc_scrollPane_1);

		scrollPane.setViewportView(textAreaResultado);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		lblUsuario2.setVisible(false);
		comboBoxUsuario1.setVisible(false);
		comboBoxUsuario2.setVisible(false);
		spinnerCantidad.setVisible(false);
		textAreaResultado.setVisible(false);
		textAreaResultado.setEditable(false);

		lblElegi.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblElegi = new GridBagConstraints();
		gbc_lblElegi.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblElegi.gridwidth = 5;
		gbc_lblElegi.insets = new Insets(0, 0, 5, 5);
		gbc_lblElegi.gridx = 1;
		gbc_lblElegi.gridy = 2;
		frmRedSocial.getContentPane().add(lblElegi, gbc_lblElegi);

		GridBagConstraints gbc_spinnerCantidad = new GridBagConstraints();
		gbc_spinnerCantidad.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerCantidad.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerCantidad.gridx = 6;
		gbc_spinnerCantidad.gridy = 3;
		frmRedSocial.getContentPane().add(spinnerCantidad, gbc_spinnerCantidad);

		GridBagConstraints gbc_comboBoxUsuario1 = new GridBagConstraints();
		gbc_comboBoxUsuario1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxUsuario1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxUsuario1.gridx = 8;
		gbc_comboBoxUsuario1.gridy = 3;
		frmRedSocial.getContentPane().add(comboBoxUsuario1, gbc_comboBoxUsuario1);

		lblUsuario2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblUsuario2 = new GridBagConstraints();
		gbc_lblUsuario2.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsuario2.gridx = 8;
		gbc_lblUsuario2.gridy = 5;
		frmRedSocial.getContentPane().add(lblUsuario2, gbc_lblUsuario2);

		GridBagConstraints gbc_comboBoxUsuario2 = new GridBagConstraints();
		gbc_comboBoxUsuario2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxUsuario2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxUsuario2.gridx = 8;
		gbc_comboBoxUsuario2.gridy = 6;
		frmRedSocial.getContentPane().add(comboBoxUsuario2, gbc_comboBoxUsuario2);

		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.gridx = 4;
		gbc_progressBar.gridy = 10;
		frmRedSocial.getContentPane().add(progressBar, gbc_progressBar);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!comboBoxOpcion.getSelectedItem().equals(lista[0])) {
					actualizarBarra(1);
					comboBoxOpcion.setEnabled(false);
					btnAceptar.setEnabled(false);
					progressBar.setVisible(true);
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[0])) {
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					lblUsuario1.setVisible(false);
					lblUsuario2.setVisible(false);
					comboBoxUsuario1.setVisible(false);
					comboBoxUsuario2.setVisible(false);
					spinnerCantidad.setVisible(false);
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[1])) {
					textAreaResultado.setVisible(false);
					scrollPane.setVisible(false);
					lblUsuario1.setVisible(false);
					lblUsuario2.setVisible(false);
					comboBoxUsuario1.setVisible(false);
					comboBoxUsuario2.setVisible(false);
					spinnerCantidad.setVisible(false);
					coordinador.ejecutarHilo(new PromedioHilo(coordinador));
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[2])) {
					String valor = spinnerCantidad.getValue() + "";
					int n = Integer.parseInt(valor);
					coordinador.ejecutarHilo(new MasInfluyentesHilo(n, coordinador));
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[3])) {
					Usuario user1 = coordinador.getUsuario((String) comboBoxUsuario1.getSelectedItem());
					Usuario user2 = coordinador.getUsuario((String) comboBoxUsuario2.getSelectedItem());
					coordinador.ejecutarHilo(new RapidoHilo(user1, user2, coordinador));
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[4])) {
					Usuario user1 = coordinador.getUsuario((String) comboBoxUsuario1.getSelectedItem());
					coordinador.ejecutarHilo(new AmistadesHilo(user1, coordinador));
				}
				if (comboBoxOpcion.getSelectedItem().equals(lista[5])) {
					Usuario user1 = coordinador.getUsuario((String) comboBoxUsuario1.getSelectedItem());
					coordinador.ejecutarHilo(new SugerenciasHilo(user1, coordinador));
				}

			}
		});
		GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
		gbc_btnAceptar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAceptar.gridx = 2;
		gbc_btnAceptar.gridy = 10;
		frmRedSocial.getContentPane().add(btnAceptar, gbc_btnAceptar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				coordinador.cancelarHilo();
				btnAceptar.setEnabled(true);
				comboBoxOpcion.setEnabled(true);
				progressBar.setVisible(false);
			}
		});
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancelar.gridx = 3;
		gbc_btnCancelar.gridy = 10;
		frmRedSocial.getContentPane().add(btnCancelar, gbc_btnCancelar);

		progressBar.setVisible(false);
	}

	public void mostrarInfluyentes(Map<Integer, List<Usuario>> influyentes, int n) {
		if (influyentes == null)
			return;
		String mensaje = "Los " + n + " usuarios mas influyentes son: \n";
		int i = 0;
		for (Entry<Integer, List<Usuario>> entry : influyentes.entrySet()) {
			for (Usuario user : entry.getValue()) {
				if (i < n)
					mensaje += (user.getNombre() + ", con " + entry.getKey() + " amigos.\n");
				i++;
			}
		}

		textAreaResultado.setVisible(true);
		scrollPane.setVisible(true);
		textAreaResultado.setText(mensaje);
		btnAceptar.setEnabled(true);
		comboBoxOpcion.setEnabled(true);
		progressBar.setVisible(false);
	}

	public void mostrarCamino(Usuario user1, Usuario user2, List<Usuario> list) {
		String s = "";
		for (int i = 0; i < list.size() - 1; i++)
			s += (list.get(i).getNombre() + " -> ");

		if (list.size() == 0)
			s = "No existe ningun camino desde " + user1.getNombre() + " hasta " + user2.getNombre() + ".";
		else
			s += list.get(list.size() - 1).getNombre();
		textAreaResultado.setVisible(true);
		scrollPane.setVisible(true);
		textAreaResultado.setText(s);
		btnAceptar.setEnabled(true);
		comboBoxOpcion.setEnabled(true);
		progressBar.setVisible(false);
	}

	public void mostrarPromedio(double prom) {
		textAreaResultado.setText("Promedio de amigos: " + Math.round(prom));
		scrollPane.setVisible(true);
		textAreaResultado.setVisible(true);
		btnAceptar.setEnabled(true);
		comboBoxOpcion.setEnabled(true);
		progressBar.setVisible(false);
	}

	public void mostrarAmigos(Map<Usuario, Integer> map) {
		String s = "";
		if (map.size() == 0)
			s += "El usuario no tiene ningun amigo.";
		for (Entry<Usuario, Integer> a : map.entrySet()) {
			s += a.getKey().getNombre() + "\n";
		}
		textAreaResultado.setVisible(true);
		scrollPane.setVisible(true);
		textAreaResultado.setText(s);
		btnAceptar.setEnabled(true);
		comboBoxOpcion.setEnabled(true);
		progressBar.setVisible(false);
	}

	public void mostrarSugerencias(Map<Double, List<Usuario>> sugerencias) {
		String s = "";
		if (sugerencias.size() == 0)
			s += "No hay ninguna sugerencia de amistades para este usuario.";
		for (Entry<Double, List<Usuario>> a : sugerencias.entrySet()) {
			for (Usuario l : a.getValue()) {
				s += l.getNombre() + "\n";
			}
		}
		textAreaResultado.setVisible(true);
		scrollPane.setVisible(true);
		textAreaResultado.setText(s);
		btnAceptar.setEnabled(true);
		comboBoxOpcion.setEnabled(true);
		progressBar.setVisible(false);
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public JFrame getFrame() {
		return frmRedSocial;
	}

	public void setFrame(JFrame frame) {
		this.frmRedSocial = frame;
		frmRedSocial.setIconImage(
				Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/resources/icono2.png")));
		frmRedSocial.setTitle("Red Social");
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

	public void actualizarBarra(int i) {
		progressBar.setValue(i);
		progressBar.repaint();
	}
}
