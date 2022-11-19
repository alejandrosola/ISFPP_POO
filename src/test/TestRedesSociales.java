package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import aplicacion.Coordinador;
import logica.Calculo;
import logica.RedSocial;
import logica.Subject;
import modelo.Usuario;


class TestRedesSociales {

	private Calculo calculo;
	private Coordinador coordinador;
	private TreeMap<String, Usuario> usuarios;
	
	@BeforeEach
	public void setUp() {
		// Cargar archivos de usuarios y relaciones
		/* 
		Cambiar archivo en config.properties: 
		usuarios = usuariosTest
		relaciones = relacionesTest 
		*/
		Subject subject = new Subject();

		RedSocial redSocial = RedSocial.getRedSocial(subject);
		coordinador = new Coordinador();
		coordinador.setRedSocial(redSocial);
		
		usuarios = coordinador.getArbolUsuarios();
		
		calculo = new Calculo( subject);
		coordinador.setCalculo(calculo);
		calculo.setCoordinador(coordinador);

	}
	
	@Test
	public void testPromedio() {
		assertEquals(1.27, calculo.promedio(), 1);
	}
	
	@Test
	public void testCamino() {
		List<Usuario> camino = new ArrayList<Usuario>();
		camino.add(usuarios.get("1"));
		camino.add(usuarios.get("9"));
		camino.add(usuarios.get("4"));
		
		assertEquals(camino, calculo.rapido(usuarios.get("1"), usuarios.get("4")));
	}
	
	@Test
	public void testInfluyentes() {
		Map<Integer, List<Usuario>> influyentes = new TreeMap<Integer, List<Usuario>>(Collections.reverseOrder());
		
		influyentes.put(2, new ArrayList<Usuario>());
		influyentes.get(2).add(0,usuarios.get("9"));
		influyentes.get(2).add(1,usuarios.get("5"));
		influyentes.get(2).add(2,usuarios.get("4"));
		influyentes.get(2).add(3,usuarios.get("3"));
		
		influyentes.put(1, new ArrayList<Usuario>());
		influyentes.get(1).add(0,usuarios.get("8"));
		influyentes.get(1).add(1,usuarios.get("6"));
		influyentes.get(1).add(2,usuarios.get("2"));
		influyentes.get(1).add(3,usuarios.get("11"));
		influyentes.get(1).add(4,usuarios.get("10"));
		influyentes.get(1).add(5,usuarios.get("1"));
		
		influyentes.put(0, new ArrayList<Usuario>());
		influyentes.get(0).add(0,usuarios.get("7"));
		
		assertEquals(influyentes,calculo.masInfluyentes(11));
	}
	
	@Test
	public void amistades() {
		Map<Usuario, Integer> amistades = new HashMap<Usuario, Integer>();
		
		amistades.put(usuarios.get("5"), 1);
		amistades.put(usuarios.get("9"), 24);
		
		assertEquals(amistades, calculo.getAmistades(usuarios.get("4")));
	
		amistades = new HashMap<Usuario, Integer>();
		
		assertEquals(amistades, calculo.getAmistades(usuarios.get("7")));
	}

	@Test
	public void sugerenciaAmigos() {
		
		Map<Double, List<Usuario>> sugerencia = new TreeMap<Double, List<Usuario>>();
		
		sugerencia.put((22 * 1.5) + (9 * 0.4), new ArrayList<Usuario>());
		sugerencia.get((22 * 1.5) + (9 * 0.4)).add(usuarios.get("2"));
		
		sugerencia.put((90 * 1.5) + (32 * 0.4), new ArrayList<Usuario>());
		sugerencia.get((90 * 1.5) + (32 * 0.4)).add(usuarios.get("1"));
		
		assertEquals(sugerencia,calculo.getSugerencias(usuarios.get("4")));
	}
	
	

}
