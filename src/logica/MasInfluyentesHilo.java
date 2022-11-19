package logica;

import java.util.Random;

import javax.swing.SwingWorker;

import aplicacion.Coordinador;

public class MasInfluyentesHilo extends SwingWorker<Void, Void> {
	private Coordinador coordinador;
	private Random random = new Random();
	private int n;

	public MasInfluyentesHilo(int n, Coordinador coordinador) {
		this.coordinador = coordinador;
		this.n = n;
	}

	@Override
	public Void doInBackground() {
		int i = 0;

		while (i < 100 && !this.isCancelled()) {
			try {
				Thread.sleep(random.nextInt(500) + 50 * n);
				i += random.nextInt(19) + 1;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			coordinador.actualizarBarra(i);
		}
		if (!this.isCancelled())
			coordinador.resultadoInfluyentes(coordinador.getInfluyentes(n), n);
		return null;
	}

}
