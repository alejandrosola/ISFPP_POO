package servicio;

import java.util.TreeMap;

import conexion.Factory;
import dao.UsuarioDAO;
import modelo.Usuario;

public class UsuarioServicioDAO implements UsuarioServicioInterfaz {
	
	private UsuarioDAO usuarioservicio;
	
	public UsuarioServicioDAO() {
		usuarioservicio = (UsuarioDAO) Factory.getInstancia("USUARIO");
	}

	@Override
	public void insertar(Usuario usuario) {
		usuarioservicio.insertar(usuario);
	}

	@Override
	public void actualizar(Usuario usuario) {
		usuarioservicio.actualizar(usuario);
	}

	@Override
	public void borrar(Usuario usuario) {
		usuarioservicio.borrar(usuario);
	}

	@Override
	public TreeMap<String, Usuario> buscarTodos() {
		return usuarioservicio.buscarTodos();
	}
	
	
	

}
