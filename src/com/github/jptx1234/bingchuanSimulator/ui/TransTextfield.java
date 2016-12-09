package com.github.jptx1234.bingchuanSimulator.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import com.github.jptx1234.bingchuanSimulator.BingchuanSimulator;

@SuppressWarnings("serial")
public class TransTextfield extends JTextField implements FocusListener{
	Font font = BingchuanSimulator.font;
	Color foreColor = BingchuanSimulator.foreColor;
	Color tipColor;
	String tip;
	boolean tipShowed = false;
	int thick = 1;

	public TransTextfield(){
		setLook();
	}

	public TransTextfield(String text){
		setText(text);
		setLook();
	}

	private void setLook(){
		setBackground(BingchuanSimulator.transColor);
		setForeground(foreColor);
		setBorder(new MatteBorder(0, 0, thick, 0, BingchuanSimulator.foreColor));
		setFont(font);
		setOpaque(false);
		setCaretColor(foreColor);
	}

	@Override
	public void setFont(Font f) {
		super.setFont(f);
		this.font = f;
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		foreColor = fg;
	}

	public void setTip(String tip){
		tipColor = new Color(foreColor.getRed(),foreColor.getGreen(),foreColor.getBlue(),150);
		this.tip = tip;
		addFocusListener(this);
		if (getText().length() == 0) {
			focusLost(null);
		}
	}

	public String getTip(){
		return tip == null ? "" : tip;
	}

	@Override
	public void setText(String t) {
		super.setText(t);
		if (tip == null || !t.equals(tip)) {
			tipShowed = false;
			setForeground(foreColor);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (tipShowed) {
			tipShowed = false;
			setForeground(foreColor);
			setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (getText().length() == 0) {
			tipShowed = true;
			super.setForeground(tipColor);
			setText(tip);
		}

	}


}
