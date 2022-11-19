package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import aplicacion.Constantes;
import modelo.Relacion;
import modelo.Usuario;
import servicio.RelacionServicioDAO;
import servicio.RelacionServicio;
import servicio.UsuarioServicioDAO;
import servicio.UsuarioServicioInterfaz;
import util.RelacionRepetidaException;

public class RedSocial {

	private static RedSocial redSocial = null;
	private Subject subject;

	private String nombre;
	private TreeMap<String, Usuario> usuarios;
	private UsuarioServicioInterfaz usuarioService;
	private List<Relacion> relaciones;
	private RelacionServicio relacionService;

	public static RedSocial getRedSocial(Subject subject) {
		if (redSocial == null)
			redSocial = new RedSocial(subject);
		return redSocial;
	}

	private RedSocial(Subject subject) {
		super();
		this.subject = subject;
		usuarios = new TreeMap<String, Usuario>();
		usuarioService = new UsuarioServicioDAO();
		usuarios.putAll(usuarioService.buscarTodos());
		relaciones = new ArrayList<Relacion>();
		relacionService = new RelacionServicioDAO();
		relaciones.addAll(relacionService.buscarTodos());
	}

	/**
	 * Agrega un usuario al mapa, indica que se debe actualizar el grafo
	 * @param usuario
	 */
	public void agregarUsuario(Usuario usuario) {
		subject.refresh();
		usuario.setId(Integer.toString(Usuario.contadorIds++));
		usuarios.put(usuario.getId(), usuario);
		usuarioService.insertar(usuario);
	}
	
	/**
	 * Modifica un usuario al mapa, indica que se debe actualizar el grafo
	 * @param usuario
	 */
	public void modificarUsuario(Usuario usuario) {
		subject.refresh();
		usuarios.put(usuario.getId(), usuario);
		usuarioService.actualizar(usuario);
	}
	
	/**
	 * Elimina un usuario al mapa, indica que se debe actualizar el grafo
	 * @param usuario
	 */
	public void borrarUsuario(Usuario usuario) {
		subject.refresh();
		Usuario user = buscarUsuario(usuario);
		usuarios.remove(user.getId());
		usuarioService.borrar(usuario);
	}

	public Usuario buscarUsuario(Usuario usuario) {
		return usuarios.get(usuario.getId());
	}

	/**
	 * Agrega una relacion a la lista, indica que se debe actualizar el grafo
	 * @param relacion
	 * @throws RelacionRepetidaException
	 */
	public void agregarRelacion(Relacion relacion) throws RelacionRepetidaException {
		subject.refresh();
		String id1 = relacion.getAmigo_destino().getId();
		String id2 = relacion.getAmigo_origen().getId();
		for (Relacion r : relaciones) {
			if ((r.getAmigo_destino().getId().equals(id1) && r.getAmigo_origen().getId().equals(id2))
					|| (r.getAmigo_origen().getId().equals(id1) && r.getAmigo_destino().getId().equals(id2))) {
				throw new RelacionRepetidaException(Constantes.RELACIONEXISTENTE);
			}
		}

		relaciones.add(relacion);
		relacionService.insertar(relacion);
	}

	/**
	 * Modifica una relacion a la lista, indica que se debe actualizar el grafo
	 * @param relacion
	 * @throws RelacionRepetidaException
	 */
	public void modificarRelacion(Relacion relacion) {
		subject.refresh();
		int pos = relaciones.indexOf(relacion);
		relaciones.set(pos, relacion);
		relacionService.actualizar(relacion);
	}

	/**
	 * Elimina una relacion a la lista, indica que se debe actualizar el grafo
	 * @param relacion
	 * @throws RelacionRepetidaException
	 */
	public void borrarRelacion(Relacion relacion) {
		subject.refresh();
		Relacion relac = relaciones.get(relaciones.indexOf(relacion));
		relaciones.remove(relac);
		relacionService.borrar(relacion);
	}

	public List<Relacion> getRelaciones() {
		return relaciones;
	}

	public TreeMap<String, Usuario> getUsuarios() {
		return usuarios;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Busca todas las relaciones del usuario dado
	 * @param usuario
	 * @return List<Relacion>
	 */
	public List<Relacion> buscarRelaciones(Usuario usuario) {
		List<Relacion> relacionesRespuesta = new ArrayList<Relacion>();
		for (Relacion relacion : relaciones) {
			if (relacion.getAmigo_destino().equals(usuario) || relacion.getAmigo_origen().equals(usuario)) {
				relacionesRespuesta.add(relacion);
			}
		}
		return relacionesRespuesta;
	}

	public Usuario buscarUsuario(String id) {
		return usuarios.get(id);
	}

}
