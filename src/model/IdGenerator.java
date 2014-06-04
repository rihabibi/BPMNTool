package model;

public class IdGenerator {
	private int id = 0;

	public int get_id() {
		id++;
		return id;
	}
}
