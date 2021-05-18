package com.kvm;
import com.huawei.vm.console.utils.TestPrint;
import com.library.LoggerUtil;
import com.library.decoder.ImageBlock;
import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Timer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
public class ImagePane
  extends JPanel
  implements MouseMotionListener, MouseListener, MouseWheelListener, FocusListener
{
  private static final long serialVersionUID = 1L;
  Canvas canvas;
  private Border border;
  private boolean isControl = true;
  private transient BufferedImage bi;
  public boolean isControl() {
    return this.isControl;
  }
  private boolean actionFlage = false;
  public boolean isActionFlage() {
    return this.actionFlage;
  }
  public void setActionFlage(boolean actionFlage) {
    this.actionFlage = actionFlage;
  }
  private int posiX = 0;
  public int getPosiX() {
    return this.posiX;
  }
  public void setPosiX(int posiX) {
    this.posiX = posiX;
  }
  private int posiY = 0;
  public int getPosiY() {
    return this.posiY;
  }
  public void setPosiY(int posiY) {
    this.posiY = posiY;
  }
  private byte[] pixels = null;
  private transient BufferedImage biSrc;
  private transient BufferedImage biDest;
  private boolean showtoolBar = false;
  private boolean ismove = true;
  public boolean isIsmove() {
    return this.ismove;
  }
  public void setIsmove(boolean ismove) {
    this.ismove = ismove;
  }
  private boolean ifmove = false;
  public boolean isIfmove() {
    return this.ifmove;
  }
  public void setIfmove(boolean ifmove) {
    this.ifmove = ifmove;
  }
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
  private transient Image image;
  private transient Timer showTimer = null;
  private transient Timer hideTimer = null;
  private Object images;
  private transient PackData pack;
  private transient Graphics2D big;
  public void setImage(Image image) {
    this.image = image;
  }
  public PackData getPack() {
    return this.pack;
  }
  public Graphics2D getBig() {
    return this.big;
  }
  public void setBig(Graphics2D big) {
    this.big = big;
  }
  private int remoteX = 0;
  public int getRemoteX() {
    return this.remoteX;
  }
  public void setRemoteX(int remoteX) {
    this.remoteX = remoteX;
  }
  private int remoteY = 0;
  public int getRemoteY() {
    return this.remoteY;
  }
  public void setRemoteY(int remoteY) {
    this.remoteY = remoteY;
  }
  private int remotePreX = 0;
  public int getRemotePreX() {
    return this.remotePreX;
  }
  public void setRemotePreX(int remotePreX) {
    this.remotePreX = remotePreX;
  }
  private int remotePreY = 0;
  public int getRemotePreY() {
    return this.remotePreY;
  }
  public void setRemotePreY(int remotePreY) {
    this.remotePreY = remotePreY;
  }
  private int remotemstscX = 0;
  public int getRemotemstscX() {
    return this.remotemstscX;
  }
  public void setRemotemstscX(int remotemstscX) {
    this.remotemstscX = remotemstscX;
  }
  private int remotemstscY = 0;
  public int getRemotemstscY() {
    return this.remotemstscY;
  }
  public void setRemotemstscY(int remotemstscY) {
    this.remotemstscY = remotemstscY;
  }
  private transient Timer receiveList = null;
  public Timer getReceiveList() {
    return this.receiveList;
  }
  public void setReceiveList(Timer receiveList) {
    this.receiveList = receiveList;
  }
  private transient Timer focusList = null;
  private int bladeNO = 0;
  private transient MemoryImageSource source;
  public int getBladeNumber() {
    return this.bladeNO;
  }
  public void setBladeNumber(int bladeNO) {
    this.bladeNO = bladeNO;
  }
  public MemoryImageSource getSource() {
    return this.source;
  }
  public void setSource(MemoryImageSource source) {
    this.source = source;
  }
  private boolean resolutionCh = false;
  private transient ColorModel cm;
  private AffineTransform transform;
  public boolean isResolutionCh() {
    return this.resolutionCh;
  }
  public void setResolutionCh(boolean resolutionCh) {
    this.resolutionCh = resolutionCh;
  }
  public AffineTransform getTransform() {
    return this.transform;
  }
  public void setTransform(AffineTransform transform) {
    this.transform = transform;
  }
  private int imageReceive = 0;
  public int getImageReceive() {
    return this.imageReceive;
  }
  public void setImageReceive(int imageReceive) {
    this.imageReceive = imageReceive;
  }
  private int show = 0;
  private int width = 0;
  public int getImagePaneWidth() {
    return this.width;
  }
  public void setImagePaneWidth(int width) {
    this.width = width;
  }
  private int height = 0;
  public int getImagePaneHeight() {
    return this.height;
  }
  public void setImagePaneHeight(int height) {
    this.height = height;
  }
  private byte colorBit = 0;
  public byte getColorBit() {
    return this.colorBit;
  }
  public void setColorBit(byte colorBit) {
    this.colorBit = colorBit;
  }
  private KVMInterface kvmInterface = null;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  private transient BladeThread bladeThread = null;
  private transient MouseTimerTask mouseTimerTask = null;
  public MouseTimerTask getMouseTimerTask() {
    return this.mouseTimerTask;
  }
  public void setMouseTimerTask(MouseTimerTask mouseTimerTask) {
    this.mouseTimerTask = mouseTimerTask;
  }
  private transient StatReceiveTask statReceiveTask = null;
  public StatReceiveTask getStatReceiveTask() {
    return this.statReceiveTask;
  }
  public void setStatReceiveTask(StatReceiveTask statReceiveTask) {
    this.statReceiveTask = statReceiveTask;
  }
  private transient KeyHandler keyListener = null;
  public KeyHandler getKeyListener() {
    return this.keyListener;
  }
  public void setKeyListener(KeyHandler keyListener) {
    this.keyListener = keyListener;
  }
  private byte custBit = 0;
  public byte getCustBit() {
    return this.custBit;
  }
  public void setCustBit(byte custBit) {
    this.custBit = custBit;
  }
  private boolean isFocusChange = true;
  public boolean isFocusChange() {
    return this.isFocusChange;
  }
  public void setFocusChange(boolean isFocusChange) {
    this.isFocusChange = isFocusChange;
  }
  private boolean lostEndRow = true;
  private transient Robot robot;
  private transient ActionListener taskPerformer = new PerformACtionListener(this);
  private transient ActionListener taskSetFocus = new FocusACtionListener(this);
  private final Object lock;
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    this.taskSetFocus = (ActionListener)in.readObject();
    this.taskPerformer = (ActionListener)in.readObject();
  }
  public void quitConn(String messageKey) {
    String messkey = "No_bladeData";
    if (null == messageKey || !messageKey.equals(""))
    {
      messkey = messageKey;
    }
    this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    if (this.kvmInterface.isFullScreen())
    {
      this.kvmInterface.getKvmUtil().returnToWin();
    }
    this.kvmInterface.getKvmUtil().disconnectBlade(this.bladeNO);
    this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface.getKvmUtil().getString(messkey));
  }
  protected void quitConn() {
    this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    if (this.kvmInterface.isFullScreen())
    {
      this.kvmInterface.getKvmUtil().returnToWin();
    }
    int tempBladeNO = this.bladeNO;
    this.kvmInterface.getKvmUtil().disconnectBlade(this.bladeNO);
    this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    if (this.kvmInterface.getBladeSize() == 1) {
      JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface.getKvmUtil()
          .getString("No_bladeData"));
    }
    else {
      JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), "Blade" + tempBladeNO + " " + this.kvmInterface
          .getKvmUtil().getString("No_bladeData"));
    } 
  }
  protected void setFocus() {
    if (!isFocusOwner() && !this.kvmInterface.isFullScreen())
    {
      requestFocus();
    }
  }
  public boolean isFocusTraversable() {
    return true;
  }
  private void jbInit() {
    this.canvas = new Canvas();
    setLayout((LayoutManager)null);
    Dimension dim = new Dimension(1920, 1200);
    setPreferredSize(dim);
    setFocusTraversalKeysEnabled(false);
    this.keyListener = new KeyHandler(this);
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
    int size = this.kvmInterface.getBase().getImageWidth() * this.kvmInterface.getBase().getImageHeight();
    this.pixels = new byte[size];
    this
      .source = new MemoryImageSource(this.kvmInterface.getBase().getImageWidth(), this.kvmInterface.getBase().getImageHeight(), this.cm, this.pixels, 0, this.kvmInterface.getBase().getImageWidth());
    this.image = createImage(this.source);
    this.source.setAnimated(true);
    this.biSrc = new BufferedImage(1920, 1200, 13, (IndexColorModel)this.cm);
    this.big = this.biSrc.createGraphics();
    this.big.drawImage(this.image, 0, 0, this);
    this.bi = this.biSrc;
    this.biDest = new BufferedImage(1920, 1200, 13, (IndexColorModel)this.cm);
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
    imaPane.transform.setToScale(Base.getScreenSize().getWidth() / 4.0D / imaPane.width, Base.getScreenSize()
        .getHeight() / 4.0D / imaPane.height);
    imaPane.applyFilter();
  }
  public void setNewImages(Object pix, int width, int height) {
    synchronized (this.lock) {
      this.images = pix;
      this.width = width;
      this.height = height;
      if (null == createImage(width, height)) {
        TestPrint.println(3, "createImage error");
        return;
      } 
      Image copyImage = createImage(width, height);
      try {
        Graphics g = copyImage.getGraphics();
        Graphics2D g2D = (Graphics2D)g;
        Shape shape = g2D.getClip();
        ImageBlock[] blocks = (ImageBlock[])this.images;
        for (int i = 0; blocks != null && i < blocks.length; i++) {
          ImageBlock block = blocks[i];
          if (block != null)
          {
            if (block.isAboveSame()) {
              block.setAboveSame(false);
            }
            else {
              if (block.isFill())
              {
                g2D.clipRect(block.getX(), block.getY(), block.getCutWidth(), block.getCutHeight());
              }
              g2D.drawImage(block.getImage(), block.getX(), block.getY(), 64, 64, this);
              if (block.isFill())
              {
                g2D.setClip(shape);
              }
            } 
          }
        } 
        this.image = copyImage;
        this.width = width;
        this.height = height;
        zoomState(width, height);
      }
      catch (Exception e) {
        TestPrint.println(3, "catch copy image error");
        LoggerUtil.error(e.getClass().getName());
      } 
      repaint();
    } 
  }
  public ImagePane(KVMInterface kvmInterface2) { this.lock = new Object(); this.kvmInterface = kvmInterface2; try {
      this.robot = new Robot();
    } catch (AWTException e) {
      LoggerUtil.error(e.getClass().getName());
    }  jbInit(); } protected void setImages(Object pix) { synchronized (this.lock) {
      this.images = pix;
      repaint();
    }  }
  public void setImage(byte[] pix) {
    if (this.resolutionCh) {
      this.pixels = new byte[pix.length];
      System.arraycopy(pix, 0, this.pixels, 0, this.pixels.length);
      DirectColorModel directColorModel = new DirectColorModel(24, 7, 56, 192);
      this.cm = directColorModel;
      this.source = new MemoryImageSource(this.width, this.height, this.cm, this.pixels, 0, this.width);
      this.image = createImage(this.source);
      this.source.setAnimated(true);
      this.source.setFullBufferUpdates(true);
      this.resolutionCh = false;
    }
    else {
      if (this.pixels.length == pix.length) {
        System.arraycopy(pix, 0, this.pixels, 0, this.pixels.length);
      } else {
        return;
      } 
      this.source.newPixels(pix, this.cm, 0, this.width);
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
  public void zoomState(int width, int height) {
    int tempX = 0;
    int tempY = 0;
    if (this.kvmInterface.isFullScreen()) {
      tempX = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2;
      tempY = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2;
      if (tempX < 0) {
        tempX = 0;
        width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      } 
      if (tempY < 0) {
        tempY = 0;
        height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
      } 
      this.image = this.image.getScaledInstance(width, height, 2);
    }
    else {
      width = Base.getAppletWidth();
      height = Base.getAppletHeight();
      this.image = this.image.getScaledInstance(width, height, 2);
    } 
    setSize(new Dimension(width, height));
    setPreferredSize(new Dimension(width, height));
    setLocation(tempX, tempY);
  }
  public void paintComponent22(Graphics g) {
    super.paintComponent(g);
    if (g instanceof Graphics2D) {
      Graphics2D g2D = (Graphics2D)g;
      g2D.drawImage(this.image, 0, 0, (ImageObserver)null);
    } 
    this.show = 0;
  }
  public void paintComponent(Graphics g) {
    synchronized (this.lock) {
      if (!Base.getIsNewCompAlgorithm()) {
        super.paintComponent(g);
        if (g instanceof Graphics2D)
        {
          Graphics2D g2D = (Graphics2D)g;
          g2D.drawImage(this.image, 0, 0, (ImageObserver)null);
        }
      } else {
        super.paintComponent(g);
        if (g instanceof Graphics2D) {
          Graphics2D g2D = (Graphics2D)g;
          Shape shape = g2D.getClip();
          ImageBlock[] blocks = (ImageBlock[])this.images;
          for (int i = 0; blocks != null && i < blocks.length; i++) {
            ImageBlock block = blocks[i];
            if (block != null)
            {
              if (block.isAboveSame()) {
                block.setAboveSame(false);
              }
              else {
                if (block.isFill())
                {
                  g2D.clipRect(block.getX(), block.getY(), block.getCutWidth(), block.getCutHeight());
                }
                g2D.drawImage(block.getImage(), block.getX(), block.getY(), 64, 64, this);
                if (block.isFill())
                {
                  g2D.setClip(shape);
                }
              } 
            }
          } 
        } 
      } 
      this.show = 0;
    } 
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
    if (!this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY())) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      this.actionFlage = true;
      this.ismove = true;
      byte[] bytes = null;
      if (!this.isNew) {
        bytes = this.pack.mousePack(this.posiX - getImagePaneX(), this.posiY - getImagePaneY(), this.bladeNO);
        this.bladeThread.getBladeCommu().sentData(bytes);
      } 
      this.ifmove = false;
    } 
    if (this.isNew && (KVMUtil.isLinuxOS() || this.kvmInterface.getBase().isMstsc()) && 
      judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && !Base.getIsSynMouse()) {
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
        this.remotemstscY = paney;
        this.remotemstscX = panex;
      }
    }
    else if (this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && Base.getIsSynMouse()) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      byte[] bytes = null;
      bytes = this.pack.mousePackNew_abs(this.posiX, this.posiY, this.bladeNO);
      this.bladeThread.getBladeCommu().sentData(bytes);
    } 
  }
  public void mouseMoved(MouseEvent e) {
    if (KVMUtil.isWindowsOS()) {
      if (this.kvmInterface.isFullScreen() && this.isNew && e
        .getY() <= this.kvmInterface.getFullScreen().getV() && e
        .getX() >= this.kvmInterface.getFullScreen().getToolBarFrame().getX() + this.kvmInterface
        .getFullScreen().getH() && e.getX() <= this.kvmInterface.getFullScreen()
        .getToolBarFrame()
        .getX() + this.kvmInterface
        .getFullScreen().getH() + this.kvmInterface.getFullScreen().getToolBarFrame().getWidth() && 
        !this.kvmInterface.getFullScreen().getToolBarFrame().isShowing())
      {
        this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
        setCursor(this.kvmInterface.getBase().getDefCursor());
        MouseDisplacementImpl.setMode(0);
        this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
      }
    }
    else if (KVMUtil.isLinuxOS()) {
      if (this.kvmInterface.isFullScreen() && this.isNew && e
        .getY() <= this.kvmInterface.getFullScreen().getV() + 21 && e
        .getX() >= this.kvmInterface.getFullScreen().getToolBarFrame().getX() + this.kvmInterface
        .getFullScreen().getH() && e.getX() <= this.kvmInterface.getFullScreen()
        .getToolBarFrame()
        .getX() + this.kvmInterface
        .getFullScreen().getH() + this.kvmInterface.getFullScreen().getToolBarFrame().getWidth() && 
        !this.kvmInterface.getFullScreen().getToolBarFrame().isShowing()) {
        this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
        setCursor(this.kvmInterface.getBase().getDefCursor());
        MouseDisplacementImpl.setMode(0);
        this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
      } 
    } 
    if (!this.kvmInterface.isFullScreen() && (null == this.kvmInterface
      .getToolbar().getFrmFr() || this.kvmInterface.getImageFile().isShowing()))
    {
      if (!this.kvmInterface.getFloatToolbar().isShowPanel())
      {
        if (e.getY() >= this.kvmInterface.getVv() && e
          .getY() <= 32 + this.kvmInterface.getVv() + 6 && e
          .getX() >= this.kvmInterface.getFloatToolbar().getX() + this.kvmInterface.getH() && e
          .getX() <= this.kvmInterface.getFloatToolbar().getWidth() + this.kvmInterface.getH() + this.kvmInterface
          .getFloatToolbar().getX()) {
          if (e.getY() >= this.kvmInterface.getVv() && e
            .getY() <= 6 + this.kvmInterface.getVv() && e
            .getX() >= this.kvmInterface.getFloatToolbar().getX() + this.kvmInterface.getH() && e
            .getX() <= this.kvmInterface.getFloatToolbar().getWidth() + this.kvmInterface.getH() + this.kvmInterface
            .getFloatToolbar().getX()) {
            if (this.hideTimer != null) {
              this.hideTimer.cancel();
              this.hideTimer = null;
            } 
            if (this.showTimer == null) {
              this.showTimer = new Timer();
              this.showTimer.schedule(new ShowToolBarTask(this.kvmInterface), 500L);
            } 
          } 
          if (this.kvmInterface.getFloatToolbar().isVisible() && e
            .getY() >= this.kvmInterface.getVv() + 32 && e
            .getY() <= 32 + this.kvmInterface.getVv() + 6 && e
            .getX() >= this.kvmInterface.getFloatToolbar().getX() + this.kvmInterface.getH() && e
            .getX() <= this.kvmInterface.getFloatToolbar().getWidth() + this.kvmInterface.getH() + this.kvmInterface
            .getFloatToolbar().getX())
          {
            if (this.hideTimer != null) {
              this.hideTimer.cancel();
              this.hideTimer = null;
            } 
          }
        } else {
          if (this.showTimer != null) {
            this.showTimer.cancel();
            this.showTimer = null;
          } 
          if (this.hideTimer == null && this.kvmInterface.getFloatToolbar().isVisible()) {
            this.hideTimer = new Timer();
            this.hideTimer.schedule(new HideToolBarTask(this.kvmInterface), 1000L);
          } 
        } 
      }
    }
    if (!this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY())) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      this.actionFlage = true;
      this.ismove = true;
      if (this.ifmove) {
        byte[] bytes = null;
        bytes = this.pack.mousePack(this.posiX - getImagePaneX(), this.posiY - getImagePaneY(), this.bladeNO);
        this.bladeThread.getBladeCommu().sentData(bytes);
        this.ifmove = false;
      } 
    } 
    if (this.isNew && (KVMUtil.isLinuxOS() || this.kvmInterface.getBase().isMstsc()) && 
      judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && !Base.getIsSynMouse()) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      int panex = this.posiX - getImagePaneX();
      int paney = this.posiY - getImagePaneY();
      if (this.remotemstscX != panex || this.remotemstscY != this.posiY)
      {
        int disY = paney - this.remotemstscY;
        int disX = panex - this.remotemstscX;
        if (disX != 0 || disY != 0)
        {
          sentMstscMouse(disX, disY);
        }
        this.remotemstscX = panex;
        this.remotemstscY = paney;
      }
    }
    else if (this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()) && Base.getIsSynMouse()) {
      this.posiX = e.getX();
      this.posiY = e.getY();
      byte[] bytes = null;
      bytes = this.pack.mousePackNew_abs(this.posiX, this.posiY, this.bladeNO);
      this.bladeThread.getBladeCommu().sentData(bytes);
    } 
    if (this.lostEndRow) {
      if (this.kvmInterface.getBladeSize() != 1)
      {
        this.kvmInterface.getTabbedpane().setPreferredSize(new Dimension(this.width + 2, this.height + 29));
      }
      this.lostEndRow = false;
    } 
  }
  public void mouseClicked(MouseEvent e) {
    this.isFocusChange = true;
    if (Base.isSingleMouse()) {
      setCursor((this.kvmInterface.getBase()).myCursor);
      this.kvmInterface.setCursor((this.kvmInterface.getBase()).myCursor);
    } 
  }
  public void mouseEntered(MouseEvent e) {
    this.posiX = e.getX();
    this.posiY = e.getY();
    byte[] mousData = this.pack.getMousData();
    switch (e.getModifiers()) {
      case 0:
        mousData[0] = 0;
        break;
      case 16:
        mousData[0] = (byte)(mousData[0] & 0x1);
        break;
      case 8:
        mousData[0] = (byte)(mousData[0] & 0x4);
        break;
      case 4:
        mousData[0] = (byte)(mousData[0] & 0x2);
        break;
      case 24:
        mousData[0] = (byte)(mousData[0] & 0x5);
        break;
      case 28:
        mousData[0] = (byte)(mousData[0] & 0x7);
        break;
      case 20:
        mousData[0] = (byte)(mousData[0] & 0x3);
        break;
      case 12:
        mousData[0] = (byte)(mousData[0] & 0x6);
        break;
      default:
        Debug.println("error command"); break;
    } 
    this.pack.setMousData(mousData);
    if (!this.isNew) {
      this.bladeThread.getBladeCommu().sentData(this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO));
    }
    else if ((KVMUtil.isLinuxOS() || this.kvmInterface.getBase().isMstsc()) && this.isFocusChange && !Base.getIsSynMouse()) {
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
    if (this.isFocusChange && Base.isSingleMouse()) {
      setCursor((this.kvmInterface.getBase()).myCursor);
      this.kvmInterface.setCursor((this.kvmInterface.getBase()).myCursor);
    } 
  }
  public void mouseExited(MouseEvent e) {
    if (!this.kvmInterface.isFullScreen() && this.isFocusChange && Base.isSingleMouse()) {
      int hx = (this.kvmInterface.getLocationOnScreen()).x + this.kvmInterface.getWidth() / 2;
      int hy = (this.kvmInterface.getLocationOnScreen()).y + this.kvmInterface.getHeight() / 2;
      this.robot.mouseMove(hx, hy);
      try {
        Thread.sleep(300L);
      }
      catch (InterruptedException e1) {
        LoggerUtil.error(e1.getClass().getName());
      } 
    } 
  }
  public void mousePressed(MouseEvent e) {
    this.isFocusChange = true;
    if (this.kvmInterface.isFullScreen() && this.kvmInterface.getFullScreen().getToolBarFrame().isShowing() && this.isNew)
    {
      if (this.kvmInterface.getBase().isMstsc()) {
        this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
        setCursor(this.kvmInterface.getBase().getDefCursor());
        if (!this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
          this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
        }
        else if ((null != this.kvmInterface.getFloatToolbar().getFlpPanel() && this.kvmInterface
          .getFullScreen().getCdMenu().isShowing()) || this.kvmInterface
          .getFullScreen().getFlpMenu().isShowing()) {
          if (KVMUtil.isWindowsOS())
          {
            this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
          }
          else if (KVMUtil.isLinuxOS())
          {
            this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
            this.kvmInterface.getFullScreen().getCdMenu().setVisible(false);
            this.kvmInterface.getFullScreen().getFlpMenu().setVisible(false);
            this.kvmInterface.getFullScreen().getToolBar().getBtnCDMenu().setBorder((Border)null);
            this.kvmInterface.getFullScreen().getToolBar().getBtnFlpMenu().setBorder((Border)null);
            this.kvmInterface.getFullScreen().getImageMenu().setVisible(false);
            this.kvmInterface.getFullScreen().getToolBar().getBtnCreateImage().setBorder((Border)null);
          }
        } else {
          this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
        }
      }
      else {
        this.kvmInterface.getFullScreen().setCursor((this.kvmInterface.getBase()).myCursor);
        setCursor((this.kvmInterface.getBase()).myCursor);
        if (!this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
          this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
        }
        else if (this.kvmInterface.getFullScreen().getCdMenu().isShowing() || this.kvmInterface
          .getFullScreen().getFlpMenu().isShowing() || this.kvmInterface
          .getFullScreen().getImageMenu().isShowing()) {
          if (KVMUtil.isWindowsOS())
          {
            this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
          }
          else if (KVMUtil.isLinuxOS())
          {
            this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
            this.kvmInterface.getFullScreen().getCdMenu().setVisible(false);
            this.kvmInterface.getFullScreen().getFlpMenu().setVisible(false);
            this.kvmInterface.getFullScreen().getToolBar().getBtnCDMenu().setBorder((Border)null);
            this.kvmInterface.getFullScreen().getToolBar().getBtnFlpMenu().setBorder((Border)null);
          }
        } else {
          this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
        } 
        MouseDisplacementImpl.setMode(1);
      } 
    }
    if (this.kvmInterface.isFullScreen() && this.kvmInterface.getFullScreen().getToolBarFrame().isShowing() && !this.isNew) {
      this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
      setCursor(this.kvmInterface.getBase().getDefCursor());
      if (!this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
      }
      else if ((null != this.kvmInterface.getFloatToolbar().getFlpPanel() && this.kvmInterface
        .getFullScreen().getCdMenu().isShowing()) || this.kvmInterface
        .getFullScreen().getFlpMenu().isShowing()) {
        this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
      }
      else {
        this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
      } 
    } 
    if (this.isControl) {
      requestFocus();
      if (!this.isNew) {
        if (judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
        {
          this.pack.mousePressedPack(e);
          byte[] bytes = this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO);
          this.bladeThread.getBladeCommu().sentData(bytes);
          this.ifmove = false;
        }
      }
      else if ((KVMUtil.isLinuxOS() || this.kvmInterface.getBase().isMstsc()) && !Base.getIsSynMouse()) {
        if (judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
        {
          this.pack.mousePressedPack(e);
          this.bladeThread.getBladeCommu().sentData(this.pack.mousePackNew((byte)0, (byte)0, this.bladeNO));
        }
      } else {
        if (this.kvmInterface.isFullScreen())
        {
          MouseDisplacementImpl.setMode(1);
        }
        if (Base.getIsSynMouse()) {
          this.pack.mousePressedPack(e);
          this.bladeThread.getBladeCommu().sentData(this.pack.mousePackNew_abs(e.getX(), e.getY(), this.bladeNO));
        } 
      } 
    } 
    if (!this.kvmInterface.isFullScreen()) {
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        if (this.kvmInterface.getFloatToolbar().getFlpPanel().isShowing())
        {
          if (e.getX() <= this.kvmInterface.getFloatToolbar().getFlpX() || e
            .getX() >= this.kvmInterface.getFloatToolbar().getFlpWidth() + this.kvmInterface
            .getFloatToolbar().getFlpX() || e
            .getY() <= this.kvmInterface.getFloatToolbar().getFlpY() || e.getY() >= this.kvmInterface.getFloatToolbar()
            .getFlpY() + this.kvmInterface
            .getFloatToolbar().getFlpHeight()) {
            this.kvmInterface.getFloatToolbar().setFlpVisible(false);
            this.kvmInterface.getFloatToolbar().setShowingFlp(false);
            this.kvmInterface.getFloatToolbar().getBtnFlpMenu().setBorder((Border)null);
          } 
        }
        if (this.kvmInterface.getFloatToolbar().getCDPanel().isShowing())
        {
          if (e.getX() <= this.kvmInterface.getFloatToolbar().getCDX() || e
            .getX() >= this.kvmInterface.getFloatToolbar().getCDWidth() + this.kvmInterface
            .getFloatToolbar().getCDX() || e
            .getY() <= this.kvmInterface.getFloatToolbar().getCDY() || e.getY() >= this.kvmInterface.getFloatToolbar()
            .getCDY() + this.kvmInterface
            .getFloatToolbar().getCDHeight()) {
            this.kvmInterface.getFloatToolbar().setCDVisible(false);
            this.kvmInterface.getFloatToolbar().setShowingCD(false);
            this.kvmInterface.getFloatToolbar().getBtnCDMenu().setBorder((Border)null);
          } 
        }
      } 
      if (this.kvmInterface.getBladeSize() == 1 && this.kvmInterface.getImageFile().isShowing())
      {
        if (e.getX() <= this.kvmInterface.getImageFile().getX() || e
          .getX() >= this.kvmInterface.getImageFile().getWidth() + this.kvmInterface.getImageFile().getX() || e
          .getY() <= this.kvmInterface.getImageFile().getY() || e.getY() >= this.kvmInterface.getImageFile().getY() + this.kvmInterface
          .getImageFile().getHeight())
        {
          this.kvmInterface.getImageFile().setVisible(false);
          this.kvmInterface.getFloatToolbar().setShowingImagep(false);
          this.kvmInterface.getFloatToolbar().getBtnCreateImage().setBorder((Border)null);
        }
      }
    } else {
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        if (this.kvmInterface.getFloatToolbar().getCDPanel().isShowing()) {
          if (e.getX() <= this.kvmInterface.getFloatToolbar().getCDX() || e
            .getX() >= this.kvmInterface.getFloatToolbar().getCDWidth() + this.kvmInterface
            .getFloatToolbar().getCDX() || e
            .getY() <= this.kvmInterface.getFloatToolbar().getCDY() || e.getY() >= this.kvmInterface.getFloatToolbar()
            .getCDY() + this.kvmInterface
            .getFloatToolbar().getCDHeight()) {
            this.kvmInterface.getFullScreen().getCdMenu().setVisible(false);
            this.kvmInterface.getFloatToolbar().setShowingCD(false);
            this.kvmInterface.getFullScreen().getToolBar().getBtnCDMenu().setBorder((Border)null);
          } 
          if (this.kvmInterface.getFloatToolbar().getCDPanel().isShowing())
          {
            this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
          }
        } 
        if (this.kvmInterface.getFloatToolbar().getFlpPanel().isShowing()) {
          if (e.getX() <= this.kvmInterface.getFloatToolbar().getFlpX() || e
            .getX() >= this.kvmInterface.getFloatToolbar().getFlpWidth() + this.kvmInterface
            .getFloatToolbar().getFlpX() || e
            .getY() <= this.kvmInterface.getFloatToolbar().getFlpY() || e.getY() >= this.kvmInterface.getFloatToolbar()
            .getFlpY() + this.kvmInterface
            .getFloatToolbar().getFlpHeight()) {
            this.kvmInterface.getFullScreen().getFlpMenu().setVisible(false);
            this.kvmInterface.getFloatToolbar().setShowingFlp(false);
            this.kvmInterface.getFullScreen().getToolBar().getBtnFlpMenu().setBorder((Border)null);
          } 
          if (this.kvmInterface.getFloatToolbar().getFlpPanel().isShowing())
          {
            this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
          }
        } 
      } 
      if (this.kvmInterface.getImageFile().isShowing()) {
        if (e.getX() <= this.kvmInterface.getImageFile().getX() || e
          .getX() >= this.kvmInterface.getImageFile().getWidth() + this.kvmInterface.getImageFile().getX() || e
          .getY() <= this.kvmInterface.getImageFile().getY() || e.getY() >= this.kvmInterface.getImageFile().getY() + this.kvmInterface
          .getImageFile().getHeight()) {
          this.kvmInterface.getFullScreen().getImageMenu().setVisible(false);
          this.kvmInterface.getFloatToolbar().setShowingImagep(false);
          this.kvmInterface.getFullScreen().getToolBar().getBtnCreateImage().setBorder((Border)null);
        } 
        if (this.kvmInterface.getImageFile().isShowing())
        {
          this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
        }
      } 
    } 
    if (this.kvmInterface.isFullScreen() && null != this.kvmInterface.getFloatToolbar().getHelpFrm())
    {
      if (this.kvmInterface.getFloatToolbar().getHelpFrm().isShowing())
      {
        this.kvmInterface.getFloatToolbar().getHelpFrm().setVisible(false);
      }
    }
  }
  public void mouseReleased(MouseEvent e) {
    if (!this.isNew) {
      if (this.isControl && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
      {
        this.pack.mouseReleasedPack(e);
        byte[] bytes = this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO);
        this.bladeThread.getBladeCommu().sentData(bytes);
        this.ifmove = false;
      }
    }
    else if ((KVMUtil.isLinuxOS() || this.kvmInterface.getBase().isMstsc()) && !Base.getIsSynMouse()) {
      if (judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY()))
      {
        this.pack.mouseReleasedPack(e);
        this.bladeThread.getBladeCommu().sentData(this.pack.mousePackNew((byte)0, (byte)0, this.bladeNO));
      }
    } else if (Base.getIsSynMouse()) {
      this.pack.mouseReleasedPack(e);
      this.bladeThread.getBladeCommu().sentData(this.pack.mousePackNew_abs(e.getX(), e.getY(), this.bladeNO));
    } 
  }
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (this.isNew && judgeReflact(getImagePaneX(), getImagePaneY(), e.getX(), e.getY())) {
      byte[] bytes = this.pack.mousePack(this.remoteX, this.remoteY, this.bladeNO);
      this.bladeThread.getBladeCommu().sentData(bytes);
      this.ifmove = false;
    } 
  }
  protected int getImagePaneY() {
    return 0;
  }
  protected int getImagePaneX() {
    return 0;
  }
  public boolean isContr() {
    return this.isControl;
  }
  public void setControl(boolean isControl) {
    this.isControl = isControl;
  }
  public void setShowtoolBar(boolean showtoolBar) {
    this.showtoolBar = showtoolBar;
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
    if (null != pixels) {
      this.pixels = (byte[])pixels.clone();
    }
    else {
      this.pixels = null;
    } 
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
  private void sentMstscMouse(int disX, int disY) {
    for (int j = 0; j < 16; j++) {
      this.arry[j] = 0; this.arrx[j] = 0;
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
      this.bladeThread.getBladeCommu().sentData(this.pack.mousePackNew((byte)this.arrx[i], (byte)this.arry[i], this.bladeNO));
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
    if (this.kvmInterface.getBase().getHookNum() == 0)
    {
      this.kvmInterface.getBase().setHookNum(MouseDisplacementImpl.installHook());
    }
    this.kvmInterface.getKvmUtil().setiWindosFocus(1);
    this.kvmInterface.setiImageFocused(1);
    this.kvmInterface.setiKeyPressControl(0);
    this.kvmInterface.setiKeyPressTotal(0);
    ImagePane imagePane = this.kvmInterface.getKvmUtil().getImagePane(this.kvmInterface.getActionBlade());
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)-112) == imagePane.getNum() && 
      MouseDisplacementImpl.getKeyBoardStatus((byte)20) == imagePane.getCaps() && 
      MouseDisplacementImpl.getKeyBoardStatus((byte)-111) == imagePane.getScroll()) {
      this.kvmInterface.setiImageFocused(2);
      return;
    } 
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)-112) != imagePane.getNum()) {
      this.kvmInterface.setiKeyPressTotal(this.kvmInterface.getiKeyPressTotal() + 1);
      MouseDisplacementImpl.setKeyBoardStatus((imagePane.getNum() == 1), (byte)-112);
    } 
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)20) != imagePane.getCaps()) {
      this.kvmInterface.setiKeyPressTotal(this.kvmInterface.getiKeyPressTotal() + 1);
      MouseDisplacementImpl.setKeyBoardStatus((imagePane.getCaps() == 1), (byte)20);
    } 
    if (MouseDisplacementImpl.getKeyBoardStatus((byte)-111) != imagePane.getScroll()) {
      this.kvmInterface.setiKeyPressTotal(this.kvmInterface.getiKeyPressTotal() + 1);
      MouseDisplacementImpl.setKeyBoardStatus((imagePane.getScroll() == 1), (byte)-111);
    } 
  }
  public void focusLost(FocusEvent e) {
    this.kvmInterface.setiImageFocused(0);
    this.kvmInterface.setiKeyPressControl(0);
    this.kvmInterface.setiKeyPressTotal(0);
    this.kvmInterface.getKvmUtil().setiWindosFocus(0);
    if (this.kvmInterface.getBase().getHookNum() != 0) {
      MouseDisplacementImpl.removeHook(this.kvmInterface.getBase().getHookNum());
      this.kvmInterface.getBase().setHookNum(0);
    } 
  }
  public BladeThread getBladeThread() {
    return this.bladeThread;
  }
}
