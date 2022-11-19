package logica;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import aplicacion.Coordinador;
import modelo.Relacion;
import modelo.Usuario;
import net.AdjacencyMapGraph;
import net.Edge;
import net.Graph;
import net.GraphAlgorithms;
import net.PositionalList;
import net.Vertex;

public class Calculo implements Observer {
	final static Logger logger = Logger.getLogger(Calculo.class);

	private Graph<Usuario, Relacion> redSocial;
	private TreeMap<String, Vertex<Usuario>> vertices;
	private Coordinador coordinador;
	private TreeMap<String, Usuario> usuarios;
	private List<Relacion> relaciones;
	private boolean actualizar;
	private Subject subject;
	private ResourceBundle constantes;
	
	public Calculo(Subject subject) {
		this.actualizar = true;
		this.subject = subject;
		this.subject.attach(this);
		constantes = ResourceBundle.getBundle("redes");
	}

	public void cargarGrafo() {
		redSocial = new AdjacencyMapGraph<>(false);

		// Cargar usuarios al grafo
		vertices = new TreeMap<String, Vertex<Usuario>>();
		for (Entry<String, Usuario> usr : usuarios.entrySet())
			vertices.put(usr.getKey(), redSocial.insertVertex(usr.getValue()));

		// Cargar relaciones
		for (Relacion rel : relaciones) {
			try {
				redSocial.insertEdge(vertices.get(rel.getAmigo_origen().getId()),
						vertices.get(rel.getAmigo_destino().getId()), rel);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		actualizar = false;
	}

	/**
	 * Actualiza el grafo si es necesario.
	 * 
	 * @return void
	 */
	private void realizarCalculo() {
		if (actualizar) {
			usuarios = coordinador.getArbolUsuarios();
			relaciones = coordinador.getRelaciones();
			cargarGrafo();
			actualizar = false;
			logger.info("Se actualizaron los datos para realizar calculos");
		} else
			logger.info("No se actualizaron los datos");
	}

	public Usuario getUsuario(String id) {
		realizarCalculo();
		return coordinador.getUsuario(id);
	}

	/**
	 * Devuelve el promedio de la cantidad de relaciones de la red
	 * 
	 * @return promedio
	 */
	public double promedio() {
		realizarCalculo();

		double sum = 0;

		for (Vertex<Usuario> user : redSocial.vertices())
			sum += redSocial.inDegree(user);

		return sum / redSocial.numVertices();
	}

	/**
	 * Devuelve un mapa con la influencia y la lista de usuarios con dicha
	 * influencia.
	 * 
	 * @param n
	 * @return lista de usuarios
	 */
	public Map<Integer, List<Usuario>> masInfluyentes(int n) {
		realizarCalculo();
		if (n > redSocial.numVertices() || n <= 0) {
			throw new IndexOutOfBoundsException();
		}
		Map<Integer, List<Usuario>> influyentes = new TreeMap<Integer, List<Usuario>>(Collections.reverseOrder());

		int arcos;
		for (Vertex<Usuario> v : redSocial.vertices()) {
			arcos = redSocial.outDegree(v);
			if (influyentes.get(arcos) == null)
				influyentes.put(arcos, new ArrayList<Usuario>());
			List<Usuario> temp = influyentes.get(arcos);
			temp.add(0, v.getElement());
			influyentes.put(arcos, temp);
		}

		return influyentes;
	}

	/**
	 * Devuelve una PositionalList con los usuarios que integran el camino mas corto
	 * entre dos usuarios user1 y user2
	 * 
	 * @param user1
	 * @param user2
	 * @return
	 */
	public List<Usuario> rapido(Usuario user1, Usuario user2) {
		realizarCalculo();
		// copia grafo
		Graph<Usuario, Integer> rapido = new AdjacencyMapGraph<>(false);
		Map<Usuario, Vertex<Usuario>> res = new HashMap<>();

		for (Vertex<Usuario> result : redSocial.vertices())
			res.put(result.getElement(), rapido.insertVertex(result.getElement()));

		Vertex<Usuario>[] vert;

		for (Edge<Relacion> result : redSocial.edges()) {
			vert = redSocial.endVertices(result);
			rapido.insertEdge(res.get(vert[0].getElement()), res.get(vert[1].getElement()),
					(int) ChronoUnit.YEARS.between(LocalDate.now(), result.getElement().getTiempo()));
		}

		List<Usuario> answer = new ArrayList<Usuario>();
		PositionalList<Vertex<Usuario>> lista = null;
		try {
			lista = GraphAlgorithms.shortestPathList(rapido, res.get(user1), res.get(user2));
		} catch (IllegalArgumentException e) {
			return answer;
		} catch (NullPointerException e) {
			return answer;
		}

		for (Vertex<Usuario> v : lista)
			answer.add(answer.size(), v.getElement());

		return answer;
	}
	
	/**
	 * Devuelve un mapa con los amigos del usuario dado como clave, y la interacción como valor
	 * @param usuario
	 * @return Map<Usuario, Integer>
	 */

	public Map<Usuario, Integer> getAmistades(Usuario usuario) {
		realizarCalculo();
		Map<Usuario, Integer> answer = new HashMap<>();

		Vertex<Usuario> v = getVertice(usuario);
		for (Edge<Relacion> e : redSocial.incomingEdges(v)) {
			if (e.getElement().getAmigo_destino().equals(usuario))
				answer.put(e.getElement().getAmigo_origen(), e.getElement().getInteraccion());
			else
				answer.put(e.getElement().getAmigo_destino(), e.getElement().getInteraccion());
		}
		return answer;
	}

	/** 
	 * Devuelve el vértice del grafo correspondiente al usuario ingresado como parámetro
	 * 
	 * @param usuario
	 * @return Vertex<Usuario>
	 */
	public Vertex<Usuario> getVertice(Usuario usuario) {
		realizarCalculo();
		for (Vertex<Usuario> v : redSocial.vertices())
			if (v.getElement().equals(usuario))
				return v;
		return null;
	}
	
	
	/**
	 * Devuelve un mapa con el usuario como clave, y una lista de enteros representando la interacción y los likes
	 * en ese orden
	 * 
	 * @param usuario
	 * @return
	 */
	private Map<Usuario, List<Integer>> getAmistadesAtributos(Usuario usuario) {
		realizarCalculo();
		Map<Usuario, List<Integer>> answer = new HashMap<>();

		List<Integer> temp;

		Vertex<Usuario> v = getVertice(usuario);
		for (Edge<Relacion> e : redSocial.incomingEdges(v)) {
			temp = new ArrayList<Integer>();
			temp.add(0, (int) ChronoUnit.YEARS.between(LocalDate.now(), e.getElement().getTiempo()));
			temp.add(1, e.getElement().getInteraccion());
			temp.add(2, e.getElement().getLikes());
			if (e.getElement().getAmigo_destino().equals(usuario))
				answer.put(e.getElement().getAmigo_origen(), temp);
			else
				answer.put(e.getElement().getAmigo_destino(), temp);
		}
		return answer;
	}

	/**
	 * Devuelve las sugerencias de amistades para un usuario dado basándose en sus amigos,
	 * ordenados por interacción.
	 * @param usuario
	 * @return Map<Double, List<Usuario>>
	 */
	public Map<Double, List<Usuario>> getSugerencias(Usuario usuario) {
		realizarCalculo();
		Map<Double, List<Usuario>> answer = new TreeMap<Double, List<Usuario>>(Collections.reverseOrder());
		double peso = 0;

		Vertex<Usuario> v = getVertice(usuario);
		Collection<Usuario> amigos = getAmistades(v.getElement()).keySet();

		for (Usuario amigo2 : amigos) {
			Map<Usuario, List<Integer>> amigos2 = getAmistadesAtributos(amigo2);

			for (Entry<Usuario, List<Integer>> e : amigos2.entrySet()) {
				// Interaccion 1, likes 2
				if (!e.getKey().equals(usuario) && !getAmistades(usuario).containsKey(e.getKey())) {
					peso = e.getValue().get(1) * Double.parseDouble(constantes.getString("constanteInteraccion")) 
							+ e.getValue().get(2) * Double.parseDouble(constantes.getString("constanteLikes")) ;

					if (answer.get(peso) == null) {
						answer.put(peso, new ArrayList<Usuario>());
					}
					List<Usuario> temp = answer.get(peso);
					temp.add(e.getKey());
					answer.put(peso, temp);
				}
			}
		}
		return answer;
	}

	/** 
	 * Devuelve los usuarios más densamente conectados de acuerdo a su interacción
	 * @return Map<Integer, List<Usuario>>
	 */
	public Map<Integer, List<Usuario>> densamenteConectados() {
		realizarCalculo();

		Map<Integer, List<Usuario>> densos = new TreeMap<Integer, List<Usuario>>(Collections.reverseOrder());
		int suma = 0;

		for (Vertex<Usuario> v : redSocial.vertices()) {
			for (Edge<Relacion> e : redSocial.incomingEdges(v))
				suma += e.getElement().getInteraccion();

			if (densos.get(suma) == null)
				densos.put(suma, new ArrayList<Usuario>());

			List<Usuario> temp = densos.get(suma);
			temp.add(0, v.getElement());
			densos.put(suma, temp);

			suma = 0;
		}

		return densos;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	/**
	 * Devuelve una lista con los ids de todos los usuarios
	 * @return List<String>
	 */
	public List<String> getListaIds() {
		realizarCalculo();
		List<String> usuarios = new ArrayList<String>();
		for (Vertex<Usuario> v : redSocial.vertices()) {
			usuarios.add(usuarios.size(), v.getElement().getId());
		}
		return usuarios;
	}

	public TreeMap<String, Usuario> getUsuarios() {
		realizarCalculo();
		return this.usuarios;
	}

	/**
	 * Devuelve una lista de usuarios
	 * @return List<Usuario>
	 */
	public List<Usuario> getUsuariosLista() {
		realizarCalculo();

		List<Usuario> ans = new ArrayList<Usuario>();
		for (Vertex<Usuario> v : redSocial.vertices()) {
			ans.add(v.getElement());
		}
		return ans;
	}

	/**
	 * Devuelve la relación entre dos usuarios, null si no existe
	 * @param idUserD
	 * @param idUserO
	 * @return Relacion
	 */
	public Relacion getRelacion(String idUserD, String idUserO) {
		realizarCalculo();
		for (Relacion r : relaciones)
			if ((r.getAmigo_destino().getId().equals(idUserD) && r.getAmigo_origen().getId().equals(idUserO))
					|| (r.getAmigo_origen().getId().equals(idUserD) && r.getAmigo_destino().getId().equals(idUserO)))
				return r;
		return null;
	}

	public int getCantidadUsuarios() {
		realizarCalculo();
		return vertices.size();
	}

	@Override
	public void update() {
		actualizar = true;
	}

}
