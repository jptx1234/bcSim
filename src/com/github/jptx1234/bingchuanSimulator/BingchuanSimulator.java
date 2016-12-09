package com.github.jptx1234.bingchuanSimulator;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import com.github.jptx1234.bingchuanSimulator.ui.TransButton;
import com.github.jptx1234.bingchuanSimulator.ui.TransCheckBox;
import com.github.jptx1234.bingchuanSimulator.ui.TransMsgBox;
import com.github.jptx1234.bingchuanSimulator.ui.TransTextfield;
import com.github.jptx1234.bingchuanSimulator.util.MACIPGetter;


public final class BingchuanSimulator {

	public static final String VERSION = "bcSimulator beta 0.1";
	public static final String ARCH = System.getProperty("os.arch");
	public static final String CLIENTVERSION = "411";
	public static final String OSNAME = System.getProperty("os.name");
	public static Color transColor = new Color(0, 0, 0, 0);
	public static Color foreColor = new Color(255, 255, 255, 255);
	public static Font font = null;
	public static int effectDelay = 10;
	public static String defaultConfig = "账户=|密码=|路由器IP=|路由器MAC=|认证成功后最小化=true|认证心跳延时=30|启动时自动认证=false|认证时添加路由=true";
	public static boolean traySupport = SystemTray.isSupported();
	private static volatile HashMap<String, String> settingMap = new HashMap<>();
	public static volatile boolean authOK = false;
	private static String configPath = "bcSim.ini";
	static boolean ch = true;
	String shadowImage = "/resources/shadowBG.png";
	String trayIconFile = "/resources/bcSim64.png";
	JLabel stateJLabel;
	AuthThread authThread;
	JFrame w;
	TransButton startButton;
	TransCheckBox miniAfterAuthBox;
	TransTextfield userNameTextfield;
	JPasswordField passwordField;
	TransTextfield ipTextfield;
	TransTextfield macTextfield;
	String routerIP = "";
	String routerMAC = "";
	slideTransButton settingButton;
	TransTextfield timeSettingTextfield;
	TransCheckBox authOnStartCheckBox;
	TransCheckBox routeOnAuthCheckBox;
	TransButton saveButton;
	TransButton cancelButton;
	transJPanel mainPanel;
	transJPanel settingPanel;
	MenuItem startItem;
	boolean authStart = false;
	public static TrayIcon trayIcon;
	boolean configReaded;

	public static void main(String[] args) {
		new BingchuanSimulator();
		for (String string : args) {
			if (string.indexOf("fuckBC") != -1) {
				ch = false;
				break;
			}
		}
	}

