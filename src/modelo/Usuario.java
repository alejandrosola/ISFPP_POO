package modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 * La clase Usuario representa un usuario de la red social indicando
 * su id, nombre, edad, genero y ciudad
 * @author Alejandro
 *
 */
public class Usuario {
	private String id;
	private String nombre;
	private LocalDate fechaNac;
	private String genero;
	private String ciudad;
	public static int contadorIds;
	
	/**
	 * Crea un nuevo Usuario con los par�metros dados
	 * 
	 * @param id
	 * Id del usuario
	 * @param nombre
	 * Nombre del usuario
	 * @param edad
	 * Edad del usuario
	 * @param genero
	 * G�nero del usuario
	 * @param ciudad
	 * Ciudad en la que vive el usuario
	 */
	public Usuario(String id, String nombre, LocalDate fechaNac, String genero, String ciudad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechaNac = fechaNac;
		this.genero = genero;
		this.ciudad = ciudad;
	}
	/**
	 * Devuelve el id del Usuario
	 * @return
	 * Id del usuario
	 */
	public String getId() {
		return id;
	}

	/**
	 * Permite modificar el id del Usuario
	 * @param id
	 * Nueva id del usuario
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre del Usuario
	 * @return
	 * Nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Permite modificar el nombre del Usuario
	 * @param nombre
	 * Nuevo nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la edad del Usuario
	 * @return
	 * Edad del usuario
	 */
	public LocalDate getFechaNac() {
		return fechaNac;
	}
	
	/**
	 * Permite modificar la edad del Usuario
	 * @param edad
	 * Nueva edad del usuario
	 */
	public void setFechaNac(LocalDate fecha) {
		this.fechaNac = fecha;
	}

	/**
	 * Devuelve el g�nero del Usuario
	 * @return
	 * G�nero del usuario
	 */
	public String getGenero() {
		return genero;
	}
	
	/**
	 * Permite modificar el g�nero del Usuario
	 * @param genero
	 * Nuevo g�nero del usuario
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * Devuelve la ciudad en la que vive el Usuario
	 * @return
	 * Ciudad en la que vive el usuario
	 */
	public String getCiudad() {
		return ciudad;
	}
	
	/**
	 * Permite modificar la ciudad en la que vive el Usuario
	 * @param ciudad
	 * Nueva ciudad en la que vive el usuario
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", fechaNac=" + fechaNac + ", genero=" + genero + ", ciudad="
				+ ciudad + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
}
