package com.kvm;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
class FullToolBar
  extends JToolBar
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_FullToolBar;
  private JLabel lblcombodqtz;
  private JButton mouseSynButton;
  private JButton combineKey;
  private JButton mouseModeButton;
  private JButton returnButton;
  public JLabel getLblcombodqtz() {
    return this.lblcombodqtz;
  }
  public void setLblcombodqtz(JLabel lblcombodqtz) {
    this.lblcombodqtz = lblcombodqtz;
  }
  public JButton getMouseSynButton() {
    return this.mouseSynButton;
  }
  public void setMouseSynButton(JButton mouseSynButton) {
    this.mouseSynButton = mouseSynButton;
  }
  public JButton getCombineKey() {
    return this.combineKey;
  }
  public void setCombineKey(JButton combineKey) {
    this.combineKey = combineKey;
  }
  public JButton getMouseModeButton() {
    return this.mouseModeButton;
  }
  public void setMouseModeButton(JButton mouseModeButton) {
    this.mouseModeButton = mouseModeButton;
  }
  public JButton getReturnButton() {
    return this.returnButton;
  }
  public void setReturnButton(JButton returnButton) {
    this.returnButton = returnButton;
  }
  private JSlider dqtSlider = null;
  public JSlider getDqtSlider() {
    return this.dqtSlider;
  }
  public void setDqtSlider(JSlider dqtSlider) {
    this.dqtSlider = dqtSlider;
  }
  private int slider_value = 0;
  public int getSlider_value() {
    return this.slider_value;
  }
  public void setSlider_value(int slider_value) {
    this.slider_value = slider_value;
  }
  private int pre_slider_value = 0; private BackColorButton numColorButton; private BackColorButton capsColorButton;
  private BackColorButton scrollColorButton;
  public int getPre_slider_value() {
    return this.pre_slider_value;
  }
  private JButton btnCreateImage; private JButton btnCDMenu; private JButton btnFlpMenu;
  public void setPre_slider_value(int pre_slider_value) {
    this.pre_slider_value = pre_slider_value;
  }
  public BackColorButton getNumColorButton() {
    return this.numColorButton;
  }
  public void setNumColorButton(BackColorButton numColorButton) {
    this.numColorButton = numColorButton;
  }
  public BackColorButton getCapsColorButton() {
    return this.capsColorButton;
  }
  public void setCapsColorButton(BackColorButton capsColorButton) {
    this.capsColorButton = capsColorButton;
  }
  public BackColorButton getScrollColorButton() {
    return this.scrollColorButton;
  }
  public void setScrollColorButton(BackColorButton scrollColorButton) {
    this.scrollColorButton = scrollColorButton;
  }
  public JButton getBtnCreateImage() {
    return this.btnCreateImage;
  }
  public void setBtnCreateImage(JButton btnCreateImage) {
    this.btnCreateImage = btnCreateImage;
  }
  public JButton getBtnCDMenu() {
    return this.btnCDMenu;
  }
  public void setBtnCDMenu(JButton btnCDMenu) {
    this.btnCDMenu = btnCDMenu;
  }
  public JButton getBtnFlpMenu() {
    return this.btnFlpMenu;
  }
  public void setBtnFlpMenu(JButton btnFlpMenu) {
    this.btnFlpMenu = btnFlpMenu;
  }
  private Color numColor = Base.LIGHT_OFF;
  public Color getNumColor() {
    return this.numColor;
  }
  public void setNumColor(Color numColor) {
    this.numColor = numColor;
  }
  private Color capsColor = Base.LIGHT_OFF;
  public Color getCapsColor() {
    return this.capsColor;
  }
  public void setCapsColor(Color capsColor) {
    this.capsColor = capsColor;
  }
  private Color scrollColor = Base.LIGHT_OFF;
  public Color getScrollColor() {
    return this.scrollColor;
  }
  public void setScrollColor(Color scrollColor) {
    this.scrollColor = scrollColor;
  }
  private JLabel labelnum = new JLabel("num");
  public JLabel getLabelnum() {
    return this.labelnum;
  }
  public void setLabelnum(JLabel labelnum) {
    this.labelnum = labelnum;
  }
  private JLabel labelcaps = new JLabel("caps");
  public JLabel getLabelcaps() {
    return this.labelcaps;
  }
  public void setLabelcaps(JLabel labelcaps) {
    this.labelcaps = labelcaps;
  }
  private JLabel labelscroll = new JLabel("scroll"); private JButton helpButton; private JButton powerMenuButton; private PowerPopupMenu powerMenu; private JButton mouseMenuButton; private MousePopupMenu mouseMenu;
  private JButton keyboardLayoutButton;
  private KeyboardPopupMenu keyboardLayoutMenu;
  private JButton usbResetButton;
  private JButton videoButton;
  public JLabel getLabelscroll() {
    return this.labelscroll;
  }
  public void setLabelscroll(JLabel labelscroll) {
    this.labelscroll = labelscroll;
  }
  public JButton getHelpButton() {
    return this.helpButton;
  }
  public void setHelpButton(JButton helpButton) {
    this.helpButton = helpButton;
  }
  public JButton getPowerMenuButton() {
    return this.powerMenuButton;
  }
  public void setPowerMenuButton(JButton powerMenuButton) {
    this.powerMenuButton = powerMenuButton;
  }
  public PowerPopupMenu getPowerMenu() {
    return this.powerMenu;
  }
  public void setPowerMenu(PowerPopupMenu powerMenu) {
    this.powerMenu = powerMenu;
  }
  public JButton getMouseMenuButton() {
    return this.mouseMenuButton;
  }
  public void setMouseMenuButton(JButton mouseMenuButton) {
    this.mouseMenuButton = mouseMenuButton;
  }
  public MousePopupMenu getMouseMenu() {
    return this.mouseMenu;
  }
  public void setMouseMenu(MousePopupMenu mouseMenu) {
    this.mouseMenu = mouseMenu;
  }
  public JButton getKeyboardLayoutButton() {
    return this.keyboardLayoutButton;
  }
  public void setKeyboardLayoutButton(JButton keyboardLayoutButton) {
    this.keyboardLayoutButton = keyboardLayoutButton;
  }
  public KeyboardPopupMenu getKeyboardLayoutMenu() {
    return this.keyboardLayoutMenu;
  }
  public void setKeyboardLayoutMenu(KeyboardPopupMenu keyboardLayoutMenu) {
    this.keyboardLayoutMenu = keyboardLayoutMenu;
  }
  public JButton getVideoButton() {
    return this.videoButton;
  }
  public void setVideoButton(JButton videoButton) {
    this.videoButton = videoButton;
  }
  public JButton createPowerButton() {
    this.powerMenuButton = createButton("Power_Management", "resource/images/dev_com_power.gif");
    this.powerMenu = new PowerPopupMenu(this.refer_FullToolBar.getKvmInterface());
    this.powerMenuButton.setBackground(new Color(158, 202, 232));
    this.powerMenuButton.addActionListener(new PowerMenuButtonListener(this.refer_FullToolBar, this));
    return this.powerMenuButton;
  }
  public JButton createMouseMenuButton() {
    this.mouseMenuButton = createButton("Mouse_Manager", "resource/images/mouse_manager.png");
    this.mouseMenu = new MousePopupMenu(this.refer_FullToolBar.getKvmInterface());
    this.mouseMenuButton.setBackground(new Color(158, 202, 232));
    this.mouseMenuButton.addActionListener(new MouseMenuButtonListener(this.refer_FullToolBar, this));
    return this.mouseMenuButton;
  }
  public JButton createKeyboardLayoutMenuButton() {
    this.keyboardLayoutButton = createButton("Keyboard_Layout", "resource/images/keyboard_layout.png");
    this.keyboardLayoutMenu = new KeyboardPopupMenu(this.refer_FullToolBar.getKvmInterface());
    this.keyboardLayoutButton.setBackground(new Color(158, 202, 232));
    this.keyboardLayoutButton.addActionListener(new KeyboardLayoutMenuButtonListener(this.refer_FullToolBar, this));
    return this.keyboardLayoutButton;
  }
  public FullToolBar(FullScreen refer_FullToolBar) {
    this.refer_FullToolBar = refer_FullToolBar;
    this.lblcombodqtz = new JLabel(refer_FullToolBar.getKvmInterface().getKvmUtil().getString("DQT_Value"));
    setRequestFocusEnabled(false);
    createMouseSynButton();
    createComKeyButton();
    createMouseMode();
    createReturnButton();
    createNumColorButton();
    createCapsColorButton();
    createScrollColorButton();
    createDQTSlider();
    createUsbResetButton();
    createKeyboardLayoutMenuButton();
    createVideoButton();
    createHelpButton();
    createImageButton();
    createImageMenu();
    if (refer_FullToolBar.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      createCDMenuButton();
      createFlpMenuButton();
      createCDMenu();
      createFLPMenu();
    } 
    if (KVMUtil.isAdmin())
    {
      createPowerButton();
    }
    if ("USB".equals("USB"))
    {
      createMouseMenuButton();
    }
    setLayout((LayoutManager)null);
  }
  public JButton createUsbResetButton() {
    this.usbResetButton = createButton("usb_reset", "resource/images/usb_reset.png");
    this.usbResetButton.addActionListener(usbResetAction());
    this.usbResetButton.setBackground(new Color(158, 202, 232));
    this.usbResetButton.setBorder((Border)null);
    this.usbResetButton.setFocusable(false);
    return this.usbResetButton;
  }
  public JButton createVideoButton() {
    this.videoButton = createButton("localKinescope", "resource/images/video_start.png");
    this.videoButton.addActionListener(videoAction());
    this.videoButton.setBackground(new Color(158, 202, 232));
    this.videoButton.setBorder((Border)null);
    this.videoButton.setFocusable(false);
    return this.videoButton;
  }
  public JDialog createImageMenu() {
    if (this.refer_FullToolBar.getImageMenu() == null) {
      this.refer_FullToolBar.setImageMenu(new JDialog());
      this.refer_FullToolBar.getImageMenu().setUndecorated(true);
      if (KVMUtil.isLinuxOS() && Base.getLocal().equalsIgnoreCase("en")) {
        this.refer_FullToolBar.getImageMenu().setSize(380, 49);
      }
      else {
        this.refer_FullToolBar.getImageMenu().setSize(337, 49);
      } 
      this.refer_FullToolBar.getImageMenu().setAlwaysOnTop(true);
      this.refer_FullToolBar.getImageMenu().setVisible(false);
    } 
    return this.refer_FullToolBar.getImageMenu();
  }
  public JDialog createCDMenu() {
    if (this.refer_FullToolBar.getCdMenu() == null) {
      this.refer_FullToolBar.setCdMenu(new JDialog());
      this.refer_FullToolBar.getCdMenu().setUndecorated(true);
      if (KVMUtil.isLinuxOS() && Base.getLocal().equalsIgnoreCase("en")) {
        this.refer_FullToolBar.getCdMenu().setSize(453, 69);
      }
      else {
        this.refer_FullToolBar.getCdMenu().setSize(410, 69);
      } 
      this.refer_FullToolBar.getCdMenu().setAlwaysOnTop(true);
      this.refer_FullToolBar.getCdMenu().setVisible(false);
    } 
    return this.refer_FullToolBar.getCdMenu();
  }
  public JDialog createFLPMenu() {
    if (this.refer_FullToolBar.getFlpMenu() == null) {
      this.refer_FullToolBar.setFlpMenu(new JDialog());
      this.refer_FullToolBar.getFlpMenu().setUndecorated(true);
      if (KVMUtil.isLinuxOS() && Base.getLocal().equalsIgnoreCase("en")) {
        this.refer_FullToolBar.getFlpMenu().setSize(503, 49);
      }
      else {
        this.refer_FullToolBar.getFlpMenu().setSize(460, 49);
      } 
      this.refer_FullToolBar.getFlpMenu().setAlwaysOnTop(true);
      this.refer_FullToolBar.getFlpMenu().setVisible(false);
    } 
    return this.refer_FullToolBar.getFlpMenu();
  }
  public JDialog createPowerPanel() {
    if (this.refer_FullToolBar.getPowerPanelDialog() == null)
    {
      this.refer_FullToolBar.setPowerPanelDialog(new PowerPanel(this.refer_FullToolBar));
    }
    return this.refer_FullToolBar.getPowerPanelDialog();
  }
  public void startButtonState() {
    this.returnButton.setBounds(30, 1, 20, 20);
    this.mouseSynButton.setBounds(60, 1, 30, 20);
    this.mouseModeButton.setBounds(95, 1, 20, 20);
    if (null != this.powerMenuButton)
    {
      this.powerMenuButton.setBounds(125, 1, 20, 20);
    }
    this.videoButton.setBounds(155, 1, 23, 23);
    add(this.videoButton);
    this.mouseMenuButton.setBounds(185, 1, 23, 23);
    this.mouseMenuButton.setBorder(BorderFactory.createEmptyBorder());
    add(this.mouseMenuButton);
    this.btnCDMenu.setBounds(215, 1, 23, 23);
    this.btnFlpMenu.setBounds(245, 1, 23, 23);
    this.btnCreateImage.setBounds(275, 1, 23, 23);
    this.combineKey.setBounds(305, 1, 20, 20);
    this.helpButton.setBounds(690, 1, 20, 20);
    this.keyboardLayoutButton.setBounds(335, 1, 20, 20);
    add(this.keyboardLayoutButton);
    this.lblcombodqtz.setBounds(365, 1, 75, 20);
    this.dqtSlider.setBounds(440, 0, 100, 32);
    this.lblcombodqtz.setFont(new Font("sansserif", 0, 10));
    add(this.dqtSlider);
    add(this.lblcombodqtz);
    this.labelnum.setBounds(550, 1, 28, 20);
    this.labelnum.setFont(new Font("sansserif", 0, 12));
    this.numColorButton.setBounds(578, 5, 10, 10);
    this.labelcaps.setBounds(593, 1, 30, 20);
    this.labelcaps.setFont(new Font("sansserif", 0, 12));
    this.capsColorButton.setBounds(623, 5, 10, 10);
    this.labelscroll.setBounds(638, 1, 35, 20);
    this.labelscroll.setFont(new Font("sansserif", 0, 12));
    this.scrollColorButton.setBounds(673, 5, 10, 10);
    this.mouseSynButton.setBackground(new Color(158, 202, 232));
    this.mouseSynButton.setBorder(BorderFactory.createEmptyBorder());
    this.combineKey.setBackground(new Color(158, 202, 232));
    this.combineKey.setBorder(BorderFactory.createEmptyBorder());
    this.mouseModeButton.setBackground(new Color(158, 202, 232));
    this.mouseModeButton.setBorder(BorderFactory.createEmptyBorder());
    this.returnButton.setBackground(new Color(158, 202, 232));
    this.returnButton.setBorder(BorderFactory.createEmptyBorder());
    if (null != this.powerMenuButton) {
      this.powerMenuButton.setBackground(new Color(158, 202, 232));
      this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder());
    } 
    this.dqtSlider.setBackground(new Color(158, 202, 232));
    this.dqtSlider.setBorder(BorderFactory.createEmptyBorder());
    this.helpButton.setBackground(new Color(158, 202, 232));
    this.helpButton.setBorder(BorderFactory.createEmptyBorder());
    this.btnCreateImage.setBackground(new Color(158, 202, 232));
    this.btnCreateImage.setBorder(BorderFactory.createEmptyBorder());
    add(this.btnCreateImage);
    if (this.refer_FullToolBar.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.btnCDMenu.setBackground(new Color(158, 202, 232));
      this.btnCDMenu.setBorder(BorderFactory.createEmptyBorder());
      this.btnFlpMenu.setBackground(new Color(158, 202, 232));
      this.btnFlpMenu.setBorder(BorderFactory.createEmptyBorder());
    } 
  }
  public JButton createHelpButton() {
    this.helpButton = createButton("help_document", "resource/images/help.gif");
    this.helpButton.addActionListener(createHelpAction());
    add(this.helpButton);
    return this.helpButton;
  }
  public Action usbResetAction() {
    Action action = new FullScreenUsbResetAction(this.refer_FullToolBar);
    return action;
  }
  public Action videoAction() {
    Action action = new VideoAction(this.refer_FullToolBar, this);
    return action;
  }
  public ChangeListener createDQTSliderAction() {
    ChangeListener listner = new DQTSliderAction(this.refer_FullToolBar, this);
    return listner;
  }
  public Action createHelpAction() {
    Action action = new HelpAction(this.refer_FullToolBar);
    return action;
  }
  private JButton createImageButton() {
    this.btnCreateImage = createButton("create_image", "resource/images/virtualne.gif");
    this.btnCreateImage.addActionListener(createImageAction());
    this.btnCreateImage.setBackground(new Color(158, 202, 232));
    return this.btnCreateImage;
  }
  public JButton createCDMenuButton() {
    this.btnCDMenu = createButton("cd_cdroms", "resource/images/cd.gif");
    this.btnCDMenu.addActionListener(createCDMenuAction());
    add(this.btnCDMenu);
    return this.btnCDMenu;
  }
  public JButton createFlpMenuButton() {
    this.btnFlpMenu = createButton("flp_floppy", "resource/images/flp.gif");
    this.btnFlpMenu.addActionListener(createFlpMenuAction());
    add(this.btnFlpMenu);
    return this.btnFlpMenu;
  }
  private Action createImageAction() {
    Action action = new ImageAction(this.refer_FullToolBar, this);
    return action;
  }
  private Action createCDMenuAction() {
    Action action = new CDMenuAction(this.refer_FullToolBar, this);
    return action;
  }
  private Action createFlpMenuAction() {
    Action action = new FlpMenuAction(this.refer_FullToolBar, this);
    return action;
  }
  private JButton createButton(String resCode, String imageURL) {
    JButton button = new JButton(new ImageIcon(FloatToolbar.class.getResource(imageURL)));
    button.setToolTipText(this.refer_FullToolBar.getKvmInterface().getKvmUtil().getString(resCode));
    return button;
  }
  public JButton createMouseSynButton() {
    this.mouseSynButton = createButton("MouseSyn.Tip", "resource/images/mousesyn.gif");
    this.mouseSynButton.addActionListener(this.refer_FullToolBar.synMouseAction());
    add(this.mouseSynButton);
    return this.mouseSynButton;
  }
  public JButton createComKeyButton() {
    this.combineKey = createButton("ConfigMenu.shortCut.text", "resource/images/combinekey.gif");
    this.combineKey.addActionListener(this.refer_FullToolBar.shortCutAction());
    add(this.combineKey);
    return this.combineKey;
  }
  public JButton createMouseMode() {
    this.mouseModeButton = createButton("MouseMode.Tip", "resource/images/mouseMode.gif");
    this.mouseModeButton.addActionListener(this.refer_FullToolBar.mouseModeAction());
    add(this.mouseModeButton);
    return this.mouseModeButton;
  }
  public JButton createReturnButton() {
    this.returnButton = createButton("Return.Tip", "resource/images/return.gif");
    this.returnButton.addActionListener(this.refer_FullToolBar.returnAction());
    add(this.returnButton);
    return this.returnButton;
  }
  private void createNumColorButton() {
    this.numColorButton = new BackColorButton();
    this.numColorButton.setBackground(this.numColor);
    this.numColorButton.setEnabled(false);
    add(this.labelnum);
    add(this.numColorButton);
  }
  private void createCapsColorButton() {
    this.capsColorButton = new BackColorButton(this.capsColor);
    this.capsColorButton.setEnabled(false);
    add(this.labelcaps);
    add(this.capsColorButton);
  }
  private void createScrollColorButton() {
    this.scrollColorButton = new BackColorButton(this.scrollColor);
    this.scrollColorButton.setEnabled(false);
    add(this.labelscroll);
    add(this.scrollColorButton);
  }
  public JLabel createDqtLabel(String resCode) {
    JLabel label = new JLabel(this.refer_FullToolBar.getKvmInterface().getKvmUtil().getString(resCode));
    return label;
  }
  public JSlider createDQTSlider() {
    this.dqtSlider = new JSlider(0, 40, 90, 40);
    this.dqtSlider.setUI(new SliderUI(this.dqtSlider));
    this.dqtSlider.setPaintTicks(false);
    this.dqtSlider.setValue(70);
    this.dqtSlider.setPaintTrack(true);
    this.dqtSlider.setPaintLabels(true);
    Dictionary<Integer, Component> labelTable = new Hashtable<>(10);
    labelTable.put(Integer.valueOf(90), createDqtLabel("dqt_max"));
    labelTable.put(Integer.valueOf(65), createDqtLabel("dqt_mid"));
    labelTable.put(Integer.valueOf(40), createDqtLabel("dqt_min"));
    this.dqtSlider.setLabelTable(labelTable);
    this.dqtSlider.setFocusable(false);
    this.dqtSlider.addChangeListener(createDQTSliderAction());
    this.dqtSlider.addMouseListener(new DQTSliderMouseListener(this.refer_FullToolBar, this));
    return this.dqtSlider;
  }
  public void fullToolBarRelease() {
    this.mouseSynButton.removeActionListener(this.refer_FullToolBar.synMouseAction());
    this.combineKey.removeActionListener(this.refer_FullToolBar.shortCutAction());
    this.mouseModeButton.removeActionListener(this.refer_FullToolBar.mouseModeAction());
    this.returnButton.removeActionListener(this.refer_FullToolBar.returnAction());
    this.btnCreateImage.removeActionListener(createImageAction());
    if (this.refer_FullToolBar.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.btnCDMenu.removeActionListener(createCDMenuAction());
      this.btnFlpMenu.removeActionListener(createFlpMenuAction());
      this.btnCDMenu = null;
      this.btnFlpMenu = null;
    } 
    this.btnCreateImage = null;
    this.powerMenu = null;
    this.powerMenuButton = null;
    this.helpButton = null;
    this.mouseSynButton = null;
    this.combineKey = null;
    this.mouseModeButton = null;
    this.returnButton = null;
    this.numColorButton = null;
    this.capsColorButton = null;
    this.scrollColorButton = null;
    this.numColor = null;
    this.capsColor = null;
    this.scrollColor = null;
    this.labelnum = null;
    this.labelcaps = null;
    this.labelscroll = null;
  }
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(new Color(158, 202, 232));
    int w = getWidth();
    int h = getHeight();
    int[] x = { 0, w, w - h, h, 0 };
    int[] y = { 0, 0, h, h, 0 };
    if (!KVMUtil.isWindowsOS()) {
      x[2] = w;
      x[3] = 0;
    } 
    int nPoints = 4;
    Polygon poly = new Polygon(x, y, nPoints);
    g2d.fillPolygon(poly);
    g2d.drawPolygon(poly);
  }
}
