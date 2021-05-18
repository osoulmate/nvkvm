package com.kvmV1;
import com.kvm.StatReceiveTask;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
public class ImagePane
  extends JPanel
  implements MouseMotionListener, MouseListener, MouseWheelListener, FocusListener
{
  private static final long serialVersionUID = 1L;
  private Border border;
  private boolean isControl = true;
  private BufferedImage bi;
  private boolean actionFlage = false;
  private int posiX = 0;
  private int posiY = 0;
  private byte[] pixels = null;
  private BufferedImage biSrc;
  private BufferedImage biDest;
  private boolean ismove = true;
  private boolean ifmove = false;
  private boolean isNew = false;
  private int num = 0;
  private int caps = 0;
  private int scroll = 0;
  private static final int NONE = 0;
  private static final int LEFT = 16;
  private static final int MIDDLE = 8;
  private static final int RIGHT = 4;
  private static final int ax = 120;
  private static final int ay = 120;
  private int[] arrx = new int[16];
  private int[] arry = new int[16];
  public Image image;
  public PackData pack;
  public Graphics2D big;
  public int remoteX = 0;
  public int remoteY = 0;
  public int remotePreX = 0;
  public int remotePreY = 0;
  public int remotemstscX = 0;
  public int remotemstscY = 0;
  public Timer receiveList = null;
  public Timer focusList = null;
  public int bladeNO = 0;
  public MemoryImageSource source;
  public boolean resolutionCh = false;
  public boolean diff = true;
  public ColorModel cm;
  public AffineTransform transform;
  public int imageReceive = 0;
  public int show = 0;
  public int width = 0;
  public int height = 0;
  public byte colorBit = 0;
  public KVMInterface kvmInterface = null;
  public BladeThread bladeThread = null;
  public  MouseTimerTask mouseTimerTask = null;
  public  StatReceiveTask statReceiveTask = null;
  public KeyHandler keyListener = null;
  public byte custBit = 0;
  public boolean isFocusChange = true;
  public boolean lostEndRow = true;
  private Robot robot;
  public ActionListener taskPerformer = new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        ImagePane.this.ifmove = true;
        if (ImagePane.this.remoteX == 65535 && ImagePane.this.remoteY == 65535) {
          if (!ImagePane.this.isNew)
          {
            ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.mousePack(65535, 65535, ImagePane.this.bladeNO));
          }
        }
        else {
          if (ImagePane.this.actionFlage) {
            ImagePane.this.actionFlage = false;
            return;
          } 
          if (ImagePane.this.ismove) {
            ImagePane.this.ismove = false;
            return;
          } 
          if (ImagePane.this.remotePreX == ImagePane.this.remoteX && ImagePane.this.remotePreY == ImagePane.this.remoteY && ImagePane.this.remoteX != ImagePane.this.posiX - ImagePane.this.getImagePaneX() && ImagePane.this.remoteY != ImagePane.this.posiY - ImagePane.this.getImagePaneY()) {
            byte[] bytes = ImagePane.this.pack.mousePack(ImagePane.this.posiX - ImagePane.this.getImagePaneX(), ImagePane.this.posiY - ImagePane.this.getImagePaneY(), ImagePane.this.bladeNO);
            ImagePane.this.bladeThread.bladeCommu.sentData(bytes);
            ImagePane.this.ifmove = false;
            ImagePane.this.ismove = true;
          } 
        } 
      }
    };
  public ActionListener taskSetFocus = new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        ImagePane.this.setFocus();
      }
    };
  public void quitConn(String messageKey) {
    String messkey = "No_bladeData";
    if (null == messageKey || !messageKey.equals(""))
    {
      messkey = messageKey;
    }
    this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
    if (this.kvmInterface.isFullScreen)
    {
      this.kvmInterface.kvmUtil.returnToWin();
    }
    JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString(messkey));
    this.kvmInterface.kvmUtil.disconnectBlade(this.bladeNO);
    this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
  }
  private void setFocus() {
    if (!isFocusOwner() && !this.kvmInterface.isFullScreen)
    {
      requestFocus();
    }
  }
  public ImagePane(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
    try {
      this.robot = new Robot();
    }
    catch (AWTException e) {
      e.printStackTrace();
    } 
    jbInit();
  }
  public void setbladeNO(int bladeNO) {
    this.bladeNO = bladeNO;
  }
  private void jbInit() {
    setLayout((LayoutManager)null);
    Dimension dim = new Dimension(1280, 1024);
    setPreferredSize(dim);
    setFocusTraversalKeysEnabled(false);
    this.keyListener = new KeyHandler();
    addKeyListener(this.keyListener);
    addMouseMotionListener(this);
    addMouseListener(this);
    addFocusListener(this);
    this.pack = new PackData();
    this.pack.setKvmInterface(this.kvmInterface);
    byte[] reds = new byte[256];
    byte[] greens = new byte[256];
    byte[] blues = new byte[256];
    for (int i = 0; i < 256; i++) {
      reds[i] = (byte)(((i & 0x7) * 255 + 3) / 7);
      greens[i] = (byte)(((i >> 3 & 0x7) * 255 + 3) / 7);
      blues[i] = (byte)(((i >> 6 & 0x3) * 255 + 1) / 3);
    } 
    this.cm = new IndexColorModel(8, 256, reds, greens, blues);
    int size = this.kvmInterface.base.imageWidth * this.kvmInterface.base.imageHeight;
    this.pixels = new byte[size];
    this.source = new MemoryImageSource(this.kvmInterface.base.imageWidth, this.kvmInterface.base.imageHeight, this.cm, this.pixels, 0, this.kvmInterface.base.imageWidth);
    this.image = createImage(this.source);
    this.source.setAnimated(true);
    this.biSrc = new BufferedImage(1280, 1024, 13, (IndexColorModel)this.cm);
    this.big = this.biSrc.createGraphics();
    this.big.drawImage(this.image, 0, 0, this);
    this.bi = this.biSrc;
    this.biDest = new BufferedImage(1280, 1024, 13, (IndexColorModel)this.cm);
    this.transform = new AffineTransform();
  }
  public void applyFilter() {
    AffineTransformOp op = new AffineTransformOp(this.transform, null);
    Graphics2D biDestG2D = this.biDest.createGraphics();
    biDestG2D.clearRect(0, 0, this.biDest.getWidth(this), this.biDest.getHeight(this));
    op.filter(this.biSrc, this.biDest);
    this.bi = this.biDest;
  }
  public void divScreenIni(ImagePane imaPane) {
    imaPane.source.newPixels(0, 0, imaPane.width, imaPane.height);
    imaPane.big.drawImage(imaPane.image, 0, 0, imaPane);
    imaPane.transform.setToScale(Base.getScreenSize().getWidth() / 4.0D / imaPane.width, Base.getScreenSize().getHeight() / 4.0D / imaPane.height);
    imaPane.applyFilter();
  }
  public synchronized void setImage(byte[] pix) {
    if (this.resolutionCh) {
      this.pixels = null;
      this.pixels = new byte[pix.length];
      System.arraycopy(pix, 0, this.pixels, 0, this.pixels.length);
      this.source = new MemoryImageSource(this.width, this.height, this.cm, this.pixels, 0, this.width);
      this.image = createImage(this.source);
      this.source.setAnimated(true);
      this.resolutionCh = false;
      if (this.kvmInterface.base.isDiv)
      {
        this.source.newPixels(0, 0, this.width, this.height);
        this.big.drawImage(this.image, 0, 0, this);
        this.transform.setToScale(Base.getScreenSize().getWidth() / 4.0D / this.width, Base.getScreenSize().getHeight() / 4.0D / this.height);
        applyFilter();
      }
      else
      {
        this.source.newPixels(0, 0, this.width, this.height);
      }
    }
    else {
      if (this.pixels.length == pix.length) {
        System.arraycopy(pix, 0, this.pixels, 0, this.pixels.length);
      } else {
        return;
      } 
      if (this.kvmInterface.base.isDiv) {
        this.source.newPixels(0, 0, this.width, this.height);
        this.big.drawImage(this.image, 0, 0, this);
        this.transform.setToScale(Base.getScreenSize().getWidth() / 4.0D / this.width, Base.getScreenSize().getHeight() / 4.0D / this.height);
        applyFilter();
      }
      else {
        this.source.newPixels(0, 0, this.width, this.height);
      } 
    } 
    repaint();
  }
  public void setBorder(Border border) {
    this.border = border;
  }
  public Image getImage() {
    return this.image;
  }
  public Border getBorder() {
    return this.border;
  }
  public void paintComponent(Graphics g) {
    if (this.kvmInterface.base.isDiv) {
      super.paintComponent(g);
      Graphics2D g2D = (Graphics2D)g;
      g2D.drawImage(this.bi, 0, 0, this);
    }
    else {
      super.paintComponent(g);
      g.drawImage(this.image, 0, 0, this);
    } 
    this.show = 0;
  }
  public boolean judgeReflact(int posiX, int posiY, int epX, int epY) {
    boolean flag = false;
    int bordX = 0;
    int bordY = 0;
    if (getWidth() <= this.width) {
      bordX = getWidth();
    }
    else {
      bordX = this.width;
    } 
    if (getHeight() <= this.height) {
      bordY = getHeight();
    }
    else {
      bordY = this.height;
    } 
    if (epX >= posiX && epX <= posiX + bordX && epY >= posiY && epY <= posiY + bordY)
    {
      flag = true;
    }
    return flag;
  }
  public void mouseDragged(MouseEvent e) {
    if (!this.kvmInterface.base.isDiv && !this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY())) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      this.actionFlage = true;
      this.ismove = true;
      byte[] bytes = null;
      if (!this.isNew) {
        bytes = this.pack.mousePack(this.posiX - getImagePaneX(), this.posiY - getImagePaneY(), this.bladeNO);
        this.bladeThread.bladeCommu.sentData(bytes);
      } 
      this.ifmove = false;
    } 
    if (!this.kvmInterface.base.isDiv && this.isNew && (KVMUtil.isLinuxOS() || this.kvmInterface.base.isMstsc) && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && !Base.isSynMouse) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      int panex = this.posiX - getImagePaneX();
      int paney = this.posiY - getImagePaneY();
      if (this.remotemstscX != panex || this.remotemstscY != this.posiY)
      {
        int disX = panex - this.remotemstscX;
        int disY = paney - this.remotemstscY;
        if (disX != 0 || disY != 0)
        {
          sentMstscMouse(disX, disY);
        }
        this.remotemstscX = panex;
        this.remotemstscY = paney;
      }
    }
    else if (!this.kvmInterface.base.isDiv && this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && Base.isSynMouse) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      byte[] bytes = null;
      bytes = this.pack.mousePackNew_abs(this.posiX, this.posiY, this.bladeNO);
      this.bladeThread.bladeCommu.sentData(bytes);
    } 
  }
  public void mouseMoved(MouseEvent e) {
    if (KVMUtil.isWindowsOS()) {
      if (this.kvmInterface.isFullScreen && this.isNew && e.getY() <= this.kvmInterface.fullScreen.v && e.getX() >= this.kvmInterface.fullScreen.toolBarFrame.getX() + this.kvmInterface.fullScreen.h && e.getX() <= this.kvmInterface.fullScreen.toolBarFrame.getX() + this.kvmInterface.fullScreen.h + this.kvmInterface.fullScreen.toolBarFrame.getWidth() && !this.kvmInterface.fullScreen.toolBarFrame.isShowing())
      {
        this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
        setCursor(this.kvmInterface.base.defCursor);
        MouseDisplacementImpl.setMode(0);
        this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
      }
    }
    else if (KVMUtil.isLinuxOS()) {
      if (this.kvmInterface.isFullScreen && this.isNew && e.getY() <= this.kvmInterface.fullScreen.v + 21 && e.getX() >= this.kvmInterface.fullScreen.toolBarFrame.getX() + this.kvmInterface.fullScreen.h && e.getX() <= this.kvmInterface.fullScreen.toolBarFrame.getX() + this.kvmInterface.fullScreen.h + this.kvmInterface.fullScreen.toolBarFrame.getWidth() && !this.kvmInterface.fullScreen.toolBarFrame.isShowing()) {
        this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
        setCursor(this.kvmInterface.base.defCursor);
        MouseDisplacementImpl.setMode(0);
        this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
      } 
    } 
    if (!this.kvmInterface.isFullScreen && (null == this.kvmInterface.toolbar.frmFr || this.kvmInterface.imageFile.isShowing()))
    {
      if (!this.kvmInterface.floatToolbar.isShowPanel) {
        if (e.getY() >= this.kvmInterface.vv && e.getY() <= 24 + this.kvmInterface.vv && e.getX() >= this.kvmInterface.floatToolbar.getX() + this.kvmInterface.h && e.getX() <= this.kvmInterface.floatToolbar.getWidth() + this.kvmInterface.h + this.kvmInterface.floatToolbar.getX()) {
          this.kvmInterface.floatToolbar.setVisible(true);
          if (this.kvmInterface.floatToolbar.isVirtualMedia())
          {
            this.kvmInterface.floatToolbar.setVirtualMediaVisible(this.kvmInterface.floatToolbar.isShowingCD, this.kvmInterface.floatToolbar.isShowingFlp);
          }
          if (this.kvmInterface.getBladeSize() == 1)
          {
            this.kvmInterface.imageFile.setVisible(this.kvmInterface.floatToolbar.isShowingImagep);
          }
        }
        else {
          this.kvmInterface.floatToolbar.setVisible(false);
          if (this.kvmInterface.floatToolbar.isVirtualMedia())
          {
            this.kvmInterface.floatToolbar.setVirtualMediaVisible(false, false);
          }
          if (this.kvmInterface.getBladeSize() == 1)
          {
            this.kvmInterface.imageFile.setVisible(false);
          }
          if (null != this.kvmInterface.floatToolbar.powerMenu && this.kvmInterface.floatToolbar.powerMenu.isShowing())
          {
            this.kvmInterface.floatToolbar.powerMenu.setVisible(false);
          }
        } 
        if (this.kvmInterface.floatToolbar.isVirtualMedia()) {
          if (this.kvmInterface.floatToolbar.isShowingCD)
          {
            if (e.getY() > this.kvmInterface.floatToolbar.getHeight() && e.getY() < this.kvmInterface.floatToolbar.getHeight() + this.kvmInterface.floatToolbar.getCDHeight() && e.getX() > this.kvmInterface.floatToolbar.getCDX() && e.getX() <= this.kvmInterface.floatToolbar.getCDX() + this.kvmInterface.floatToolbar.getCDWidth()) {
              this.kvmInterface.floatToolbar.setVisible(true);
              this.kvmInterface.floatToolbar.setCDVisible(true);
            } 
          }
          if (this.kvmInterface.floatToolbar.isShowingFlp)
          {
            if (e.getY() > this.kvmInterface.floatToolbar.getHeight() && e.getY() < this.kvmInterface.floatToolbar.getHeight() + this.kvmInterface.floatToolbar.getFlpHeight() && e.getX() > this.kvmInterface.floatToolbar.getFlpX() && e.getX() <= this.kvmInterface.floatToolbar.getFlpX() + this.kvmInterface.floatToolbar.getFlpWidth()) {
              this.kvmInterface.floatToolbar.setVisible(true);
              this.kvmInterface.floatToolbar.setFlpVisible(true);
            } 
          }
        } 
      } 
    }
    if (!this.kvmInterface.base.isDiv && !this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY())) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      this.actionFlage = true;
      this.ismove = true;
      if (this.ifmove) {
        byte[] bytes = null;
        bytes = this.pack.mousePack(this.posiX - getImagePaneX(), this.posiY - getImagePaneY(), this.bladeNO);
        this.bladeThread.bladeCommu.sentData(bytes);
        this.ifmove = false;
      } 
    } 
    if (!this.kvmInterface.base.isDiv && this.isNew && (KVMUtil.isLinuxOS() || this.kvmInterface.base.isMstsc) && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && !Base.isSynMouse) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      int panex = this.posiX - getImagePaneX();
      int paney = this.posiY - getImagePaneY();
      if (this.remotemstscX != panex || this.remotemstscY != this.posiY)
      {
        int disX = panex - this.remotemstscX;
        int disY = paney - this.remotemstscY;
        if (disX != 0 || disY != 0)
        {
          sentMstscMouse(disX, disY);
        }
        this.remotemstscX = panex;
        this.remotemstscY = paney;
      }
    }
    else if (!this.kvmInterface.base.isDiv && this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && Base.isSynMouse) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      byte[] bytes = null;
      bytes = this.pack.mousePackNew_abs(this.posiX, this.posiY, this.bladeNO);
      this.bladeThread.bladeCommu.sentData(bytes);
    } 
    if (this.lostEndRow) {
      if (this.kvmInterface.getBladeSize() == 1) {
        this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(this.width + 2, this.height + 9));
      }
      else {
        this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(this.width + 2, this.height + 29));
      } 
      this.lostEndRow = false;
    } 
  }
  public void mouseClicked(MouseEvent e) {
    this.isFocusChange = true;
    if (this.isFocusChange && Base.isSingleMouse) {
      setCursor(this.kvmInterface.base.myCursor);
      this.kvmInterface.setCursor(this.kvmInterface.base.myCursor);
    } 
    if (this.kvmInterface.base.isDiv) {
      int clickCount = e.getClickCount();
      if (SwingUtilities.isLeftMouseButton(e) && clickCount == 2) {
        this.kvmInterface.fullScreen.imageParentPane.removeAll();
        this.kvmInterface.fullScreen.imageParentPane.setLayout(new BorderLayout());
        this.kvmInterface.fullScreen.getContentPane().removeAll();
        this.kvmInterface.base.isDiv = false;
        this.kvmInterface.fullScreen.toolBar.combo.removeActionListener(this.kvmInterface.fullScreen.action);
        this.kvmInterface.fullScreen.toolBar.combo.setSelectedItem("blade" + this.bladeNO);
        this.kvmInterface.fullScreen.toolBar.combo.addActionListener(this.kvmInterface.fullScreen.action);
        this.kvmInterface.kvmUtil.setFullToolBar(this.kvmInterface.base.isDiv);
        this.kvmInterface.kvmUtil.setImageTipText(this.kvmInterface.base.isDiv);
        Iterator<String> iter = this.kvmInterface.base.threadGroup.keySet().iterator();
        String name = "";
        while (iter.hasNext()) {
          name = iter.next();
          BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(name);
          if (!name.equals(String.valueOf(this.bladeNO)))
          {
            (bladeThread.getDrawThread()).isDisplay = false;
          }
        } 
        this.kvmInterface.kvmUtil.setFullToolBar(this.kvmInterface.base.isDiv);
        if (this.isControl) {
          this.kvmInterface.kvmUtil.setNumKeyColor(getNum());
          this.kvmInterface.kvmUtil.setCapsKeyColor(getCaps());
          this.kvmInterface.kvmUtil.setScrollKeyColor(getScroll());
        }
        else {
          this.kvmInterface.kvmUtil.setMoniKeyState(this.kvmInterface.isFullScreen);
        } 
        this.kvmInterface.fullScreen.imageParentScrollPane = null;
        this.kvmInterface.fullScreen.imageParentPane.setPreferredSize(new Dimension(this.width, this.height));
        this.kvmInterface.fullScreen.imageParentPane.add(this);
        this.kvmInterface.fullScreen.imageParentScrollPane = new JScrollPane(this.kvmInterface.fullScreen.imageParentPane);
        this.kvmInterface.fullScreen.getContentPane().add(this.kvmInterface.fullScreen.imageParentScrollPane, "Center");
        this.kvmInterface.fullScreen.imagePane = this;
        this.kvmInterface.kvmUtil.setDrawDisplay(false);
        (this.bladeThread.getDrawThread()).isDisplay = true;
        if (this.isNew) {
          this.bladeThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(35, this.bladeThread.getBladeNO()));
          if (this.kvmInterface.base.isMstsc)
          {
            this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
            setCursor(this.kvmInterface.base.defCursor);
            this.kvmInterface.fullScreen.toolBar.mouseSynButton.setToolTipText(this.kvmInterface.kvmUtil.getString("MouseSyn.Tip"));
          }
          else
          {
            this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.myCursor);
            setCursor(this.kvmInterface.base.myCursor);
            this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(false);
            MouseDisplacementImpl.setMode(1);
          }
        } else {
          this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
          setCursor(this.kvmInterface.base.defCursor);
          this.kvmInterface.fullScreen.toolBar.mouseSynButton.setToolTipText(this.kvmInterface.kvmUtil.getString("MouseSyn.Tip"));
        } 
        MouseDisplacementImpl.setKeyBoardStatus((getNum() == 1), (byte)-112);
        MouseDisplacementImpl.setKeyBoardStatus((getCaps() == 1), (byte)20);
        MouseDisplacementImpl.setKeyBoardStatus((getScroll() == 1), (byte)-111);
        this.kvmInterface.fullScreen.actionBlade = this.bladeNO;
        this.kvmInterface.actionBlade = this.bladeNO;
        this.kvmInterface.fullScreen.setVisible(false);
        this.kvmInterface.fullScreen.setVisible(true);
        this.kvmInterface.fullScreen.toolBarFrame.removeAll();
        this.kvmInterface.fullScreen.toolBarFrame.add(this.kvmInterface.fullScreen.toolBar, "Center");
        add(this.kvmInterface.fullScreen.toolBarFrame);
        if (this.kvmInterface.toolFrame != null && this.kvmInterface.toolFrame.isShowing())
        {
          this.kvmInterface.toolFrame.setVisible(false);
        }
        if (KVMUtil.isWindowsOS()) {
          this.kvmInterface.fullScreen.toolBarFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.kvmInterface.fullScreen.toolBarFrame.getWidth()) / 2.0D), 0);
        }
        else if (KVMUtil.isLinuxOS()) {
          this.kvmInterface.fullScreen.toolBarFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.kvmInterface.fullScreen.toolBarFrame.getWidth()) / 2.0D) + 1, 1);
        } 
        this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
      } 
      this.kvmInterface.fullScreen.toolBar.startButtonState();
      this.kvmInterface.fullScreen.setButtonEnable();
    } 
  }
  public void mouseEntered(MouseEvent e) {
    this.posiX = e.getX();
    this.posiY = e.getY();
    if (!this.kvmInterface.base.isDiv) {
      switch (e.getModifiers()) {
        case 0:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x0);
          break;
        case 16:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x1);
          break;
        case 8:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x4);
          break;
        case 4:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x2);
          break;
        case 24:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x5);
          break;
        case 28:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x7);
          break;
        case 20:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x3);
          break;
        case 12:
          this.pack.mousData[0] = (byte)(this.pack.mousData[0] & 0x6);
          break;
        default:
          Debug.println("error command");
          break;
      } 
      if (!this.isNew) {
        this.bladeThread.bladeCommu.sentData(this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO));
      }
      else if ((KVMUtil.isLinuxOS() || this.kvmInterface.base.isMstsc) && this.isFocusChange && !Base.isSynMouse) {
        int panex = this.posiX - getImagePaneX();
        int paney = this.posiY - getImagePaneY();
        if (this.remotemstscX != panex || this.remotemstscY != this.posiY) {
          int disX = panex - this.remotemstscX;
          int disY = paney - this.remotemstscY;
          if (disX != 0 || disY != 0)
          {
            sentMstscMouse(disX, disY);
          }
          this.remotemstscX = panex;
          this.remotemstscY = paney;
        } 
      } 
      this.ifmove = false;
    } 
    if (this.isFocusChange && Base.isSingleMouse) {
      setCursor(this.kvmInterface.base.myCursor);
      this.kvmInterface.setCursor(this.kvmInterface.base.myCursor);
    } 
  }
  public void mouseExited(MouseEvent e) {
    if (!this.kvmInterface.isFullScreen && !this.kvmInterface.base.isDiv && this.isFocusChange && Base.isSingleMouse) {
      int hx = (this.kvmInterface.getLocationOnScreen()).x + this.kvmInterface.getWidth() / 2;
      int hy = (this.kvmInterface.getLocationOnScreen()).y + this.kvmInterface.getHeight() / 2;
      this.robot.mouseMove(hx, hy);
      try {
        Thread.sleep(300L);
      }
      catch (InterruptedException e1) {
        e1.printStackTrace();
      } 
    } 
  }
  public void mousePressed(MouseEvent e) {
    this.isFocusChange = true;
    if (this.kvmInterface.isFullScreen && this.kvmInterface.fullScreen.toolBarFrame.isShowing() && this.isNew)
    {
      if (this.kvmInterface.base.isMstsc) {
        this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
        setCursor(this.kvmInterface.base.defCursor);
        if (!this.kvmInterface.floatToolbar.isVirtualMedia())
        {
          this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
        }
        else if ((null != this.kvmInterface.floatToolbar.getFlpPanel() && this.kvmInterface.fullScreen.cdMenu.isShowing()) || this.kvmInterface.fullScreen.flpMenu.isShowing())
        {
          if (KVMUtil.isWindowsOS())
          {
            this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
          }
          else if (KVMUtil.isLinuxOS())
          {
            this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
            this.kvmInterface.fullScreen.cdMenu.setVisible(false);
            this.kvmInterface.fullScreen.flpMenu.setVisible(false);
            this.kvmInterface.fullScreen.toolBar.btnCDMenu.setBorder((Border)null);
            this.kvmInterface.fullScreen.toolBar.btnFlpMenu.setBorder((Border)null);
          }
        }
        else
        {
          this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
        }
      }
      else {
        if (this.kvmInterface.base.isDiv) {
          this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
          setCursor(this.kvmInterface.base.defCursor);
        }
        else {
          this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.myCursor);
          setCursor(this.kvmInterface.base.myCursor);
        } 
        if (!this.kvmInterface.floatToolbar.isVirtualMedia()) {
          this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
        }
        else if ((null != this.kvmInterface.floatToolbar.getFlpPanel() && this.kvmInterface.fullScreen.cdMenu.isShowing()) || this.kvmInterface.fullScreen.flpMenu.isShowing()) {
          if (KVMUtil.isWindowsOS())
          {
            this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
          }
          else if (KVMUtil.isLinuxOS())
          {
            this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
            this.kvmInterface.fullScreen.cdMenu.setVisible(false);
            this.kvmInterface.fullScreen.flpMenu.setVisible(false);
            this.kvmInterface.fullScreen.toolBar.btnCDMenu.setBorder((Border)null);
            this.kvmInterface.fullScreen.toolBar.btnFlpMenu.setBorder((Border)null);
          }
        }
        else {
          this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
        } 
        MouseDisplacementImpl.setMode(1);
      } 
    }
    if (this.kvmInterface.isFullScreen && this.kvmInterface.fullScreen.toolBarFrame.isShowing() && !this.isNew) {
      this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
      setCursor(this.kvmInterface.base.defCursor);
      if (!this.kvmInterface.floatToolbar.isVirtualMedia()) {
        this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
      }
      else if ((null != this.kvmInterface.floatToolbar.getFlpPanel() && this.kvmInterface.fullScreen.cdMenu.isShowing()) || this.kvmInterface.fullScreen.flpMenu.isShowing()) {
        this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
      }
      else {
        this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
      } 
    } 
    if (!this.kvmInterface.base.isDiv && this.isControl) {
      requestFocus();
      if (!this.isNew) {
        if (judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
        {
          this.pack.mousePressedPack(e);
          byte[] bytes = null;
          bytes = this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO);
          this.bladeThread.bladeCommu.sentData(bytes);
          this.ifmove = false;
        }
      }
      else if (KVMUtil.isLinuxOS() || this.kvmInterface.base.isMstsc) {
        if (!Base.isSynMouse) {
          if (judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
          {
            this.pack.mousePressedPack(e);
            this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew((byte)0, (byte)0, this.bladeNO));
          }
        } else {
          this.pack.mousePressedPack(e);
          this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew_abs(e.getX(), e.getY(), this.bladeNO));
        } 
        if (this.kvmInterface.isFullScreen)
        {
          MouseDisplacementImpl.setMode(1);
        }
      }
      else {
        if (this.kvmInterface.isFullScreen)
        {
          MouseDisplacementImpl.setMode(1);
        }
        if (Base.isSynMouse) {
          this.pack.mousePressedPack(e);
          this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew_abs(e.getX(), e.getY(), this.bladeNO));
        } 
      } 
    } 
    if (!this.kvmInterface.isFullScreen) {
      if (this.kvmInterface.floatToolbar.isVirtualMedia()) {
        if (this.kvmInterface.floatToolbar.getFlpPanel().isShowing())
        {
          if (e.getX() <= this.kvmInterface.floatToolbar.getFlpX() || e.getX() >= this.kvmInterface.floatToolbar.getFlpWidth() + this.kvmInterface.floatToolbar.getFlpX() || e.getY() <= this.kvmInterface.floatToolbar.getFlpY() || e.getY() >= this.kvmInterface.floatToolbar.getFlpY() + this.kvmInterface.floatToolbar.getFlpHeight()) {
            this.kvmInterface.floatToolbar.setFlpVisible(false);
            this.kvmInterface.floatToolbar.isShowingFlp = false;
            this.kvmInterface.floatToolbar.btnFlpMenu.setBorder((Border)null);
          } 
        }
        if (this.kvmInterface.floatToolbar.getCDPanel().isShowing())
        {
          if (e.getX() <= this.kvmInterface.floatToolbar.getCDX() || e.getX() >= this.kvmInterface.floatToolbar.getCDWidth() + this.kvmInterface.floatToolbar.getCDX() || e.getY() <= this.kvmInterface.floatToolbar.getCDY() || e.getY() >= this.kvmInterface.floatToolbar.getCDY() + this.kvmInterface.floatToolbar.getCDHeight()) {
            this.kvmInterface.floatToolbar.setCDVisible(false);
            this.kvmInterface.floatToolbar.isShowingCD = false;
            this.kvmInterface.floatToolbar.btnCDMenu.setBorder((Border)null);
          } 
        }
      } 
      if (this.kvmInterface.getBladeSize() == 1 && this.kvmInterface.imageFile.isShowing())
      {
        if (e.getX() <= this.kvmInterface.imageFile.getX() || e.getX() >= this.kvmInterface.imageFile.getWidth() + this.kvmInterface.imageFile.getX() || e.getY() <= this.kvmInterface.imageFile.getY() || e.getY() >= this.kvmInterface.imageFile.getY() + this.kvmInterface.imageFile.getHeight())
        {
          this.kvmInterface.imageFile.setVisible(false);
          this.kvmInterface.floatToolbar.isShowingImagep = false;
          this.kvmInterface.floatToolbar.btnCreateImage.setBorder((Border)null);
        }
      }
    }
    else {
      if (this.kvmInterface.floatToolbar.isVirtualMedia()) {
        if (this.kvmInterface.floatToolbar.getCDPanel().isShowing()) {
          if (e.getX() <= this.kvmInterface.floatToolbar.getCDX() || e.getX() >= this.kvmInterface.floatToolbar.getCDWidth() + this.kvmInterface.floatToolbar.getCDX() || e.getY() <= this.kvmInterface.floatToolbar.getCDY() || e.getY() >= this.kvmInterface.floatToolbar.getCDY() + this.kvmInterface.floatToolbar.getCDHeight()) {
            this.kvmInterface.fullScreen.cdMenu.setVisible(false);
            this.kvmInterface.floatToolbar.isShowingCD = false;
            this.kvmInterface.fullScreen.toolBar.btnCDMenu.setBorder((Border)null);
          } 
          if (this.kvmInterface.floatToolbar.getCDPanel().isShowing())
          {
            this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
          }
        } 
        if (this.kvmInterface.floatToolbar.getFlpPanel().isShowing()) {
          if (e.getX() <= this.kvmInterface.floatToolbar.getFlpX() || e.getX() >= this.kvmInterface.floatToolbar.getFlpWidth() + this.kvmInterface.floatToolbar.getFlpX() || e.getY() <= this.kvmInterface.floatToolbar.getFlpY() || e.getY() >= this.kvmInterface.floatToolbar.getFlpY() + this.kvmInterface.floatToolbar.getFlpHeight()) {
            this.kvmInterface.fullScreen.flpMenu.setVisible(false);
            this.kvmInterface.floatToolbar.isShowingFlp = false;
            this.kvmInterface.fullScreen.toolBar.btnFlpMenu.setBorder((Border)null);
          } 
          if (this.kvmInterface.floatToolbar.getFlpPanel().isShowing())
          {
            this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
          }
        } 
      } 
      if (KVMUtil.isLinuxOS() && this.kvmInterface.fullScreen.powerPanelDialog.isShowing())
      {
        this.kvmInterface.fullScreen.powerPanelDialog.setVisible(false);
      }
    } 
    if (this.kvmInterface.isFullScreen && null != this.kvmInterface.floatToolbar.helpFrm)
    {
      if (this.kvmInterface.floatToolbar.helpFrm.isShowing())
      {
        this.kvmInterface.floatToolbar.helpFrm.setVisible(false);
      }
    }
  }
  public void mouseReleased(MouseEvent e) {
    if (!this.isNew) {
      if (!this.kvmInterface.base.isDiv && this.isControl && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
      {
        this.pack.mouseReleasedPack(e);
        byte[] bytes = null;
        bytes = this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO);
        this.bladeThread.bladeCommu.sentData(bytes);
        this.ifmove = false;
      }
    }
    else if (KVMUtil.isLinuxOS() || this.kvmInterface.base.isMstsc) {
      if (!Base.isSynMouse) {
        if (judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
        {
          this.pack.mouseReleasedPack(e);
          this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew((byte)0, (byte)0, this.bladeNO));
        }
      } else {
        this.pack.mouseReleasedPack(e);
        this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew_abs(e.getX(), e.getY(), this.bladeNO));
      }
    } else {
      if (this.kvmInterface.isFullScreen)
      {
        MouseDisplacementImpl.setMode(1);
      }
      if (Base.isSynMouse) {
        this.pack.mouseReleasedPack(e);
        this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew_abs(e.getX(), e.getY(), this.bladeNO));
      } 
    } 
  }
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (!this.kvmInterface.base.isDiv && this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY())) {
      byte[] bytes = this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO);
      this.bladeThread.bladeCommu.sentData(bytes);
      this.ifmove = false;
    } 
  }
  class KeyHandler
    implements KeyListener
  {
    public int last_pressed_key_code = 0;
    public void keyPressed(KeyEvent event) {
      this.last_pressed_key_code = event.getKeyCode();
      if (ImagePane.this.kvmInterface.iImageFocused == 1) {
        ImagePane.this.kvmInterface.iKeyPressControl++;
        if (ImagePane.this.kvmInterface.iKeyPressControl <= ImagePane.this.kvmInterface.iKeyPressTotal && KVMUtil.isWindowsOS()) {
          return;
        }
        ImagePane.this.kvmInterface.iImageFocused = 2;
      } 
      if (event.isControlDown() && event.isAltDown() && event.isShiftDown() && ImagePane.this.kvmInterface.isFullScreen) {
        ImagePane.this.kvmInterface.fullScreen.setCursor(ImagePane.this.kvmInterface.base.defCursor);
        ImagePane.this.setCursor(ImagePane.this.kvmInterface.base.defCursor);
        MouseDisplacementImpl.setMode(0);
        if (ImagePane.this.kvmInterface.base.isDiv) {
          ImagePane.this.kvmInterface.toolFrame.setState(0);
          ImagePane.this.kvmInterface.toolFrame.setVisible(true);
          ImagePane.this.kvmInterface.toolFrame.setAlwaysOnTop(true);
        }
        else {
          ImagePane.this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
        } 
        return;
      } 
      if (ImagePane.this.isFocusChange && Base.isSingleMouse && event.isControlDown() && event.isAltDown() && event.isShiftDown()) {
        ImagePane.this.isFocusChange = false;
        ImagePane.this.setCursor(ImagePane.this.kvmInterface.base.defCursor);
        ImagePane.this.kvmInterface.setCursor(ImagePane.this.kvmInterface.base.defCursor);
      } 
      if (!ImagePane.this.kvmInterface.base.isDiv) {
        if (ImagePane.this.isNew) {
          if (17 != event.getKeyCode() && 16 != event.getKeyCode() && 18 != event.getKeyCode() && 65406 != event.getKeyCode()) {
            int usbCode = KVMUtil.translateToUSBCode(event);
            for (int j = 2; j < ImagePane.this.pack.keyData.length; j++) {
              if (ImagePane.this.pack.keyData[j] == usbCode) {
                ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.keyRePressedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew));
                return;
              } 
            } 
          } else {
            int mask = 0;
            int shift = (event.getKeyLocation() == 2) ? 0 : 4;
            switch (event.getKeyCode()) {
              case 17:
                mask = 1 << shift;
                break;
              case 16:
                mask = 2 << shift;
                break;
              case 18:
                mask = 4 << shift;
                break;
              case 65406:
                mask = 64;
                break;
            } 
            if ((ImagePane.this.pack.keyData[0] & mask) == mask) {
              return;
            }
            if (17 == event.getKeyCode() && (ImagePane.this.pack.keyData[0] & 0x40) == 64) {
              return;
            }
          } 
          ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.keyPressedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew));
        }
        else {
          for (int j = 2; j < ImagePane.this.pack.keyData.length; j++) {
            if (ImagePane.this.pack.keyData[j] == KVMUtil.translateToUSBCode(event)) {
              return;
            }
          } 
          ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.keyPressedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew));
        } 
        for (int i = 2; i < ImagePane.this.pack.keyData.length; i++)
        {
          ImagePane.this.pack.keyData[i] = 0;
        }
      } 
    }
    public void keyReleased(KeyEvent event) {
      if (KVMUtil.isLinux())
      {
        if ((130 == event.getKeyCode() || 91 == event.getKeyCode()) && this.last_pressed_key_code != event.getKeyCode())
        {
          keyPressed(event);
        }
      }
      if (!ImagePane.this.kvmInterface.base.isDiv && ImagePane.this.isControl)
      {
        if (ImagePane.this.isNew) {
          if (17 == event.getKeyCode() || 18 == event.getKeyCode() || 16 == event.getKeyCode() || 65406 == event.getKeyCode())
          {
            ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.clearKey(ImagePane.this.bladeNO));
          }
          else
          {
            ImagePane.this.pack.keyReleasedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew);
          }
        }
        else if (event.getKeyCode() == 154) {
          ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.keyPressedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew));
          try {
            Thread.sleep(100L);
          }
          catch (InterruptedException e1) {
            Debug.printExc(e1.getMessage());
          } 
          ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.keyReleasedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew));
        }
        else {
          ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.keyReleasedPack(event, ImagePane.this.bladeNO, ImagePane.this.isNew));
        } 
      }
      this.last_pressed_key_code = 0;
    }
    public void keyTyped(KeyEvent event) {}
  }
  private int getImagePaneY() {
    Point p = getLocation();
    return p.y - p.y;
  }
  private int getImagePaneX() {
    Point p = getLocation();
    return p.x - p.x;
  }
  public boolean isContr() {
    return this.isControl;
  }
  public void setControl(boolean isControl) {
    this.isControl = isControl;
  }
  public void setPack(PackData pack) {
    this.pack = pack;
  }
  public int getCaps() {
    return this.caps;
  }
  public void setCaps(int caps) {
    this.caps = caps;
  }
  public int getNum() {
    return this.num;
  }
  public void setNum(int num) {
    this.num = num;
  }
  public int getScroll() {
    return this.scroll;
  }
  public void setScroll(int scroll) {
    this.scroll = scroll;
  }
  public void setPixels(byte[] pixels) {
    this.pixels = pixels;
  }
  public void releaseImagePanel() {
    removeAll();
    removeMouseMotionListener(this);
    removeMouseListener(this);
    this.taskPerformer = null;
    this.taskSetFocus = null;
    this.pixels = null;
    this.source = null;
    this.image = null;
    this.biSrc = null;
    this.border = null;
    this.receiveList = null;
    this.focusList = null;
    this.pack = null;
    this.bi = null;
    this.big = null;
    this.biDest = null;
    this.transform = null;
  }
  class StatReceiveTask
    extends TimerTask {
    int count = 0;

	public void run() {
      if (this.count < 10) {
        if (ImagePane.this.imageReceive == 0)
        {
          ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.kvmInterface.packData.connectBlade(ImagePane.this.bladeNO, (ImagePane.this.kvmInterface.kvmUtil.getImagePane(ImagePane.this.bladeNO)).custBit));
          this.count++;
        }
        else
        {
          this.count = 0;
          ImagePane.this.imageReceive = 0;
        }
      } else {
        this.count = 0;
      } 
    }
  }
  class MouseTimerTask
    extends Thread
  {
    byte[] bytes = new byte[8];
	public void run() {
      while (!Base.isSynMouse) {
        if (!ImagePane.this.isFocusOwner()) {
          if (ImagePane.this.isFocusChange == true) {
            ImagePane.this.pack.mousData[0] = 0;
            ImagePane.this.pack.mousData[3] = 0;
            ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.mousePackNew((byte)0, (byte)0, ImagePane.this.bladeNO));
          } 
          ImagePane.this.isFocusChange = false;
        } 
        if (ImagePane.this.isNew) {
          if (!ImagePane.this.kvmInterface.base.isMstsc)
          {
            if (!ImagePane.this.kvmInterface.base.isDiv && ImagePane.this.isFocusChange && !Base.isSynMouse)
            {
              int i = MouseDisplacementImpl.getMouseDisplacement(this.bytes);
              if (i == 1 && (this.bytes[0] != ImagePane.this.pack.mousData[0] || this.bytes[1] != 0 || this.bytes[2] != 0 || this.bytes[3] != 0))
              {
                ImagePane.this.pack.mousData[0] = this.bytes[0];
                ImagePane.this.pack.mousData[3] = this.bytes[3];
                ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.mousePackNew(this.bytes[1], this.bytes[2], ImagePane.this.bladeNO));
              }
            }
          }
        }
        else if (!ImagePane.this.kvmInterface.base.isDiv) {
          ImagePane.this.ifmove = true;
          if (ImagePane.this.remoteX == 65535 && ImagePane.this.remoteY == 65535) {
            if (!ImagePane.this.isNew)
            {
              ImagePane.this.bladeThread.bladeCommu.sentData(ImagePane.this.pack.mousePack(65535, 65535, ImagePane.this.bladeNO));
            }
          }
          else {
            if (ImagePane.this.actionFlage) {
              ImagePane.this.actionFlage = false;
              return;
            } 
            if (ImagePane.this.ismove) {
              ImagePane.this.ismove = false;
              return;
            } 
            if (ImagePane.this.remotePreX == ImagePane.this.remoteX && ImagePane.this.remotePreY == ImagePane.this.remoteY && ImagePane.this.remoteX != ImagePane.this.posiX - ImagePane.this.getImagePaneX() && ImagePane.this.remoteY != ImagePane.this.posiY - ImagePane.this.getImagePaneY()) {
              byte[] bytes = null;
              if (!ImagePane.this.isNew) {
                bytes = ImagePane.this.pack.mousePack(ImagePane.this.posiX - ImagePane.this.getImagePaneX(), ImagePane.this.posiY - ImagePane.this.getImagePaneY(), ImagePane.this.bladeNO);
                ImagePane.this.bladeThread.bladeCommu.sentData(bytes);
                ImagePane.this.ifmove = false;
                ImagePane.this.ismove = true;
              } 
            } 
          } 
        } 
        try {
          Thread.sleep(20L);
        }
        catch (InterruptedException e) {}
      } 
    }
  }
  private void sentMstscMouse(int disX, int disY) {
    for (int j = 0; j < 16; j++) {
      this.arrx[j] = 0;
      this.arry[j] = 0;
    } 
    if (disX >= 0) {
      for (int k = 0; k < 16; k++, disX -= 120) {
        if (disX > 120) {
          this.arrx[k] = 120;
        }
        else {
          this.arrx[k] = disX;
          break;
        } 
      } 
    } else {
      for (int k = 0; k < 16; k++, disX += 120) {
        if (disX < -120) {
          this.arrx[k] = -120;
        }
        else {
          this.arrx[k] = disX;
          break;
        } 
      } 
    } 
    if (disY >= 0) {
      for (int k = 0; k < 16; k++, disY -= 120) {
        if (disY > 120) {
          this.arry[k] = 120;
        }
        else {
          this.arry[k] = disY;
          break;
        } 
      } 
    } else {
      for (int k = 0; k < 16; k++, disY += 120) {
        if (disY < -120) {
          this.arry[k] = -120;
        }
        else {
          this.arry[k] = disY;
          break;
        } 
      } 
    } 
    for (int i = 0; i < 16; i++) {
      if (this.arrx[i] == 0 && this.arry[i] == 0) {
        break;
      }
      this.bladeThread.bladeCommu.sentData(this.pack.mousePackNew((byte)this.arrx[i], (byte)this.arry[i], this.bladeNO));
    } 
  }
  public void setBladeThread(BladeThread bladeThread) {
    this.bladeThread = bladeThread;
  }
  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }
  public boolean isNew() {
    return this.isNew;
  }
  public void focusGained(FocusEvent e) {
    if (this.kvmInterface.base.getHookNum() == 0)
    {
      this.kvmInterface.base.setHookNum(MouseDisplacementImpl.installHook());
    }
    this.kvmInterface.kvmUtil.setiWindosFocus(1);
    this.kvmInterface.iImageFocused = 1;
    this.kvmInterface.iKeyPressControl = 0;
    this.kvmInterface.iKeyPressTotal = 0;
    ImagePane imagePane = this.kvmInterface.kvmUtil.getImagePane(this.kvmInterface.actionBlade);
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)-112) == imagePane.getNum() && MouseDisplacementImpl.getKeyBoardStatus((byte)20) == imagePane.getCaps() && MouseDisplacementImpl.getKeyBoardStatus((byte)-111) == imagePane.getScroll()) {
      this.kvmInterface.iImageFocused = 2;
      return;
    } 
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)-112) != imagePane.getNum()) {
      this.kvmInterface.iKeyPressTotal++;
      MouseDisplacementImpl.setKeyBoardStatus((imagePane.getNum() == 1), (byte)-112);
    } 
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)20) != imagePane.getCaps()) {
      this.kvmInterface.iKeyPressTotal++;
      MouseDisplacementImpl.setKeyBoardStatus((imagePane.getCaps() == 1), (byte)20);
    } 
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)-111) != imagePane.getScroll()) {
      this.kvmInterface.iKeyPressTotal++;
      MouseDisplacementImpl.setKeyBoardStatus((imagePane.getScroll() == 1), (byte)-111);
    } 
  }
  public void focusLost(FocusEvent e) {
    this.kvmInterface.iImageFocused = 0;
    this.kvmInterface.iKeyPressControl = 0;
    this.kvmInterface.iKeyPressTotal = 0;
    this.kvmInterface.kvmUtil.setiWindosFocus(0);
    if (this.kvmInterface.base.getHookNum() != 0) {
      MouseDisplacementImpl.removeHook(this.kvmInterface.base.getHookNum());
      this.kvmInterface.base.setHookNum(0);
    } 
  }
}
