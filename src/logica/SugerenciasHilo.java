package logica;

import java.util.Random;

import javax.swing.SwingWorker;

import aplicacion.Coordinador;
import modelo.Usuario;

public class SugerenciasHilo extends SwingWorker<Void,Void>{
	private Coordinador coordinador;
	private Random random = new Random();
	private Usuario u1;
	
	public SugerenciasHilo (Usuario u1, Coordinador coordinador) {
		this.coordinador = coordinador;
		this.u1 = u1;
	}

	@Override
	public Void doInBackground() {
		int i = 0;
		
		while (i < 100 && !this.isCancelled()) {
			try {
				Thread.sleep(random.nextInt(500)+100);
				i+= random.nextInt(19)+1;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			coordinador.actualizarBarra(i);
		}
		if (!this.isCancelled())
			coordinador.resultadoSugerencias(coordinador.getSugerencias(u1));
		return null;
	}


}
