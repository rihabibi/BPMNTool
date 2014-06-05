package model;

import java.awt.Color;
import java.awt.Graphics;

public class Task extends ObjectBPMN {

	public Task()
	{
		
	}
	public Task(String lbl) {
		super();
		max_link_partant = 1;
		max_link_arrivant = 1;
		if (lbl.length() <= 15) {
			l = (lbl.length() * 6) + 10;
			h = 25;
		} else {
			l = 100;
			h = 25 * ((lbl.length() / 15) + 1);
			System.out.println(h);
		}

		label = lbl;
	}

	@Override
	public void affiche(Graphics g) {
		// choix de la couleur
		float[] hsbvals = null;
		hsbvals = Color.RGBtoHSB(50, 200, 200, hsbvals);
		g.setColor(Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));
		g.fillRoundRect(x, y, l, h, 10, 10);

		g.setColor(Color.black);
		g.drawRoundRect(x, y, l, h, 10, 10);

		// affichage du label par ligne
		for (int i = 0; i <= (label.length() / 15); i++) {
			String str = "";
			if (((i + 1) * 15) < label.length()) {
				str = label.substring(i * 15, (i + 1) * 15);
			} else {
				str = label.substring(i * 15, label.length());
			}
			// si le premier char de la ligne est un espace on le retire
			if (str.charAt(0) == ' ') {
				str = str.substring(1, str.length());
			}
			g.drawString(str, x + 5, y + 15 + (15 * i));
		}

		super.affiche(g);
	}
}
