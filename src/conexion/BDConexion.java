package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class BDConexion {
	private static Connection con = null;
	private static final Logger logger = Logger.getLogger(BDConexion.class);
	
	public static Connection getConnection() {
		try {
			if (con == null) {
				// con esto determinamos cuando finalize el programa
				Runtime.getRuntime().addShutdownHook(new MiShDwnHook());
				ResourceBundle rb = ResourceBundle.getBundle("jdbc");
				String driver = rb.getString("driver");
				String url = rb.getString("url");
				String usr = rb.getString("usr");
				String pwd = rb.getString("pwd");
				Class.forName(driver);
				con = DriverManager.getConnection(url, usr, pwd);
			}
			logger.info("Conexión exitos a la base de datos.");
			return con;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Error al crear la conexión a la base de datos.");
			throw new RuntimeException("Error al crear la conexion", ex);
		}
	}

	public static class MiShDwnHook extends Thread {
		// justo antes de finalizar el programa la JVM invocara
		// a este metodo donde podemos cerrar la conexion
		public void run() {
			try {
				Connection con = BDConexion.getConnection();
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}
}
