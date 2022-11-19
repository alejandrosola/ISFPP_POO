package dao.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import aplicacion.Constantes;
import dao.UsuarioDAO;
import modelo.Usuario;

public class UsuarioArchivoDAO implements UsuarioDAO{
	
	private TreeMap<String, Usuario> map;
	private String name;
	private final Logger logger = Logger.getLogger(this.getClass());
	
	public UsuarioArchivoDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("usuarios");
		map = readFromFile(name);
	}
	
	private TreeMap<String, Usuario> readFromFile(String filename) {
		Scanner read = null;
		int mayorId = 0;

		TreeMap<String, Usuario> usuarios = new TreeMap<String, Usuario>();
		try {
			read = new Scanner(new File(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		read.useDelimiter("\\s*;\\s*");
		String codigo, nombre, ciudad, genero;
		LocalDate fecha = null;
		int anio, mes, dia;

		while (read.hasNext()) {
			codigo = read.next();
			nombre = read.next();
			try {
				anio = Integer.parseInt(read.next());
				mes = Integer.parseInt(read.next());
				dia = Integer.parseInt(read.next());
				fecha = LocalDate.of(anio, mes, dia);
			} catch (NumberFormatException e) {
				logger.error(Constantes.FORMATOFECHAINVALIDO);
				fecha = LocalDate.of(2000, 1, 1);
			} catch (DateTimeException e) {
				logger.error(Constantes.FECHAINVALIDA);
				fecha = LocalDate.of(2000, 1, 1);
			}
			genero = read.next();
			ciudad = read.next();
			
			if (Integer.parseInt(codigo) > mayorId)
				mayorId = Integer.parseInt(codigo);
			
			usuarios.put(codigo, new Usuario(codigo, nombre, fecha, genero, ciudad));
		}
		Usuario.contadorIds = mayorId + 1;
		read.close();
		logger.info("Usuarios cargados con exito");
		return usuarios;
	}

	private void writeToFile(TreeMap<String, Usuario> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Entry<String, Usuario> entry : map.entrySet()) {
				Usuario usr = entry.getValue();
				outFile.format("%s;%s;%d;%d;%d;%s;%s;\n", usr.getId(), usr.getNombre(), usr.getFechaNac().getYear(),
						usr.getFechaNac().getMonthValue(), usr.getFechaNac().getDayOfMonth(),
						usr.getGenero(), usr.getCiudad());
			}
		} catch (FileNotFoundException fileNotFoundException) {
			logger.info("Error creando el archivo");
		} catch (FormatterClosedException formatterClosedException) {
			logger.info("Error escribiendo en el archivo");
		} finally {
			if (outFile != null)
				outFile.close();
		}
	}

	@Override
	public void insertar(Usuario usuario) {
		this.map.put(usuario.getId(), usuario);
		writeToFile(this.map, name);
		logger.info("Usuario insertado al archivo: " + usuario);
	}

	@Override
	public void actualizar(Usuario usuario) {
		// PREGUNTAR A GUSTAVO
		map.put(usuario.getId(), usuario);
		writeToFile(map,name);
		logger.info("Usuario actualizado del archivo: " + usuario);
	}

	@Override
	public void borrar(Usuario usuario) {
		map.remove(usuario.getId(), usuario);
		writeToFile(map, name);
		logger.info("Usuario borrado del archivo: " + usuario);
	}

	@Override
	public TreeMap<String, Usuario> buscarTodos() {
		return map;
	}
	
}
