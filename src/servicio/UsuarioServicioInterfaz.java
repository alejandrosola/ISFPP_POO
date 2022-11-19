package servicio;

import java.util.TreeMap;

import modelo.Usuario;

public interface UsuarioServicioInterfaz {
	
	void insertar(Usuario usuario);

	void actualizar(Usuario usuario);

	void borrar(Usuario usuario);
	
	TreeMap<String, Usuario> buscarTodos();

}
