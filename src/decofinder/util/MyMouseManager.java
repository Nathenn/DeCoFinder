package decofinder.util;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.util.DefaultMouseManager;

public class MyMouseManager extends DefaultMouseManager implements MouseWheelListener {

	public void init(GraphicGraph graph, MyView view) {
		this.view = view;
		this.graph = graph;
		view.addMouseListener(this);
		view.addMouseMotionListener(this);
		view.addMouseWheelListener(this);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.getWheelRotation() < 0){
			this.view.getCamera().setViewPercent(Math.max(0.0001f, view.getCamera().getViewPercent() * 0.9f));
		}else if(arg0.getWheelRotation() > 0){
			this.view.getCamera().setViewPercent(view.getCamera().getViewPercent() * 1.1f);
		}
		
	}}
