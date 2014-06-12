package view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.WorkFlow;

public class GraphContainer extends JPanel {
	private WorkFlow work = null;

	public void refresh(WorkFlow graph) {
		work = graph;
		this.setPreferredSize(new Dimension(work.getL(),work.getH()));
	}

	@Override
	public void paintComponent(Graphics g) {
		if (work != null) {
			super.paintComponent(g);
			work.affiche(g);
		}
	}
}
