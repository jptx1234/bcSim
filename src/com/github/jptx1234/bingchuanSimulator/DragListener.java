package com.github.jptx1234.bingchuanSimulator;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DragListener extends MouseAdapter implements MouseMotionListener {
	Window w;
	Point pre = null;



	public DragListener(Window w) {
		this.w = w;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point newPoint = e.getPoint();
		int off_x = newPoint.x-pre.x;
		int off_y = newPoint.y-pre.y;
		Point wPoint = w.getLocation();
		w.setLocation(wPoint.x+off_x, wPoint.y+off_y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		pre = e.getPoint();
	}




}
