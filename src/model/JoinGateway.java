package model;

import java.awt.Color;
import java.awt.Graphics;

public class JoinGateway extends ObjectBPMN {

	public JoinGateway() {
		super();
		max_link_partant = 1;
		max_link_arrivant = -1;
		h = 40;
		l = 40;
	}

	@Override
	public void affiche(Graphics g) {
		int xt[] = { x + (l / 2), x, x + (l / 2), x + l };
		int yt[] = { y, y + (h / 2), y + h, y + (h / 2) };
		// choix de la couleur
		float[] hsbvals = null;
		hsbvals = Color.RGBtoHSB(255, 200, 0, hsbvals);
		g.setColor(Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));

		g.fillPolygon(xt, yt, 4);
		g.setColor(Color.black);
		g.drawPolygon(xt, yt, 4);

		g.drawLine(x + (l / 2), y + 5, x + (l / 2), (y + h) - 5);
		g.drawLine(x + 5, y + (h / 2), (x + l) - 5, y + (h / 2));
		super.affiche(g);
	}
}