	public BingchuanSimulator() {
		// initUI();
		init();
		initAction();
		if (Boolean.valueOf(getConfig("启动时自动认证"))) {
			startButton.autoAction(0);
		}
	}

	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			System.setProperty("awt.useSystemAAFontSettings", "on");
			System.setProperty("swing.aatext", "true");
		} catch (Exception e) {
		}
		font = initFont();
		w = new JFrame(VERSION);
		w.setLayout(null);
		w.setUndecorated(true);
		w.setBackground(transColor);
		ImagePanel shadowPanel = new ImagePanel(shadowImage);
		Container container = w.getContentPane();
		container.setLayout(null);
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.add(shadowPanel, new Integer(0));
		shadowPanel.setLocation(0, 0);
		int xOFF = (shadowPanel.getWidth() - 500) / 2;
		int yOFF = (shadowPanel.getHeight() - 300) / 2;
		transJPanel contentPanel = new transJPanel(null);
		layeredPane.add(contentPanel, new Integer(1));
		contentPanel.setBounds(xOFF, yOFF, 500, 300);
		container.add(layeredPane);
		layeredPane.setBounds(shadowPanel.getBounds());
		initDefaultConfig();
		configReaded = readConfig();
		transJPanel titleJPanel = initTitlePanel(500);
		contentPanel.add(titleJPanel);
		titleJPanel.setLocation(0, 0);
		int width = titleJPanel.getWidth();
		int height = 300 - titleJPanel.getHeight();
		mainPanel = initMainPanel(width, height);
		contentPanel.add(mainPanel);
		mainPanel.setLocation(0, titleJPanel.getHeight());
		settingPanel = initSettingPanel(width, height);
		contentPanel.add(settingPanel);
		if (traySupport) {
			initTray(trayIconFile);
		}

		w.setBounds(shadowPanel.getBounds());
		w.setLocationRelativeTo(null);
		w.setResizable(false);
		w.setVisible(true);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DragListener dragListener = new DragListener(w);
		w.addMouseListener(dragListener);
		w.addMouseMotionListener(dragListener);

	}

	private Font initFont() {
		// UIDefaults uidefs = UIManager.getLookAndFeelDefaults();
		// Iterator<Object> iterator =uidefs.keySet().iterator();
		// while (iterator.hasNext()) {
		// Object s = iterator.next();
		// Object o;
		// if ((o = uidefs.get(s)) instanceof Font) {
		// System.out.print(((Font)(o)).getFontName());
		// System.out.println("-----"+s);
		// }
		// }
		// Font font = new
		// Font(UIManager.getLookAndFeelDefaults().getFont("MenuItem.font",Locale.SIMPLIFIED_CHINESE).getFontName(Locale.SIMPLIFIED_CHINESE),
		// Font.PLAIN, 14);
		// System.out.println(font.getName());
		Font font = null;
		String fName = "/resources/LTH.ttf";
		try {
			InputStream is = BingchuanSimulator.class.getResourceAsStream(fName);
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(15f);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(fName + " not loaded.  Using serif font.");
			font = new Font("微软雅黑", Font.PLAIN, 14);
		}
		return font;
		// return new Font("方正兰亭黑简体", Font.PLAIN, 15);
	}

	private transJPanel initMainPanel(int width, int height) {
		transJPanel mainPanel = new transJPanel(null);
		mainPanel.setSize(width, height);
		int blankWidth = 50;

		transJPanel topJPanel = new transJPanel(null);
		mainPanel.add(topJPanel);
		topJPanel.setLocation(0, 0);
		topJPanel.setSize(width, height * 2 / 3);

		transJPanel userNamePanel = new transJPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		JLabel userNameLabel = new JLabel("账 户");
		userNameLabel.setForeground(foreColor);
		userNameLabel.setFont(font);
		userNameTextfield = new TransTextfield(getConfig("账户") == null ? "" : getConfig("账户"));
		userNameTextfield.setColumns(18);
		userNamePanel.add(userNameLabel);
		userNamePanel.add(userNameTextfield);
		userNamePanel.setSize(width - 2 * blankWidth, userNameLabel.getMinimumSize().height + 10);

		transJPanel passwordPanel = new transJPanel(userNamePanel.getLayout());
		JLabel passwordLabel = new JLabel("密 码");
		passwordLabel.setFont(font);
		passwordLabel.setForeground(foreColor);
		passwordField = new JPasswordField();
		passwordField.setText(EncrUtil.decrypt(getConfig("密码")));
		passwordField.setForeground(foreColor);
		passwordField.setCaretColor(foreColor);
		passwordField.setFont(font);
		passwordField.setBackground(transColor);
		passwordField.setOpaque(false);
		passwordField.setColumns(userNameTextfield.getColumns());
		passwordField.setBorder(userNameTextfield.getBorder());
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		passwordPanel.setSize(userNamePanel.getSize());

		// miniAfterAuthBox = new
		// TransCheckBox("认证成功后最小化",Boolean.valueOf(getConfig("认证成功后最小化")));
		// topJPanel.add(miniAfterAuthBox);
		// miniAfterAuthBox.setSize(miniAfterAuthBox.getMinimumSize());

		try {
			startButton = new TransButton("上  线") {
				private static final long serialVersionUID = 1L;
				private final Color transButtonColor = new Color(foreColor.getRed(), foreColor.getGreen(),
						foreColor.getBlue(), 80);
				private final Image bor = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(
						"iVBORw0KGgoAAAANSUhEUgAAASgAAAAyCAMAAADY47ZiAAAAS1BMVEUAAADz8/Pz8/Px8fHx8fH29vb39/f09PT19fX5+fn4+Pjw8PDv7+/v7+/v7+/u7u719fXu7u7u7u7w8PDw8PDy8vLu7u77+/vy8vIM1QyFAAAAGXRSTlMAv7KUheHoytP172E1VUEO2ioGfmqfHPusS14JSwAAAZ5JREFUaN7t20myrCAQBdBr0YMK9rX/lf7yV9RrfCLMvWdghKEjgkwzBXAitmESqbmnRc49agzCaJ1so5p7ssq7LYkWl8ZgvRUtbq4V1qWAvFk7MYJeVqHUnHvWeAH6iNKbFSc6ZQbQD9G4Dn9IJ0EHUkscSN+D/ui8OA5dBzrRu+nX7cb5lNGpgC+rYn7Kki7iozGgnNGa7zqTdcEVF/CmWWdekgn/Bce25VJUAbvECVUgLF4GB7q26vY9XFRgxH5h5FXFXtS3/09XNjig1aAi3yMoUJGeMTGXV0gSogEVmQXPB6jo8eBA1Q7UwtCrCj0m8ypKIiRQkQ4sOKu4HiNbmLLWjWyKa4i0X7iyUKQEgFZH0KW4DXixjL2CRWHXedCVqAN2I9eJr0mFt9mtoKxVzVxSr2EMPkY/gTKkH/AlaG77yehchx/kxo1kp/rjl25SnFMn+k3iYNqYp47GSUluny6L1gecGBol2fZ9k84gY1Z+Ae1W4VMYkdXpLfHQ0CCMtl3xJeu9suZxU41VuvaEWeyDXJ6PW3oucm5x4h9rXxLcRbnikgAAAABJRU5ErkJggg==")));

				@Override
				protected void paintBorder(Graphics g) {
					if (mouseIn && getBorder().equals(overBorder)) {
//						g.setColor(transColor);
//						g.fillRect(0, 0, getWidth(), getHeight());
						g.setColor(transButtonColor);
						g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 2, getWidth() / 6, getHeight());
					} else {
						// g.setColor(transColor);
						// g.fillRoundRect(0, 0, getWidth() - 1, getHeight() -
						// 2, getWidth() / 6, getHeight());
						// g.setColor(foreColor);
						// g.drawRoundRect(0, 0, getWidth() - 1, getHeight() -
						// 2, getWidth() / 6, getHeight());
						g.drawImage(bor, 0, 0, null);
					}
				}

			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		startButton.setSize(296, 50);
		startButton.setFont(font.deriveFont(Font.BOLD, font.getSize() + 5));

		topJPanel.add(userNamePanel);
		userNamePanel.setLocation(blankWidth, 40);
		topJPanel.add(passwordPanel);
		passwordPanel.setLocation(blankWidth, userNamePanel.getY() + userNamePanel.getHeight() + 15);
		topJPanel.add(startButton);
		startButton.setLocation(blankWidth + 54, passwordPanel.getY() + passwordPanel.getHeight() + 15);

		settingButton = new slideTransButton("        设置");
		settingButton.setHorizontalAlignment(SwingConstants.LEFT);
		settingButton.setSize(100, 30);
		topJPanel.add(settingButton);
		settingButton.setLocation(-settingButton.getWidth() / 5,
				startButton.getHeight() / 2 + startButton.getY() - settingButton.getHeight() / 2);

		transJPanel stateJPanel = new transJPanel(new BorderLayout());
		stateJLabel = new JLabel("请点击“上线”", SwingConstants.CENTER) {
			private static final long serialVersionUID = 1976089438037976857L;

			Font f = new Font("微软雅黑", font.getStyle(), font.getSize() - 2);

			@Override
			public void setText(String text) {
				int len = text.length();
				if (len > 40) {
					StringBuilder sb = new StringBuilder("<html><center>");
					for (int i = 0; i <= len; i += 40) {
						sb.append(text.subSequence(i, i + 40 >= len ? len : i + 40));
						sb.append("<br>");
					}
					sb.append("</center></html>");
					text = sb.toString();
					setFont(f);
				} else {
					setFont(font);
				}
				super.setText(text);
			}
		};
		stateJLabel.setForeground(foreColor);
		stateJPanel.add(stateJLabel, "Center");

		mainPanel.add(stateJPanel);
		stateJPanel.setLocation(0, topJPanel.getHeight());
		stateJPanel.setSize(width, height - topJPanel.getHeight());
		stateJPanel.setBorder(new MatteBorder(2, 0, 0, 0, foreColor));

		return mainPanel;
	}

	private transJPanel initTitlePanel(int width) {
		int buttonSize = 20;
		transJPanel titlePanel = new transJPanel(null);
		titlePanel.setSize(width, buttonSize + 5);
		JLabel titleLabel = new JLabel(w.getTitle());
		titleLabel.setFont(font);
		titlePanel.add(titleLabel);
		titleLabel.setForeground(foreColor);
		titleLabel.setSize(titleLabel.getMinimumSize());
		titleLabel.setLocation(10, 5);
		titleLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isPopupTrigger()) {
					TransMsgBox.showMulti(w,
							"<html><br>" + VERSION + "<br>Java version:" + System.getProperty("java.version")
									+ "<br>OS:" + OSNAME + " " + ARCH
									+ "<br>Font: FZLanTingHei-R-GBK,Founder Corporation.<br>Background image designed by Sanadas Young.</html>",
							new String[] { TransMsgBox.CONFIRM }, 400, 170);
				}
			}
		});

		TransButton minimumButton = new TransButton("-") {
			private static final long serialVersionUID = 1976089438037976857L;

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setBorder(emptyBorder);
			}
		};
		minimumButton.setFont(font);
		titlePanel.add(minimumButton);
		minimumButton.setSize(buttonSize, buttonSize);
		minimumButton.setLocation(titlePanel.getWidth() - (buttonSize + 5) * 2, 2);
		minimumButton.addActionListener(e -> {
			w.setExtendedState(Frame.ICONIFIED);
			if (traySupport) {
				trayIcon.displayMessage(VERSION, "程序已最小化", TrayIcon.MessageType.INFO);
			}
		});

		TransButton closeButton = new TransButton("X");
		closeButton.setFont(font);
		titlePanel.add(closeButton);
		closeButton.setSize(buttonSize, buttonSize);
		closeButton.setLocation(titlePanel.getWidth() - buttonSize - 5, 2);

		closeButton.addActionListener(e -> {
			char[] pass = passwordField.getPassword();
			if (userNameTextfield.getText() != null && userNameTextfield.getText().trim().length() != 0
					&& passwordField.getPassword() != null && passwordField.getPassword().length != 0) {
				Arrays.fill(pass, '0');
				writeConfig();
			}
			System.exit(0);
		});

		return titlePanel;
	}

	private transJPanel initSettingPanel(int width, int height) {
		int borderWidth = 20;
		int interval = 20;
		Color borderColor = new Color(foreColor.getRed(), foreColor.getGreen(), foreColor.getBlue(), 80);

		transJPanel settingPanel = new transJPanel(null);
		settingPanel.setSize(width, height);

		transJPanel ipmacPanel = new transJPanel(null);
		ipmacPanel.setSize(width - borderWidth * 2, height * 3 / 7);
		ipmacPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 1), "路由器认证",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, font, foreColor));

		transJPanel ipPanel = new transJPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		JLabel ipLabel = new JLabel("路由器WAN端口IP地址");
		ipLabel.setFont(font);
		ipLabel.setForeground(foreColor);
		ipTextfield = new TransTextfield(getConfig("路由器IP"));
		ipTextfield.setColumns(16);
		ipPanel.add(ipLabel);
		ipPanel.add(ipTextfield);

		transJPanel macPanel = new transJPanel(ipPanel.getLayout());
		JLabel macLabel = new JLabel("路由器WAN端口MAC地址");
		macLabel.setFont(font);
		macLabel.setForeground(foreColor);
		macTextfield = new TransTextfield(getConfig("路由器MAC"));
		macTextfield.setColumns(ipTextfield.getColumns());
		macTextfield.setTip("格式:XX-XX-XX-XX-XX-XX");
		macPanel.add(macLabel);
		macPanel.add(macTextfield);

		ipmacPanel.add(ipPanel);
		ipPanel.setLocation(interval, 35);
		ipPanel.setSize(ipmacPanel.getWidth() - 39, ipLabel.getMinimumSize().height + 1);
		ipmacPanel.add(macPanel);
		macPanel.setLocation(interval, ipPanel.getY() + ipPanel.getHeight() + interval);
		macPanel.setSize(ipPanel.getSize());

		transJPanel optionsPanel = new transJPanel(null);
		optionsPanel.setSize(ipmacPanel.getWidth(), height * 31 / 90);
		optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 1), "认证选项",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, font, foreColor));

		transJPanel timeSettingPanel = new transJPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JLabel timeSettingLabelLeft = new JLabel("认证心跳包间隔");
		timeSettingLabelLeft.setForeground(foreColor);
		timeSettingLabelLeft.setFont(font);
		timeSettingTextfield = new TransTextfield(getConfig("认证心跳延时"));
		timeSettingTextfield.setColumns(2);
		timeSettingTextfield.setHorizontalAlignment(SwingConstants.RIGHT);
		timeSettingTextfield.setToolTipText("建议小于180秒");
		JLabel timeSettingLabelRight = new JLabel("秒");
		timeSettingLabelRight.setForeground(foreColor);
		timeSettingLabelRight.setFont(font);
		timeSettingPanel.add(timeSettingLabelLeft);
		timeSettingPanel.add(timeSettingTextfield);
		timeSettingPanel.add(timeSettingLabelRight);
		optionsPanel.add(timeSettingPanel);
		timeSettingPanel.setSize(
				(int) (timeSettingLabelLeft.getMinimumSize().getWidth()
						+ timeSettingTextfield.getPreferredSize().getWidth()
						+ timeSettingLabelRight.getMinimumSize().getWidth()
						+ ((FlowLayout) timeSettingPanel.getLayout()).getHgap() * 4),
				(int) timeSettingPanel.getMinimumSize().getHeight());
		timeSettingPanel.setLocation(interval, interval * 5 / 4);

		authOnStartCheckBox = new TransCheckBox("软件启动时自动认证", Boolean.valueOf(getConfig("启动时自动认证")));
		authOnStartCheckBox.setSize(new Dimension(authOnStartCheckBox.getMinimumSize().width + 10,
				authOnStartCheckBox.getMinimumSize().height));
		optionsPanel.add(authOnStartCheckBox);
		authOnStartCheckBox.setLocation(optionsPanel.getWidth() - interval - authOnStartCheckBox.getWidth(),
				timeSettingPanel.getY());

		routeOnAuthCheckBox = new TransCheckBox("认证时自动添加路由(可解决VPN下无法认证的问题)", Boolean.valueOf(getConfig("认证时添加路由")));
		optionsPanel.add(routeOnAuthCheckBox);
		routeOnAuthCheckBox.setSize(routeOnAuthCheckBox.getMinimumSize());
		routeOnAuthCheckBox.setLocation(interval,
				timeSettingPanel.getY() + timeSettingPanel.getHeight() + interval * 2 / 3);

		transJPanel savePanel = new transJPanel(null);
		savePanel.setSize(optionsPanel.getWidth(), height / 10);
		int savePanelWidth = savePanel.getWidth();
		saveButton = new TransButton("确定");
		saveButton.setSize(savePanelWidth * 2 / 7, savePanel.getHeight());
		savePanel.add(saveButton);
		saveButton.setLocation(savePanelWidth / 7, 0);
		cancelButton = new TransButton("取消");
		cancelButton.setSize(saveButton.getSize());
		savePanel.add(cancelButton);
		cancelButton.setLocation(savePanelWidth * 4 / 7, 0);

		settingPanel.add(ipmacPanel);
		ipmacPanel.setLocation(borderWidth, borderWidth / 3);
		settingPanel.add(optionsPanel);
		optionsPanel.setLocation(borderWidth, ipmacPanel.getY() + ipmacPanel.getHeight() + interval / 2);
		settingPanel.add(savePanel);
		savePanel.setLocation(borderWidth, optionsPanel.getY() + optionsPanel.getHeight() + interval / 2);

		settingPanel.setVisible(false);
		return settingPanel;
	}

	private void initTray(String iconFile) {
		if (!traySupport) {
			return;
		}
		SystemTray tray = SystemTray.getSystemTray();
		PopupMenu popupMenu = new PopupMenu();
		MenuItem show = new MenuItem("显示窗口");
		startItem = new MenuItem("上线");
		MenuItem exit = new MenuItem("退出");
		popupMenu.add(show);
		popupMenu.add(startItem);
		popupMenu.add(exit);
		trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(iconFile)), VERSION,
				popupMenu);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!e.isPopupTrigger() && e.getClickCount() == 2) {
					w.setExtendedState(Frame.NORMAL);
					w.setVisible(true);
				}
			}
		});
		show.addActionListener(e -> {
			w.setExtendedState(Frame.NORMAL);
			w.setVisible(true);
		});
		startItem.addActionListener(e -> startButton.dispatchEvent(
				new MouseEvent(startButton, MouseEvent.MOUSE_CLICKED, e.getWhen(), 0, 10, 10, 1, false)));
		exit.addActionListener(e -> System.exit(0));
		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			TransMsgBox.showConfirm(w, "添加托盘图标失败");
		}
	}

	private void initAction() {

		settingButton.addActionListener(e -> slideFromLeft(mainPanel, settingPanel));
		// miniAfterAuthBox.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// putConfig("认证成功后最小化", miniAfterAuthBox.isChecked());
		// writeConfig();
		// }
		// });
		cancelButton.addActionListener(e -> slideFromeRight(settingPanel, mainPanel));
		startButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
			TransButton button = (TransButton) e.getSource();
			button.setEnabled(false);
			String userName = userNameTextfield.getText();
			char[] password = passwordField.getPassword();
			if (!authStart) {
				if (userName.length() == 0) {
					TransMsgBox.showConfirm(w, "账户不能为空");
					userNameTextfield.requestFocus();
					button.setEnabled(true);
					return;
				}
				if (password.length == 0) {
					TransMsgBox.showConfirm(w, "密码不能为空");
					passwordField.requestFocus();
					button.setEnabled(true);
					return;
				}
				String[] macip = new String[] { getConfig("路由器IP"), getConfig("路由器MAC") };
				if (macip[0].length() == 0 || macip[1].length() == 0) {
					String[] getmacip = MACIPGetter.getMACIP();
					macip[0] = macip[0].length() == 0 ? getmacip[0] : macip[0];
					macip[1] = macip[1].length() == 0 ? getmacip[1] : macip[1];
				}
				if (macip[0].length() == 0 || macip[1].length() == 0) {
					String[] choices1 = new String[] { "手动填写", "放弃认证" };
					String choice1 = TransMsgBox.showMulti(w, "试图通过解析默认路由获取本机IP和MAC失败，是否手动填写认证所需IP和MAC？", choices1);
					if (choice1.equals(choices1[0])) {
						settingButton.autoAction(0);
						ipTextfield.requestFocus();
					}
					button.setEnabled(true);
					return;
				} else if (!macip[0].startsWith("10.")
						&& (ipTextfield.getText() == null || ipTextfield.getText().trim().length() == 0)) {
					String[] choices2 = new String[] { "手动填写", "保持现状" };
					String choice2 = TransMsgBox.showMulti(w, "您的IP为" + macip[0] + "，可能正在通过路由器进行认证，是否手动填写认证所需IP和MAC？",
							choices2);
					if (choice2.equals(choices2[0])) {
						settingButton.autoAction(0);
						ipTextfield.requestFocus();
						button.setEnabled(true);
						return;
					}
				}
				startNewAuthThread(userName, password, macip[1], macip[0]);
				button.setText("下  线");
				authStart = true;
				if (traySupport) {
					startItem.setLabel("下线");
				}
			} else {
				try {
					authThread.auth = false;
				} catch (NullPointerException e2) {
				}
				button.setText("上  线");
				stateJLabel.setText("已停止认证");
				authStart = false;
				if (traySupport) {
					startItem.setLabel("上线");
				}
			}
			button.setEnabled(true);
		}));
		saveButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
			String ip = (ipTextfield.getText() == null ? "" : ipTextfield.getText()).trim();
			String mac = ((macTextfield.getText() == null || macTextfield.getText().equals(macTextfield.getTip())) ? ""
					: macTextfield.getText()).trim();
			if (mac.length() != 0) {
				mac = mac.toUpperCase();
				macTextfield.setText(mac);
			}
			if (ip.length() != 0 && !ip.matches(
					"(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
				TransMsgBox.showConfirm(w, "IP 格式不正确，格式示例：10.123.45.67");
				ipTextfield.requestFocus();
				return;
			}
			if (mac.length() != 0 && !mac.matches("(([A-Z]|[0-9]){2}-){5}([A-Z]|[0-9]){2}")) {
				TransMsgBox.showConfirm(w, "MAC 格式不正确，格式示例：AB-CD-EF-GH-IJ-KL");
				macTextfield.requestFocus();
				return;
			}
			if (authStart) {
				startButton.autoAction(0);
				startButton.autoAction(1);
			}
			putConfig("路由器IP", ip);
			putConfig("路由器MAC", mac);
			putConfig("认证心跳延时", timeSettingTextfield.getText());
			putConfig("启动时自动认证", authOnStartCheckBox.isChecked());
			putConfig("认证时添加路由", routeOnAuthCheckBox.isChecked());
			writeConfig();
			slideFromeRight(settingPanel, mainPanel);
		}));
		w.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				w.setVisible(false);
				w.dispose();
			}
		});
	}

	private void slideFromLeft(JPanel oldPanel, JPanel newPanel) {
		if (!oldPanel.getSize().equals(newPanel.getSize())) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				int x = oldPanel.getX();
				int y = oldPanel.getY();
				int o = x;
				int w = oldPanel.getWidth();
				oldPanel.setLocation(x, y);
				newPanel.setLocation(x - w, y);
				newPanel.setVisible(true);
				for (; x <= w; x += 20) {
					oldPanel.setLocation(x, y);
					newPanel.setLocation(x - w, y);
					try {
						Thread.sleep(effectDelay);
					} catch (InterruptedException e) {
					}
				}
				newPanel.setLocation(o, y);
				oldPanel.setVisible(false);
			}

		}.start();
	}

	private void slideFromeRight(JPanel oldPanel, JPanel newPanel) {
		if (!oldPanel.getSize().equals(newPanel.getSize())) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				int x = oldPanel.getX();
				int y = oldPanel.getY();
				int o = x;
				int w = oldPanel.getWidth();
				oldPanel.setLocation(x, y);
				newPanel.setLocation(x + w, y);
				newPanel.setVisible(true);
				for (; x >= -w; x -= 20) {
					oldPanel.setLocation(x, y);
					newPanel.setLocation(x + w, y);
					try {
						Thread.sleep(effectDelay);
					} catch (InterruptedException e) {
					}
				}
				newPanel.setLocation(o, y);
				oldPanel.setVisible(false);
			}

		}.start();
	}


	public static String getConfig(String configItem) {
		return settingMap.get(configItem);
	}

	public static void putConfig(String configItem, Object value) {
		settingMap.put(configItem, value.toString());
	}

	public void startNewAuthThread(String userName, char[] passoword, String mac, String ip) {
		try {
			authThread.auth = false;
		} catch (NullPointerException e) {
		}
		authThread = new AuthThread(stateJLabel, userName, passoword, mac, ip, w);
		authThread.start();
		if (Boolean.valueOf(getConfig("认证时添加路由"))) {
			new Thread(() -> {
				while (!authOK && authStart) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}
				if (authOK) {
					if (OSNAME.toLowerCase().indexOf("windows") != -1) {
						addRoute();
					}
				}
			}).start();
		}
	}

	public void addRoute() {
	}

	private static void initDefaultConfig() {
		String[] defaultConfigs = defaultConfig.split("\\|");
		for (String string : defaultConfigs) {
			analysis(string);
		}
	}

	private static void analysis(String configLine) {
		String[] pas = configLine.trim().split("=", 2);
		if (pas.length == 2) {
			putConfig(pas[0], pas[1]);
		}
	}

	private boolean readConfig() {
		File file = new File(configPath);
		if (!file.exists() || file.isDirectory()) {
			return false;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String temp = "";
			while (temp != null) {
				analysis(temp);
				temp = br.readLine();
			}
		} catch (IOException e) {
			TransMsgBox.showConfirm(w, "配置文件读取失败");
			return false;
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				TransMsgBox.showConfirm(w, "配置文件读取流关闭失败");
				return false;
			}
		}
		return true;

	}

	private String prepareConfig() {
		StringBuilder sb = new StringBuilder(
				"本文件是 bcSimulator 的配置文件，删除本文件会导致 bcSimulator 的设置被清空，密码是加密过的，请不要更改。把本文件放到和程序同目录下即可被自动加载（本文件可跨平台使用）\r\n");
		String userName = userNameTextfield.getText() == null ? "" : userNameTextfield.getText();
		putConfig("账户", userName);
		char[] pass = passwordField.getPassword();
		String passEnt = EncrUtil.encrypt(pass);
		Arrays.fill(pass, '0');
		putConfig("密码", passEnt);
		Iterator<Entry<String, String>> iterator = settingMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			sb.append(entry.getKey() + "=" + entry.getValue() + "\r\n");
		}

		return sb.toString();
	}

	public void writeConfig() {
		new Thread(() -> {
			String configString = prepareConfig();
			File file = new File(configPath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e1) {
					TransMsgBox.showConfirm(w, "配置文件创建失败");
					return;
				}
			}
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				outputStream.write(configString.getBytes("utf-8"));
			} catch (IOException e2) {
				TransMsgBox.showConfirm(w, "配置文件已创建，但输出流失败");
				return;
			} finally {
				try {
					outputStream.close();
				} catch (IOException e3) {
					TransMsgBox.showConfirm(w, "配置文件输出流关闭失败");
					return;
				}
			}
		}).start();

	}

	class transJPanel extends JPanel {

		private static final long serialVersionUID = -6934220028696900430L;

		public transJPanel() {
			setBackground(transColor);
			setOpaque(false);
		}

		public transJPanel(LayoutManager l) {
			super(l);
			setBackground(transColor);
			setOpaque(false);
		}

	}

	class ImagePanel extends transJPanel {

		private static final long serialVersionUID = 3602544785116642939L;
		private Image backgroundImage;

		public ImagePanel(String imgUrl) {
			super(null);
			backgroundImage = new ImageIcon(BingchuanSimulator.class.getResource(imgUrl)).getImage();
			this.setBounds(new Rectangle(backgroundImage.getWidth(null), backgroundImage.getHeight(null)));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, this);
		}

	}

	@SuppressWarnings("serial")
	class slideTransButton extends TransButton {
		final TransButton transButton = this;
		String buttonText;

		public slideTransButton(String text) {
			super(text);
			this.buttonText = text;
		}

		@Override
		protected void paintBorder(Graphics g) {
			g.setColor(getForeground());
			g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, getWidth() / 5, getHeight() * 9 / 10);
		}

		final class Run implements Runnable {

			public boolean r = true;

			@Override
			public void run() {
				StringBuilder sb = new StringBuilder(buttonText);
				out: while (r) {
					for (int i = 0; i < 3; i++) {
						if (!r) {
							break out;
						}
						transButton.setText(sb.append(">").toString());
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						}
					}
					sb.delete(0, sb.length());
					transButton.setText(buttonText);
					sb.append(buttonText);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}
				setText(buttonText);
			}

		}

		final Run run = new Run();


		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			run.r = true;
			new Thread(run).start();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			run.r = false;
		}
	}

}
