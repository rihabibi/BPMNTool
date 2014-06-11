package view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.WorkFlow;

public class GraphContainer extends JPanel {
	private WorkFlow work = null;

	public void refresh(WorkFlow graph) {
		work = graph;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (work != null) {
			work.affiche(g);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension;
		if (work != null) {
			dimension = new Dimension(work.getL(), work.getH());
		} else {
			dimension = new Dimension(480, 500);
		}
		System.out.println(dimension);
		return dimension;
	}
}
