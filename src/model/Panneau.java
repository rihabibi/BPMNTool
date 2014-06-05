package model;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Panneau extends JPanel {
	WorkFlow work;

	public Panneau(WorkFlow w) {
		work = w;
	}

	@Override
	public void paintComponent(Graphics g) {
		work.affiche(g);
	}
}