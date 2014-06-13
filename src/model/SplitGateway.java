package model;

import java.awt.Color;
import java.awt.Graphics;

public class SplitGateway extends ObjectBPMN {

	public SplitGateway() {
		super();
		max_link_partant = -1;
		max_link_arrivant = 1;
		h = 40;
		l = 40;
	}

	@Override
	public void affiche(Graphics g) {
		int xt[] = { x + (l / 2), x, x + (l / 2), x + l };
		int yt[] = { y, y + (h / 2), y + h, y + (h / 2) };
		// choix de la couleur

		g.setColor(new Color(255, 230, 63));

		g.fillPolygon(xt, yt, 4);
		g.setColor(Color.black);
		g.drawPolygon(xt, yt, 4);

		g.drawLine(x + (l / 2), y + 5, x + (l / 2), (y + h) - 5);
		g.drawLine(x + 5, y + (h / 2), (x + l) - 5, y + (h / 2));
		super.affiche(g);
	}
}