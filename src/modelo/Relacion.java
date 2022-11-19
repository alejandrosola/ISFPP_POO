package modelo;

import java.time.LocalDate;

/**
 * La clase Relacion representa relaciones entre usuarios indicando los dos usuarios,
 * el tiempo de amistad en d�as, la cantidad de interaccion diaria entre ellos, y la cantidad de likes
 * 
 * @author Alejandro
 *
 */
public class Relacion {
	private Usuario amigo_destino;
	private Usuario amigo_origen;
	private LocalDate tiempo;		// En dias
	private int interaccion;		// Interaccion diaria
	private int likes;				// Likes
	
	/**
	 * Crea una nueva relacion entre dos usuarios amigo_destino y amigo_origen
	 * con los par�metros indicados
	 * 
	 * @param tiempo
	 * Tiempo de amistad en d�as
	 * @param amigo_destino
	 * Usuario
	 * @param amigo_origen
	 * Usuario
	 * @param interaccion
	 * Interacci�n diaria
	 * @param likes
	 */
	public Relacion(LocalDate tiempo, Usuario amigo_destino, Usuario amigo_origen, int interaccion, int likes) {
		super();
		this.tiempo = tiempo;
		this.amigo_destino = amigo_destino;
		this.amigo_origen = amigo_origen;
		this.interaccion = interaccion;
		this.likes = likes;
	}


	/**
	 * Devuelve en tiempo de amistad de la Relacion
	 * @return
	 * tiempo
	 */
	public LocalDate getTiempo() {
		return tiempo;
	}

	/**
	 * Permite modificar el tiempo de amistad de la Relacion
	 * @param tiempo
	 * Nuevo tiempo de amistad
	 */
	public void setTiempo(LocalDate tiempo) {
		this.tiempo = tiempo;
	}

	/**
	 * Devuelve el amigo destino de la Relacion
	 * @return
	 * amigo_destino
	 */
	public Usuario getAmigo_destino() {
		return amigo_destino;
	}

	/**
	 * Permite modificar el amigo destino de la Relacion
	 * @param amigo_destino
	 * Nuevo amigo destino
	 */
	public void setAmigo_destino(Usuario amigo_destino) {
		this.amigo_destino = amigo_destino;
	}

	/**
	 * Devuelve el amigo origen de la Relacion
	 * @return
	 * amigo_origen
	 */
	public Usuario getAmigo_origen() {
		return amigo_origen;
	}

	/**
	 * Permite modificar el amigo origen de la Relacion
	 * @param amigo_origen
	 * Nuevo amigo origen
	 */
	public void setAmigo_origen(Usuario amigo_origen) {
		this.amigo_origen = amigo_origen;
	}

	/**
	 * Devuelve la interacci�n en d�as de la Relacion
	 * @return
	 * interaccion
	 */
	public int getInteraccion() {
		return interaccion;
	}

	/**
	 * Permite modificar la interacci�n en d�as de la Relacion
	 * @param interaccion
	 * Nueva interacci�n diaria
	 */
	public void setInteraccion(int interaccion) {
		this.interaccion = interaccion;
	}
	
	/**
	 * Devuelve la cantidad de likes de la Relacion
	 * @return
	 * likes
	 */
	public int getLikes() {
		return this.likes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amigo_destino == null) ? 0 : amigo_destino.hashCode());
		result = prime * result + ((amigo_origen == null) ? 0 : amigo_origen.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relacion other = (Relacion) obj;
		
		if (amigo_origen == null || amigo_destino == null)
			return false;
		
		if (amigo_origen.equals(other.amigo_origen) && amigo_destino.equals(other.amigo_destino))
			return true;
		if (amigo_destino.equals(other.amigo_origen) && amigo_origen.equals(other.amigo_destino))
			return true;
		return false;
	}


	@Override
	public String toString() {
		return "Relacion [amigo_destino=" + amigo_destino.getId() + ", amigo_origen=" + amigo_origen.getId()
				+ ", tiempo=" + tiempo.toString()
				+ ", interaccion=" + interaccion + ", likes=" + likes + "]";
	}
	
	
	
	
	
}