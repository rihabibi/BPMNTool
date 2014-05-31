package model;

import java.awt.Color;
import java.awt.Graphics;


public class SplitGateway extends ObjectBPMN{

	public SplitGateway(){
		super();
		max_link_partant=-1;
		max_link_arrivant=1;
		h=40;
		l=40;
	}
	
	public void affiche(Graphics g)
	{
		int xt[] = {x+l/2,x,x+l/2,x+l};
	    int yt[] = {y,y+h/2,y+h,y+h/2};

	 // choix de la couleur
	    float[] hsbvals = null;
	    hsbvals= Color.RGBtoHSB(255, 200, 0, hsbvals);
	    g.setColor(Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));
	    
	    g.fillPolygon(xt, yt, 4);
	    g.setColor(Color.black);
	    g.drawPolygon(xt, yt, 4);
		
		g.drawLine(x+3*l/10, y+3*(h/10),x+7*(l/10), y+7*(h/10));
		g.drawLine(x+3*l/10, y+7*(h/10),x+7*(l/10), y+3*(h/10));
		
		super.affiche(g);
	}
}