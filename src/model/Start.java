package model;

import java.awt.Color;
import java.awt.Graphics;

public class Start extends ObjectBPMN {
	public Start() {

	}

	public Start(String lbl) {
		super();
		max_link_partant = 1;
		max_link_arrivant = 0;
		h = 40;
		l = 40;
		label = lbl;
	}

	@Override
	public void affiche(Graphics g) {

		// choix de la couleur

		g.setColor(new Color(119, 238, 119));
		g.fillOval(x, y, h, l);

		g.setColor(Color.black);
		g.drawOval(x, y, h, l);

		g.drawString(label, (x + (l / 2)) - ((5 * label.length()) / 2), y + h
				+ 12); // positionne
		// le
		// texte
		// centr�
		super.affiche(g);
	}
}
