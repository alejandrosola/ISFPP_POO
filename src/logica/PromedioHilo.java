package logica;

import java.util.Random;

import javax.swing.SwingWorker;

import aplicacion.Coordinador;

public class PromedioHilo extends SwingWorker<Void,Void>{
	private Coordinador coordinador;
	private Random random = new Random();
	
	public PromedioHilo (Coordinador coordinador) {
		this.coordinador = coordinador;
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
			coordinador.resultadoPromedio(coordinador.getPromedio());
		return null;
	}


}
