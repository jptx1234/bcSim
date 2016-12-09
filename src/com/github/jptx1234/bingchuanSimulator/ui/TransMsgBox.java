package com.github.jptx1234.bingchuanSimulator.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.github.jptx1234.bingchuanSimulator.DragListener;
import com.github.jptx1234.bingchuanSimulator.BingchuanSimulator;

@SuppressWarnings("serial")
public class TransMsgBox extends JDialog {
	public static final String YES = "是";
	public static final String NO = "否";
	public static final String CANCEL = "取消";
	public static final String CONFIRM = "确定";
	private static final Color BACKCOLOR = new Color(51, 51, 51, 210);
	int minIns = 40;
	TransMsgBox box = this;
	String choice;

	private TransMsgBox(JFrame owner, String msg, String[] buttons, int width, int heigh) {
		super(owner, true);
		setUndecorated(true);
		setBackground(BACKCOLOR);
		setLayout(null);
		DragListener dragListener = new DragListener(this);
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
		getRootPane().setBorder(new LineBorder(BingchuanSimulator.foreColor));
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(BingchuanSimulator.transColor);
		topPanel.setOpaque(false);

		JLabel msgLabel = new JLabel(msg, SwingConstants.CENTER);
		msgLabel.setFont(BingchuanSimulator.font);
		msgLabel.setForeground(BingchuanSimulator.foreColor);
		topPanel.add(msgLabel, "Center");
		add(topPanel);
		topPanel.setSize(width == 0 ? (int) msgLabel.getMinimumSize().getWidth() + 10 : width,
				heigh == 0 ? (int) msgLabel.getMinimumSize().getHeight() + 20 : heigh * 2 / 3);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, heigh == 0 ? 0 : 15));
		buttonPanel.setBackground(BingchuanSimulator.transColor);
		buttonPanel.setOpaque(false);
		int buttonSize = 10 * (buttons.length + 1);
		for (String buttonText : buttons) {
			TransButton button = new TransButton(buttonText);
			buttonPanel.add(button);
			int btnWidth = (int) (button.getMinimumSize().getWidth() + 40);
			button.setPreferredSize(new Dimension(btnWidth, (int) button.getMinimumSize().getHeight() + 5));
			buttonSize += btnWidth;
			button.addActionListener(e -> {
				choice = e.getActionCommand();
				box.setVisible(false);
				box.dispose();
			});
		}
		buttonPanel.setSize(buttonSize, (heigh == 0 ? 100 : heigh) - topPanel.getHeight());
		add(buttonPanel);

		int maxWidth = Math.max(topPanel.getWidth(), buttonPanel.getWidth());
		setSize(width == 0 ? maxWidth + minIns : width,
				heigh == 0 ? topPanel.getHeight() + buttonPanel.getHeight() : heigh);
		topPanel.setSize(getWidth(), getHeight() * 2 / 3);
		buttonPanel.setSize(getWidth(), getHeight() - topPanel.getHeight());
		topPanel.setLocation(0, 0);
		buttonPanel.setLocation(0, topPanel.getHeight());
		setLocationRelativeTo(owner);
	}

	public static void showConfirm(JFrame owner, String msg) {
		showMulti(owner, msg, new String[] { CONFIRM });
	}

	public static String showYesNoCancel(JFrame owner, String msg) {
		return showMulti(owner, msg, new String[] { YES, NO, CANCEL });
	}

	public static String showYesNo(JFrame owner, String msg) {
		return showMulti(owner, msg, new String[] { YES, NO });
	}

	public static String showMulti(JFrame owner, String msg, String[] buttonText) {
		return showMulti(owner, msg, buttonText, 0, 0);
	}

	public static String showMulti(JFrame owner, String msg, String[] buttonText, int width, int heigh) {
		TransMsgBox box = new TransMsgBox(owner, msg, buttonText, width, heigh);
		box.setVisible(true);
		return box.choice;
	}

}
