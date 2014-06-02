package view;

import java.awt.Graphics;

import javax.swing.JPanel;

import model.WorkFlow;

public class GraphContainer extends JPanel {
	private WorkFlow work;

	public void refresh(WorkFlow graph) {
		work = graph;
	}

	@Override
	public void paintComponent(Graphics g) {
		work.affiche(g);
	}
}
