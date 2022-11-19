package dao.postgresql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import conexion.BDConexion;
import conexion.Factory;
import dao.RelacionDAO;
import dao.UsuarioDAO;
import modelo.Relacion;
import modelo.Usuario;

public class RelacionPostgresqlDAO implements RelacionDAO {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	private UsuarioDAO usuarioservicio;
	private TreeMap<String, Usuario> usuarios;
	
	public RelacionPostgresqlDAO() {
		usuarioservicio = (UsuarioDAO) Factory.getInstancia("USUARIO");
		usuarios = usuarioservicio.buscarTodos();
	}
	
	public List<Relacion> buscarTodos(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT usuario_destino, usuario_origen, tiempo, interaccion, likes FROM bd2022p.relaciones_grupo2 ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			
			List<Relacion> ret = new ArrayList<Relacion>();
			
			while (rs.next())
				ret.add(new Relacion(rs.getDate("tiempo").toLocalDate(), usuarios.get(rs.getString("usuario_destino")),
						usuarios.get(rs.getString("usuario_origen")), rs.getInt("interaccion"), rs.getInt("likes")));
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
				logger.info("Relaciones cargadas con exito");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	public void insertar(Relacion relacion){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="INSERT INTO bd2022p.relaciones_grupo2 (usuario_destino, usuario_origen, tiempo, interaccion, likes) ";
			sql+="VALUES(?,?,?,?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, relacion.getAmigo_destino().getId());
			pstm.setString(2, relacion.getAmigo_origen().getId());
			pstm.setDate(3, Date.valueOf(relacion.getTiempo()));
			pstm.setInt(4, relacion.getInteraccion());
			pstm.setInt(5, relacion.getLikes());			
			pstm.executeUpdate();
			logger.info("Relacion insertada a la base de datos: " + relacion);
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
				logger.info("Error al insertar la relacion a la base de datos: " + relacion);
				throw new RuntimeException(ex);
			}
		}		
	}

	public void actualizar(Relacion relacion){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="UPDATE bd2022p.relaciones_grupo2 ";
			sql+="SET interaccion = ?, likes = ? ";
			sql+="WHERE (usuario_destino = ? and usuario_origen = ?) OR (usuario_destino = ? and usuario_origen = ?) ";
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, relacion.getInteraccion());
			pstm.setInt(2, relacion.getLikes());			
			pstm.setString(3, relacion.getAmigo_destino().getId());
			pstm.setString(4, relacion.getAmigo_origen().getId());
			pstm.setString(5, relacion.getAmigo_origen().getId());
			pstm.setString(6, relacion.getAmigo_destino().getId());
			pstm.executeUpdate();
			logger.error("Relacion modificada de la base de datos: " + relacion);
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
				logger.error("Error al modificar la relacion en la base de datos: " + relacion);
				throw new RuntimeException(ex);
			}
		}		
	}
	
	public void borrar(Relacion relacion){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="DELETE FROM bd2022p.relaciones_grupo2 WHERE (usuario_destino = ? and usuario_origen = ?) OR"
					+ " (usuario_destino = ? and usuario_origen = ?)";		
			pstm = con.prepareStatement(sql);
			pstm.setString(1, relacion.getAmigo_destino().getId());
			pstm.setString(2, relacion.getAmigo_origen().getId());
			pstm.setString(3, relacion.getAmigo_origen().getId());
			pstm.setString(4, relacion.getAmigo_destino().getId());
			pstm.executeUpdate();
			logger.info("Relacion borrada de la base de datos: " + relacion);
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
				logger.error("Error al eliminar la relacion de la base de datos: " + relacion);
				throw new RuntimeException(ex);
			}
		}		
	}

}
