package com.github.jptx1234.bingchuanSimulator.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.github.jptx1234.bingchuanSimulator.BingchuanSimulator;

public class TransCheckBox extends JLabel implements MouseListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String checked = "√ ";
	public static final String unchecked = "X ";

	Color foreColor = BingchuanSimulator.foreColor;
	Color backColor = BingchuanSimulator.transColor;

	private boolean isChecked;



	public TransCheckBox(String text,boolean isChecked){
		if (isChecked) {
			text = checked+text;
		}else {
			text = unchecked+text;
		}
		this.isChecked = isChecked;
		setText(text);
		setHorizontalAlignment(JLabel.CENTER);
		setForeground(foreColor);
		setBackground(backColor);
		addMouseListener(this);
		setFont(BingchuanSimulator.font);

	}

	public void setChecked(boolean isChecked){
		this.isChecked = isChecked;
	}

	public boolean isChecked(){
		return isChecked;
	}

	//Copy from AbstractButton
	protected void fireActionPerformed(ActionEvent event) {
		String text = getText();
		if (isChecked) {
			setText(text.replaceFirst(checked, unchecked));
		}else{
			setText(text.replaceFirst(unchecked, checked));
		}
		isChecked = !isChecked;
	        // Guaranteed to return a non-null array
	        Object[] listeners = listenerList.getListenerList();
	        ActionEvent e = null;
	        // Process the listeners last to first, notifying
	        // those that are interested in this event
	        for (int i = listeners.length-2; i>=0; i-=2) {
	            if (listeners[i]==ActionListener.class) {
	                // Lazily create the event:
	                if (e == null) {
	                      String actionCommand = event.getActionCommand();
	                      if(actionCommand == null) {
	                         actionCommand = getText();
	                      }
	                      e = new ActionEvent(this,
	                                          ActionEvent.ACTION_PERFORMED,
	                                          actionCommand,
	                                          event.getWhen(),
	                                          event.getModifiers());
	                }
	                ((ActionListener)listeners[i+1]).actionPerformed(e);
	            }
	        }
	    }

	public void addActionListener(ActionListener listener){
		listenerList.add(ActionListener.class, listener);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText(),e.getWhen(),e.getModifiers()));
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}


}
