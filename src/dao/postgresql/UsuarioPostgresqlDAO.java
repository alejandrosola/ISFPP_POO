package dao.postgresql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import conexion.BDConexion;
import dao.UsuarioDAO;
import modelo.Usuario;

public class UsuarioPostgresqlDAO implements UsuarioDAO {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	public TreeMap<String, Usuario> buscarTodos(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			int mayorId = 0;
			String sql = "SELECT id, nombre, fechanacimiento, genero, ciudad FROM bd2022p.usuarios_grupo2 ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			TreeMap<String, Usuario> ret = new TreeMap<String, Usuario>();
			while (rs.next()) {
				String id = rs.getString("id");
				if (Integer.parseInt(rs.getString("id")) > mayorId)
					mayorId = Integer.parseInt(id);
				
				ret.put(rs.getString("id"), new Usuario(rs.getString("id"), rs.getString("nombre"), rs.getDate("fechanacimiento").toLocalDate(),
						rs.getString("genero"), rs.getString("ciudad")));				
			}
			Usuario.contadorIds = mayorId + 1;
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
				logger.info("Usuarios cargados con exito");
			} catch (Exception ex) {
				logger.info("Error al cargar los usuarios");
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	public void insertar(Usuario usuario){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="INSERT INTO bd2022p.usuarios_grupo2 (id, nombre, fechanacimiento, genero, ciudad) ";
			sql+="VALUES(?,?,?,?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, usuario.getId());
			pstm.setString(2, usuario.getNombre());
			pstm.setDate(3, Date.valueOf(usuario.getFechaNac()));
			pstm.setString(4,usuario.getGenero());
			pstm.setString(5, usuario.getCiudad());			
			pstm.executeUpdate();
			logger.info("Usuario insertado a la base de datos: " + usuario);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}		
	}

	public void actualizar(Usuario usuario){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="UPDATE bd2022p.usuarios_grupo2 ";
			sql+="SET nombre = ?, fechanacimiento = ?, genero = ?, ciudad = ? ";
			sql+="WHERE id = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, usuario.getNombre());
			pstm.setDate(2, Date.valueOf(usuario.getFechaNac()));			
			pstm.setString(3, usuario.getGenero());
			pstm.setString(4, usuario.getCiudad());
			pstm.setString(5, usuario.getId());
			pstm.executeUpdate();
			logger.info("Usuario modificado de la base de datos: " + usuario);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Error al modificar al usuario de la base de datos: " + usuario);
				throw new RuntimeException(ex);
			}
		}		
	}
	
	public void borrar(Usuario usuario){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="DELETE FROM bd2022p.usuarios_grupo2 WHERE id = ? ";		
			pstm = con.prepareStatement(sql);		
			pstm.setString(1,usuario.getId());
			pstm.executeUpdate();
			logger.info("Usuario borrado de la base de datos: " + usuario);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Error al borrar al usuario de la base de datos: " + usuario);
				throw new RuntimeException(ex);
			}
		}		
	}

}
