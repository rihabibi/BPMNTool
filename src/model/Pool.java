package model;

import java.awt.Graphics;
import java.util.ArrayList;


public class Pool {
	private String label;
	private int y = 0, h = 0, l = 0;
	private ArrayList<ObjectBPMN> Objects = new ArrayList<ObjectBPMN>();

	
	
	public Pool(String label, int y, int h, int l, ArrayList<ObjectBPMN> objects) {
		super();
		this.label = label;
		this.y = y;
		this.h = h;
		this.l = l;
		Objects = objects;
	}

	public Pool()
	{
		
	}
	
	public Pool(String lbl, int yi, int hi, int li) {
		setLabel(lbl);
		y = yi;
		h = hi;
		l = li;
	}

	public void AddObject(ObjectBPMN o) {
		Objects.add(o);
	}

	public void affiche(Graphics g) {
		g.drawRect(0, y, l, h);
		g.drawRect(0, y, 20, h);
		int posy = (y + (h / 2)) - (label.length() / 2);
		for (int i = 0; i < label.length(); i++) {
			g.drawString("" + label.charAt(i), 3, posy + (i * 12));
		}
	}

	public void delete_obj(int id) {
		for (int i = 0; i < Objects.size(); i++) {
			if (Objects.get(i).getId() == id) {
				Objects.remove(i);
			}
		}
	}

	// Accesseurs

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<ObjectBPMN> getObjects() {
		return Objects;
	}

	public void setObjects(ArrayList<ObjectBPMN> objects) {
		Objects = objects;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

}
