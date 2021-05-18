package com.kvm;
import com.library.LibException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AdjustmentListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
public class FullScreen
  extends JDialog
{
  private static final long serialVersionUID = 1L;
  private int locate = 0;
  private ImagePane imagePane = null;
  public ImagePane getImagePane() {
    return this.imagePane;
  }
  public void setImagePane(ImagePane imagePane) {
    this.imagePane = imagePane;
  }
  private JPanel imageParentPane = new JPanel();
  private JScrollPane imageParentScrollPane;
  private FullToolBar toolBar;
  private JPanel toolBarFrame;
  public JPanel getImageParentPane() {
    return this.imageParentPane;
  }
  public void setImageParentPane(JPanel imageParentPane) {
    this.imageParentPane = imageParentPane;
  }
  public JScrollPane getImageParentScrollPane() {
    return this.imageParentScrollPane;
  }
  public void setImageParentScrollPane(JScrollPane imageParentScrollPane) {
    this.imageParentScrollPane = imageParentScrollPane;
  }
  public FullToolBar getToolBar() {
    return this.toolBar;
  }
  public void setToolBarParam(FullToolBar toolBar) {
    this.toolBar = toolBar;
  }
  public JPanel getToolBarFrame() {
    return this.toolBarFrame;
  }
  public void setToolBarFrame(JPanel toolBarFrame) {
    this.toolBarFrame = toolBarFrame;
  }
  private int actionBlade = 0;
  public int getActionBlade() {
    return this.actionBlade;
  }
  public void setActionBlade(int actionBlade) {
    this.actionBlade = actionBlade;
  }
  private transient KVMInterface kvmInterface = null;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
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
  private int newh = 0; private JDialog imageMenu; private JDialog cdMenu; private JDialog flpMenu; private JDialog powerPanelDialog; private transient AdjustmentListener vlistener;
  private transient AdjustmentListener hlistener;
  public int getNewh() {
    return this.newh;
  }
  public void setNewh(int newh) {
    this.newh = newh;
  }
  public JDialog getImageMenu() {
    return this.imageMenu;
  }
  public void setImageMenu(JDialog imageMenu) {
    this.imageMenu = imageMenu;
  }
  public JDialog getCdMenu() {
    return this.cdMenu;
  }
  public void setCdMenu(JDialog cdMenu) {
    this.cdMenu = cdMenu;
  }
  public JDialog getFlpMenu() {
    return this.flpMenu;
  }
  public void setFlpMenu(JDialog flpMenu) {
    this.flpMenu = flpMenu;
  }
  public JDialog getPowerPanelDialog() {
    return this.powerPanelDialog;
  }
  public void setPowerPanelDialog(JDialog powerPanelDialog) {
    this.powerPanelDialog = powerPanelDialog;
  }
  public FullScreen(JDialog dialog, ImagePane imagePane, KVMInterface kvmInterface2) {
    super(dialog, true);
    this.vlistener = new VListener(this);
    this.hlistener = new HListener(this); this.kvmInterface = kvmInterface2; this.imagePane = imagePane; this.imageParentPane.setLayout(new BorderLayout()); this.actionBlade = imagePane.getBladeNumber(); imagePane.setShowtoolBar(true);
    this.imageParentPane.setPreferredSize(new Dimension(imagePane.getImagePaneWidth(), imagePane.getImagePaneHeight()));
    this.imageParentPane.add(imagePane);
    jbInit();
    addscrollBarListener(); } private void jbInit() { setModal(false);
    this.toolBarFrame = new JPanel();
    this.toolBarFrame.setOpaque(false);
    setDefaultCloseOperation(0);
    if (KVMUtil.isWindowsOS()) {
      setBounds(-1, -1, (Toolkit.getDefaultToolkit().getScreenSize()).width + 3, 
          (Toolkit.getDefaultToolkit().getScreenSize()).height + 3);
    }
    else if (KVMUtil.isLinuxOS()) {
      setBounds(-1, 20, (Toolkit.getDefaultToolkit().getScreenSize()).width + 3, 
          (Toolkit.getDefaultToolkit().getScreenSize()).height + 3);
    } 
    setToolBar(new FullToolBar(this));
    if (this.imagePane != null && this.imagePane.isNew()) {
      if (this.kvmInterface.getBase().isMstsc())
      {
        this.imagePane.setCursor(this.kvmInterface.getBase().getDefCursor());
        this.toolBar.getMouseSynButton().setToolTipText(this.kvmInterface.getKvmUtil().getString("MouseSyn.Tip"));
      }
      else
      {
        setCursor((this.kvmInterface.getBase()).myCursor);
        this.imagePane.setCursor((this.kvmInterface.getBase()).myCursor);
        MouseDisplacementImpl.setMode(1);
      }
    } else {
      if (this.imagePane != null)
      {
        this.imagePane.setCursor(this.kvmInterface.getBase().getDefCursor());
      }
      this.toolBar.getMouseSynButton().setToolTipText(this.kvmInterface.getKvmUtil().getString("MouseSyn.Tip"));
    } 
    if (this.imagePane != null) {
      MouseDisplacementImpl.setKeyBoardStatus((this.imagePane.getNum() == 1), (byte)-112);
      MouseDisplacementImpl.setKeyBoardStatus((this.imagePane.getCaps() == 1), (byte)20);
      MouseDisplacementImpl.setKeyBoardStatus((this.imagePane.getScroll() == 1), (byte)-111);
    } 
    this.imageParentScrollPane = new JScrollPane(this.imageParentPane);
    getContentPane().add(this.imageParentScrollPane, "Center");
    FullScreenKeyHandler keyListener = new FullScreenKeyHandler(this);
    addKeyListener(keyListener);
    setUndecorated(true);
    setVisible(true); }
   public void addscrollBarListener() {
    this.vbar = this.imageParentScrollPane.getVerticalScrollBar();
    this.vbar.addAdjustmentListener(this.vlistener);
    this.vbar.addMouseListener(new ScrollBarAdapter(this));
    this.hbar = this.imageParentScrollPane.getHorizontalScrollBar();
    this.hbar.addAdjustmentListener(this.hlistener);
    this.hbar.addMouseListener(new ScrollBarAdapter2(this));
  } public void setButtonEnable() {
    if (Base.getIsSynMouse()) {
      this.toolBar.getMouseSynButton().setEnabled(false);
      this.toolBar.getMouseModeButton().setEnabled(false);
      this.toolBar.getPowerMenu().getMouseModeSwitchMenu().setSelected(true);
    }
    else {
      this.toolBar.getMouseModeButton().setEnabled(true);
      this.toolBar.getPowerMenu().getMouseModeSwitchMenu().setSelected(false);
    } 
    if (Base.isSingleMouse()) {
      this.toolBar.getPowerMenu().getSingleMouseMenu().setSelected(true);
    }
    else {
      this.toolBar.getPowerMenu().getSingleMouseMenu().setSelected(false);
    } 
    if (Base.getIsSynMouse()) {
      (this.toolBar.getMouseMenu()).mouseModeSwitchMenu.setSelected(true);
    }
    else {
      (this.toolBar.getMouseMenu()).mouseModeSwitchMenu.setSelected(false);
    } 
    if (Base.isSingleMouse()) {
      (this.toolBar.getMouseMenu()).singleMouseMenu.setSelected(true);
    }
    else {
      (this.toolBar.getMouseMenu()).singleMouseMenu.setSelected(false);
    } 
    if (Base.getKeyboardLayout() == 1) {
      (this.toolBar.getKeyboardLayoutMenu()).selfAdaptionMenu.setSelected(true);
      (this.toolBar.getKeyboardLayoutMenu()).japaneseKeyboardMenu.setSelected(false);
      (this.toolBar.getKeyboardLayoutMenu()).frenchKeyboardMenu.setSelected(false);
    }
    else if (Base.getKeyboardLayout() == 2) {
      (this.toolBar.getKeyboardLayoutMenu()).selfAdaptionMenu.setSelected(false);
      (this.toolBar.getKeyboardLayoutMenu()).japaneseKeyboardMenu.setSelected(true);
      (this.toolBar.getKeyboardLayoutMenu()).frenchKeyboardMenu.setSelected(false);
    }
    else {
      (this.toolBar.getKeyboardLayoutMenu()).selfAdaptionMenu.setSelected(false);
      (this.toolBar.getKeyboardLayoutMenu()).japaneseKeyboardMenu.setSelected(false);
      (this.toolBar.getKeyboardLayoutMenu()).frenchKeyboardMenu.setSelected(true);
    } 
    this.toolBar.getPowerMenu().setKineScopeDataCollect(this.kvmInterface.getFloatToolbar()
        .getPowerMenu()
        .getKineScopeDataCollect());
    if (null != this.toolBar.getPowerMenu().getKineScopeDataCollect()) {
      if (this.toolBar.getPowerMenu().getKineScopeDataCollect().isCollect())
      {
        (this.toolBar.getPowerMenu()).localKinescopeMenu.setText(this.kvmInterface.getKvmUtil().getString("Stop_KinScope"));
      }
      else
      {
        (this.toolBar.getPowerMenu()).localKinescopeMenu.setText(this.kvmInterface.getKvmUtil().getString("localKinescope"));
      }
    } else {
      (this.toolBar.getPowerMenu()).localKinescopeMenu.setText(this.kvmInterface.getKvmUtil().getString("localKinescope"));
    } 
    if (null != this.toolBar.getPowerMenu().getKineScopeDataCollect()) {
      if (this.toolBar.getPowerMenu().getKineScopeDataCollect().isCollect())
      {
        this.toolBar.getVideoButton().setIcon(new ImageIcon(FullScreen.class
              .getResource("resource/images/video_stop.png")));
        this.toolBar.getVideoButton().setToolTipText(this.kvmInterface.getKvmUtil().getString("Stop_KinScope"));
      }
      else
      {
        this.toolBar.getVideoButton().setIcon(new ImageIcon(FullScreen.class
              .getResource("resource/images/video_start.png")));
        this.toolBar.getVideoButton().setToolTipText(this.kvmInterface.getKvmUtil().getString("localKinescope"));
      }
    } else {
      this.toolBar.getVideoButton().setIcon(new ImageIcon(FullScreen.class
            .getResource("resource/images/video_start.png")));
      this.toolBar.getVideoButton().setToolTipText(this.kvmInterface.getKvmUtil().getString("localKinescope"));
    } 
    this.toolBar.getCombineKey().setEnabled(true);
    this.toolBar.getLabelnum().setVisible(true);
    this.toolBar.getLabelcaps().setVisible(true);
    this.toolBar.getLabelscroll().setVisible(true);
    this.toolBar.getNumColorButton().setVisible(true);
    this.toolBar.getCapsColorButton().setVisible(true);
    this.toolBar.getScrollColorButton().setVisible(true);
    this.toolBar.getLblcombodqtz().setVisible(true);
    this.toolBar.getBtnCreateImage().setVisible(true);
    if (KVMUtil.isLinuxOS())
    {
      this.toolBar.getBtnCreateImage().setEnabled(false);
    }
    if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
      this.toolBar.getBtnCDMenu().setVisible(true);
      this.toolBar.getBtnFlpMenu().setVisible(true);
    } 
    if (null != this.toolBar.getPowerMenuButton())
    {
      this.toolBar.getPowerMenuButton().setVisible(true);
    }
    this.toolBar.getHelpButton().setVisible(true);
  }
  public void setFlag(boolean b) {
    if (b) {
      this.toolBarFrame.setVisible(true);
      repaint();
      validate();
    }
    else {
      this.toolBarFrame.setVisible(false);
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        this.cdMenu.setVisible(false);
        this.flpMenu.setVisible(false);
      } 
      repaint();
      validate();
      if (this.kvmInterface.getKvmUtil().getImagePane(this.actionBlade).isContr()) {
        try {
          this.kvmInterface.getClient().sentData(this.kvmInterface.getKvmUtil()
              .getImagePane(this.actionBlade)
              .getPack()
              .clearKey(this.actionBlade));
        }
        catch (LibException ex) {
          if ("IO_ERRCODE".equals(ex.getErrCode()))
          {
            JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
                .getKvmUtil().getString("Network_interrupt_message"));
          }
        } 
      }
    } 
  }
  private void setToolBar(FullToolBar tool) {
    this.toolBar = tool;
    this.toolBar.setFloatable(false);
    this.toolBar.setOpaque(false);
    this.toolBarFrame.setLayout(new BorderLayout());
    if (this.kvmInterface.getBladeSize() == 1) {
      this.toolBarFrame.setSize(750, 34);
    }
    else if (KVMUtil.isWindowsOS()) {
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia())
      {
        this.toolBarFrame.setSize(540, 25);
      }
      else
      {
        this.toolBarFrame.setSize(490, 25);
      }
    }
    else if (KVMUtil.isLinuxOS()) {
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        this.toolBarFrame.setSize(590, 25);
      }
      else {
        this.toolBarFrame.setSize(540, 25);
      } 
    } 
    this.toolBarFrame.add(this.toolBar, "Center");
    this.toolBarFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.toolBarFrame.getWidth()) / 2.0D), this.locate);
    this.toolBar.startButtonState();
  }
  public void setToolLocate(int locatetion) {
    this.locate = locatetion;
  }
  public boolean showTool(int position) {
    boolean showFlag = false;
    if (this.locate == 0) {
      showFlag = (position <= 2);
    }
    else {
      showFlag = (position > 2);
    } 
    return showFlag;
  }
  public Action synMouseAction() {
    Action action = new SynMouseAction(this);
    return action;
  }
  public Action shortCutAction() {
    Action action = new ShortCutAction(this);
    return action;
  }
  protected void produceComKey(int bladeNO) {
    new CombinationKey(this, bladeNO, this.kvmInterface.getKvmUtil().getImagePane(bladeNO).getPack(), this.kvmInterface);
  }
  public Action mouseModeAction() {
    Action action = new FullScreenMouseModeAction(this);
    return action;
  }
  public Action returnAction() {
    Action action = new ReturnAction(this);
    return action;
  }
}
