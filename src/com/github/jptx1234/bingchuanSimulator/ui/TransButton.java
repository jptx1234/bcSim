package com.github.jptx1234.bingchuanSimulator.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.github.jptx1234.bingchuanSimulator.BingchuanSimulator;

public class TransButton extends JLabel implements MouseListener {
	private static final long serialVersionUID = 1L;
	// protected EventListenerList listenerList = new EventListenerList();
	Color foreColor = BingchuanSimulator.foreColor;
	Color backColor = BingchuanSimulator.transColor;
	public static Border overBorder = BorderFactory.createLineBorder(BingchuanSimulator.foreColor, 3, true);
	public static Border emptyBorder = BorderFactory.createLineBorder(BingchuanSimulator.foreColor, 1, true);
	protected boolean mouseIn = false;
	protected boolean disable = false;
	protected volatile boolean stopAutoAction = true;

	public TransButton(String text) {
		super(text, JLabel.CENTER);
		setBorder(emptyBorder);
		addMouseListener(this);
		setForeground(foreColor);
		setFont(BingchuanSimulator.font);
		setFocusable(true);
	}

	public void setEnable(boolean enable) {
		this.disable = !enable;
	}

	/**
	 *
	 * @param delay 延时（毫秒）
	 */
	public void autoAction(int delay) {
		autoAction(delay, false, "");
	}

	/**
	 *
	 * @param delay 延时（毫秒）
	 * @param tips 按钮上秒数后的文字，如：tips = " 秒后自动返回"，倒计时显示“5 秒后自动返回”，“4 秒后自动返回”
	 */
	public void autoAction(int delay, String tips) {
		autoAction(delay, true, tips);
	}

	private void autoAction(int delay, boolean showTips, String tips) {
		if (delay < 0) {
			return;
		}
		stopAutoAction = false;
		TransButton button = this;
		new Thread(new Runnable() {

			@Override
			public void run() {
				String oldText = getText();
				int time = delay;
				for (time = delay; time >= 1000; time -= 1000) {
					if (showTips) {
						setText(String.valueOf(time/ 1000) + tips);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					if (stopAutoAction) {
						break;
					}
				}
				if (showTips) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							setText(oldText);
						}
					});
				}
				if (!stopAutoAction) {
					try {
						Thread.sleep(time);  //休眠不能被1000整除的毫秒
					} catch (InterruptedException e) {
					}
				}
				if (!stopAutoAction) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0,
									0, 0, 1, false));
						}
					});
				}
			}
		}).start();
	}

	// Copy from AbstractButton
	protected void fireActionPerformed(ActionEvent event) {
		stopAutoAction = true;
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				if (e == null) {
					String actionCommand = event.getActionCommand();
					if (actionCommand == null) {
						actionCommand = getText();
					}
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand, event.getWhen(),
							event.getModifiers());
				}
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}


	public void addActionListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}


	public ActionListener[] getActionListeners() {
        return listenerList.getListeners(ActionListener.class);
    }

	public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

	@Override
	public void setBorder(Border border) {
		SwingUtilities.invokeLater(() -> {
			super.setBorder(border);
		});
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (disable) {
			return;
		}
		fireActionPerformed(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText(), e.getWhen(), e.getModifiers()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (disable) {
			return;
		}
		setBorder(emptyBorder);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (disable) {
			return;
		}
		if (mouseIn) {
			setBorder(overBorder);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (disable) {
			return;
		}
		mouseIn = true;
		setBorder(overBorder);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (disable) {
			return;
		}
		mouseIn = false;
		setBorder(emptyBorder);
	}

}
