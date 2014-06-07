package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
include=JsonTypeInfo.As.PROPERTY,
property="type")
@JsonSubTypes({
@JsonSubTypes.Type(value=Start.class, name="start"),
@JsonSubTypes.Type(value=End.class, name="end"),
@JsonSubTypes.Type(value=Task.class, name="task"),
@JsonSubTypes.Type(value=JoinGateway.class, name="joinGateway"),
@JsonSubTypes.Type(value=SplitGateway.class, name="splitGateway"),
})

public abstract class ObjectBPMN {
	protected int id;
	protected int x, y; // position
	protected int l, h; // largeur/hauteur
	protected int ligne, colone; // utilisï¿½ pour le placement
	protected int prio = -1;
	protected String label = "";
	protected boolean optimised;

	protected ArrayList<Integer> links_partant = new ArrayList<Integer>(); // liens
																					// partant
	protected ArrayList<Integer> links_arrivant = new ArrayList<Integer>(); // liens
																					// arrivant
	int max_link_partant, max_link_arrivant; // nombre de liens max dï¿½finis
												// pour chaque type d'objet
												// hï¿½ritï¿½

	
	public ObjectBPMN() {
		x = 0;
		y = 0;
		ligne = 0;
		colone = 0;
	}

	
	public boolean isOptimised() {
		return optimised;
	}


	public void setOptimised(boolean optimised) {
		this.optimised = optimised;
	}



	public ObjectBPMN(int id, int x, int y, int l, int h, int ligne,
			int colone, int prio, String label, boolean optimised,
			ArrayList<Integer> links_partant,
			ArrayList<Integer> links_arrivant, int max_link_partant,
			int max_link_arrivant) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.l = l;
		this.h = h;
		this.ligne = ligne;
		this.colone = colone;
		this.prio = prio;
		this.label = label;
		this.optimised = optimised;
		this.links_partant = links_partant;
		this.links_arrivant = links_arrivant;
		this.max_link_partant = max_link_partant;
		this.max_link_arrivant = max_link_arrivant;
	}





	@Override
	public String toString() {
		return label;
	}

	// link un objet ï¿½ l'objet actuel en partant de l'objet actuel
	public void linker_partant(Integer o) {
		links_partant.add(o);
	}

	// link un objet ï¿½ l'objet actuel en partant de cet objet
	public void linker_arrivant(Integer o) {
		links_arrivant.add(o);
	}

	// permet de savoir s'il est possible de linker un objet partant de l'objet
	// actuel
	public boolean linkable_partant() {
		if (max_link_partant == -1) {
			return true;
		}
		if (this.links_partant.size() < max_link_partant) {
			return true;
		} else {
			return false;
		}
	}

	// permet de savoir s'il est possible de linker un objet partant d'un autre
	// objet
	public boolean linkable_arrivant() {
		if (max_link_arrivant == -1) {
			return true;
		}
		if (this.links_arrivant.size() < max_link_arrivant) {
			return true;
		} else {
			return false;
		}
	}

	// permet de supprimer le lien vers un objet
	public void unlink(Integer o) {
		links_partant.remove(o);
		links_arrivant.remove(o);
	}

	// affichage
	public void affiche(Graphics g) {

	}

	public void affiche_link(Graphics g, WorkFlow wf) {
		// affichage de l'id
		g.drawString(Integer.toString(id), x - 10, y - 1);

		int px1, px2, py1, py2;
		g.setColor(Color.BLUE);
		// gestion de l'affichge des liens sortants
		for (int i = 0; i < links_partant.size(); i++) {
			ObjectBPMN o = wf.get_objet(links_partant.get(i));
			if (o.getX() > (x + l)) // dessin si le deuxieme objet est ï¿½
									// droite du premier
			{
				px1 = x + l;
				px2 = o.getX();
				py1 = y + (h / 2);
				py2 = o.getY() + (o.getH() / 2);
				g.drawLine(px1, py1, px2 - 5, py2);

				int x[] = { px2, px2 - 5, px2 - 5 };
				int y[] = { py2, py2 - 5, py2 + 5 };
				g.fillPolygon(x, y, 3);
			} else // dessin si l'objet est en dessous ou au dessus du premier
			{
				px1 = x + (l / 2) + (prio * 4);
				px2 = o.getX() + (o.getL() / 2) + (prio * 4);

				if (o.getY() < y) // is l'objet est au dessus
				{
					py1 = y;
					py2 = o.getY() + o.getH();

					g.drawLine(px1, py1, px2, py2 + 5);
					int x[] = { px2, px2 - 5, px2 + 5 };
					int y[] = { py2, py2 + 5, py2 + 5 };
					g.fillPolygon(x, y, 3);
				} else {
					py1 = y + h;
					py2 = o.getY();

					g.drawLine(px1, py1, px2, py2 - 5);

					int x[] = { px2, px2 - 5, px2 + 5 };
					int y[] = { py2, py2 - 5, py2 - 5 };
					g.fillPolygon(x, y, 3);
				}

			}

		}
		g.setColor(Color.black);

	}

	// /////////////
	// Accesseurs
	// ////////////

	public int getId() {
		return id;
	}

	public void setId(int id) {
		//System.out.println(id);
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ArrayList<Integer> getLinks_partant() {
		return links_partant;
	}

	public void setLinks_partant(ArrayList<Integer> links_partant) {
		this.links_partant = links_partant;
	}

	public ArrayList<Integer> getLinks_arrivant() {
		return links_arrivant;
	}

	public void setLinks_arrivant(ArrayList<Integer> links_arrivant) {
		this.links_arrivant = links_arrivant;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getMax_link_partant() {
		return max_link_partant;
	}

	public void setMax_link_partant(int max_link_partant) {
		this.max_link_partant = max_link_partant;
	}

	public int getMax_link_arrivant() {
		return max_link_arrivant;
	}

	public void setMax_link_arrivant(int max_link_arrivant) {
		this.max_link_arrivant = max_link_arrivant;
	}

	public int getLigne() {
		return ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	public int getColone() {
		return colone;
	}

	public void setColone(int colone) {
		this.colone = colone;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	

}
