package logica;

import java.util.Random;

import javax.swing.SwingWorker;

import aplicacion.Coordinador;
import modelo.Usuario;

public class RapidoHilo extends SwingWorker<Void, Void> {
	private Coordinador coordinador;
	private Random random = new Random();
	private Usuario u1;
	private Usuario u2;

	public RapidoHilo(Usuario u1, Usuario u2, Coordinador coordinador) {
		this.coordinador = coordinador;
		this.u1 = u1;
		this.u2 = u2;
	}

	@Override
	public Void doInBackground() {
		int i = 0;

		while (i < 100 && !this.isCancelled()) {
			try {
				Thread.sleep(random.nextInt(500) + 100);
				i += random.nextInt(19) + 1;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			coordinador.actualizarBarra(i);
		}
		if (!this.isCancelled())
			coordinador.resultadoRapido(coordinador.getCamino(u1, u2), u1, u1);
		return null;
	}

}
