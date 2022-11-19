package aplicacion;

import interfaz.Interfaz;
import interfaz.RelacionForm;
import interfaz.RelacionList;
import interfaz.UsuarioForm;
import interfaz.UsuarioList;
import logica.Calculo;
import logica.RedSocial;
import logica.Subject;

public class Aplicacion {
	
	public static void main(String[] args) {
		Subject subject = new Subject();

		RedSocial redSocial = RedSocial.getRedSocial(subject);
		Coordinador coordinador = new Coordinador();
		coordinador.setRedSocial(redSocial);
		
		
		Calculo calculo = new Calculo(subject);
		calculo.setCoordinador(coordinador);
		coordinador.setCalculo(calculo);
		
		UsuarioList usuarioList = new UsuarioList(coordinador);
		coordinador.setUsuarioList(usuarioList);
		UsuarioForm usuarioForm = new UsuarioForm();
		coordinador.setUsuarioForm(usuarioForm);
		usuarioForm.setCoordinador(coordinador);
		
		RelacionList relacionList = new RelacionList(coordinador);
		coordinador.setRelacionList(relacionList);
		
		RelacionForm relacionForm = new RelacionForm(coordinador);
		coordinador.setRelacionForm(relacionForm);

		Interfaz interfaz = new Interfaz(coordinador);
		coordinador.setInterfaz(interfaz);
		
		coordinador.setRedSocial(redSocial);
		
		interfaz.getFrame().setVisible(true);
	}
}