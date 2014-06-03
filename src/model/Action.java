package model;

import java.util.ArrayList;


public class Action {
	private String Type;
	private ArrayList <ObjectBPMN> Objects;
	private ArrayList <String> Id;
	public void setType(String type){Type=type;}
	public String getType(){return Type;}
	public void addObject(ObjectBPMN obj) {Objects.add(obj);}
	public void addId(String id){Id.add(id);}
	
	public ArrayList<ObjectBPMN> getObjects() {
		return Objects;
	}
	public void setObjects(ArrayList<ObjectBPMN> objects) {
		Objects = objects;
	}
	public ArrayList<String> getId() {
		return Id;
	}
	public void setId(ArrayList<String> id) {
		Id = id;
	}
}
