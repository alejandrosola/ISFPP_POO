package dao;

import java.util.TreeMap;

import modelo.Usuario;

public interface UsuarioDAO {
	void insertar(Usuario usuario);
	void actualizar(Usuario usuario);
	void borrar(Usuario usuario);
	
	TreeMap<String, Usuario> buscarTodos();
}
