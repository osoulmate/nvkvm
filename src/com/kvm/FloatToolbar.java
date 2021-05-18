package com.kvm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
public class FloatToolbar
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  private transient VirtualMedia virtualMedia = null;
  private ImagePane imagePanel = null;
  public ImagePane getImagePanel() {
    return this.imagePanel;
  }
  private FloatToolbar floatToolbar = null;
  private int imgwidth = 0;
  public int getImgwidth() {
    return this.imgwidth;
  }
  private int imgheight = 0;
  private JButton btnCDMenu;
  private JButton btnFlpMenu;
  private JButton btnCreateImage;
  private JButton btnShow;
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
  public JButton getBtnCreateImage() {
    return this.btnCreateImage;
  }
  public void setBtnCreateImage(JButton btnCreateImage) {
    this.btnCreateImage = btnCreateImage;
  }
  public JButton getBtnShow() {
    return this.btnShow;
  }
  public void setBtnShow(JButton btnShow) {
    this.btnShow = btnShow;
  }
  private boolean isShowPanel = true;
  public boolean isShowPanel() {
    return this.isShowPanel;
  }
  public void setShowPanel(boolean isShowPanel) {
    this.isShowPanel = isShowPanel;
  }
  private boolean isShowingCD = false;
  public boolean isShowingCD() {
    return this.isShowingCD;
  }
  public void setShowingCD(boolean isShowingCD) {
    this.isShowingCD = isShowingCD;
  }
  private boolean isShowingFlp = false;
  public boolean isShowingFlp() {
    return this.isShowingFlp;
  }
  public void setShowingFlp(boolean isShowingFlp) {
    this.isShowingFlp = isShowingFlp;
  }
  private boolean isShowingImagep = false;
  public static final int KVMMENU_WIDTH = 705;
  public boolean isShowingImagep() {
    return this.isShowingImagep;
  }
  public static final int KVMMENU_WIDTH_OTHER = 285; public static final int KVMMENU_HEIGTH = 32;
  public void setShowingImagep(boolean isShowingImagep) {
    this.isShowingImagep = isShowingImagep;
  }
  private boolean flag = true;
  private JButton powerMenuButton;
  private PowerPopupMenu powerMenu;
  private JButton mouseMenuButton;
  private MousePopupMenu mouseMenu;
  private JButton keyboardLayoutButton;
  private KeyboardPopupMenu keyboardLayoutMenu;
  private JButton usbResetButton;
  private JButton videoButton;
  private JButton helpButton;
  public PowerPopupMenu getPowerMenu() {
    return this.powerMenu;
  }
  public void setPowerMenu(PowerPopupMenu powerMenu) {
    this.powerMenu = powerMenu;
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
  private JSlider dqtSlider = null; private JFrame helpFrm;
  private transient HelpDocument help;
  public JSlider getDqtSlider() {
    return this.dqtSlider;
  }
  public void setDqtSlider(JSlider dqtSlider) {
    this.dqtSlider = dqtSlider;
  }
  public JFrame getHelpFrm() {
    return this.helpFrm;
  }
  public void setHelpFrm(JFrame helpFrm) {
    this.helpFrm = helpFrm;
  }
  private int slider_value = 0;
  private int pre_slider_value = 0;
  public FloatToolbar(ImagePane imagePanel, VirtualMedia virtualMedia, KVMInterface kvmInterface2) {
    this.imagePanel = imagePanel;
    this.virtualMedia = virtualMedia;
    this.kvmInterface = kvmInterface2;
    this.floatToolbar = this;
    setLayout((LayoutManager)null);
    if (null != virtualMedia) {
      setSize(705, 32);
    }
    else {
      setSize(285, 32);
    } 
    createFloatButton();
    if (null != virtualMedia) {
      createCDMenuButton();
      createFlpMenuButton();
    } 
    if (KVMUtil.isAdmin())
    {
      createPowerButton();
    }
    if ("USB".equals("USB"))
    {
      createMouseMenuButton();
    }
    createKeyboardLayoutMenuButton();
    createHelpButton();
    createDQTSlider();
    createUsbResetButton();
    createVideoButton();
    if (kvmInterface2.getBladeSize() == 1)
    {
      createImageButton();
    }
    init();
    imagePanel.add(this);
    imagePanel.add(kvmInterface2.getImageFile());
    startStateMenu();
    this.floatToolbar.addMouseListener(kvmMenuMonuseListener());
    if (null != virtualMedia) {
      imagePanel.add(virtualMedia.getCdp());
      imagePanel.add(virtualMedia.getFlp());
      virtualMedia.getFlp().addMouseListener(vmmMonuseListener());
      virtualMedia.getCdp().addMouseListener(vmmMonuseListener());
    } 
    kvmInterface2.getImageFile().addMouseListener(vmmMonuseListener());
    if (KVMUtil.isLinuxOS())
    {
      this.btnCreateImage.setEnabled(false);
    }
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
  public JButton createHelpButton() {
    this.helpButton = createButton("help_document", "resource/images/help.gif");
    this.helpButton.addActionListener(createHelpAction());
    this.helpButton.setBackground(new Color(158, 202, 232));
    this.helpButton.setBorder((Border)null);
    this.helpButton.setFocusable(false);
    return this.helpButton;
  }
  private Action createHelpAction() {
    Action action = new CreateHelpAction(this);
    return action;
  }
  public JSlider createDQTSlider() {
    this.dqtSlider = new JSlider(0, 40, 90, 40);
    this.dqtSlider.setUI(new SliderUI(this.dqtSlider));
    this.dqtSlider.setPaintTicks(false);
    this.dqtSlider.setMajorTickSpacing(16);
    this.dqtSlider.setMinorTickSpacing(1);
    this.dqtSlider.setValue(70);
    this.dqtSlider.setPaintLabels(true);
    this.dqtSlider.setPaintTrack(true);
    Dictionary<Integer, Component> labelTable = new Hashtable<>(10);
    labelTable.put(Integer.valueOf(40), createDqtLabel("dqt_min"));
    labelTable.put(Integer.valueOf(65), createDqtLabel("dqt_mid"));
    labelTable.put(Integer.valueOf(90), createDqtLabel("dqt_max"));
    this.dqtSlider.setLabelTable(labelTable);
    this.dqtSlider.setFocusable(false);
    this.dqtSlider.addChangeListener(createDQTSliderAction());
    this.dqtSlider.addMouseListener(new CreateDQTSliderMouseListener(this));
    return this.dqtSlider;
  }
  public ChangeListener createDQTSliderAction() {
    ChangeListener listner = new CreateDQTSliderAction(this);
    return listner;
  }
  public Action usbResetAction() {
    Action action = new FloatToolbarUsbResetAction(this);
    return action;
  }
  public Action videoAction() {
    Action action = new FloatToolbarVideoAction(this);
    return action;
  }
  public JButton createPowerButton() {
    this.powerMenuButton = createButton("Power_Management", "resource/images/dev_com_power.gif");
    this.powerMenu = new PowerPopupMenu(this.kvmInterface);
    this.powerMenuButton.setBackground(new Color(158, 202, 232));
    this.powerMenuButton.setFocusable(false);
    this.powerMenuButton.addActionListener(new CreatePowerButtonActionListener(this));
    return this.powerMenuButton;
  }
  public void init() {
    if (null != this.virtualMedia) {
      add(this.btnCDMenu);
      add(this.btnFlpMenu);
    } 
    if (this.powerMenuButton != null)
    {
      add(this.powerMenuButton);
    }
    if (this.mouseMenuButton != null)
    {
      add(this.mouseMenuButton);
    }
    this.btnShow.setBounds(22, 2, 20, 16);
    add(this.btnShow);
    this.kvmInterface.getToolbar().getFullButton().setBounds(60, 1, 20, 20);
    add(this.kvmInterface.getToolbar().getFullButton());
    if (this.powerMenuButton != null) {
      this.powerMenuButton.setBackground(new Color(158, 202, 232));
      this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder());
      this.powerMenuButton.setBounds(90, 0, 20, 20);
    } 
    this.videoButton.setBackground(new Color(158, 202, 232));
    this.videoButton.setBorder(BorderFactory.createEmptyBorder());
    this.videoButton.setBounds(120, 0, 20, 20);
    add(this.videoButton);
    if (this.mouseMenuButton != null) {
      this.mouseMenuButton.setBackground(new Color(158, 202, 232));
      this.mouseMenuButton.setBorder(BorderFactory.createEmptyBorder());
      this.mouseMenuButton.setBounds(150, 0, 20, 20);
    } 
    if (null != this.virtualMedia) {
      this.btnCDMenu.setBackground(new Color(158, 202, 232));
      this.btnCDMenu.setBounds(180, 0, 23, 23);
      this.btnFlpMenu.setBackground(new Color(158, 202, 232));
      this.btnFlpMenu.setBounds(210, 0, 23, 23);
    } 
    this.btnCreateImage.setBackground(new Color(158, 202, 232));
    this.btnCreateImage.setBounds(240, 0, 23, 23);
    add(this.btnCreateImage);
    this.kvmInterface.getToolbar().getCombineKey().setBounds(270, 1, 20, 20);
    add(this.kvmInterface.getToolbar().getCombineKey());
    if (this.keyboardLayoutButton != null) {
      this.keyboardLayoutButton.setBackground(new Color(158, 202, 232));
      this.keyboardLayoutButton.setBorder(BorderFactory.createEmptyBorder());
      this.keyboardLayoutButton.setBounds(300, 0, 20, 20);
      add(this.keyboardLayoutButton);
    } 
    JLabel lblcombo = new JLabel(this.kvmInterface.getKvmUtil().getString("DQT_Value"));
    if (Base.getLocal().equalsIgnoreCase("zh")) {
      lblcombo.setFont(new Font("榛戜綋", 0, 12));
    }
    else if (Base.getLocal().equalsIgnoreCase("ja")) {
      lblcombo.setFont(new Font("MS Mincho", 0, 12));
    }
    else {
      lblcombo.setFont(new Font("Times new roman", 0, 12));
    } 
    lblcombo.setBounds(330, 0, 90, 20);
    add(lblcombo);
    this.dqtSlider.setBackground(new Color(158, 202, 232));
    this.dqtSlider.setBorder(BorderFactory.createEmptyBorder());
    this.dqtSlider.setBounds(402, 0, 96, 32);
    add(this.dqtSlider);
    JLabel lblnum = new JLabel("num");
    lblnum.setFont(new Font("sansserif", 0, 12));
    lblnum.setBounds(510, 0, 28, 20);
    add(lblnum);
    this.kvmInterface.getToolbar().getNumColorButton().setBounds(538, 5, 10, 10);
    add(this.kvmInterface.getToolbar().getNumColorButton());
    JLabel lblcaps = new JLabel("caps");
    lblcaps.setFont(new Font("sansserif", 0, 12));
    lblcaps.setBounds(553, 0, 30, 20);
    add(lblcaps);
    this.kvmInterface.getToolbar().getCapsColorButton().setBounds(583, 5, 10, 10);
    add(this.kvmInterface.getToolbar().getCapsColorButton());
    JLabel lblscroll = new JLabel("scroll");
    lblscroll.setFont(new Font("sansserif", 0, 12));
    lblscroll.setBounds(598, 0, 35, 20);
    add(lblscroll);
    this.kvmInterface.getToolbar().getScrollColorButton().setBounds(633, 5, 10, 10);
    add(this.kvmInterface.getToolbar().getScrollColorButton());
    this.helpButton.setBackground(new Color(158, 202, 232));
    this.helpButton.setBorder(BorderFactory.createEmptyBorder());
    this.helpButton.setBounds(648, 0, 20, 20);
    add(this.helpButton);
    if (this.kvmInterface.getBladeSize() == 1) {
      this.btnCreateImage.setContentAreaFilled(false);
      this.btnCreateImage.addMouseListener(new BtnCreateImageMouseAdapter(this));
    } 
    if (null != this.virtualMedia) {
      this.btnCDMenu.setContentAreaFilled(false);
      this.btnCDMenu.addMouseListener(new BtnCDMenuMouseAdapter(this));
      this.btnFlpMenu.setContentAreaFilled(false);
      this.btnFlpMenu.addMouseListener(new BtnFlpMenuMouseAdapter(this));
    } 
    if (this.powerMenuButton != null) {
      this.powerMenuButton.setContentAreaFilled(false);
      this.powerMenuButton.addMouseListener(new PowerMenuButtonMouseAdapter(this));
    } 
    this.helpButton.setContentAreaFilled(false);
    this.helpButton.addMouseListener(new HelpButtonMouseAdapter(this));
    this.btnShow.setContentAreaFilled(false);
    this.btnShow.addMouseListener(new BtnShowMouseAdapter(this));
  }
  public MouseListener vmmMonuseListener() {
    MouseAdapter adapter = new VmmMonuseListenerMouseAdapter(this);
    return adapter;
  }
  public MouseListener kvmMenuMonuseListener() {
    MouseAdapter adapter = new KvmMenuMouseAdapter(this);
    return adapter;
  }
  public void startStateMenu() {
    if (this.kvmInterface.isFullScreen()) {
      setVisible(false);
      return;
    } 
    if (!this.kvmInterface.isFullScreen() && this.imagePanel.getImagePaneWidth() != 0 && this.imagePanel
      .getImagePaneWidth() == this.imgwidth) {
      if (this.kvmInterface.isReturnToWin()) {
        setVisible(true);
        setLocationToPanel();
        this.kvmInterface.setReturnToWin(false);
      } 
      return;
    } 
    if (this.imagePanel.getImagePaneWidth() != 0 && this.imagePanel.getImagePaneWidth() != this.imgwidth) {
      this.imgwidth = this.imagePanel.getImagePaneWidth();
      this.imgheight = this.imagePanel.getImagePaneHeight();
    }
    else {
      this.imgwidth = 1024;
      this.imgheight = 768;
    } 
    setLocationToPanel();
  }
  public void setLocationToPanel() {
    if (this.imgwidth > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
      setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2 + this.kvmInterface
          .getH(), -1 + this.kvmInterface.getVv());
      if (null != this.virtualMedia) {
        this.virtualMedia.getFlp()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.virtualMedia.getFlp().getWidth()) / 2 + this.kvmInterface.getH(), 
            getHeight() + this.kvmInterface.getVv() - 1);
        this.virtualMedia.getCdp()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.virtualMedia.getCdp().getWidth()) / 2 + this.kvmInterface.getH(), 
            getHeight() + this.kvmInterface.getVv() - 1);
      } 
      if (this.kvmInterface.getBladeSize() == 1)
      {
        this.kvmInterface.getImageFile()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.kvmInterface.getImageFile().getWidth()) / 2 + this.kvmInterface
            .getH(), 
            getHeight() + this.kvmInterface.getVv() - 1);
      }
    }
    else {
      setLocation((this.imgwidth - getWidth()) / 2 + this.kvmInterface.getH(), -1 + this.kvmInterface.getVv());
      if (null != this.virtualMedia) {
        this.virtualMedia.getFlp().setLocation((this.imgwidth - this.virtualMedia.getFlp().getWidth()) / 2 + this.kvmInterface
            .getH(), 
            getHeight() + this.kvmInterface.getVv() - 1);
        this.virtualMedia.getCdp().setLocation((this.imgwidth - this.virtualMedia.getCdp().getWidth()) / 2 + this.kvmInterface
            .getH(), 
            getHeight() + this.kvmInterface.getVv() - 1);
      } 
      if (this.kvmInterface.getBladeSize() == 1)
      {
        this.kvmInterface.getImageFile().setLocation((this.imgwidth - this.kvmInterface.getImageFile().getWidth()) / 2 + this.kvmInterface
            .getH(), 
            getHeight() + this.kvmInterface.getVv() - 1);
      }
    } 
    if (this.isShowPanel)
    {
      setVisible(true);
    }
    if (null != this.virtualMedia) {
      this.virtualMedia.getFlp().setVisible(false);
      this.virtualMedia.getCdp().setVisible(false);
      this.btnCDMenu.setBorder((Border)null);
      this.btnFlpMenu.setBorder((Border)null);
    } 
    if (this.kvmInterface.getBladeSize() == 1) {
      this.kvmInterface.getImageFile().setVisible(false);
      this.btnCreateImage.setBorder((Border)null);
    } 
    if (null != this.floatToolbar && null != this.floatToolbar.powerMenu)
    {
      this.floatToolbar.powerMenu.setVisible(false);
    }
  }
  public void fullStateMenu() {
    if (null != this.virtualMedia) {
      this.virtualMedia.getCdp().setVisible(false);
      this.virtualMedia.getFlp().setVisible(false);
    } 
    setVisible(false);
  }
  private JButton createButton(String resCode, String imageURL) {
    JButton button = new JButton(new ImageIcon(FloatToolbar.class.getResource(imageURL)));
    button.setToolTipText(this.kvmInterface.getKvmUtil().getString(resCode));
    return button;
  }
  public JLabel createDqtLabel(String resCode) {
    JLabel label = new JLabel(this.kvmInterface.getKvmUtil().getString(resCode));
    if (Base.getLocal().equalsIgnoreCase("zh")) {
      label.setFont(new Font("榛戜綋", 0, 12));
    }
    else if (Base.getLocal().equalsIgnoreCase("ja")) {
      label.setFont(new Font("MS Mincho", 0, 12));
    }
    else {
      label.setFont(new Font("Times new roman", 0, 12));
    } 
    return label;
  }
  private JButton createFloatButton() {
    this.btnShow = createButton("button_float", "resource/images/float.gif");
    this.btnShow.addActionListener(createFloatAction());
    this.btnShow.setBackground(new Color(158, 202, 232));
    this.btnShow.setBorder((Border)null);
    this.btnShow.setFocusable(false);
    return this.btnShow;
  }
  private JButton createImageButton() {
    this.btnCreateImage = createButton("create_image", "resource/images/virtualne.gif");
    this.btnCreateImage.addActionListener(createImageAction());
    this.btnShow.setBackground(new Color(158, 202, 232));
    this.btnCreateImage.setFocusable(false);
    return this.btnCreateImage;
  }
  private JButton createCDMenuButton() {
    this.btnCDMenu = createButton("cd_cdroms", "resource/images/cd.gif");
    this.btnCDMenu.setBorder((Border)null);
    this.btnCDMenu.addActionListener(createCDAction());
    this.btnCDMenu.setBackground(new Color(158, 202, 232));
    this.btnCDMenu.setFocusable(false);
    return this.btnCDMenu;
  }
  private JButton createFlpMenuButton() {
    this.btnFlpMenu = createButton("flp_floppy", "resource/images/flp.gif");
    this.btnFlpMenu.setBorder((Border)null);
    this.btnFlpMenu.addActionListener(createFlpAction());
    this.btnFlpMenu.setBackground(new Color(158, 202, 232));
    this.btnFlpMenu.setFocusable(false);
    return this.btnFlpMenu;
  }
  public JButton createMouseMenuButton() {
    this.mouseMenuButton = createButton("Mouse_Manager", "resource/images/mouse_manager.png");
    this.mouseMenu = new MousePopupMenu(this.kvmInterface);
    this.mouseMenuButton.setBackground(new Color(158, 202, 232));
    this.mouseMenuButton.setFocusable(false);
    this.mouseMenuButton.addActionListener(new CreateMouseMenuButton(this));
    return this.mouseMenuButton;
  }
  public JButton createKeyboardLayoutMenuButton() {
    this.keyboardLayoutButton = createButton("Keyboard_Layout", "resource/images/keyboard_layout.png");
    this.keyboardLayoutMenu = new KeyboardPopupMenu(this.kvmInterface);
    this.keyboardLayoutButton.setBackground(new Color(158, 202, 232));
    this.keyboardLayoutButton.setFocusable(false);
    this.keyboardLayoutButton.addActionListener(new CreateKeyboardLayoutMenuButton(this));
    return this.keyboardLayoutButton;
  }
  private Action createImageAction() {
    Action action = new CreateImageAction(this);
    return action;
  }
  private Action createCDAction() {
    Action action = new CreateCDAction(this);
    return action;
  }
  private Action createFlpAction() {
    Action action = new CreateFlpAction(this);
    return action;
  }
  private Action createFloatAction() {
    Action action = new CreateFloatAction(this);
    return action;
  }
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(new Color(158, 202, 232));
    int w = getWidth();
    int h = getHeight();
    int[] x = { 0, w, w - h, h, 0 };
    int[] y = { 0, 0, h, h, 0 };
    int nPoints = 4;
    Polygon poly = new Polygon(x, y, nPoints);
    g2d.fillPolygon(poly);
    g2d.drawPolygon(poly);
  }
  public void setVirtualMediaVisible(boolean cdflag, boolean flpflag) {
    this.virtualMedia.getCdp().setVisible(cdflag);
    this.virtualMedia.getFlp().setVisible(flpflag);
  }
  public void setCDVisibleAndLocation(boolean cdflag, int x, int y) {
    this.virtualMedia.getCdp().setVisible(cdflag);
    this.virtualMedia.getCdp().setLocation(x, y);
  }
  public void setFlpVisibleAndLocation(boolean flpflag, int x, int y) {
    this.virtualMedia.getFlp().setVisible(flpflag);
    this.virtualMedia.getFlp().setLocation(x, y);
  }
  public void setCDVisible(boolean cdflag) {
    this.virtualMedia.getCdp().setVisible(cdflag);
  }
  public void setFlpVisible(boolean flpflag) {
    this.virtualMedia.getFlp().setVisible(flpflag);
  }
  public void setCDLocation(int x, int y) {
    this.virtualMedia.getCdp().setLocation(x, y);
  }
  public void setFlpLocation(int x, int y) {
    this.virtualMedia.getFlp().setLocation(x, y);
  }
  public boolean isVirtualMedia() {
    if (null != this.virtualMedia)
    {
      return true;
    }
    return false;
  }
  public int getCDX() {
    return this.virtualMedia.getCdp().getX();
  }
  public int getCDY() {
    return this.virtualMedia.getCdp().getY();
  }
  public int getCDWidth() {
    return this.virtualMedia.getCdp().getWidth();
  }
  public int getCDHeight() {
    return this.virtualMedia.getCdp().getHeight();
  }
  public int getFlpX() {
    return this.virtualMedia.getFlp().getX();
  }
  public int getFlpY() {
    return this.virtualMedia.getFlp().getY();
  }
  public int getFlpWidth() {
    return this.virtualMedia.getFlp().getWidth();
  }
  public int getFlpHeight() {
    return this.virtualMedia.getFlp().getHeight();
  }
  public JPanel getCDPanel() {
    return this.virtualMedia.getCdp();
  }
  public JPanel getFlpPanel() {
    return this.virtualMedia.getFlp();
  }
  public VirtualMedia getVirtualMedia() {
    return this.virtualMedia;
  }
  public void setVirtualMedia(VirtualMedia virtualMedia) {
    this.virtualMedia = virtualMedia;
  }
  public void destroyVmLink() {
    if (null == this.virtualMedia) {
      return;
    }
    this.virtualMedia.destoryVMM();
  }
  public JFrame getBMCHelpDocument() {
    String path = "";
    if (Base.getLocal().equalsIgnoreCase("en")) {
      path = "resource/helpdoc/help/kvmvmm_en.html";
    }
    else if (Base.getLocal().equalsIgnoreCase("ja")) {
      path = "resource/helpdoc/help/kvmvmm_ja.html";
    }
    else if (Base.getLocal().equalsIgnoreCase("fr")) {
      path = "resource/helpdoc/help/kvmvmm_fr.html";
    }
    else {
      path = "resource/helpdoc/help/kvmvmm_zh.html";
    } 
    if (null == this.help)
    {
      this.help = new HelpDocument(path);
    }
    this.helpFrm = new JFrame();
    this.helpFrm.addWindowListener(new GetBMCHelpDocumentWindowAdapter(this));
    this.helpFrm.setSize(800, 650);
    int x = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.helpFrm.getWidth()) / 2;
    int y = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.helpFrm.getHeight()) / 2;
    this.helpFrm.setLocation(x, y);
    this.helpFrm.setLayout(new BorderLayout());
    this.helpFrm.getContentPane().add(this.help.getScroller());
    this.helpFrm.setVisible(true);
    return this.helpFrm;
  }
  public JButton getMouseMenuButton() {
    return this.mouseMenuButton;
  }
  public void setMouseMenuButton(JButton mouseMenuButton) {
    this.mouseMenuButton = mouseMenuButton;
  }
  public FloatToolbar getFloatToolbar() {
    return this.floatToolbar;
  }
  public void setFloatToolbar(FloatToolbar floatToolbar) {
    this.floatToolbar = floatToolbar;
  }
  public int getSlider_value() {
    return this.slider_value;
  }
  public void setSlider_value(int sliderValue) {
    this.slider_value = sliderValue;
  }
  public int getPre_slider_value() {
    return this.pre_slider_value;
  }
  public void setPre_slider_value(int preSliderValue) {
    this.pre_slider_value = preSliderValue;
  }
  public JButton getPowerMenuButton() {
    return this.powerMenuButton;
  }
  public void setPowerMenuButton(JButton powerMenuButton) {
    this.powerMenuButton = powerMenuButton;
  }
  public JButton getHelpButton() {
    return this.helpButton;
  }
  public void setHelpButton(JButton helpButton) {
    this.helpButton = helpButton;
  }
}
