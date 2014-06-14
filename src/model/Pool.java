package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Pool {
	private String label;
	private int y = 0, h = 50, l = 0;
	private int id;
	private int[][] couleurs = { { 255, 237, 199 }, { 235, 255, 210 },
			{ 216, 253, 255 }, { 255, 254, 182 } };
	private int couleur = 0;
	private ArrayList<ObjectBPMN> Objects = new ArrayList<ObjectBPMN>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		couleur = id % 4;
	}

	public Pool(String label, int y, int h, int l, int id,
			ArrayList<ObjectBPMN> objects, int coul) {
		super();
		this.label = label;
		this.y = y;
		this.h = h;
		this.l = l;
		this.id = id;
		Objects = objects;
		couleur = id % 4;
	}

	public Pool() {

	}

	public Pool(String lbl, int yi, int hi, int li) {
		setLabel(lbl);
		y = yi;
		h = hi;
		l = li;
		couleur = id % 4;
	}

	public void AddObject(ObjectBPMN o) {
		Objects.add(o);
	}

	public void affiche(Graphics g) {

		g.setColor(new Color(couleurs[couleur][0], couleurs[couleur][1],
				couleurs[couleur][2]));
		g.fillRect(0, y, l, h);
		g.setColor(Color.BLACK);
		g.drawRect(0, y, l, h);
		g.drawRect(0, y, 20, h);
		String s = id + " " + label;
		s = s.toUpperCase();
		int posy = ((y + (h / 2)) - ((s.length() * 12) / 2)) + 12;
		for (int i = 0; i < s.length(); i++) {
			g.drawString("" + s.charAt(i), 3, posy + (i * 12));
		}
		System.out.println("Affiche pool n " + id + "pos y " + y + "hauteur "
				+ h);
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

	public int[][] getCouleurs() {
		return couleurs;
	}

	public void setCouleurs(int[][] couleurs) {
		this.couleurs = couleurs;
	}

	public int getCouleur() {
		return couleur;
	}

	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}

}
