package dao.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import conexion.Factory;
import dao.RelacionDAO;
import dao.UsuarioDAO;
import modelo.Relacion;
import modelo.Usuario;

public class RelacionArchivoDAO implements RelacionDAO {
	
	private List<Relacion> list;
	private String name;
	private final Logger logger = Logger.getLogger(this.getClass());
	
	private Hashtable<String, Usuario> usuarios;
	private List<Relacion> relaciones;
	
	public RelacionArchivoDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("relaciones");
		usuarios = cargarUsuarios();
		list = readFromFile(name);
	}
	
	private List<Relacion> readFromFile(String fileName) {
		Scanner read = null;
		List<Relacion> relaciones = new ArrayList<Relacion>();
		try {
			read = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		read.useDelimiter("\\s*;\\s*");
		Usuario usr1, usr2;
		int anio, mes, dia, interaccion, likes;
		LocalDate tiempo = null;

		while (read.hasNext()) {
			usr1 = usuarios.get(read.next());
			usr2 = usuarios.get(read.next());
			anio = read.nextInt();
			mes = read.nextInt();
			dia = read.nextInt();
			tiempo = LocalDate.of(anio, mes, dia);
			interaccion = read.nextInt();
			likes = read.nextInt();
			relaciones.add(0, new Relacion(tiempo, usr1, usr2, interaccion, likes));
		}
		read.close();
		logger.info("Relaciones cargadas con exito");
		return relaciones;
	}
	
	private void writeToFile(List<Relacion> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Relacion e : list) {
				outFile.format("%s;%s;%d;%d;%d;%s;%s;\n", e.getAmigo_destino().getId(), e.getAmigo_origen().getId(),
						e.getTiempo().getYear(), e.getTiempo().getMonthValue(), e.getTiempo().getDayOfMonth(), 
						e.getInteraccion(), e.getLikes());
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
	
	private Hashtable<String, Usuario> cargarUsuarios() {
		Hashtable<String, Usuario> usuarios = new Hashtable<String, Usuario>();
		// Es más eficiente porque si ya hay un UsuarioDAO instanciado, usa ese
		UsuarioDAO usuarioDAO = (UsuarioDAO) Factory.getInstancia("USUARIO");
		
		for (Entry<String, Usuario> e : usuarioDAO.buscarTodos().entrySet())
			usuarios.put(e.getValue().getId(), e.getValue());
		return usuarios;
	}
	
	@Override
	public void insertar(Relacion relacion) {
		list.add(relacion);
		writeToFile(list, name);
		logger.info("Relacion insertada al archivo: " + relacion);
	}

	@Override
	public void actualizar(Relacion relacion) {
		int pos = list.indexOf(relacion);
		list.set(pos, relacion);
		writeToFile(list,name);
		logger.info("Relacion modificada del archivo: " + relacion);
	}

	@Override
	public void borrar(Relacion relacion) {
		list.remove(relacion);
		writeToFile(list,name);
		logger.info("Relacion borrada del archivo: " + relacion);
	}

	@Override
	public List<Relacion> buscarTodos() {
		return list;
	}
}
