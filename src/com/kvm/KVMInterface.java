package com.kvm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
class KVMInterface
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private ToolBarPanel toolBarPanel;
  private int bladeSize = 1;
  private ArrayList<Object> bladeList = null;
  public ArrayList<Object> getBladeList() {
    return this.bladeList;
  }
  private String verifyValueExt = null;
  public void setVerifyValueExt(String verifyExt) {
    this.verifyValueExt = verifyExt;
  }
  public String getVerifyValueExt() {
    return this.verifyValueExt;
  }
  public boolean isNeedConsultation() {
    if (this.verifyValueExt == null || this.verifyValueExt.equals(""))
    {
      return false;
    }
    return true;
  }
  private String hmac = "PBKDF2WithHmacSHA1";
  public void setHmac(String h) {
    this.hmac = h;
  }
  public String getHmac() {
    return this.hmac;
  }
  private int iterations = 5000;
  public void setIterations(int iter) {
    this.iterations = iter;
  }
  public int getIterations() {
    return this.iterations;
  }
  public void setBladeList(ArrayList<Object> bladeList) {
    this.bladeList = bladeList;
  }
  private KvmAppletToolBar toolbar = null;
  private FullScreen fullScreen;
  public KvmAppletToolBar getToolbar() {
    return this.toolbar;
  }
  public void setToolbar(KvmAppletToolBar toolbar) {
    this.toolbar = toolbar;
  }
  public FullScreen getFullScreen() {
    return this.fullScreen;
  }
  private boolean isFullScreen = false;
  public boolean isFullScreen() {
    return this.isFullScreen;
  }
  public void setFullScreen(boolean isFullScreen) {
    this.isFullScreen = isFullScreen;
  }
  private StatusBar statusBar = null;
  public StatusBar getStatusBar() {
    return this.statusBar;
  }
  public void setStatusBar(StatusBar statusBar) {
    this.statusBar = statusBar;
  }
  private JTabbedPane tabbedpane = null;
  protected JTabbedPane getTabbedpane() {
    return this.tabbedpane;
  }
  public void setTabbedpane(JTabbedPane tabbedpane) {
    this.tabbedpane = tabbedpane;
  }
  private JFrame toolFrame = null;
  public JFrame getToolFrame() {
    return this.toolFrame;
  }
  public void setToolFrame(JFrame toolFrame) {
    this.toolFrame = toolFrame;
  }
  private ImagePane imagePane1 = null;
  public ImagePane getImagePane1() {
    return this.imagePane1;
  }
  public void setImagePane1(ImagePane imagePane1) {
    this.imagePane1 = imagePane1;
  }
  private ImagePane imagePane2 = null;
  public ImagePane getImagePane2() {
    return this.imagePane2;
  }
  public void setImagePane2(ImagePane imagePane2) {
    this.imagePane2 = imagePane2;
  }
  private ImagePane imagePane3 = null;
  public ImagePane getImagePane3() {
    return this.imagePane3;
  }
  public void setImagePane3(ImagePane imagePane3) {
    this.imagePane3 = imagePane3;
  }
  private ImagePane imagePane4 = null;
  public ImagePane getImagePane4() {
    return this.imagePane4;
  }
  public void setImagePane4(ImagePane imagePane4) {
    this.imagePane4 = imagePane4;
  }
  private ImagePane imagePane5 = null;
  public ImagePane getImagePane5() {
    return this.imagePane5;
  }
  public void setImagePane5(ImagePane imagePane5) {
    this.imagePane5 = imagePane5;
  }
  private ImagePane imagePane6 = null;
  public ImagePane getImagePane6() {
    return this.imagePane6;
  }
  public void setImagePane6(ImagePane imagePane6) {
    this.imagePane6 = imagePane6;
  }
  private ImagePane imagePane7 = null;
  public ImagePane getImagePane7() {
    return this.imagePane7;
  }
  public void setImagePane7(ImagePane imagePane7) {
    this.imagePane7 = imagePane7;
  }
  private ImagePane imagePane8 = null;
  public ImagePane getImagePane8() {
    return this.imagePane8;
  }
  public void setImagePane8(ImagePane imagePane8) {
    this.imagePane8 = imagePane8;
  }
  private ImagePane imagePane9 = null;
  public ImagePane getImagePane9() {
    return this.imagePane9;
  }
  public void setImagePane9(ImagePane imagePane9) {
    this.imagePane9 = imagePane9;
  }
  private ImagePane imagePane10 = null;
  public ImagePane getImagePane10() {
    return this.imagePane10;
  }
  public void setImagePane10(ImagePane imagePane10) {
    this.imagePane10 = imagePane10;
  }
  private ImagePane imagePane11 = null;
  public ImagePane getImagePane11() {
    return this.imagePane11;
  }
  public void setImagePane11(ImagePane imagePane11) {
    this.imagePane11 = imagePane11;
  }
  private ImagePane imagePane12 = null;
  public ImagePane getImagePane12() {
    return this.imagePane12;
  }
  public void setImagePane12(ImagePane imagePane12) {
    this.imagePane12 = imagePane12;
  }
  private ImagePane imagePane13 = null;
  public ImagePane getImagePane13() {
    return this.imagePane13;
  }
  public void setImagePane13(ImagePane imagePane13) {
    this.imagePane13 = imagePane13;
  }
  private ImagePane imagePane14 = null;
  public ImagePane getImagePane14() {
    return this.imagePane14;
  }
  public void setImagePane14(ImagePane imagePane14) {
    this.imagePane14 = imagePane14;
  }
  private int actionBlade = 0;
  public static final int BUTTON_POSITION = 6;
  public int getActionBlade() {
    return this.actionBlade;
  }
  public void setActionBlade(int actionBlade) {
    this.actionBlade = actionBlade;
  }
  private transient KVMUtil kvmUtil = null;
  public KVMUtil getKvmUtil() {
    return this.kvmUtil;
  }
  public void setKvmUtil(KVMUtil kvmUtil) {
    this.kvmUtil = kvmUtil;
  }
  private transient Client client = null;
  public Client getClient() {
    return this.client;
  }
  public void setClient(Client client) {
    this.client = client;
  }
  private transient PackData packData = new PackData();
  public PackData getPackData() {
    return this.packData;
  }
  public void setPackData(PackData packData) {
    this.packData = packData;
  }
  private transient ClientSocketCommunity clientSocket = new ClientSocketCommunity();
  public ClientSocketCommunity getClientSocket() {
    return this.clientSocket;
  }
  public void setClientSocket(ClientSocketCommunity clientSocket) {
    this.clientSocket = clientSocket;
  }
  private transient Base base = null;
  public Base getBase() {
    return this.base;
  }
  public void setBase(Base base) {
    this.base = base;
  }
  private int codeKey = 0;
  public int getCodeKey() {
    return this.codeKey;
  }
  public void setCodeKey(int codeKey) {
    this.codeKey = codeKey;
  }
  private byte[] encodeKey = null;
  public byte[] getEncodeKey() {
    return (byte[])this.encodeKey.clone();
  }
  public void setEncodeKey(byte[] encodeKey) {
    if (encodeKey != null) {
      this.encodeKey = (byte[])encodeKey.clone();
    }
    else {
      this.encodeKey = null;
    } 
  }
  private byte[] reconnKey = null;
  public byte[] getReconnKey() {
    if (this.reconnKey == null)
    {
      return this.reconnKey;
    }
    return (byte[])this.reconnKey.clone();
  }
  public void setReconnKey(byte[] reconnKey) {
    if (reconnKey != null) {
      this.reconnKey = (byte[])reconnKey.clone();
    }
    else {
      this.reconnKey = null;
    } 
  }
  private boolean isInReconnect = false;
  public boolean getReconnectState() {
    return this.isInReconnect;
  }
  public void setReconnectState(boolean state) {
    this.isInReconnect = state;
  }
  private int reconnectUserErrorTimes = 0;
  public int getReconnectUserErrorTimes() {
    return this.reconnectUserErrorTimes;
  }
  public void setReconnectUserErrorTimes(int times) {
    this.reconnectUserErrorTimes = times;
  }
  private boolean clickFlag = false;
  private String bladeFlag;
  public boolean isClickFlag() {
    return this.clickFlag;
  }
  public void setClickFlag(boolean clickFlag) {
    this.clickFlag = clickFlag;
  }
  private boolean isReturnToWin = false;
  public boolean isReturnToWin() {
    return this.isReturnToWin;
  }
  public void setReturnToWin(boolean isReturnToWin) {
    this.isReturnToWin = isReturnToWin;
  }
  private FloatToolbar floatToolbar = null;
  public FloatToolbar getFloatToolbar() {
    return this.floatToolbar;
  }
  public void setFloatToolbar(FloatToolbar floatToolbar) {
    this.floatToolbar = floatToolbar;
  }
  private ImageFile imageFile = null;
  private JScrollPane scrollPane;
  public ImageFile getImageFile() {
    return this.imageFile;
  }
  public void setImageFile(ImageFile imageFile) {
    this.imageFile = imageFile;
  }
  private JScrollBar vbar = null;
  private JScrollBar hbar = null;
  private int v = 0;
  public int getV() {
    return this.v;
  }
  public void setV(int v) {
    this.v = v;
  }
  private int vv = 0;
  public int getVv() {
    return this.vv;
  }
  public void setVv(int vv) {
    this.vv = vv;
  }
  private int h = 0;
  public int getH() {
    return this.h;
  }
  public void setH(int h) {
    this.h = h;
  }
  private int newv = 0;
  public int getNewv() {
    return this.newv;
  }
  public void setNewv(int newv) {
    this.newv = newv;
  }
  private int newh = 0;
  public int getNewh() {
    return this.newh;
  }
  public void setNewh(int newh) {
    this.newh = newh;
  }
  private int SCROLLPANE_TITLE_HEIGHT = 5;
  public int getSCROLLPANE_TITLE_HEIGHT() {
    return this.SCROLLPANE_TITLE_HEIGHT;
  }
  public void setSCROLLPANE_TITLE_HEIGHT(int sCROLLPANE_TITLE_HEIGHT) {
    this.SCROLLPANE_TITLE_HEIGHT = sCROLLPANE_TITLE_HEIGHT;
  }
  private int iImageFocused = 0;
  public int getiImageFocused() {
    return this.iImageFocused;
  }
  public void setiImageFocused(int iImageFocused) {
    this.iImageFocused = iImageFocused;
  }
  private int iKeyPressControl = 0;
  public int getiKeyPressControl() {
    return this.iKeyPressControl;
  }
  public void setiKeyPressControl(int iKeyPressControl) {
    this.iKeyPressControl = iKeyPressControl;
  }
  private int iKeyPressTotal = 0; private String bladeNames; transient MouseMotionAdapter scrollMouseMotion; private transient AdjustmentListener vlistener;
  private transient AdjustmentListener hlistener;
  public int getiKeyPressTotal() {
    return this.iKeyPressTotal;
  }
  public void setiKeyPressTotal(int iKeyPressTotal) {
    this.iKeyPressTotal = iKeyPressTotal;
  }
  public String getBladeNames() {
    return this.bladeNames;
  }
  public void setBladeNames(String bladeNames) {
    this.bladeNames = bladeNames;
  }
  public String getBladeFlag() {
    return this.bladeFlag;
  }
  public void setBladeFlag(String bladeFlag) {
    this.bladeFlag = bladeFlag;
  }
  public KVMInterface(KVMUtil kvmUtil, Client client, PackData packData, ClientSocketCommunity clientSocket, Base base) {
    this.scrollMouseMotion = new ScrollMouseMotionAdapter(this);
    this.vlistener = new VAdjustmentListener(this);
    this.hlistener = new HAdjustmentListener(this); this.bladeSize = Base.getBladeSize(); this.bladeList = Base.getBladeList(); this.client = client; this.kvmUtil = kvmUtil; this.clientSocket = clientSocket; this.packData = packData; this.base = base; parseTypeDataInitBlade(Base.getTypeData()); String tip = kvmUtil.getString("return_window"); this.statusBar = new StatusBar(tip); this.statusBar.setClient(client); initToolBarPanel(); this.statusBar.setToolBarPanel(this.toolBarPanel); setLayout(new BorderLayout()); add(this.toolBarPanel, "North"); this.tabbedpane = new JTabbedPane(); this.tabbedpane.setFocusable(false); Dimension dim = new Dimension(1920, 1200); this.tabbedpane.setPreferredSize(dim); if (getBladeSize() == 1)
      this.tabbedpane.setUI(new PaintTabBorder());  this.scrollPane = new JScrollPane(this.tabbedpane); add(this.scrollPane, "Center"); add(this.statusBar, "South"); this.tabbedpane.getModel().addChangeListener(this.kvmUtil.changeListener); addscrollBarListener(); this.scrollPane.addMouseMotionListener(this.scrollMouseMotion); this.imageFile = new ImageFile(Base.getLocal()); if (getBladeSize() == 1) { this.SCROLLPANE_TITLE_HEIGHT = 5; }
    else
    { this.SCROLLPANE_TITLE_HEIGHT = 26; }
     } private void initToolBarPanel() { this.toolBarPanel = new ToolBarPanel();
    this.toolBarPanel.setLayout(new BorderLayout());
    this.toolbar = new KvmAppletToolBar(this);
    this.toolbar.setFloatable(false);
    if (KVMUtil.isLinuxOS() && getBladeSize() > 1) {
      this.toolbar.setPreferredSize(new Dimension(700, 50));
      this.toolbar.setLayout((LayoutManager)null);
    } 
    this.toolbar.createDisConnectBladeButton();
    this.toolbar.createComKeyButton();
    this.toolbar.createFullButton();
    this.toolbar.createRefreshButton();
    this.toolbar.creatBladeButton();
    this.toolbar.createNumColorButton();
    this.toolbar.createCapsColorButton();
    this.toolbar.createScrollColorButton();
    this.toolbar.createMenuButton();
    if (KVMUtil.isLinuxOS() && getBladeSize() > 1)
    {
      this.toolbar.getImageButton().setEnabled(false);
    }
    this.toolbar.createHelpButton();
    this.toolBarPanel.add(this.toolbar, "Center"); }
  public void addscrollBarListener() { this.vbar = this.scrollPane.getVerticalScrollBar();
    this.vbar.addAdjustmentListener(this.vlistener);
    this.hbar = this.scrollPane.getHorizontalScrollBar();
    this.hbar.addAdjustmentListener(this.hlistener); } protected void setLinuxSize(Object objName, int x, int y, int width, int heigh) {
    Component cp = null;
    if (objName != null && objName instanceof Component) {
      cp = (Component)objName;
    } else {
      return;
    } 
    cp.setBounds(x, y, width, heigh);
  }
  public void createToolFrame() {
    if (this.toolFrame == null) {
      this.toolFrame = new JFrame();
      this.toolFrame.setUndecorated(true);
      this.toolFrame.getContentPane().setBackground(new Color(158, 202, 232));
      this.toolFrame.setSize(this.fullScreen.getToolBarFrame().getSize());
      this.toolFrame.setLayout(new BorderLayout());
      if (KVMUtil.isWindowsOS()) {
        this.toolFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.fullScreen.getToolBarFrame().getWidth()) / 2.0D) + 1, 1);
      }
      else if (KVMUtil.isLinuxOS()) {
        this.toolFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.fullScreen.getToolBarFrame().getWidth()) / 2.0D) + 1, 21);
      } 
    } 
    this.toolFrame.getContentPane().removeAll();
    this.toolFrame.getContentPane().add(this.fullScreen.getToolBar(), "Center");
    this.toolFrame.setVisible(false);
  }
  public Action shortCutAction() {
    Action action = new KVMAppletShortCutAction(this);
    return action;
  }
  public Action colorBitAction() {
    Action action = new KVMAppletColorBitAction(this);
    return action;
  }
  protected void colorBit() {
    JDialog tem = (new JOptionPane()).createDialog(this, "");
    new ColorBit(tem, this.actionBlade, this);
  }
  protected void produceComKey() {
    JDialog tem = (new JOptionPane()).createDialog(this, "");
    new CombinationKey(tem, this.actionBlade, this.kvmUtil.getImagePane(this.actionBlade).getPack(), this);
  }
  public Action fullScreenAction() {
    Action action = new KVMAppletFullScreenAction(this);
    return action;
  }
  protected void produceFullScreen() {
    if (!this.isFullScreen) {
      ImagePane imagePane = this.kvmUtil.getImagePane(this.actionBlade);
      this.tabbedpane.removeAll();
      if (getBladeSize() == 1) {
        this.imageFile.setVisible(false);
        this.floatToolbar.setShowingImagep(false);
      } 
      this.floatToolbar.fullStateMenu();
      if (this.fullScreen == null) {
        JDialog tem = (new JOptionPane()).createDialog(this, "");
        tem.setModal(true);
        this.fullScreen = new FullScreen(tem, imagePane, this);
        this.kvmUtil.setFullToolBar(false);
        if (imagePane.isNew())
        {
          if (this.base.isMstsc()) {
            this.fullScreen.getToolBar().getMouseSynButton().setEnabled(true);
          }
          else {
            this.fullScreen.getToolBar().getMouseSynButton().setEnabled(false);
          } 
        }
        this.fullScreen.getToolBar().startButtonState();
      }
      else {
        this.fullScreen.getContentPane().removeAll();
        this.fullScreen.getImageParentPane().setLayout(new BorderLayout());
        this.fullScreen.setImageParentScrollPane((JScrollPane)null);
        this.fullScreen.getImageParentPane().setPreferredSize(new Dimension(imagePane.getImagePaneWidth(), imagePane
              .getImagePaneHeight()));
        this.fullScreen.getImageParentPane().add(imagePane);
        imagePane.setShowtoolBar(true);
        this.fullScreen.setImageParentScrollPane(new JScrollPane(this.fullScreen.getImageParentPane()));
        this.fullScreen.addscrollBarListener();
        this.fullScreen.getContentPane().add(this.fullScreen.getImageParentScrollPane(), "Center");
        this.fullScreen.setImagePane(imagePane);
        this.fullScreen.setActionBlade(this.actionBlade);
        this.kvmUtil.setFullToolBar(this.base.isDiv());
        if (imagePane.isNew()) {
          if (this.base.isMstsc())
          {
            this.fullScreen.setCursor(this.base.getDefCursor());
            imagePane.setCursor(this.base.getDefCursor());
            this.fullScreen.getToolBar().getMouseSynButton().setToolTipText(this.kvmUtil.getString("MouseSyn.Tip"));
          }
          else
          {
            this.fullScreen.setCursor(this.base.myCursor);
            imagePane.setCursor(this.base.myCursor);
            this.fullScreen.getToolBar().getMouseSynButton().setEnabled(false);
            MouseDisplacementImpl.setMode(1);
          }
        } else {
          this.fullScreen.setCursor(this.base.getDefCursor());
          imagePane.setCursor(this.base.getDefCursor());
          this.fullScreen.getToolBar().getMouseSynButton().setToolTipText(this.kvmUtil.getString("MouseSyn.Tip"));
        } 
        MouseDisplacementImpl.setKeyBoardStatus((imagePane.getNum() == 1), (byte)-112);
        MouseDisplacementImpl.setKeyBoardStatus((imagePane.getCaps() == 1), (byte)20);
        MouseDisplacementImpl.setKeyBoardStatus((imagePane.getScroll() == 1), (byte)-111);
        this.fullScreen.setVisible(true);
        this.fullScreen.addscrollBarListener();
        this.fullScreen.getToolBar().startButtonState();
      } 
      this.isFullScreen = true;
      if (imagePane.isContr()) {
        this.kvmUtil.setNumKeyColor(imagePane.getNum());
        this.kvmUtil.setCapsKeyColor(imagePane.getCaps());
        this.kvmUtil.setScrollKeyColor(imagePane.getScroll());
      }
      else {
        this.kvmUtil.setMoniKeyState(this.isFullScreen);
      } 
      this.kvmUtil.setNumAndCapLock();
      this.fullScreen.setButtonEnable();
      if (this.toolFrame != null) {
        this.toolFrame.setVisible(false);
        this.fullScreen.getToolBarFrame().removeAll();
        this.fullScreen.getToolBarFrame().add(this.fullScreen.getToolBar());
      } 
      imagePane.add(this.fullScreen.getToolBarFrame());
      this.fullScreen.getToolBarFrame().setVisible(false);
    } 
  }
  public Action refreshAction() {
    Action action = new KVMAppletRefreshAction(this);
    return action;
  }
  public Action disConnBladeAction() {
    Action action = new KVMAppletDisConnBladeAction(this);
    return action;
  }
  public Action synMouseAction() {
    Action action = new KVMAppletSynMouseAction(this);
    return action;
  }
  public void setFullScreen(FullScreen fullScreen) {
    this.fullScreen = fullScreen;
  }
  public void setBladeTip(String[] bladeNames) {
    if (bladeNames != null)
    {
      if (this.toolbar.isDynamicBlade()) {
        int bladeSize = this.bladeList.size();
        for (int i = 0; i < bladeSize; i++) {
          JButton blade = ((InterfaceContainer)this.bladeList.get(i)).getBladeButton();
          if (blade != null)
          {
            blade.setToolTipText(bladeNames[i]);
          }
        } 
      } 
    }
  }
  public int getBladeSize() {
    return this.bladeSize;
  }
  public void setBladeSize(int bladeSize) {
    this.bladeSize = bladeSize;
  }
  public JButton createBladeButton(int bladeNO) {
    String imagesRes = "resource/images/blade1.gif";
    imagesRes = "resource/images/blade" + bladeNO + ".gif";
    JButton blade = new JButton(new ImageIcon(KVMInterface.class.getResource(imagesRes.trim())));
    return blade;
  }
  public void parseTypeData(long typeData) {
    int bladeCount = 0;
    int connMode = 0;
    int bladeNO = 0;
    int mask = 1;
    bladeCount = (int)(typeData >> 32L & 0xFFL);
    connMode = (int)(typeData >> 40L & 0xFFL);
    Base.setBladeSize(bladeCount);
    this.bladeSize = bladeCount;
    Base.setConnMode(connMode);
    int[] blades = new int[bladeCount];
    this.bladeList = new ArrayList();
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < bladeCount; i++) {
      blades[i] = ((typeData & mask) == mask) ? 1 : 0;
      if (blades[i] == 1) {
        InterfaceContainer iContainer = new InterfaceContainer();
        if (connMode == 0) {
          virtualMedia = new VirtualMedia(Base.getLocal(), this.kvmUtil.getKvmInterface());
          virtualMedia.setStrIP(Base.getVmmConnIP());
          virtualMedia.setCodeKey(Base.getVmmCodeKey());
          virtualMedia.setPort(Base.getVmmPort());
          iContainer.setVirtualMedia(virtualMedia);
        } 
        ImagePane imagePane = new ImagePane(this);
        FloatToolbar floatToolbar = new FloatToolbar(imagePane, virtualMedia, this);
        floatToolbar.setVirtualMedia(virtualMedia);
        iContainer.setImagePane(imagePane);
        iContainer.setKvmInterface(this);
        iContainer.setFloatToolbar(floatToolbar);
        bladeNO = i + 1;
        JButton bladeButton = createBladeButton(bladeNO);
        iContainer.setBladeButton(bladeButton);
        iContainer.setBladeNumber(bladeNO);
        this.bladeList.add(iContainer);
        this.floatToolbar = floatToolbar;
      } 
      mask *= 2;
    } 
  }
  public void parseTypeDataInitBlade(long typeData) {
    int bladeCount = 0;
    int connMode = 0;
    int bladeNO = 0;
    int bRelPosition = 0;
    int mask = 1;
    bladeCount = (int)(typeData >> 32L & 0xFFL);
    connMode = (int)(typeData >> 40L & 0xFFL);
    Long[] aa = { Long.valueOf(typeData) };
    Debug.println(" bladeCount :" + bladeCount + " connMode :" + connMode);
    Debug.printf("\n typeData : %x \n", (Object[])aa);
    Base.setBladeSize(bladeCount);
    this.bladeSize = bladeCount;
    Base.setConnMode(connMode);
    int[] blades = new int[bladeCount];
    this.bladeList = new ArrayList();
    for (int i = 0; i < bladeCount; i++) {
      blades[i] = ((typeData & mask) == mask) ? 1 : 0;
      if (blades[i] == 1) {
        InterfaceContainer iContainer = new InterfaceContainer();
        bladeNO = i + 1;
        JButton bladeButton = createBladeButton(bladeNO);
        iContainer.setBladeButton(bladeButton);
        iContainer.setBladeNumber(bladeNO);
        iContainer.setBRelPosition(bRelPosition);
        this.bladeList.add(iContainer);
      }
      else {
        bRelPosition++;
      } 
      mask *= 2;
    } 
  }
  public void parseTypeDataCreatePanel() {
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.bladeList.size(); i++) {
      InterfaceContainer iContainer = (InterfaceContainer)this.bladeList.get(i);
      if (Base.getConnMode() == 0) {
        virtualMedia = new VirtualMedia(Base.getLocal(), this.kvmUtil.getKvmInterface());
        virtualMedia.setStrIP(Base.getVmmConnIP());
        virtualMedia.setCodeKey(Base.getVmmCodeKey());
        virtualMedia.setPort(Base.getVmmPort());
        iContainer.setVirtualMedia(virtualMedia);
      } 
      ImagePane imagePane = new ImagePane(this);
      FloatToolbar floatToolbar = new FloatToolbar(imagePane, virtualMedia, this);
      iContainer.setImagePane(imagePane);
      iContainer.setKvmInterface(this);
      iContainer.setFloatToolbar(floatToolbar);
    } 
  }
  public ImagePane createImagPanePanel(int bladeNO) {
    ImagePane imagePane = null;
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.bladeList.size(); i++) {
      iContainer = (InterfaceContainer)this.bladeList.get(i);
      imagePane = iContainer.getImagePane();
      if (iContainer.getBladeNumber() == bladeNO)
      {
        if (imagePane == null || imagePane.getBladeNumber() == 0) {
          if (Base.getConnMode() == 0) {
            virtualMedia = new VirtualMedia(Base.getLocal(), this.kvmUtil.getKvmInterface());
            virtualMedia.setStrIP(Base.getVmmConnIP());
            virtualMedia.setCodeKey(Base.getVmmCodeKey());
            virtualMedia.setPort(Base.getVmmPort());
            iContainer.setVirtualMedia(virtualMedia);
          } 
          imagePane = new ImagePane(this);
          FloatToolbar floatToolbar = new FloatToolbar(imagePane, virtualMedia, this);
          iContainer.setImagePane(imagePane);
          iContainer.setKvmInterface(this);
          iContainer.setFloatToolbar(floatToolbar);
          this.floatToolbar = iContainer.getFloatToolbar();
          break;
        } 
      }
    } 
    return imagePane;
  }
}
