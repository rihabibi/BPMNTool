package model;

import java.util.ArrayList;

public class Action {
	private String Type;
	private ArrayList<ObjectBPMN> Objects= new ArrayList<ObjectBPMN>();
	private ArrayList<String> Id= new ArrayList<String>();
	private Start stt;
	private End nd;

	public Start getStt() {
		return stt;
	}

	public void setStt(Start stt) {
		this.stt = stt;
	}

	public End getNd() {
		return nd;
	}

	public void setNd(End nd) {
		this.nd = nd;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getType() {
		return Type;
	}

	public void addObject(ObjectBPMN obj) {
		Objects.add(obj);
	}

	public void addId(String id) {
		Id.add(id);
	}

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
