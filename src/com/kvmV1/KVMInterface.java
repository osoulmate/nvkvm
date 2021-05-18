package com.kvmV1;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.library.LoggerUtil;
public class KVMInterface
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private ToolBarPanel toolBarPanel;
  private int bladeSize = 14;
  private String productType = "";
  public ArrayList<InterfaceContainer> bladeList = null;
  public KvmToolBar toolbar = null;
  public FullScreen fullScreen;
  public JPanel imageParentPane;
  public boolean isFullScreen = false;
  public StatusBar statusBar = null;
  public JTabbedPane tabbedpane = null;
  public JFrame toolFrame = null;
  public ImagePane imagePane1 = null;
  public ImagePane imagePane2 = null;
  public ImagePane imagePane3 = null;
  public ImagePane imagePane4 = null;
  public ImagePane imagePane5 = null;
  public ImagePane imagePane6 = null;
  public ImagePane imagePane7 = null;
  public ImagePane imagePane8 = null;
  public ImagePane imagePane9 = null;
  public ImagePane imagePane10 = null;
  public ImagePane imagePane11 = null;
  public ImagePane imagePane12 = null;
  public ImagePane imagePane13 = null;
  public ImagePane imagePane14 = null;
  public int actionBlade = 0;
  public static final int BUTTON_POSITION = 6;
  public KVMUtil kvmUtil = null;
  public Client client = null;
  public PackData packData = new PackData();
  public ClientSocketCommunity clientSocket = new ClientSocketCommunity();
  public Base base = null;
  public int codeKey = 0;
  public boolean clickFlag = false;
  private String bladeFlag;
  public boolean isReturnToWin = false;
  public FloatToolbar floatToolbar = null;
  public ImageFile imageFile = null;
  public JScrollPane scrollPane;
  public JScrollBar vbar = null;
  public JScrollBar hbar = null;
  public int v = 0;
  public int vv = 0;
  public int h = 0;
  public int newv = 0;
  public int newh = 0;
  public int SCROLLPANE_TITLE_HEIGHT = 5;
  public int iImageFocused = 0;
  public int iKeyPressControl = 0;
  public int iKeyPressTotal = 0;
  private String bladeNames;
  MouseMotionAdapter scrollMouseMotion;
  public AdjustmentListener vlistener;
  public AdjustmentListener hlistener;
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
  public String getProductType() {
    return this.productType;
  }
  public void setProductType(String productType) {
    this.productType = productType;
  }
  public KVMInterface(KVMUtil kvmUtil, Client client, PackData packData, ClientSocketCommunity clientSocket, Base base)
  {
    this.scrollMouseMotion = new MouseMotionAdapter()
      {
        public void mouseMoved(MouseEvent e)
        {
          if (null != KVMInterface.this.floatToolbar) {
            KVMInterface.this.floatToolbar.imagePanel.setCursor(KVMInterface.this.base.defCursor);
            KVMInterface.this.floatToolbar.imagePanel.kvmInterface.setCursor(KVMInterface.this.base.defCursor);
          } 
        }
      };
    this.vlistener = new AdjustmentListener()
      {
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
          if (null == KVMInterface.this.floatToolbar) {
            return;
          }
          KVMInterface.this.v = e.getValue();
          if (KVMInterface.this.newv != KVMInterface.this.v && KVMInterface.this.v > KVMInterface.this.SCROLLPANE_TITLE_HEIGHT) {
            KVMInterface.this.vv = KVMInterface.this.v - KVMInterface.this.SCROLLPANE_TITLE_HEIGHT;
            if (KVMInterface.this.floatToolbar.imgwidth >= Toolkit.getDefaultToolkit().getScreenSize().getWidth())
            {
              KVMInterface.this.floatToolbar.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.vv - 1);
              if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                KVMInterface.this.floatToolbar.setCDLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getFlpWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
                KVMInterface.this.floatToolbar.setFlpLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getCDWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              } 
              if (KVMInterface.this.getBladeSize() == 1)
              {
                KVMInterface.this.imageFile.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.imageFile.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              }
            }
            else
            {
              KVMInterface.this.floatToolbar.setLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.vv - 1);
              if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                KVMInterface.this.floatToolbar.setCDLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getFlpWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
                KVMInterface.this.floatToolbar.setFlpLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getCDWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              } 
              if (KVMInterface.this.getBladeSize() == 1)
              {
                KVMInterface.this.imageFile.setLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.imageFile.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              }
            }
          }
          else if (KVMInterface.this.newv != KVMInterface.this.v && KVMInterface.this.v <= KVMInterface.this.SCROLLPANE_TITLE_HEIGHT) {
            if (KVMInterface.this.floatToolbar.imgwidth >= Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
              KVMInterface.this.floatToolbar.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getWidth()) / 2 + KVMInterface.this.h, -1);
              if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                KVMInterface.this.floatToolbar.setFlpLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getFlpWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() - 1);
                KVMInterface.this.floatToolbar.setCDLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getCDWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() - 1);
              } 
              if (KVMInterface.this.getBladeSize() == 1)
              {
                KVMInterface.this.imageFile.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.imageFile.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() - 1);
              }
            }
            else {
              KVMInterface.this.floatToolbar.setLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getWidth()) / 2 + KVMInterface.this.h, -1);
              if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                KVMInterface.this.floatToolbar.setCDLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getCDWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() - 1);
                KVMInterface.this.floatToolbar.setFlpLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getFlpWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() - 1);
              } 
              if (KVMInterface.this.getBladeSize() == 1)
              {
                KVMInterface.this.imageFile.setLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.imageFile.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() - 1);
              }
            } 
            KVMInterface.this.vv = 0;
          } 
          KVMInterface.this.newv = KVMInterface.this.v;
          if (null != KVMInterface.this.floatToolbar && null != KVMInterface.this.floatToolbar.powerMenu)
          {
            KVMInterface.this.floatToolbar.powerMenu.setVisible(false);
          }
        }
      };
    this.hlistener = new AdjustmentListener()
      {
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
          if (null == KVMInterface.this.floatToolbar) {
            return;
          }
          KVMInterface.this.h = e.getValue();
          if (KVMInterface.this.newh != KVMInterface.this.h)
          {
            if (KVMInterface.this.floatToolbar.imgwidth >= Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
              KVMInterface.this.floatToolbar.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.vv - 1);
              if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                KVMInterface.this.floatToolbar.setFlpLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getFlpWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
                KVMInterface.this.floatToolbar.setCDLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.floatToolbar.getCDWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              } 
              if (KVMInterface.this.getBladeSize() == 1)
              {
                KVMInterface.this.imageFile.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - KVMInterface.this.imageFile.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              }
            }
            else {
              KVMInterface.this.floatToolbar.setLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.vv - 1);
              if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                KVMInterface.this.floatToolbar.setFlpLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getFlpWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
                KVMInterface.this.floatToolbar.setCDLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.floatToolbar.getCDWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              } 
              if (KVMInterface.this.getBladeSize() == 1)
              {
                KVMInterface.this.imageFile.setLocation((KVMInterface.this.floatToolbar.imgwidth - KVMInterface.this.imageFile.getWidth()) / 2 + KVMInterface.this.h, KVMInterface.this.floatToolbar.getHeight() + KVMInterface.this.vv - 1);
              }
            } 
          }
          KVMInterface.this.newh = KVMInterface.this.h;
          if (null != KVMInterface.this.floatToolbar && null != KVMInterface.this.floatToolbar.powerMenu)
          {
            KVMInterface.this.floatToolbar.powerMenu.setVisible(false); }  } }; this.bladeSize = Base.bladeSize; this.bladeList = Base.bladeList; this.client = client; this.kvmUtil = kvmUtil; this.clientSocket = clientSocket; this.packData = packData; this.base = base; parseTypeDataInitBlade(Base.typeData); String tip = kvmUtil.getString("return_window"); this.statusBar = new StatusBar(tip); this.statusBar.setClient(client); initToolBarPanel(); setLayout(new BorderLayout()); add(this.toolBarPanel, "North"); this.tabbedpane = new JTabbedPane(); this.tabbedpane.setFocusable(false); Dimension dim = new Dimension(640, 480); this.tabbedpane.setPreferredSize(dim); if (getBladeSize() == 1)
      this.tabbedpane.setUI(new PaintTabBorder()); 
    this.scrollPane = new JScrollPane(this.tabbedpane);
    add(this.scrollPane, "Center");
    add(this.statusBar, "South");
    this.tabbedpane.getModel().addChangeListener(this.kvmUtil.changeListener);
    addscrollBarListener();
    this.scrollPane.addMouseMotionListener(this.scrollMouseMotion);
    this.imageFile = new ImageFile(Base.local);
    if (getBladeSize() == 1) {
      this.SCROLLPANE_TITLE_HEIGHT = 5;
    } else {
      this.SCROLLPANE_TITLE_HEIGHT = 26;
    }  } private void initToolBarPanel() { this.toolBarPanel = new ToolBarPanel();
    this.toolBarPanel.setLayout(new BorderLayout());
    this.toolbar = new KvmToolBar();
    this.toolbar.setFloatable(false);
    if (KVMUtil.isLinuxOS() && getBladeSize() > 1) {
      this.toolbar.setPreferredSize(new Dimension(700, 50));
      this.toolbar.setLayout((LayoutManager)null);
    } 
    this.toolbar.createDisConnectBladeButton();
    this.toolbar.createComKeyButton();
    this.toolbar.createFullButton();
    this.toolbar.createDivButton();
    this.toolbar.createRefreshButton();
    this.toolbar.creatBladeButton();
    this.toolbar.createNumColorButton();
    this.toolbar.createCapsColorButton();
    this.toolbar.createScrollColorButton();
    this.toolbar.createMenuButton();
    if (KVMUtil.isLinuxOS() && getBladeSize() > 1)
    {
      this.toolbar.imageButton.setEnabled(false);
    }
    this.toolbar.createHelpButton();
    this.toolBarPanel.add(this.toolbar, "Center"); }
  public void addscrollBarListener() {
    this.vbar = this.scrollPane.getVerticalScrollBar();
    this.vbar.addAdjustmentListener(this.vlistener);
    this.hbar = this.scrollPane.getHorizontalScrollBar();
    this.hbar.addAdjustmentListener(this.hlistener);
  }
  private void setLinuxSize(Object objName, int x, int y, int width, int heigh) {
    Component cp = null;
    if (objName != null && objName instanceof Component) {
      cp = (Component)objName;
    } else {
      return;
    } 
    cp.setBounds(x, y, width, heigh);
  }
  public JFrame createFrame() {
    JFrame frame = new JFrame();
    frame.addWindowListener(new WindowAdapter()
        {
          public void windowClosing(WindowEvent e)
          {
            System.exit(0);
          }
        });
    return frame;
  }
  class ToolBarPanel
    extends JPanel
    implements ContainerListener
  {
    private static final long serialVersionUID = 1L;
    public boolean contains(int x, int y) {
      Component c = getParent();
      if (c != null) {
        Rectangle r = c.getBounds();
        return (x >= 0 && x < r.width && y >= 0 && y < r.height);
      } 
      return super.contains(x, y);
    }
    public void componentAdded(ContainerEvent e) {
      Container c = e.getContainer().getParent();
      if (c != null) {
        c.getParent().validate();
        c.getParent().repaint();
      } 
    }
    public void componentRemoved(ContainerEvent e) {
      Container c = e.getContainer().getParent();
      if (c != null) {
        c.getParent().validate();
        c.getParent().repaint();
      } 
    }
  }
  class KvmToolBar
    extends JToolBar
    implements ActionListener
  {
    private static final long serialVersionUID = 1L;
    public JButton disConnectBladeButton;
    public JButton mouseSynButton;
    public JButton combineKey;
    public JButton setColorBit;
    public JButton fullButton;
    public JButton divButton;
    public BackColorButton numColorButton;
    public BackColorButton capsColorButton;
    public BackColorButton scrollColorButton;
    public JButton refreshButton;
    public JTextField text = new JTextField();
    public boolean dynamicBlade = true;
    public JButton imageButton;
    public boolean isShowingOperationJDialog = false;
    public JFrame frmFr;
    public JButton helpButton;
    public JFrame helpFrm;
    public HelpDocument help;
    public Color NumColor = Base.LIGHT_OFF;
    public Color CapsColor = Base.LIGHT_OFF;
    public Color ScrollColor = Base.LIGHT_OFF;
    public KvmToolBar(int bladeSize) {
      createBladeButton();
    }
    void createBladeButton() {
      String imagesRes = "resource/images/blade1.gif";
      for (int i = 0; i < KVMInterface.this.bladeSize; i++) {
        imagesRes = "resource/images/blade" + (i + 1) + ".gif";
        JButton blade = new JButton(new ImageIcon(getClass().getResource(imagesRes.trim())));
        ((InterfaceContainer)KVMInterface.this.bladeList.get(i)).setBladeButton(blade);
      } 
    }
    JButton createButton(String resCode, String imageURL) {
      JButton button = new JButton(new ImageIcon(getClass().getResource(imageURL)));
      button.setToolTipText(KVMInterface.this.kvmUtil.getString(resCode));
      return button;
    }
    void createDisConnectBladeButton() {
      this.disConnectBladeButton = createButton("DisconnectBlade.Tip", "resource/images/disconnectblade.gif");
      this.disConnectBladeButton.addActionListener(KVMInterface.this.disConnBladeAction());
      add(this.disConnectBladeButton);
      if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
      {
        KVMInterface.this.setLinuxSize(this.disConnectBladeButton, 5, 5, 30, 35);
      }
      if (KVMInterface.this.bladeSize == 1)
      {
        this.disConnectBladeButton.setVisible(false);
      }
    }
    void createMouseSynButton() {
      this.mouseSynButton = createButton("MouseSyn.Tip", "resource/images/mousesyn.gif");
      this.mouseSynButton.addActionListener(KVMInterface.this.synMouseAction());
      add(this.mouseSynButton);
      if (KVMInterface.this.bladeSize == 1)
      {
        this.mouseSynButton.setVisible(false);
      }
    }
    void createComKeyButton() {
      this.combineKey = createButton("keycombination.Tip", "resource/images/combinekey.gif");
      this.combineKey.addActionListener(KVMInterface.this.shortCutAction());
      add(this.combineKey);
      if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
      {
        KVMInterface.this.setLinuxSize(this.combineKey, 38, 5, 30, 35);
      }
    }
    void createColorBitButton() {
      this.setColorBit = createButton("setColorBit", "resource/images/color.gif");
      this.setColorBit.addActionListener(KVMInterface.this.colorBitAction());
      add(this.setColorBit);
      if (KVMInterface.this.bladeSize == 1)
      {
        this.setColorBit.setVisible(false);
      }
    }
    void createFullButton() {
      this.fullButton = createButton("Full.Tip", "resource/images/fullscreen.gif");
      this.fullButton.addActionListener(KVMInterface.this.fullScreenAction());
      add(this.fullButton);
      if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
      {
        KVMInterface.this.setLinuxSize(this.fullButton, 71, 5, 30, 35);
      }
    }
    void createDivButton() {
      this.divButton = createButton("DivVid.Tip", "resource/images/divvid.gif");
      this.divButton.addActionListener(KVMInterface.this.divAction());
      add(this.divButton);
      if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
      {
        KVMInterface.this.setLinuxSize(this.divButton, 104, 5, 30, 35);
      }
      if (KVMInterface.this.bladeSize == 1)
      {
        this.divButton.setVisible(false);
      }
    }
    void createRefreshButton() {
      this.refreshButton = createButton("Refresh.Tip", "resource/images/refresh.gif");
      this.refreshButton.addActionListener(KVMInterface.this.refreshAction());
      add(this.refreshButton);
      if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
      {
        KVMInterface.this.setLinuxSize(this.refreshButton, 137, 5, 30, 35);
      }
      if (KVMInterface.this.bladeSize == 1)
      {
        this.refreshButton.setVisible(false);
      }
    }
    void createNumColorButton() {
      this.numColorButton = new BackColorButton(this.NumColor);
      this.numColorButton.setEnabled(false);
      if (KVMInterface.this.getBladeSize() > 1) {
        JLabel labelnum = new JLabel(" num ");
        add(labelnum);
        if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1) {
          KVMInterface.this.setLinuxSize(labelnum, 170 + KVMInterface.this.bladeSize * 31, 15, 37, 20);
          KVMInterface.this.setLinuxSize(this.numColorButton, 207 + KVMInterface.this.bladeSize * 31, 20, 10, 10);
        } 
      } 
      add(this.numColorButton);
    }
    void createCapsColorButton() {
      this.capsColorButton = new BackColorButton(this.CapsColor);
      this.capsColorButton.setEnabled(false);
      if (KVMInterface.this.getBladeSize() > 1) {
        JLabel labelcaps = new JLabel(" caps ");
        add(labelcaps);
        if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1) {
          KVMInterface.this.setLinuxSize(labelcaps, 220 + KVMInterface.this.bladeSize * 31, 15, 37, 20);
          KVMInterface.this.setLinuxSize(this.capsColorButton, 257 + KVMInterface.this.bladeSize * 31, 20, 10, 10);
        } 
      } 
      add(this.capsColorButton);
    }
    void createScrollColorButton() {
      this.scrollColorButton = new BackColorButton(this.ScrollColor);
      this.scrollColorButton.setEnabled(false);
      if (KVMInterface.this.getBladeSize() > 1) {
        JLabel labelscroll = new JLabel(" scroll ");
        add(labelscroll);
        if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1) {
          KVMInterface.this.setLinuxSize(labelscroll, 270 + KVMInterface.this.bladeSize * 31, 15, 43, 20);
          KVMInterface.this.setLinuxSize(this.scrollColorButton, 313 + KVMInterface.this.bladeSize * 31, 20, 10, 10);
        } 
      } 
      add(this.scrollColorButton);
    }
    void creatBladeButton() {
      if (this.dynamicBlade) {
        JButton blade = null;
        for (int i = 0; i < KVMInterface.this.bladeList.size(); i++) {
          blade = ((InterfaceContainer)KVMInterface.this.bladeList.get(i)).getBladeButton();
          blade.setActionCommand("blade" + (i + 1));
          if (KVMInterface.this.bladeSize == 1)
          {
            blade.setVisible(false);
          }
          blade.addActionListener(this);
          blade.addMouseListener(bladeBorderListener());
          add(blade);
          if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
          {
            KVMInterface.this.setLinuxSize(blade, 170 + i * 31, 0, 30, 48);
          }
        } 
      } 
    }
    private MouseAdapter bladeBorderListener() {
      MouseAdapter adapter = new MouseAdapter()
        {
          public void mousePressed(MouseEvent e)
          {
            JButton blade = (JButton)e.getSource();
            if (blade.isEnabled())
            {
              blade.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createLineBorder(Color.WHITE)), BorderFactory.createEmptyBorder(6, 3, 6, 3)));
            }
          }
        };
      return adapter;
    }
    private void createHelpButton() {
      this.helpButton = createButton("help_document", "resource/images/help.gif");
      this.helpButton.addActionListener(createHelpAction());
    }
    private Action createHelpAction() {
      Action action = new AbstractAction()
        {
          private static final long serialVersionUID = 1L;
          public void actionPerformed(ActionEvent e) {
            if (KVMInterface.this.getBladeSize() > 1) {
              KVMInterface.this.toolbar.helpButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
              if (null == KVMInterface.KvmToolBar.this.helpFrm) {
                KVMInterface.KvmToolBar.this.getMMHelpDocument();
              }
              else {
                KVMInterface.KvmToolBar.this.helpFrm.setAlwaysOnTop(true);
                KVMInterface.KvmToolBar.this.helpFrm.setAlwaysOnTop(false);
              } 
            } 
          }
        };
      return action;
    }
    public JFrame getMMHelpDocument() {
      String path = "";
      if (KVMInterface.this.getProductType().equals("OSCA")) {
        if (Base.local.equalsIgnoreCase("en"))
        {
          path = "resource/helpdoc/en/help/bmc_help_0008_OSCA.html";
        }
        else
        {
          path = "resource/helpdoc/cn/help/bmc_help_0008_OSCA.html";
        }
      }
      else if (Base.local.equalsIgnoreCase("en")) {
        path = "resource/helpdoc/en/help/bmc_help_0008.html";
      }
      else {
        path = "resource/helpdoc/cn/help/bmc_help_0008.html";
      } 
      if (null == this.help)
      {
        this.help = new HelpDocument(path);
      }
      this.helpFrm = new JFrame();
      this.helpFrm.addWindowListener(new WindowAdapter()
          {
            public void windowClosing(WindowEvent arg0)
            {
              super.windowClosing(arg0);
              KVMInterface.KvmToolBar.this.helpFrm = null;
            }
          });
      this.helpFrm.setSize(800, 650);
      int x = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.helpFrm.getWidth()) / 2;
      int y = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.helpFrm.getHeight()) / 2;
      this.helpFrm.setLocation(x, y);
      this.helpFrm.setLayout(new BorderLayout());
      this.helpFrm.getContentPane().add(this.help.getScroller());
      this.helpFrm.setVisible(true);
      return this.helpFrm;
    }
    private void createMenuButton() {
      this.imageButton = createButton("create_image", "resource/images/virtualne.gif");
      this.imageButton.addActionListener(createMenuAction());
      if (KVMInterface.this.getBladeSize() > 1)
      {
        add(this.imageButton);
      }
      if (KVMUtil.isLinuxOS() && KVMInterface.this.getBladeSize() > 1)
      {
        KVMInterface.this.setLinuxSize(this.imageButton, 326 + KVMInterface.this.bladeSize * 31, 5, 35, 35);
      }
    }
    private JFrame createFrFrame() {
      this.frmFr = new JFrame(KVMInterface.this.kvmUtil.getString("create_image"));
      this.frmFr.setDefaultCloseOperation(0);
      this.frmFr.setContentPane(KVMInterface.this.imageFile);
      this.frmFr.setSize(337, 79);
      KVMInterface.this.imageFile.setVisible(true);
      this.frmFr.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.frmFr.getWidth()) / 2, (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.frmFr.getHeight()) / 2);
      this.frmFr.addWindowListener(new WindowAdapter()
          {
            public void windowClosing(WindowEvent e)
            {
              if (!KVMInterface.this.imageFile.isImageCreate) {
                int result = JOptionPane.showConfirmDialog(null, KVMInterface.this.kvmUtil.getString("createFrame_message"), UIManager.getString("OptionPane.titleText"), 0);
                if (0 == result)
                {
                  KVMInterface.this.imageFile.doStopImageCreate();
                  KVMInterface.KvmToolBar.this.frmFr.dispose();
                  KVMInterface.KvmToolBar.this.frmFr = null;
                }
              } else {
                KVMInterface.KvmToolBar.this.frmFr.dispose();
                KVMInterface.KvmToolBar.this.frmFr = null;
              } 
            }
          });
      this.frmFr.setResizable(false);
      this.frmFr.setVisible(true);
      return this.frmFr;
    }
    private Action createMenuAction() {
      Action action = new AbstractAction()
        {
          private static final long serialVersionUID = 1L;
          public void actionPerformed(ActionEvent e) {
            if (KVMInterface.this.getBladeSize() > 1)
            {
              KVMInterface.this.toolbar.imageButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            }
            if (KVMInterface.this.getBladeSize() == 1) {
              if (KVMInterface.this.imageFile.isShowing())
              {
                KVMInterface.this.floatToolbar.isShowingImagep = false;
                KVMInterface.this.imageFile.setVisible(false);
              }
              else
              {
                if (KVMInterface.this.floatToolbar.isVirtualMedia()) {
                  if (KVMInterface.this.floatToolbar.isShowingCD) {
                    KVMInterface.this.floatToolbar.isShowingCD = false;
                    KVMInterface.this.floatToolbar.setCDVisible(false);
                  } 
                  if (KVMInterface.this.floatToolbar.isShowingFlp) {
                    KVMInterface.this.floatToolbar.isShowingFlp = false;
                    KVMInterface.this.floatToolbar.setFlpVisible(false);
                  } 
                } 
                KVMInterface.this.floatToolbar.isShowingImagep = true;
                KVMInterface.this.imageFile.setVisible(true);
              }
            }
            else if (null == KVMInterface.KvmToolBar.this.frmFr) {
              KVMInterface.KvmToolBar.this.createFrFrame();
            }
            else {
              KVMInterface.KvmToolBar.this.frmFr.toFront();
            } 
          }
        };
      return action;
    }
    public void actionPerformed(ActionEvent e) {
      Debug.println(" actionPerformed...");
      KVMInterface.this.clickFlag = true;
      KVMInterface.this.tabbedpane.getModel().removeChangeListener(KVMInterface.this.kvmUtil.changeListener);
      BladeState bladeState = null;
      try {
        if (this.dynamicBlade)
        {
          for (int i = 0; i < KVMInterface.this.bladeList.size(); i++)
          {
            JButton blade = ((InterfaceContainer)KVMInterface.this.bladeList.get(i)).getBladeButton();
            if (blade != null)
            {
              if (e.getActionCommand().equals(blade.getActionCommand()))
              {
                int bladeNO = i + 1;
                Debug.println(" blade1..." + bladeNO);
                LoggerUtil.info( "blade1: "+ bladeNO );
                if (bladeNO == 1 && KVMInterface.this.bladeList.size() == 1) {
                  bladeState = KVMInterface.this.kvmUtil.getBladStateBmc(bladeNO);
                }
                else {
                  bladeState = KVMInterface.this.kvmUtil.getBladeState(bladeNO);
                }
                LoggerUtil.info( "bladeState: "+ bladeState );
                if (bladeState.isEnable())
                {
                  LoggerUtil.info( "bladeip: "+ bladeState.getBladeIP()+",bladeport:"+  bladeState.getBladePort()+",getSecureKvm:"+bladeState.getSecureKvm());	
                  KVMInterface.this.kvmUtil.connectNewBlade(bladeNO, true, bladeState.getBladeIP(), bladeState.getBladePort(), bladeState.isNew(), bladeState.getSecureKvm());
                }
              }
            }
          }
        }
      }
      catch (KVMException ex) {
        KVMInterface.this.clickFlag = false;
        if ("IO_ERRCODE".equals(ex.getErrCode()))
        {
          JOptionPane.showMessageDialog(KVMInterface.this.toolbar, KVMInterface.this.kvmUtil.getString("Network_interrupt_message"));
        }
      }
      catch (Exception ex1) {
        ex1.printStackTrace();
        KVMInterface.this.clickFlag = false;
        return;
      } 
      KVMInterface.this.tabbedpane.getModel().addChangeListener(KVMInterface.this.kvmUtil.changeListener);
    }
    public void releaseKVMToolBar() {
      if (this.dynamicBlade) {
        if (KVMInterface.this.bladeList == null) {
          return;
        }
        JButton blade = null;
        Iterator<InterfaceContainer> iter = KVMInterface.this.bladeList.iterator();
        while (iter.hasNext()) {
          blade = ((InterfaceContainer)iter.next()).getBladeButton();
          if (blade == null) {
            continue;
          }
          blade.removeActionListener(this);
          blade.removeMouseListener(bladeBorderListener());
        } 
        KVMInterface.this.bladeList.clear();
        KVMInterface.this.bladeList = null;
      } 
      this.disConnectBladeButton.removeActionListener(KVMInterface.this.disConnBladeAction());
      if (this.mouseSynButton != null)
        this.mouseSynButton.removeActionListener(KVMInterface.this.synMouseAction()); 
      this.divButton.removeActionListener(KVMInterface.this.divAction());
      this.fullButton.removeActionListener(KVMInterface.this.fullScreenAction());
      this.refreshButton.removeActionListener(KVMInterface.this.refreshAction());
      this.combineKey.removeActionListener(KVMInterface.this.shortCutAction());
      if (this.setColorBit != null)
      {
        this.setColorBit.removeActionListener(KVMInterface.this.colorBitAction());
      }
      this.numColorButton = null;
      this.capsColorButton = null;
      this.scrollColorButton = null;
      this.disConnectBladeButton = null;
      this.divButton = null;
      this.fullButton = null;
      this.refreshButton = null;
      this.combineKey = null;
      this.setColorBit = null;
    }
    public KvmToolBar() {}
  }
  public Action divAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          KVMInterface.this.tabbedpane.getModel().removeChangeListener(KVMInterface.this.kvmUtil.changeListener);
          KVMInterface.this.produceDivScreen();
          KVMInterface.this.tabbedpane.getModel().addChangeListener(KVMInterface.this.kvmUtil.changeListener);
          if (KVMInterface.this.getBladeSize() > 1)
          {
            KVMInterface.this.toolbar.divButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
          }
        }
      };
    return action;
  }
  private void produceDivScreen() {
    if (!this.isFullScreen) {
      this.tabbedpane.removeAll();
      if (this.fullScreen == null) {
        JDialog tem = (new JOptionPane()).createDialog(this, "");
        this.fullScreen = new FullScreen(tem, this);
        this.kvmUtil.setFullToolBar(this.base.isDiv);
        this.fullScreen.setVisible(true);
        this.fullScreen.toolBar.startButtonState();
      }
      else {
        this.fullScreen.getContentPane().removeAll();
        this.fullScreen.getContentPane().add(this.fullScreen.imageParentPane);
        this.fullScreen.imageParentPane.setLayout(new GridLayout(4, 4, 1, 1));
        this.fullScreen.getClass(); 
        //FullScreen.MouseHandler listener = new FullScreen.MouseHandler(this.fullScreen);
        for (int i = 1; i <= 16; i++) {
          ImagePane imaPane = this.kvmUtil.getImagePane(i);
          if (imaPane != null) {
            imaPane.setToolTipText("blade" + imaPane.bladeNO);
            imaPane.setCursor(new Cursor(0));
            this.fullScreen.imageParentPane.add(imaPane);
            if (i != this.actionBlade) {
              BladeThread bladeThread = this.base.threadGroup.get(String.valueOf(imaPane.bladeNO));
              DrawThread drawThread = bladeThread.getDrawThread();
              drawThread.setFirstJudge(true);
              drawThread.getComImage().clear();
              drawThread.getKvmUtil().resetBuf();
              (drawThread.getKvmUtil()).firstJudge = true;
              bladeThread.bladeCommu.sentData(this.packData.connectBlade(imaPane.bladeNO, (this.kvmUtil.getImagePane(imaPane.bladeNO)).custBit));
              bladeThread.bladeCommu.sentData(this.packData.contrRate(1, bladeThread.getBladeNO()));
            }
            else {
              BladeThread bladeThread = this.base.threadGroup.get(String.valueOf(this.actionBlade));
              bladeThread.bladeCommu.sentData(this.packData.contrRate(1, bladeThread.getBladeNO()));
            } 
            imaPane.divScreenIni(imaPane);
            imaPane.kvmInterface.floatToolbar.setVisible(false);
            if (imaPane.kvmInterface.floatToolbar.isVirtualMedia())
            {
              imaPane.kvmInterface.floatToolbar.setVirtualMediaVisible(false, false);
            }
          }
          else {
            JPanel panel = new JPanel();
            panel.setBackground(Color.black);
            panel.setToolTipText(this.kvmUtil.getString("NoSignal.Tip"));
            //panel.addMouseListener(listener);
            this.fullScreen.imageParentPane.add(panel);
          } 
        } 
        this.base.isDiv = true;
        this.fullScreen.toolBar.produceComboBox();
        this.kvmUtil.setFullToolBar(this.base.isDiv);
        this.fullScreen.setVisible(true);
        this.fullScreen.toolBar.startButtonState();
      } 
      this.kvmUtil.setDrawDisplay(true);
      this.isFullScreen = true;
      this.fullScreen.setButtonEnable();
      createToolFrame();
    } 
  }
  public void createToolFrame() {
    if (this.toolFrame == null) {
      this.toolFrame = new JFrame();
      this.toolFrame.setUndecorated(true);
      this.toolFrame.getContentPane().setBackground(new Color(158, 202, 232));
      this.toolFrame.setSize(this.fullScreen.toolBarFrame.getSize());
      this.toolFrame.setLayout(new BorderLayout());
      if (KVMUtil.isWindowsOS()) {
        this.toolFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.fullScreen.toolBarFrame.getWidth()) / 2.0D) + 1, 1);
      }
      else if (KVMUtil.isLinuxOS()) {
        this.toolFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.fullScreen.toolBarFrame.getWidth()) / 2.0D) + 1, 21);
      } 
    } 
    this.toolFrame.getContentPane().removeAll();
    this.toolFrame.getContentPane().add(this.fullScreen.toolBar, "Center");
    this.toolFrame.setVisible(false);
  }
  public Action shortCutAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (KVMInterface.this.kvmUtil.getImagePane(KVMInterface.this.actionBlade).isContr()) {
            KVMInterface.this.produceComKey();
          }
          else {
            JOptionPane.showMessageDialog(KVMInterface.this.toolbar, KVMInterface.this.kvmUtil.getString("ListenOperation"));
            return;
          } 
          if (KVMInterface.this.getBladeSize() > 1)
          {
            KVMInterface.this.toolbar.combineKey.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
          }
        }
      };
    return action;
  }
  public Action colorBitAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (KVMInterface.this.kvmUtil.getImagePane(KVMInterface.this.actionBlade).isContr()) {
            KVMInterface.this.colorBit();
          }
          else {
            JOptionPane.showMessageDialog(KVMInterface.this.toolbar, KVMInterface.this.kvmUtil.getString("ListenOperation"));
          } 
        }
      };
    return action;
  }
  private void colorBit() {
    JDialog tem = (new JOptionPane()).createDialog(this, "");
    new ColorBit(tem, this.actionBlade, this);
  }
  private void produceComKey() {
    JDialog tem = (new JOptionPane()).createDialog(this, "");
    new CombinationKey(tem, this.actionBlade, (this.kvmUtil.getImagePane(this.actionBlade)).pack, this);
  }
  public Action fullScreenAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          KVMInterface.this.tabbedpane.getModel().removeChangeListener(KVMInterface.this.kvmUtil.changeListener);
          KVMInterface.this.produceFullScreen();
          KVMInterface.this.tabbedpane.getModel().addChangeListener(KVMInterface.this.kvmUtil.changeListener);
          if (KVMInterface.this.getBladeSize() > 1)
          {
            KVMInterface.this.toolbar.fullButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
          }
        }
      };
    return action;
  }
  private void produceFullScreen() {
    if (!this.isFullScreen) {
      ImagePane imagePane = this.kvmUtil.getImagePane(this.actionBlade);
      this.tabbedpane.removeAll();
      if (getBladeSize() == 1) {
        this.imageFile.setVisible(false);
        this.floatToolbar.isShowingImagep = false;
      } 
      this.floatToolbar.fullStateMenu();
      if (this.fullScreen == null) {
        JDialog tem = (new JOptionPane()).createDialog(this, "");
        tem.setModal(true);
        this.fullScreen = new FullScreen(tem, imagePane, this);
        this.kvmUtil.setFullToolBar(false);
        if (imagePane.isNew())
        {
          if (this.base.isMstsc) {
            this.fullScreen.toolBar.mouseSynButton.setEnabled(true);
          }
          else {
            this.fullScreen.toolBar.mouseSynButton.setEnabled(false);
          } 
        }
        this.fullScreen.toolBar.startButtonState();
      }
      else {
        this.fullScreen.getContentPane().removeAll();
        this.fullScreen.imageParentPane.setLayout(new BorderLayout());
        this.fullScreen.imageParentScrollPane = null;
        this.fullScreen.imageParentPane.setPreferredSize(new Dimension(imagePane.width, imagePane.height));
        this.fullScreen.imageParentPane.add(imagePane);
        this.fullScreen.imageParentScrollPane = new JScrollPane(this.fullScreen.imageParentPane);
        this.fullScreen.addscrollBarListener();
        this.fullScreen.getContentPane().add(this.fullScreen.imageParentScrollPane, "Center");
        this.fullScreen.imagePane = imagePane;
        this.fullScreen.actionBlade = this.actionBlade;
        this.fullScreen.toolBar.produceComboBox();
        this.kvmUtil.setFullToolBar(this.base.isDiv);
        if (imagePane.isNew()) {
          if (this.base.isMstsc)
          {
            this.fullScreen.setCursor(this.base.defCursor);
            imagePane.setCursor(this.base.defCursor);
            this.fullScreen.toolBar.mouseSynButton.setToolTipText(this.kvmUtil.getString("MouseSyn.Tip"));
          }
          else
          {
            this.fullScreen.setCursor(this.base.myCursor);
            imagePane.setCursor(this.base.myCursor);
            this.fullScreen.toolBar.mouseSynButton.setEnabled(false);
            MouseDisplacementImpl.setMode(1);
          }
        } else {
          this.fullScreen.setCursor(this.base.defCursor);
          imagePane.setCursor(this.base.defCursor);
          this.fullScreen.toolBar.mouseSynButton.setToolTipText(this.kvmUtil.getString("MouseSyn.Tip"));
        } 
        MouseDisplacementImpl.setKeyBoardStatus((imagePane.getNum() == 1), (byte)-112);
        MouseDisplacementImpl.setKeyBoardStatus((imagePane.getCaps() == 1), (byte)20);
        MouseDisplacementImpl.setKeyBoardStatus((imagePane.getScroll() == 1), (byte)-111);
        this.fullScreen.setVisible(true);
        this.fullScreen.addscrollBarListener();
        this.fullScreen.toolBar.startButtonState();
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
        this.fullScreen.toolBarFrame.removeAll();
        this.fullScreen.toolBarFrame.add(this.fullScreen.toolBar);
      } 
      imagePane.add(this.fullScreen.toolBarFrame);
      this.fullScreen.toolBarFrame.setVisible(false);
      if (KVMUtil.isLinuxOS())
      {
        this.fullScreen.powerPanelDialog.setMouseSelected();
      }
    } 
  }
  public Action refreshAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          int count = 0;
          byte[] tembladePresentInfo = new byte[2];
          KVMInterface.this.clientSocket.bladePresentInfo.clear();
          try {
            KVMInterface.this.client.sentData(KVMInterface.this.packData.reqBladePresent());
          }
          catch (KVMException ex) {
            if ("IO_ERRCODE".equals(ex.getErrCode()))
            {
              JOptionPane.showMessageDialog(KVMInterface.this.toolbar, KVMInterface.this.kvmUtil.getString("Network_interrupt_message"));
            }
          } 
          while (count < 1800) {
            if (KVMInterface.this.clientSocket.bladePresentInfo.size() != 0) {
              tembladePresentInfo = KVMInterface.this.clientSocket.bladePresentInfo.remove(KVMInterface.this.clientSocket.bladePreIndex);
              // Byte code: goto -> 356
            } 
            try {
              Thread.sleep(20L);
              count++;
            }
            catch (InterruptedException e1) {
              Debug.printExc(e1.getMessage());
            } 
          } 
          JOptionPane.showMessageDialog(KVMInterface.this.toolbar, KVMInterface.this.kvmUtil.getString("Network_interrupt_message"));
          ArrayList<String> keyList = new ArrayList<String>();
          Iterator<String> iter = KVMInterface.this.base.threadGroup.keySet().iterator();
          KVMInterface.this.tabbedpane.getModel().removeChangeListener(KVMInterface.this.kvmUtil.changeListener);
          while (iter.hasNext())
          {
            keyList.add(iter.next());
          }
          int num = KVMInterface.this.base.threadGroup.size();
          for (int i = 0; i < num; i++) {
            int bladeNO = Integer.parseInt(keyList.get(i));
            KVMInterface.this.kvmUtil.disconnectBlade(bladeNO);
          } 
          keyList.clear();
          if (null != KVMInterface.this.toolbar.refreshButton)
          {
            KVMInterface.this.toolbar.refreshButton.setEnabled(false);
          }
          if (!Arrays.equals(tembladePresentInfo, KVMInterface.this.kvmUtil.bladePreInfo)) {
            KVMInterface.this.kvmUtil.bladePreInfo = tembladePresentInfo;
            KVMInterface.this.kvmUtil.setBladeEnable();
          } 
          if (KVMInterface.this.getBladeSize() > 1)
          {
            KVMInterface.this.toolbar.refreshButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
          }
        }
      };
    return action;
  }
  public Action disConnBladeAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (!KVMInterface.this.isFullScreen) {
            KVMInterface.this.tabbedpane.getModel().removeChangeListener(KVMInterface.this.kvmUtil.changeListener);
            BladeThread bladeThread = KVMInterface.this.base.threadGroup.get(String.valueOf(KVMInterface.this.actionBlade));
            bladeThread.bladeCommu.setAutoFlag(false);
            KVMInterface.this.kvmUtil.disconnectBlade(KVMInterface.this.actionBlade);
            KVMInterface.this.tabbedpane.getModel().addChangeListener(KVMInterface.this.kvmUtil.changeListener);
            if (KVMInterface.this.getBladeSize() > 1)
            {
              KVMInterface.this.toolbar.disConnectBladeButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            }
          } 
        }
      };
    return action;
  }
  public Action synMouseAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (KVMInterface.this.kvmUtil.getImagePane(KVMInterface.this.actionBlade).isContr()) {
            BladeThread bladeThread = KVMInterface.this.base.threadGroup.get(String.valueOf(KVMInterface.this.actionBlade));
            if (bladeThread.isNew()) {
              if (KVMInterface.this.base.isMstsc)
              {
                for (int i = 0; i < 15; i++)
                {
                  bladeThread.bladeCommu.sentData((KVMInterface.this.kvmUtil.getImagePane(KVMInterface.this.actionBlade)).pack.mousePackNew((byte)-127, (byte)-127, KVMInterface.this.actionBlade));
                }
                (bladeThread.getDrawThread()).imagePane.remotemstscX = 0;
                (bladeThread.getDrawThread()).imagePane.remotemstscY = 0;
              }
            } else {
              bladeThread.bladeCommu.sentData((KVMInterface.this.kvmUtil.getImagePane(KVMInterface.this.actionBlade)).pack.mousePack(65535, 65535, KVMInterface.this.actionBlade));
            } 
            KVMInterface.this.kvmUtil.getImagePane(KVMInterface.this.actionBlade).requestFocus();
          }
          else {
            JOptionPane.showMessageDialog(KVMInterface.this.toolbar, KVMInterface.this.kvmUtil.getString("ListenOperation"));
          } 
        }
      };
    return action;
  }
  public void setFullScreen(FullScreen fullScreen) {
    this.fullScreen = fullScreen;
  }
  public int getActionBlade() {
    return this.actionBlade;
  }
  public void setActionBlade(int actionBlade) {
    this.actionBlade = actionBlade;
  }
  public void setBladeTip(String[] bladeNames) {
    if (bladeNames != null)
    {
      if (this.toolbar.dynamicBlade) {
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
  public static void main(String[] args) {
    KVMUtil kvmUtil = new KVMUtil();
    Client client = new Client();
    PackData packData = new PackData();
    UnPackData unPackData = new UnPackData();
    Base base = new Base();
    kvmUtil.resourcePath = "com.kvm.resource.KVMResource";
    ClientSocketCommunity clientSocket = new ClientSocketCommunity();
    KVMInterface kvmInterface = new KVMInterface(kvmUtil, client, packData, clientSocket, base);
    client.setKvmInterface(kvmInterface);
    clientSocket.setKvmInterface(kvmInterface);
    clientSocket.setUnPackData(unPackData);
    packData.setKvmInterface(kvmInterface);
    kvmUtil.setKvmInterface(kvmInterface);
    kvmUtil.setUnPack(unPackData);
    kvmUtil.setImageData(new byte[base.imageWidth * base.imageHeight]);
    JFrame jframe = new JFrame("mainframe");
    jframe.setContentPane(kvmInterface);
    jframe.pack();
    jframe.setVisible(true);
  }
  public int getBladeSize() {
    return this.bladeSize;
  }
  public void setBladeSize(int bladeSize) {
    this.bladeSize = bladeSize;
  }
  class PaintTabBorder
    extends BasicTabbedPaneUI
  {
    protected int calculateTabHeight(int arg0, int arg1, int arg2) {
      return 0;
    }
    protected int calculateTabWidth(int arg0, int arg1, FontMetrics arg2) {
      return 0;
    }
  }
  public JButton createBladeButton(int bladeNO) {
    String imagesRes = "resource/images/blade1.gif";
    imagesRes = "resource/images/blade" + bladeNO + ".gif";
    JButton blade = new JButton(new ImageIcon(getClass().getResource(imagesRes.trim())));
    return blade;
  }
  public void parseTypeData(long typeData) {
    int bladeCount = 0;
    int connMode = 0;
    int bladeNO = 0;
    int mask = 1;
    bladeCount = (int)(typeData >> 32L & 0xFFL);
    connMode = (int)(typeData >> 40L & 0xFFL);
    Long[] aa = { Long.valueOf(typeData) };
    Debug.println(" bladeCount :" + bladeCount + " connMode :" + connMode);
    Debug.printf("\n typeData : %x \n", (Object[])aa);
    Base.bladeSize = bladeCount;
    this.bladeSize = bladeCount;
    Base.connMode = connMode;
    int[] blades = new int[bladeCount];
    this.bladeList = new ArrayList<InterfaceContainer>();
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < bladeCount; i++) {
      blades[i] = ((typeData & mask) == mask) ? 1 : 0;
      if (blades[i] == 1) {
        InterfaceContainer iContainer = new InterfaceContainer();
        if (connMode == 0) {
          virtualMedia = new VirtualMedia(Base.local, this.kvmUtil.getKvmInterface());
          virtualMedia.setStrIP(Base.vmmConnIP);
          virtualMedia.setCodeKey(Base.vmmCodeKey);
          virtualMedia.setPort(Base.vmmPort);
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
        iContainer.setBladeNO(bladeNO);
        this.bladeList.add(iContainer);
        this.floatToolbar = floatToolbar;
      } 
      mask *= 2;
    } 
    Base.bladeViews = blades;
    Debug.println("parse type data bladeSize :" + Base.bladeSize + "|" + bladeCount + " connMode :" + Base.connMode + "|" + connMode);
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
    Base.bladeSize = bladeCount;
    this.bladeSize = bladeCount;
    Base.connMode = connMode;
    int[] blades = new int[bladeCount];
    this.bladeList = new ArrayList<InterfaceContainer>();
    for (int i = 0; i < bladeCount; i++) {
      blades[i] = ((typeData & mask) == mask) ? 1 : 0;
      if (blades[i] == 1) {
        InterfaceContainer iContainer = new InterfaceContainer();
        bladeNO = i + 1;
        JButton bladeButton = createBladeButton(bladeNO);
        iContainer.setBladeButton(bladeButton);
        iContainer.setBladeNO(bladeNO);
        iContainer.setBRelPosition(bRelPosition);
        this.bladeList.add(iContainer);
      }
      else {
        bRelPosition++;
      } 
      mask *= 2;
    } 
    Base.bladeViews = blades;
    Debug.println("parse type data bladeSize :" + Base.bladeSize + "|" + bladeCount + " connMode :" + Base.connMode + "|" + connMode);
  }
  public void parseTypeDataCreatePanel() {
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.bladeList.size(); i++) {
      InterfaceContainer iContainer = this.bladeList.get(i);
      if (Base.connMode == 0) {
        virtualMedia = new VirtualMedia(Base.local, this.kvmUtil.getKvmInterface());
        virtualMedia.setStrIP(Base.vmmConnIP);
        virtualMedia.setCodeKey(Base.vmmCodeKey);
        virtualMedia.setPort(Base.vmmPort);
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
      iContainer = this.bladeList.get(i);
      imagePane = iContainer.getImagePane();
      if (iContainer.getBladeNO() == bladeNO)
      {
        if (imagePane == null || (imagePane != null && imagePane.bladeNO == 0)) {
          if (Base.connMode == 0) {
            virtualMedia = new VirtualMedia(Base.local, this.kvmUtil.getKvmInterface());
            virtualMedia.SetBladeNO(bladeNO);
            virtualMedia.setStrIP(Base.vmmConnIP);
            virtualMedia.setCodeKey(Base.vmmCodeKey);
            virtualMedia.setPort(Base.vmmPort);
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
