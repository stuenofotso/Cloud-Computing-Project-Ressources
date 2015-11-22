package gpe.tp.forms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import gpe.tp.db.GestionDB;
import gpe.tp.interfaces.crud;

public class BusinessAnnuaire implements crud {
	private GestionDB gdb = new GestionDB();
	private Connection con = null;

	@Override
	public int add(String name, String phone) {
		// TODO Auto-generated method stub
		try {
			gdb.openConnexion();
		} catch (Exception e) {}

		con = gdb.getCon();
		int status = -1;

		String query = "INSERT INTO contact(nom, tel) VALUES(?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement( query );
			ps.setString(1, name);
			ps.setString(2, phone);

			status = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			gdb.closeConnexion();
		}
		
		return status;
	}

	@Override
	public int update(int contactid, String name, String phone) {
		// TODO Auto-generated method stub
		try {
			gdb.openConnexion();
		} catch (Exception e) {}

		con = gdb.getCon();
		int status = -1;

		String query = "UPDATE contact SET nom = ?, tel = ? WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement( query );

			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setInt(3, contactid);

			status = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			gdb.closeConnexion();
		}
		
		return status;
	}

	@Override
	public int delete(int contactid) {
		// TODO Auto-generated method stub
		try {
			gdb.openConnexion();
		} catch (Exception e) {}

		con = gdb.getCon();
		int status = -1;

		String query = "DELETE FROM contact WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement( query );

			ps.setInt(1, contactid);

			status = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			gdb.closeConnexion();
		}
		
		return status;
	}

}
