package model;

public class IdGenerator {
	private int id = 0;

	public IdGenerator()
	{
		
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int news_id()
	{
		id++;
		System.out.println("new id: "+id);
		return id;
	}
	
}
