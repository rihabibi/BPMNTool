package model;

import java.awt.Color;
import java.awt.Graphics;

public class Start extends ObjectBPMN {

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
		float[] hsbvals = null;
		hsbvals = Color.RGBtoHSB(130, 255, 130, hsbvals);
		g.setColor(Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));
		g.fillOval(x, y, h, l);

		g.setColor(Color.black);
		g.drawOval(x, y, h, l);
		String s = "Start";
		g.drawString(s, (x + (l / 2)) - ((5 * s.length()) / 2), y + h + 12); // positionne
																				// le
																				// texte
																				// centr�
		g.drawString(label, ((x + (l / 2)) - ((5 * label.length()) / 2)) + 15,
				y + h + 12); // positionne le label centr�
		super.affiche(g);
	}
}
