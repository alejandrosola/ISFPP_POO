package servicio;

import java.util.List;

import modelo.Relacion;

public interface RelacionServicio {
	
	void insertar(Relacion relacion);

	void actualizar(Relacion relacion);

	void borrar(Relacion relacion);

	List<Relacion> buscarTodos();
}
