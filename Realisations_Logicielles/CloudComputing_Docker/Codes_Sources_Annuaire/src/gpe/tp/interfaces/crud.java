package gpe.tp.interfaces;

public interface crud {
	public int add(String name, String phone);
	public int update(int contactid, String name, String phone);
	public int delete(int contactid);
}
