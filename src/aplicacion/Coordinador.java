package aplicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.SwingWorker;

import interfaz.Interfaz;
import interfaz.RelacionForm;
import interfaz.RelacionList;
import interfaz.UsuarioForm;
import interfaz.UsuarioList;
import logica.Calculo;
import logica.RedSocial;
import modelo.Relacion;
import modelo.Usuario;
import util.RelacionRepetidaException;

public class Coordinador {
	private Interfaz interfaz;
	private UsuarioList usuarioList;
	private UsuarioForm usuarioForm;
	private Calculo calculo;
	private RelacionForm relacionForm;
	private RelacionList relacionList;
	private RedSocial redSocial;
	private SwingWorker<Void, Void> hilo;
	
	public Interfaz getInterfaz() {
		return interfaz;
	}
	
	public void setRedSocial(RedSocial redSocial) {
		this.redSocial = redSocial;
	}
	
	public void setInterfaz(Interfaz interfaz) {
		this.interfaz = interfaz;
	}
	
	public void setCalculo(Calculo calculo) {
		this.calculo = calculo;
	}

	public double getPromedio() {
		return calculo.promedio();
	}

	public Usuario getUsuario(String id) {
		return redSocial.buscarUsuario(id);
	}

	public String[] getIds() {
		List<String> listaIds = calculo.getListaIds();
		String[] arrayIds;
		arrayIds = listaIds.toArray(new String[listaIds.size()]);
		return arrayIds;
	}

	public List<Usuario> getCamino(Usuario user1, Usuario user2) {
		return calculo.rapido(user1, user2);
	}

	public Map<Integer, List<Usuario>> getInfluyentes(int n) {
		return calculo.masInfluyentes(n);
	}

	public Map<Usuario, Integer> getAmigos(Usuario usuario) {
		return calculo.getAmistades(usuario);
	}

	public Map<Double, List<Usuario>> getSugerencias(Usuario usuario) {
		return calculo.getSugerencias(usuario);
	}

	public TreeMap<String, Usuario> getArbolUsuarios() {
		return redSocial.getUsuarios();
	}
	
	public List<Usuario> getUsuarios() {

		List<Usuario> usuariosList = new ArrayList<Usuario>();
		
		for (Entry<String, Usuario> e : redSocial.getUsuarios().entrySet()) {
			usuariosList.add(e.getValue());
		}
		
		return usuariosList;
	}

	public void mostrarUsuarioList() {
		usuarioList.loadTable();
		usuarioList.setVisible(true);
	}

	public void setUsuarioList(UsuarioList usuarioList) {
		this.usuarioList = usuarioList;
	}

	public void actualizarModeloBorrar(String id) {
		interfaz.actualizarModeloBorrar(id);
		relacionForm.actualizarModeloBorrar(id);
	}
	
	public void actualizarModeloAgregar(String id) {
		interfaz.actualizarModeloAgregar(id);
		relacionForm.actualizarModeloAgregar(id);
	}

	public void cancelarUsuario() {
		usuarioForm.setVisible(false);
	}

	public void borrarUsuario(Usuario usuario) {
		List<Relacion> relaciones = this.buscarRelaciones(usuario);
		
		for (Relacion r : relaciones) {
			this.borrarRelacion(r);
		}
		redSocial.borrarUsuario(usuario);
	}

	private List<Relacion> buscarRelaciones(Usuario usuario) {
		return redSocial.buscarRelaciones(usuario);
	}

	public int getCantidadUsuarios() {
		return calculo.getCantidadUsuarios();
	}

	public void insertarUsuario(Usuario usuario) {
		redSocial.agregarUsuario(usuario);
		usuarioList.actualizarAgregar(usuario);
		usuarioForm.setVisible(false);
	}

	public void modificarUsuario(String id,Usuario usuario) {
		redSocial.modificarUsuario(usuario);
		usuarioList.setAccion(Constantes.MODIFICAR);
		usuarioList.setUsuario(usuario);
		usuarioForm.setVisible(false);
	}

	public void setUsuarioForm(UsuarioForm usuarioForm) {
		this.usuarioForm = usuarioForm;
	}
	
	public UsuarioForm getUsuarioForm() {
		return this.usuarioForm;
	}
	
	public void insertarUsuarioForm() {
		usuarioForm.accion(Constantes.INSERTAR, null);
		usuarioForm.setVisible(true);
	}

	public void modificarUsuarioForm(Usuario usuario) {
		usuarioForm.accion(Constantes.MODIFICAR, usuario);
		usuarioForm.setVisible(true);
		
	}
	
	public void modificarRelacion(Relacion relacion) {
		redSocial.modificarRelacion(relacion);
		relacionList.setAccion(Constantes.MODIFICAR);
		relacionList.setRelacion(relacion);
		relacionForm.setVisible(false);
	}

	public void insertarRelacionForm() {
		relacionForm.accion(Constantes.INSERTAR, null);
		relacionForm.setVisible(true);
		
	}
	
	public void modificarRelacionForm(Relacion relacion) {
		relacionForm.accion(Constantes.MODIFICAR, relacion);
		relacionForm.setVisible(true);
	}

	public void setRelacionForm(RelacionForm relacionForm) {
		this.relacionForm = relacionForm;
	}
	
	public void setRelacionList(RelacionList relacionList) {
		this.relacionList = relacionList;
	}

	public List<Relacion> getRelaciones() {
		return redSocial.getRelaciones();
	}

	public Relacion getRelacion(String idUserD, String idUserO) {
		return calculo.getRelacion(idUserD, idUserO);
	}

	public void borrarRelacion(Relacion relacion) {
		redSocial.borrarRelacion(relacion);
	}
	
	public void cancelarRelacion() {
		relacionForm.setVisible(false);
	}

	public void insertarRelacion(Relacion relacion) throws RelacionRepetidaException {
		redSocial.agregarRelacion(relacion);			
		relacionList.actualizarAgregar(relacion);
		relacionForm.setVisible(false);
		
	}

	public void mostrarRelacionList() {	
		relacionList.loadTable();
		relacionList.setVisible(true);	
	}

	public void resultadoPromedio(double promedio) {
		interfaz.mostrarPromedio(promedio);
	}
	
	public void ejecutarHilo(SwingWorker<Void, Void> sw) {
		this.hilo = sw;
		hilo.execute();
	}
	
	public void cancelarHilo() {
		this.hilo.cancel(true);
	}

	public void resultadoInfluyentes(Map<Integer, List<Usuario>> influyentes, int n) {
		interfaz.mostrarInfluyentes(influyentes, n);
	}

	public void resultadoRapido(List<Usuario> camino, Usuario u1, Usuario u2) {
		interfaz.mostrarCamino(u1, u2, camino);
	}

	public void resultadoAmigos(Map<Usuario, Integer> amigos) {
		interfaz.mostrarAmigos(amigos);
	}

	public void resultadoSugerencias(Map<Double, List<Usuario>> sugerencias) {
		interfaz.mostrarSugerencias(sugerencias);
	}

	public void actualizarBarra(int i) {
		interfaz.actualizarBarra(i);
	}
	
}
