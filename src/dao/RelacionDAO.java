package dao;

import java.util.List;

import modelo.Relacion;

public interface RelacionDAO {
	void insertar(Relacion relacion);
	void actualizar(Relacion relacion);
	void borrar(Relacion relacion);
	
	List<Relacion> buscarTodos();
}
