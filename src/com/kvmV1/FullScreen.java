package com.kvmV1;
import com.KinescopeV1.KinescopeDataCollect;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
public class FullScreen
  extends JDialog
{
  private static final long serialVersionUID = 1L;
  private int locate = 0;
  public ImagePane imagePane = null;
  public JPanel imageParentPane = new JPanel();
  public JScrollPane imageParentScrollPane;
  public FullToolBar toolBar;
  public JPanel toolBarFrame;
  public int actionBlade = 0;
  public KVMInterface kvmInterface = null;
  public JScrollBar vbar = null;
  public JScrollBar hbar = null;
  public int v = 0;
  public int h = 0;
  public int newv = 0;
  public int newh = 0;
  public JDialog cdMenu;
  public JDialog flpMenu;
  public PowerPanel powerPanelDialog;
  public AdjustmentListener vlistener;
  public AdjustmentListener hlistener;
  public ActionListener action;
  public FullScreen(JDialog dialog, ImagePane imagePane, KVMInterface kvmInterface)
  {
    super(dialog, true);
    this.vlistener = new AdjustmentListener()
      {
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
          FullScreen.this.v = e.getValue();
          if (FullScreen.this.newv != FullScreen.this.v) {
            if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) {
              FullScreen.this.cdMenu.setVisible(false);
              FullScreen.this.flpMenu.setVisible(false);
            } 
            FullScreen.this.toolBarFrame.setVisible(false);
            if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) {
              FullScreen.this.toolBar.btnCDMenu.setBorder((Border)null);
              FullScreen.this.toolBar.btnFlpMenu.setBorder((Border)null);
            } 
          } 
          FullScreen.this.newv = FullScreen.this.v;
          if (null != FullScreen.this.toolBar && KVMUtil.isWindowsOS())
          {
            if (null != FullScreen.this.toolBar.powerMenu)
            {
              FullScreen.this.toolBar.powerMenu.setVisible(false);
            }
          }
        }
      };
    this.hlistener = new AdjustmentListener()
      {
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
          FullScreen.this.h = e.getValue();
          if (FullScreen.this.newh != FullScreen.this.h) {
            if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) {
              FullScreen.this.cdMenu.setVisible(false);
              FullScreen.this.flpMenu.setVisible(false);
            } 
            FullScreen.this.toolBarFrame.setVisible(false);
            if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) {
              FullScreen.this.toolBar.btnCDMenu.setBorder((Border)null);
              FullScreen.this.toolBar.btnFlpMenu.setBorder((Border)null);
            } 
          } 
          FullScreen.this.newh = FullScreen.this.h;
          if (null != FullScreen.this.toolBar && KVMUtil.isWindowsOS())
          {
            if (null != FullScreen.this.toolBar.powerMenu)
            {
              FullScreen.this.toolBar.powerMenu.setVisible(false);
            }
          }
        }
      };
    this.action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e)
        {
          String slotNO = ((String)FullScreen.this.toolBar.combo.getSelectedItem()).substring(5);
          int bladeNO = Integer.parseInt(slotNO);
          if (bladeNO != FullScreen.this.actionBlade)
          { 
            if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) {
              FullScreen.this.kvmInterface.floatToolbar.imagePanel.add(FullScreen.this.kvmInterface.floatToolbar.getFlpPanel());
              FullScreen.this.kvmInterface.floatToolbar.imagePanel.add(FullScreen.this.kvmInterface.floatToolbar.getCDPanel());
            } 
            FullScreen.this.kvmInterface.kvmUtil.setDrawDisplay(false);
            BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
            DrawThread drawThread = bladeThread.getDrawThread();
            (drawThread.getKvmUtil()).firstJudge = true;
            FullScreen.this.imageParentPane.removeAll();
            FullScreen.this.actionBlade = bladeNO;
            FullScreen.this.kvmInterface.actionBlade = bladeNO;
            if (FullScreen.this.kvmInterface.floatToolbar.getVirtualMedia() != null) {
              (FullScreen.this.kvmInterface.floatToolbar.getVirtualMedia()).cdp.setVisible(false);
              (FullScreen.this.kvmInterface.floatToolbar.getVirtualMedia()).flp.setVisible(false);
            } 
            ImagePane imaPane = FullScreen.this.kvmInterface.kvmUtil.getImagePane(bladeNO);
            FullScreen.this.kvmInterface.floatToolbar = FullScreen.this.kvmInterface.kvmUtil.getImageFloatToolbar(bladeNO);
            if (imaPane.kvmInterface.floatToolbar.isVirtualMedia()) {
              FullScreen.this.cdMenu.setVisible(false);
              FullScreen.this.flpMenu.setVisible(false);
              FullScreen.this.cdMenu.getContentPane().add((imaPane.kvmInterface.floatToolbar.getVirtualMedia()).cdp);
              FullScreen.this.flpMenu.getContentPane().add((imaPane.kvmInterface.floatToolbar.getVirtualMedia()).flp);
              FullScreen.this.toolBar.btnCDMenu.setBorder((Border)null);
              FullScreen.this.toolBar.btnFlpMenu.setBorder((Border)null);
            } 
            FullScreen.this.kvmInterface.floatToolbar.setVisible(false);
            imaPane.add(FullScreen.this.toolBarFrame);
            FullScreen.this.toolBarFrame.setVisible(false);
            if (bladeThread.isNew()) {
              if (FullScreen.this.kvmInterface.base.isMstsc)
              {
                FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.defCursor);
                imaPane.setCursor(FullScreen.this.kvmInterface.base.defCursor);
                FullScreen.this.toolBar.mouseSynButton.setEnabled(true);
              }
              else
              {
                FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.myCursor);
                imaPane.setCursor(FullScreen.this.kvmInterface.base.myCursor);
                FullScreen.this.toolBar.mouseSynButton.setEnabled(false);
              }
            } else {
              FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.defCursor);
              imaPane.setCursor(FullScreen.this.kvmInterface.base.defCursor);
              FullScreen.this.toolBar.mouseSynButton.setEnabled(true);
            } 
            bladeThread.kvmUtil.setDrawDisplay(false);
            bladeThread.kvmUtil.resetBuf();
            drawThread.lList.clear();
            drawThread.getComImage().clear();
            drawThread.isDisplay = true;
            bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.connectBlade(FullScreen.this.actionBlade, imaPane.custBit));
            bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.contrRate(35, bladeThread.getBladeNO()));
            FullScreen.this.imageParentPane.setPreferredSize(new Dimension(imaPane.width, imaPane.height));
            FullScreen.this.imageParentPane.add(imaPane);
            FullScreen.this.imageParentPane.setVisible(false);
            FullScreen.this.imageParentPane.setVisible(true);
            MouseDisplacementImpl.setKeyBoardStatus((imaPane.getNum() == 1), (byte)-112);
            MouseDisplacementImpl.setKeyBoardStatus((imaPane.getCaps() == 1), (byte)20);
            MouseDisplacementImpl.setKeyBoardStatus((imaPane.getScroll() == 1), (byte)-111); }  } }; this.kvmInterface = kvmInterface; this.imagePane = imagePane; this.imageParentPane.setLayout(new BorderLayout()); this.actionBlade = imagePane.bladeNO; this.imageParentPane.setPreferredSize(new Dimension(imagePane.width, imagePane.height)); this.imageParentPane.add(imagePane); jbInit(false); addscrollBarListener(); } public FullScreen(JDialog dialog, KVMInterface kvmInterface) { super(dialog, true); this.vlistener = new AdjustmentListener() { public void adjustmentValueChanged(AdjustmentEvent e) { FullScreen.this.v = e.getValue(); if (FullScreen.this.newv != FullScreen.this.v) { if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); }  FullScreen.this.toolBarFrame.setVisible(false); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.toolBar.btnCDMenu.setBorder((Border)null); FullScreen.this.toolBar.btnFlpMenu.setBorder((Border)null); }  }  FullScreen.this.newv = FullScreen.this.v; if (null != FullScreen.this.toolBar && KVMUtil.isWindowsOS()) if (null != FullScreen.this.toolBar.powerMenu) FullScreen.this.toolBar.powerMenu.setVisible(false);   } }; this.hlistener = new AdjustmentListener() { public void adjustmentValueChanged(AdjustmentEvent e) { FullScreen.this.h = e.getValue(); if (FullScreen.this.newh != FullScreen.this.h) { if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); }  FullScreen.this.toolBarFrame.setVisible(false); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.toolBar.btnCDMenu.setBorder((Border)null); FullScreen.this.toolBar.btnFlpMenu.setBorder((Border)null); }  }  FullScreen.this.newh = FullScreen.this.h; if (null != FullScreen.this.toolBar && KVMUtil.isWindowsOS()) if (null != FullScreen.this.toolBar.powerMenu) FullScreen.this.toolBar.powerMenu.setVisible(false);   } }; this.action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { String slotNO = ((String)FullScreen.this.toolBar.combo.getSelectedItem()).substring(5); int bladeNO = Integer.parseInt(slotNO); if (bladeNO != FullScreen.this.actionBlade) { if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.kvmInterface.floatToolbar.imagePanel.add(FullScreen.this.kvmInterface.floatToolbar.getFlpPanel()); FullScreen.this.kvmInterface.floatToolbar.imagePanel.add(FullScreen.this.kvmInterface.floatToolbar.getCDPanel()); }  FullScreen.this.kvmInterface.kvmUtil.setDrawDisplay(false); BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO)); DrawThread drawThread = bladeThread.getDrawThread(); (drawThread.getKvmUtil()).firstJudge = true; FullScreen.this.imageParentPane.removeAll(); FullScreen.this.actionBlade = bladeNO; FullScreen.this.kvmInterface.actionBlade = bladeNO; if (FullScreen.this.kvmInterface.floatToolbar.getVirtualMedia() != null) { (FullScreen.this.kvmInterface.floatToolbar.getVirtualMedia()).cdp.setVisible(false); (FullScreen.this.kvmInterface.floatToolbar.getVirtualMedia()).flp.setVisible(false); }  ImagePane imaPane = FullScreen.this.kvmInterface.kvmUtil.getImagePane(bladeNO); FullScreen.this.kvmInterface.floatToolbar = FullScreen.this.kvmInterface.kvmUtil.getImageFloatToolbar(bladeNO); if (imaPane.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); FullScreen.this.cdMenu.getContentPane().add((imaPane.kvmInterface.floatToolbar.getVirtualMedia()).cdp); FullScreen.this.flpMenu.getContentPane().add((imaPane.kvmInterface.floatToolbar.getVirtualMedia()).flp); FullScreen.this.toolBar.btnCDMenu.setBorder((Border)null); FullScreen.this.toolBar.btnFlpMenu.setBorder((Border)null); }  FullScreen.this.kvmInterface.floatToolbar.setVisible(false); imaPane.add(FullScreen.this.toolBarFrame); FullScreen.this.toolBarFrame.setVisible(false); if (bladeThread.isNew()) { if (FullScreen.this.kvmInterface.base.isMstsc) { FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.defCursor); imaPane.setCursor(FullScreen.this.kvmInterface.base.defCursor); FullScreen.this.toolBar.mouseSynButton.setEnabled(true); } else { FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.myCursor); imaPane.setCursor(FullScreen.this.kvmInterface.base.myCursor); FullScreen.this.toolBar.mouseSynButton.setEnabled(false); }  } else { FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.defCursor); imaPane.setCursor(FullScreen.this.kvmInterface.base.defCursor); FullScreen.this.toolBar.mouseSynButton.setEnabled(true); }  bladeThread.kvmUtil.setDrawDisplay(false); bladeThread.kvmUtil.resetBuf(); drawThread.lList.clear(); drawThread.getComImage().clear(); drawThread.isDisplay = true; bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.connectBlade(FullScreen.this.actionBlade, imaPane.custBit)); bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.contrRate(35, bladeThread.getBladeNO())); FullScreen.this.imageParentPane.setPreferredSize(new Dimension(imaPane.width, imaPane.height)); FullScreen.this.imageParentPane.add(imaPane); FullScreen.this.imageParentPane.setVisible(false); FullScreen.this.imageParentPane.setVisible(true); MouseDisplacementImpl.setKeyBoardStatus((imaPane.getNum() == 1), (byte)-112); MouseDisplacementImpl.setKeyBoardStatus((imaPane.getCaps() == 1), (byte)20); MouseDisplacementImpl.setKeyBoardStatus((imaPane.getScroll() == 1), (byte)-111); }  } }; this.imageParentPane.setLayout(new GridLayout(4, 4, 1, 1)); MouseHandler listener = new MouseHandler(); this.kvmInterface = kvmInterface; for (int i = 1; i <= 16; i++) { ImagePane imaPane = kvmInterface.kvmUtil.getImagePane(i); if (imaPane != null) { imaPane.setToolTipText("blade" + imaPane.bladeNO); imaPane.setCursor(new Cursor(0)); this.imageParentPane.add(imaPane); if (i != kvmInterface.actionBlade) { BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(String.valueOf(imaPane.bladeNO)); DrawThread drawThread = bladeThread.getDrawThread(); drawThread.setFirstJudge(true); drawThread.getComImage().clear(); drawThread.getKvmUtil().resetBuf(); (drawThread.getKvmUtil()).firstJudge = true; bladeThread.bladeCommu.sentData(kvmInterface.packData.connectBlade(imaPane.bladeNO, (kvmInterface.kvmUtil.getImagePane(imaPane.bladeNO)).custBit)); bladeThread.bladeCommu.sentData(kvmInterface.packData.contrRate(1, bladeThread.getBladeNO())); } else { BladeThread bladeThread = kvmInterface.base.threadGroup.get(String.valueOf(kvmInterface.actionBlade)); bladeThread.bladeCommu.sentData(kvmInterface.packData.contrRate(1, bladeThread.getBladeNO())); }  imaPane.divScreenIni(imaPane); imaPane.kvmInterface.floatToolbar.setVisible(false); if (imaPane.kvmInterface.floatToolbar.isVirtualMedia()) imaPane.kvmInterface.floatToolbar.setVirtualMediaVisible(false, false);  } else { JPanel panel = new JPanel(); panel.setBackground(Color.black); panel.setToolTipText(kvmInterface.kvmUtil.getString("NoSignal.Tip")); panel.addMouseListener(listener); this.imageParentPane.add(panel); }  }  jbInit(true); } public void addscrollBarListener() { this.vbar = this.imageParentScrollPane.getVerticalScrollBar(); this.vbar.addAdjustmentListener(this.vlistener); this.vbar.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { FullScreen.this.toolBarFrame.setVisible(false); FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); if (KVMUtil.isLinuxOS()) FullScreen.this.powerPanelDialog.setVisible(false);  } public void mouseClicked(MouseEvent e) { mousePressed(e); } }
      ); this.hbar = this.imageParentScrollPane.getHorizontalScrollBar(); this.hbar.addAdjustmentListener(this.hlistener); this.hbar.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { FullScreen.this.toolBarFrame.setVisible(false); FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); if (KVMUtil.isLinuxOS()) FullScreen.this.powerPanelDialog.setVisible(false);  } public void mouseClicked(MouseEvent e) { mousePressed(e); } }
      ); } private void jbInit(boolean isDiv) { setModal(false); this.toolBarFrame = new JPanel(); this.toolBarFrame.setOpaque(false); setDefaultCloseOperation(0); if (KVMUtil.isWindowsOS()) { setBounds(-1, -1, (Toolkit.getDefaultToolkit().getScreenSize()).width + 3, (Toolkit.getDefaultToolkit().getScreenSize()).height + 3); } else if (KVMUtil.isLinuxOS()) { setBounds(-1, 20, (Toolkit.getDefaultToolkit().getScreenSize()).width + 3, (Toolkit.getDefaultToolkit().getScreenSize()).height + 3); }  setToolBar(new FullToolBar()); this.kvmInterface.base.isDiv = isDiv; if (this.imagePane != null && this.imagePane.isNew()) { if (this.kvmInterface.base.isMstsc) { this.imagePane.setCursor(this.kvmInterface.base.defCursor); this.toolBar.mouseSynButton.setToolTipText(this.kvmInterface.kvmUtil.getString("MouseSyn.Tip")); } else { setCursor(this.kvmInterface.base.myCursor); this.imagePane.setCursor(this.kvmInterface.base.myCursor); MouseDisplacementImpl.setMode(1); }  } else { if (this.imagePane != null) this.imagePane.setCursor(this.kvmInterface.base.defCursor);  this.toolBar.mouseSynButton.setToolTipText(this.kvmInterface.kvmUtil.getString("MouseSyn.Tip")); }  if (this.imagePane != null) { MouseDisplacementImpl.setKeyBoardStatus((this.imagePane.getNum() == 1), (byte)-112); MouseDisplacementImpl.setKeyBoardStatus((this.imagePane.getCaps() == 1), (byte)20); MouseDisplacementImpl.setKeyBoardStatus((this.imagePane.getScroll() == 1), (byte)-111); }  if (!this.kvmInterface.base.isDiv) { this.imageParentScrollPane = new JScrollPane(this.imageParentPane); getContentPane().add(this.imageParentScrollPane, "Center"); } else { getContentPane().add(this.imageParentPane, "Center"); }  KeyHandler keyListener = new KeyHandler(); addKeyListener(keyListener); setUndecorated(true); setVisible(true); } public void setButtonEnable() { if (this.kvmInterface.base.isDiv) { this.toolBar.divButton.setEnabled(false); this.toolBar.mouseSynButton.setEnabled(false); this.toolBar.combineKey.setEnabled(false); this.toolBar.combo.setVisible(false); this.toolBar.labelnum.setVisible(false); this.toolBar.labelcaps.setVisible(false); this.toolBar.labelscroll.setVisible(false); this.toolBar.numColorButton.setVisible(false); this.toolBar.capsColorButton.setVisible(false); this.toolBar.scrollColorButton.setVisible(false); if (this.kvmInterface.floatToolbar.isVirtualMedia()) { this.toolBar.btnCDMenu.setVisible(false); this.toolBar.btnFlpMenu.setVisible(false); }  if (null != this.toolBar.powerMenuButton) this.toolBar.powerMenuButton.setVisible(false);  this.toolBar.helpButton.setVisible(false); if (this.kvmInterface.floatToolbar.isVirtualMedia()) { this.toolBar.btnCDMenu.setEnabled(false); this.toolBar.btnFlpMenu.setEnabled(false); }  if (null != this.toolBar.powerMenuButton) this.toolBar.powerMenuButton.setEnabled(false);  this.toolBar.helpButton.setEnabled(false); } else { this.toolBar.divButton.setEnabled(true); if (Base.isSynMouse) { this.toolBar.mouseSynButton.setEnabled(false); this.toolBar.mouseModeButton.setEnabled(false); if (null != this.toolBar && KVMUtil.isWindowsOS()) this.toolBar.powerMenu.mouseModeSwitchMenu.setSelected(true);  } else { this.toolBar.mouseModeButton.setEnabled(true); if (null != this.toolBar && KVMUtil.isWindowsOS()) this.toolBar.powerMenu.mouseModeSwitchMenu.setSelected(false);  }  if (null != this.toolBar && KVMUtil.isWindowsOS()) { if (Base.isSingleMouse) { this.toolBar.powerMenu.SingleMouseMenu.setSelected(true); } else { this.toolBar.powerMenu.SingleMouseMenu.setSelected(false); }  this.toolBar.powerMenu.kineScopeDataCollect = this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect; if (null != this.toolBar.powerMenu.kineScopeDataCollect) { if (this.toolBar.powerMenu.kineScopeDataCollect.isCollect()) { this.toolBar.powerMenu.localKinescopeMenu.setText(this.kvmInterface.kvmUtil.getString("Stop_KinScope")); } else { this.toolBar.powerMenu.localKinescopeMenu.setText(this.kvmInterface.kvmUtil.getString("localKinescope")); }  } else { this.toolBar.powerMenu.localKinescopeMenu.setText(this.kvmInterface.kvmUtil.getString("localKinescope")); }  }  this.toolBar.combineKey.setEnabled(true); this.toolBar.combo.setVisible(true); this.toolBar.labelnum.setVisible(true); this.toolBar.labelcaps.setVisible(true); this.toolBar.labelscroll.setVisible(true); this.toolBar.numColorButton.setVisible(true); this.toolBar.capsColorButton.setVisible(true); this.toolBar.scrollColorButton.setVisible(true); if (this.kvmInterface.floatToolbar.isVirtualMedia()) { this.toolBar.btnCDMenu.setVisible(true); this.toolBar.btnFlpMenu.setVisible(true); }  if (null != this.toolBar.powerMenuButton) this.toolBar.powerMenuButton.setVisible(true);  this.toolBar.helpButton.setVisible(true); }  } public void setFlag(boolean b) { if (b) { this.toolBarFrame.setVisible(true); repaint(); validate(); } else { this.toolBarFrame.setVisible(false); if (this.kvmInterface.floatToolbar.isVirtualMedia()) { this.cdMenu.setVisible(false); this.flpMenu.setVisible(false); }  repaint(); validate(); if (!this.kvmInterface.base.isDiv && this.kvmInterface.kvmUtil.getImagePane(this.actionBlade).isContr()) try { this.kvmInterface.client.sentData((this.kvmInterface.kvmUtil.getImagePane(this.actionBlade)).pack.clearKey(this.actionBlade)); } catch (KVMException ex) { if ("IO_ERRCODE".equals(ex.getErrCode())) JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Network_interrupt_message"));  }   }  } private void setToolBar(FullToolBar tool) { this.toolBar = tool; this.toolBar.setFloatable(false); this.toolBar.setOpaque(false); this.toolBarFrame.setLayout(new BorderLayout()); if (this.kvmInterface.getBladeSize() == 1) { this.toolBarFrame.setSize(425, 25); } else if (KVMUtil.isWindowsOS()) { if (this.kvmInterface.floatToolbar.isVirtualMedia()) { this.toolBarFrame.setSize(490, 25); } else { this.toolBarFrame.setSize(440, 25); }  } else if (KVMUtil.isLinuxOS()) { if (this.kvmInterface.floatToolbar.isVirtualMedia()) { this.toolBarFrame.setSize(540, 25); } else { this.toolBarFrame.setSize(490, 25); }  }  this.toolBarFrame.add(this.toolBar, "Center"); if (KVMUtil.isWindowsOS()) { this.toolBarFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.toolBarFrame.getWidth()) / 2.0D), this.locate); } else if (KVMUtil.isLinuxOS()) { this.toolBarFrame.setLocation((int)((Base.getScreenSize().getWidth() - this.toolBarFrame.getWidth()) / 2.0D) + 1, this.locate + 1); }  this.toolBar.startButtonState(); } public void setToolLocate(int locatetion) { this.locate = locatetion; } public boolean showTool(int position) { boolean showFlag = false; if (this.locate == 0) { showFlag = (position <= 2); } else { showFlag = (position > 2); }  return showFlag; } public class FullToolBar extends JToolBar {
    private static final long serialVersionUID = 1L; public JButton divButton; public JButton mouseSynButton; public JButton combineKey; public JButton mouseModeButton; public JButton returnButton; public JComboBox combo; public BackColorButton numColorButton; public BackColorButton capsColorButton; public BackColorButton scrollColorButton; public JButton btnCDMenu; public JButton btnFlpMenu; public Color numColor = Base.LIGHT_OFF; public Color capsColor = Base.LIGHT_OFF; public Color scrollColor = Base.LIGHT_OFF; public JLabel labelnum = new JLabel("num"); public JLabel labelcaps = new JLabel("caps"); public JLabel labelscroll = new JLabel("scroll"); public JButton helpButton; public JButton powerMenuButton; public PowerPopupMenu powerMenu; public JButton createPowerButton() { this.powerMenuButton = createButton("Power_Management", "resource/images/dev_com_power.gif"); if (KVMUtil.isWindowsOS()) { this.powerMenu = new PowerPopupMenu(FullScreen.this.kvmInterface); } else if (KVMUtil.isLinuxOS()) { createPowerPanel(); }  this.powerMenuButton.setBackground(new Color(158, 202, 232)); this.powerMenuButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { FullScreen.this.toolBarFrame.setVisible(true); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); FullScreen.FullToolBar.this.btnCDMenu.setBorder((Border)null); FullScreen.FullToolBar.this.btnFlpMenu.setBorder((Border)null); }  if (KVMUtil.isWindowsOS()) { FullScreen.FullToolBar.this.powerMenu.show(FullScreen.this.imageParentPane, FullScreen.FullToolBar.this.powerMenuButton.getX() + FullScreen.this.toolBarFrame.getX() + FullScreen.this.h, FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.v - 1); } else if (KVMUtil.isLinuxOS()) { if (FullScreen.this.cdMenu.isShowing()) FullScreen.this.cdMenu.setVisible(false);  if (FullScreen.this.flpMenu.isShowing()) FullScreen.this.flpMenu.setVisible(false);  if (FullScreen.this.powerPanelDialog.isShowing()) { FullScreen.this.powerPanelDialog.setVisible(false); } else { if (KVMUtil.isLinuxOS1()) { FullScreen.this.powerPanelDialog.setLocation(FullScreen.this.toolBarFrame.getX() + FullScreen.FullToolBar.this.powerMenuButton.getX(), FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.toolBarFrame.getY() + 23); } else { FullScreen.this.powerPanelDialog.setLocation(FullScreen.this.toolBarFrame.getX() + FullScreen.FullToolBar.this.powerMenuButton.getX(), FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.toolBarFrame.getY() + 21); }  FullScreen.this.powerPanelDialog.setVisible(true); FullScreen.this.powerPanelDialog.setAlwaysOnTop(true); }  }  } }
        ); return this.powerMenuButton; } public FullToolBar() { setRequestFocusEnabled(false); createDivButton(); createMouseSynButton(); createComKeyButton(); createMouseMode(); createReturnButton(); createJComboBox(); createNumColorButton(); createCapsColorButton(); createScrollColorButton(); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { createCDMenuButton(); createFlpMenuButton(); createCDMenu(); createFLPMenu(); }  createHelpButton(); if (KVMUtil.isAdmin()) createPowerButton();  setLayout((LayoutManager)null); } public JDialog createCDMenu() { if (FullScreen.this.cdMenu == null) { FullScreen.this.cdMenu = new JDialog(); FullScreen.this.cdMenu.setUndecorated(true); if (KVMUtil.isLinuxOS() && Base.local.equalsIgnoreCase("en")) { FullScreen.this.cdMenu.setSize(380, 69); } else { FullScreen.this.cdMenu.setSize(337, 69); }  FullScreen.this.cdMenu.setAlwaysOnTop(true); FullScreen.this.cdMenu.setVisible(false); }  return FullScreen.this.cdMenu; } public JDialog createFLPMenu() { if (FullScreen.this.flpMenu == null) { FullScreen.this.flpMenu = new JDialog(); FullScreen.this.flpMenu.setUndecorated(true); if (KVMUtil.isLinuxOS() && Base.local.equalsIgnoreCase("en")) { FullScreen.this.flpMenu.setSize(380, 49); } else { FullScreen.this.flpMenu.setSize(337, 49); }  FullScreen.this.flpMenu.setAlwaysOnTop(true); FullScreen.this.flpMenu.setVisible(false); }  return FullScreen.this.flpMenu; } public JDialog createPowerPanel() { if (FullScreen.this.powerPanelDialog == null) FullScreen.this.powerPanelDialog = new FullScreen.PowerPanel();  return FullScreen.this.powerPanelDialog; } public void startButtonState() { if (FullScreen.this.kvmInterface.getBladeSize() > 1) { if (KVMUtil.isWindowsOS()) { this.combineKey.setBounds(45, 1, 20, 20); this.mouseSynButton.setBounds(70, 1, 30, 20); this.mouseModeButton.setBounds(105, 1, 20, 20); this.divButton.setBounds(130, 1, 20, 20); this.returnButton.setBounds(155, 1, 20, 20); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { this.combo.setBounds(180, 1, 65, 20); this.labelnum.setBounds(250, 0, 20, 20); this.numColorButton.setBounds(270, 5, 10, 10); this.labelcaps.setBounds(285, 0, 27, 20); this.capsColorButton.setBounds(312, 5, 10, 10); this.labelscroll.setBounds(327, 0, 38, 20); this.scrollColorButton.setBounds(367, 5, 10, 10); this.btnCDMenu.setBounds(380, 0, 23, 23); this.btnFlpMenu.setBounds(405, 0, 23, 23); if (null != this.powerMenuButton) this.powerMenuButton.setBounds(430, 1, 20, 20);  } else { this.combo.setBounds(180, 1, 65, 20); this.labelnum.setBounds(250, 0, 20, 20); this.numColorButton.setBounds(270, 5, 10, 10); this.labelcaps.setBounds(285, 0, 27, 20); this.capsColorButton.setBounds(312, 5, 10, 10); this.labelscroll.setBounds(327, 0, 38, 20); this.scrollColorButton.setBounds(367, 5, 10, 10); if (null != this.powerMenuButton) this.powerMenuButton.setBounds(380, 1, 20, 20);  }  } else if (KVMUtil.isLinuxOS()) { this.combineKey.setBounds(45, 1, 20, 20); this.mouseSynButton.setBounds(70, 1, 30, 20); this.mouseModeButton.setBounds(105, 1, 20, 20); this.divButton.setBounds(130, 1, 20, 20); this.returnButton.setBounds(155, 1, 20, 20); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { this.combo.setBounds(175, 1, 100, 20); this.labelnum.setBounds(275, 0, 30, 20); this.numColorButton.setBounds(305, 5, 10, 10); this.labelcaps.setBounds(325, 0, 37, 20); this.capsColorButton.setBounds(355, 5, 10, 10); this.labelscroll.setBounds(375, 0, 43, 20); this.scrollColorButton.setBounds(415, 5, 10, 10); this.btnCDMenu.setBounds(435, 0, 23, 23); this.btnFlpMenu.setBounds(465, 0, 23, 23); if (null != this.powerMenuButton) this.powerMenuButton.setBounds(495, 1, 20, 20);  } else { this.combo.setBounds(175, 1, 95, 20); this.labelnum.setBounds(275, 0, 30, 20); this.numColorButton.setBounds(305, 5, 10, 10); this.labelcaps.setBounds(325, 0, 37, 20); this.capsColorButton.setBounds(355, 5, 10, 10); this.labelscroll.setBounds(375, 0, 43, 20); this.scrollColorButton.setBounds(395, 5, 10, 10); if (null != this.powerMenuButton) this.powerMenuButton.setBounds(400, 1, 20, 20);  }  }  } else if (KVMUtil.isWindowsOS()) { this.combineKey.setBounds(45, 1, 20, 20); this.mouseSynButton.setBounds(70, 1, 30, 20); this.mouseModeButton.setBounds(105, 1, 20, 20); this.returnButton.setBounds(130, 1, 20, 20); Properties prop = System.getProperties(); String os = prop.getProperty("os.name").toLowerCase(); if (os.indexOf("2012") > 0 || os.indexOf("8") > 0 || os.indexOf("nt") > 0) { this.labelnum.setBounds(155, 0, 20, 20); this.numColorButton.setBounds(180, 5, 10, 10); this.labelcaps.setBounds(195, 0, 25, 20); this.capsColorButton.setBounds(223, 5, 10, 10); this.labelscroll.setBounds(238, 0, 27, 20); this.scrollColorButton.setBounds(268, 5, 10, 10); this.btnCDMenu.setBounds(283, 0, 23, 23); } else { this.labelnum.setBounds(155, 0, 20, 20); this.numColorButton.setBounds(175, 5, 10, 10); this.labelcaps.setBounds(190, 0, 27, 20); this.capsColorButton.setBounds(217, 5, 10, 10); this.labelscroll.setBounds(232, 0, 38, 20); this.scrollColorButton.setBounds(270, 5, 10, 10); this.btnCDMenu.setBounds(281, 0, 23, 23); }  this.btnFlpMenu.setBounds(310, 0, 23, 23); if (null != this.powerMenuButton) this.powerMenuButton.setBounds(335, 1, 20, 20);  this.helpButton.setBounds(360, 1, 20, 20); } else if (KVMUtil.isLinuxOS()) { this.combineKey.setBounds(45, 1, 20, 20); this.mouseSynButton.setBounds(70, 1, 30, 20); this.mouseModeButton.setBounds(105, 1, 20, 20); this.returnButton.setBounds(130, 1, 20, 20); this.labelnum.setBounds(155, 0, 30, 20); this.numColorButton.setBounds(185, 5, 10, 10); this.labelcaps.setBounds(200, 0, 35, 20); this.capsColorButton.setBounds(230, 5, 10, 10); this.labelscroll.setBounds(245, 0, 35, 20); this.scrollColorButton.setBounds(282, 5, 10, 10); this.btnCDMenu.setBounds(300, 0, 23, 23); this.btnFlpMenu.setBounds(325, 0, 23, 23); if (null != this.powerMenuButton) this.powerMenuButton.setBounds(350, 1, 20, 20);  this.helpButton.setBounds(375, 1, 20, 20); }  this.divButton.setBackground(new Color(158, 202, 232)); this.divButton.setBorder(BorderFactory.createEmptyBorder()); this.mouseSynButton.setBackground(new Color(158, 202, 232)); this.mouseSynButton.setBorder(BorderFactory.createEmptyBorder()); this.combineKey.setBackground(new Color(158, 202, 232)); this.combineKey.setBorder(BorderFactory.createEmptyBorder()); this.mouseModeButton.setBackground(new Color(158, 202, 232)); this.mouseModeButton.setBorder(BorderFactory.createEmptyBorder()); this.returnButton.setBackground(new Color(158, 202, 232)); this.returnButton.setBorder(BorderFactory.createEmptyBorder()); if (null != this.powerMenuButton) { this.powerMenuButton.setBackground(new Color(158, 202, 232)); this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder()); }  this.combo.setBackground(new Color(158, 202, 232)); this.combo.setBorder(BorderFactory.createEmptyBorder()); this.helpButton.setBackground(new Color(158, 202, 232)); this.helpButton.setBorder(BorderFactory.createEmptyBorder()); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { this.btnCDMenu.setBackground(new Color(158, 202, 232)); this.btnCDMenu.setBorder(BorderFactory.createEmptyBorder()); this.btnFlpMenu.setBackground(new Color(158, 202, 232)); this.btnFlpMenu.setBorder(BorderFactory.createEmptyBorder()); }  } private JButton createHelpButton() { this.helpButton = createButton("help_document", "resource/images/help.gif"); this.helpButton.addActionListener(createHelpAction()); add(this.helpButton); return this.helpButton; } public Action createHelpAction() { Action action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent arg0) { if (null == FullScreen.this.kvmInterface.floatToolbar.helpFrm) { FullScreen.this.kvmInterface.floatToolbar.getBMCHelpDocument(); } else { FullScreen.this.kvmInterface.floatToolbar.helpFrm.setVisible(true); }  FullScreen.this.kvmInterface.floatToolbar.helpFrm.setAlwaysOnTop(true); } }
        ; return action; } private JButton createCDMenuButton() { this.btnCDMenu = createButton("cd_cdroms", "resource/images/cd.gif"); this.btnCDMenu.addActionListener(createCDMenuAction()); add(this.btnCDMenu); return this.btnCDMenu; } private JButton createFlpMenuButton() { this.btnFlpMenu = createButton("flp_floppy", "resource/images/flp.gif"); this.btnFlpMenu.addActionListener(createFlpMenuAction()); add(this.btnFlpMenu); return this.btnFlpMenu; } private Action createCDMenuAction() { Action action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { if (FullScreen.this.cdMenu.isShowing()) { FullScreen.this.kvmInterface.floatToolbar.isShowingCD = false; FullScreen.this.cdMenu.setVisible(false); FullScreen.FullToolBar.this.btnCDMenu.setBorder((Border)null); } else { if (FullScreen.this.kvmInterface.floatToolbar.isShowingFlp) { FullScreen.this.kvmInterface.floatToolbar.isShowingFlp = false; FullScreen.this.flpMenu.setVisible(false); FullScreen.FullToolBar.this.btnFlpMenu.setBorder((Border)null); }  FullScreen.this.kvmInterface.floatToolbar.isShowingCD = true; FullScreen.FullToolBar.this.btnCDMenu.setBorder(BorderFactory.createBevelBorder(1)); FullScreen.this.kvmInterface.floatToolbar.setCDVisibleAndLocation(true, 0, 0); FullScreen.this.cdMenu.getContentPane().removeAll(); FullScreen.this.cdMenu.getContentPane().add(FullScreen.this.kvmInterface.floatToolbar.getCDPanel()); if (KVMUtil.isWindowsOS()) { FullScreen.this.cdMenu.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - FullScreen.this.kvmInterface.floatToolbar.getCDWidth()) / 2, FullScreen.this.toolBarFrame.getHeight()); } else if (KVMUtil.isLinuxOS()) { if (FullScreen.this.powerPanelDialog.isShowing()) FullScreen.this.powerPanelDialog.setVisible(false);  if (KVMUtil.isLinuxOS1()) { FullScreen.this.cdMenu.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - FullScreen.this.kvmInterface.floatToolbar.getCDWidth()) / 2, FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.toolBarFrame.getY() + 23); } else { FullScreen.this.cdMenu.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - FullScreen.this.kvmInterface.floatToolbar.getCDWidth()) / 2, FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.toolBarFrame.getY() + 21); }  }  FullScreen.this.cdMenu.setVisible(true); }  } }
        ; return action; } private Action createFlpMenuAction() { Action action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { if (FullScreen.this.flpMenu.isShowing()) { FullScreen.this.kvmInterface.floatToolbar.isShowingFlp = false; FullScreen.this.flpMenu.setVisible(false); FullScreen.FullToolBar.this.btnFlpMenu.setBorder((Border)null); } else { if (FullScreen.this.kvmInterface.floatToolbar.isShowingCD) { FullScreen.this.kvmInterface.floatToolbar.isShowingCD = false; FullScreen.this.cdMenu.setVisible(false); FullScreen.FullToolBar.this.btnCDMenu.setBorder((Border)null); }  FullScreen.this.kvmInterface.floatToolbar.isShowingFlp = true; FullScreen.FullToolBar.this.btnFlpMenu.setBorder(BorderFactory.createBevelBorder(1)); FullScreen.this.kvmInterface.floatToolbar.setFlpVisibleAndLocation(true, 0, 0); FullScreen.this.flpMenu.getContentPane().removeAll(); FullScreen.this.flpMenu.getContentPane().add(FullScreen.this.kvmInterface.floatToolbar.getFlpPanel()); if (KVMUtil.isWindowsOS()) { FullScreen.this.flpMenu.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - FullScreen.this.kvmInterface.floatToolbar.getFlpWidth()) / 2, FullScreen.this.toolBarFrame.getHeight()); } else if (KVMUtil.isLinuxOS()) { if (FullScreen.this.powerPanelDialog.isShowing()) FullScreen.this.powerPanelDialog.setVisible(false);  if (KVMUtil.isLinuxOS1()) { FullScreen.this.flpMenu.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - FullScreen.this.kvmInterface.floatToolbar.getFlpWidth()) / 2, FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.toolBarFrame.getY() + 23); } else { FullScreen.this.flpMenu.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - FullScreen.this.kvmInterface.floatToolbar.getFlpWidth()) / 2, FullScreen.this.toolBarFrame.getHeight() + FullScreen.this.toolBarFrame.getY() + 21); }  }  FullScreen.this.flpMenu.setVisible(true); }  } }
        ; return action; } private JButton createButton(String resCode, String imageURL) { JButton button = new JButton(new ImageIcon(getClass().getResource(imageURL))); button.setToolTipText(FullScreen.this.kvmInterface.kvmUtil.getString(resCode)); return button; } private void createDivButton() { this.divButton = createButton("DivVid.Tip", "resource/images/divvid.gif"); this.divButton.addActionListener(FullScreen.this.divAction()); add(this.divButton); if (FullScreen.this.kvmInterface.getBladeSize() == 1) this.divButton.setVisible(false);  } private void createMouseSynButton() { this.mouseSynButton = createButton("MouseSyn.Tip", "resource/images/mousesyn.gif"); this.mouseSynButton.addActionListener(FullScreen.this.synMouseAction()); add(this.mouseSynButton); } private void createComKeyButton() { this.combineKey = createButton("ConfigMenu.shortCut.text", "resource/images/combinekey.gif"); this.combineKey.addActionListener(FullScreen.this.shortCutAction()); add(this.combineKey); } private void createMouseMode() { this.mouseModeButton = createButton("MouseMode.Tip", "resource/images/mouseMode.gif"); this.mouseModeButton.addActionListener(FullScreen.this.mouseModeAction()); add(this.mouseModeButton); } private void createReturnButton() { this.returnButton = createButton("Return.Tip", "resource/images/return.gif"); this.returnButton.addActionListener(FullScreen.this.returnAction()); add(this.returnButton); } private void createJComboBox() { produceComboBox(); add(this.combo); } public void produceComboBox() { int num = FullScreen.this.kvmInterface.base.threadGroup.size(); String[] blades = new String[num]; int[] temp = new int[num]; Iterator<String> iter = FullScreen.this.kvmInterface.base.threadGroup.keySet().iterator(); String name = "blade"; int i = 0, j = 0, tem = 0; while (iter.hasNext()) { j = Integer.parseInt(iter.next()); temp[i] = j; i++; }  Arrays.sort(temp); for (int k = 0; k < num; k++) { blades[k] = name + temp[k]; if (FullScreen.this.actionBlade == temp[k]) tem = k;  }  this.combo = new JComboBox<String>(blades); this.combo.setSelectedItem(blades[tem]); this.combo.addActionListener(FullScreen.this.action); if (FullScreen.this.kvmInterface.getBladeSize() == 1) this.combo.setVisible(false);  } private void createNumColorButton() { this.numColorButton = new BackColorButton(); this.numColorButton.setBackground(this.numColor); this.numColorButton.setEnabled(false); add(this.labelnum); add(this.numColorButton); } private void createCapsColorButton() { this.capsColorButton = new BackColorButton(this.capsColor); this.capsColorButton.setEnabled(false); add(this.labelcaps); add(this.capsColorButton); } private void createScrollColorButton() { this.scrollColorButton = new BackColorButton(this.scrollColor); this.scrollColorButton.setEnabled(false); add(this.labelscroll); add(this.scrollColorButton); } public void fullToolBarRelease() { this.divButton.removeActionListener(FullScreen.this.divAction()); this.mouseSynButton.removeActionListener(FullScreen.this.synMouseAction()); this.combineKey.removeActionListener(FullScreen.this.shortCutAction()); this.mouseModeButton.removeActionListener(FullScreen.this.mouseModeAction()); this.returnButton.removeActionListener(FullScreen.this.returnAction()); this.combo.removeActionListener(FullScreen.this.action); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { this.btnCDMenu.removeActionListener(createCDMenuAction()); this.btnFlpMenu.removeActionListener(createFlpMenuAction()); this.btnCDMenu = null; this.btnFlpMenu = null; }  this.powerMenu = null; this.powerMenuButton = null; this.helpButton = null; this.divButton = null; this.mouseSynButton = null; this.combineKey = null; this.mouseModeButton = null; this.returnButton = null; this.combo = null; this.numColorButton = null; this.capsColorButton = null; this.scrollColorButton = null; this.numColor = null; this.capsColor = null; this.scrollColor = null; this.labelnum = null; this.labelcaps = null; this.labelscroll = null; } protected void paintComponent(Graphics g) { Graphics2D g2d = (Graphics2D)g.create(); g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2d.setColor(new Color(158, 202, 232)); int w = getWidth(); int h = getHeight(); int[] x = { 0, w, w - h, h, 0 }; int[] y = { 0, 0, h, h, 0, 0 }; int nPoints = 4; Polygon poly = new Polygon(x, y, nPoints); g2d.fillPolygon(poly); g2d.drawPolygon(poly); } } public Action divAction() { Action action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { FullScreen.this.getContentPane().removeAll(); FullScreen.this.getContentPane().add(FullScreen.this.imageParentPane); FullScreen.this.imageParentPane.setLayout(new GridLayout(4, 4, 1, 1)); FullScreen.MouseHandler listener = new FullScreen.MouseHandler(); FullScreen.this.imageParentPane.addMouseListener(listener); for (int i = 1; i <= 16; i++) { ImagePane imaPane = FullScreen.this.kvmInterface.kvmUtil.getImagePane(i); if (imaPane != null) { imaPane.setToolTipText("blade" + imaPane.bladeNO); imaPane.setCursor(new Cursor(0)); FullScreen.this.imageParentPane.add(imaPane); if (i != FullScreen.this.actionBlade) { BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(imaPane.bladeNO)); DrawThread drawThread = bladeThread.getDrawThread(); drawThread.setFirstJudge(true); drawThread.getComImage().clear(); drawThread.getKvmUtil().resetBuf(); (drawThread.getKvmUtil()).firstJudge = true; bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.connectBlade(imaPane.bladeNO, (FullScreen.this.kvmInterface.kvmUtil.getImagePane(imaPane.bladeNO)).custBit)); bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.contrRate(1, bladeThread.getBladeNO())); } else { BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade)); bladeThread.bladeCommu.sentData(FullScreen.this.kvmInterface.packData.contrRate(1, bladeThread.getBladeNO())); }  imaPane.divScreenIni(imaPane); imaPane.kvmInterface.floatToolbar.setVisible(false); if (imaPane.kvmInterface.floatToolbar.isVirtualMedia()) imaPane.kvmInterface.floatToolbar.setVirtualMediaVisible(false, false);  } else { JPanel panel = new JPanel(); panel.setBackground(Color.black); panel.setToolTipText(FullScreen.this.kvmInterface.kvmUtil.getString("NoSignal.Tip")); panel.addMouseListener(listener); FullScreen.this.imageParentPane.add(panel); }  }  FullScreen.this.kvmInterface.fullScreen.setCursor(FullScreen.this.kvmInterface.base.defCursor); FullScreen.this.kvmInterface.kvmUtil.setDrawDisplay(true); FullScreen.this.kvmInterface.base.isDiv = true; FullScreen.this.kvmInterface.kvmUtil.setFullToolBar(FullScreen.this.kvmInterface.base.isDiv); FullScreen.this.kvmInterface.fullScreen.setVisible(true); FullScreen.this.toolBarFrame.setVisible(false); FullScreen.this.setButtonEnable(); FullScreen.this.toolBar.startButtonState(); if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); }  FullScreen.this.kvmInterface.createToolFrame(); } }
      ; return action; } public Action synMouseAction() { Action action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { if (!FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade).isNew()) { if (FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade).isContr()) { BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade)); bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.mousePack(65535, 65535, FullScreen.this.actionBlade)); } else { JOptionPane.showMessageDialog(FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade), FullScreen.this.kvmInterface.kvmUtil.getString("ListenOperation")); }  } else if (FullScreen.this.kvmInterface.base.isMstsc) { BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade)); for (int i = 0; i < 15; i++) bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.mousePackNew((byte)-127, (byte)-127, FullScreen.this.actionBlade));  (bladeThread.getDrawThread()).imagePane.remotemstscX = 0; (bladeThread.getDrawThread()).imagePane.remotemstscY = 0; }  } }
      ; return action; } public Action shortCutAction() { Action action = new AbstractAction() { private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) { FullScreen.this.cdMenu.setVisible(false); FullScreen.this.flpMenu.setVisible(false); }  FullScreen.this.toolBarFrame.setVisible(false); if (FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade).isContr()) { FullScreen.this.produceComKey(FullScreen.this.actionBlade); } else { JOptionPane.showMessageDialog(FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade), FullScreen.this.kvmInterface.kvmUtil.getString("ListenOperation")); }  } }
      ; return action; } private void produceComKey(int bladeNO) { new CombinationKey(this, bladeNO, (this.kvmInterface.kvmUtil.getImagePane(bladeNO)).pack, this.kvmInterface); } public Action mouseModeAction() { Action action = new AbstractAction() {
        private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade)); if (FullScreen.this.kvmInterface.base.isMstsc) { if (bladeThread.isNew()) if (FullScreen.this.kvmInterface.isFullScreen) { FullScreen.this.kvmInterface.fullScreen.setCursor(FullScreen.this.kvmInterface.base.myCursor); FullScreen.this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(false); } else if (null != FullScreen.this.kvmInterface.toolbar.mouseSynButton) { FullScreen.this.kvmInterface.toolbar.mouseSynButton.setEnabled(false); }   FullScreen.this.kvmInterface.base.isMstsc = false; } else { if (bladeThread.isNew()) { FullScreen.this.imagePane.setCursor(FullScreen.this.kvmInterface.base.defCursor); if (FullScreen.this.kvmInterface.isFullScreen) { FullScreen.this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(true); } else if (null != FullScreen.this.kvmInterface.toolbar.mouseSynButton) { FullScreen.this.kvmInterface.toolbar.mouseSynButton.setEnabled(true); }  }  FullScreen.this.kvmInterface.base.isMstsc = true; }  }
      }; return action; } public Action returnAction() { Action action = new AbstractAction() {
        private static final long serialVersionUID = 1L; public void actionPerformed(ActionEvent e) { FullScreen.this.kvmInterface.tabbedpane.getModel().removeChangeListener(FullScreen.this.kvmInterface.kvmUtil.changeListener); FullScreen.this.kvmInterface.kvmUtil.returnToWin(); FullScreen.this.kvmInterface.tabbedpane.getModel().addChangeListener(FullScreen.this.kvmInterface.kvmUtil.changeListener); }
      }; return action; } class MouseHandler implements MouseListener {
    public void mousePressed(MouseEvent e) { if (FullScreen.this.kvmInterface.base.isDiv && FullScreen.this.toolBarFrame.isShowing()) {
        if (FullScreen.this.kvmInterface.floatToolbar.isVirtualMedia()) {
          FullScreen.this.cdMenu.setVisible(false);
          FullScreen.this.flpMenu.setVisible(false);
        } 
        FullScreen.this.toolBarFrame.setVisible(false);
      }  }
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
  }
  class KeyHandler
    implements KeyListener
  {
    public void keyPressed(KeyEvent event) {
      if (event.isControlDown() && event.isAltDown() && event.isShiftDown() && FullScreen.this.kvmInterface.isFullScreen) {
        FullScreen.this.kvmInterface.fullScreen.setCursor(FullScreen.this.kvmInterface.base.defCursor);
        FullScreen.this.setCursor(FullScreen.this.kvmInterface.base.defCursor);
        MouseDisplacementImpl.setMode(0);
        if (FullScreen.this.kvmInterface.base.isDiv) {
          FullScreen.this.kvmInterface.toolFrame.setState(0);
          FullScreen.this.kvmInterface.toolFrame.setVisible(true);
          FullScreen.this.kvmInterface.toolFrame.setAlwaysOnTop(true);
        }
        else {
          FullScreen.this.kvmInterface.fullScreen.toolBarFrame.setVisible(true);
        } 
      } 
    }
    public void keyReleased(KeyEvent event) {}
    public void keyTyped(KeyEvent event) {}
  }
  class PowerPanel
    extends JDialog
  {
    private static final long serialVersionUID = 1L;
    public JPanel mainPanel;
    public JMenuItem poweronMenu;
    public JMenuItem poweroffMenu;
    public JMenuItem restartMenu;
    public JMenuItem safetyRestartMenu;
    public JMenuItem usbResetMenu;
    public JMenuItem safePowerOffMenu;
    public JMenuItem localKinescopeMenu;
    public JCheckBoxMenuItem mouseModeSwitchMenu;
    public JCheckBoxMenuItem SingleMouseMenu;
    public KinescopeDataCollect kineScopeDataCollect = null;
    public boolean dissflag = false;
    public boolean isIdiss = false;
    public PowerPanel() {
      createMainPanle();
      setLayout(new BorderLayout());
      if (FullScreen.this.kvmInterface.getProductType().equals("OSCA")) {
        setOSCASize();
      }
      else {
        setBMCSize();
      } 
      getContentPane().add(this.mainPanel);
      setUndecorated(true);
      setAlwaysOnTop(true);
      setVisible(false);
      this.poweroffMenu.setBackground(new Color(204, 227, 242));
      this.poweronMenu.setBackground(new Color(204, 227, 242));
      if (FullScreen.this.kvmInterface.getProductType().equals("BMC"))
      {
        this.safePowerOffMenu.setBackground(new Color(204, 227, 242));
      }
      this.restartMenu.setBackground(new Color(204, 227, 242));
      this.safetyRestartMenu.setBackground(new Color(204, 227, 242));
      this.usbResetMenu.setBackground(new Color(204, 227, 242));
      this.localKinescopeMenu.setBackground(new Color(204, 227, 242));
      this.SingleMouseMenu.setBackground(new Color(204, 227, 242));
      this.mouseModeSwitchMenu.setBackground(new Color(204, 227, 242));
      if (FullScreen.this.kvmInterface.getProductType().equals("OSCA")) {
        setOSCABounds();
      }
      else {
        setBMCBounds();
      } 
    }
    private void setOSCASize() {
      if (Base.local.equalsIgnoreCase("en")) {
        if (KVMUtil.isLinuxOS1())
        {
          setSize(165, 158);
        }
        else
        {
          setSize(170, 162);
        }
      }
      else if (KVMUtil.isLinuxOS1()) {
        setSize(95, 160);
      }
      else {
        setSize(100, 165);
      } 
    }
    private void setBMCSize() {
      if (Base.local.equalsIgnoreCase("en")) {
        if (KVMUtil.isLinuxOS1())
        {
          setSize(165, 178);
        }
        else
        {
          setSize(170, 182);
        }
      }
      else if (KVMUtil.isLinuxOS1()) {
        setSize(95, 180);
      }
      else {
        setSize(100, 185);
      } 
    }
    private void setOSCABounds() {
      if (Base.local.equalsIgnoreCase("en")) {
        this.poweronMenu.setBounds(5, 0, 180, 20);
        this.poweroffMenu.setBounds(5, 20, 180, 20);
        this.restartMenu.setBounds(5, 40, 180, 20);
        this.safetyRestartMenu.setBounds(5, 60, 180, 20);
        if ("USB".equals(Base.TYPE)) {
          this.usbResetMenu.setBounds(5, 80, 180, 20);
          this.localKinescopeMenu.setBounds(5, 100, 180, 20);
          this.SingleMouseMenu.setBounds(5, 120, 180, 20);
          this.mouseModeSwitchMenu.setBounds(5, 140, 180, 20);
        }
        else {
          this.localKinescopeMenu.setBounds(5, 100, 180, 20);
          if (KVMUtil.isLinuxOS1())
          {
            setSize(165, 118);
          }
          else
          {
            setSize(170, 122);
          }
        } 
      } else {
        this.poweronMenu.setBounds(5, 0, 110, 20);
        this.poweroffMenu.setBounds(5, 20, 110, 20);
        this.restartMenu.setBounds(5, 40, 110, 20);
        this.safetyRestartMenu.setBounds(5, 60, 110, 20);
        if ("USB".equals(Base.TYPE)) {
          this.usbResetMenu.setBounds(5, 80, 110, 20);
          this.localKinescopeMenu.setBounds(5, 100, 110, 20);
          this.SingleMouseMenu.setBounds(5, 120, 110, 20);
          this.mouseModeSwitchMenu.setBounds(5, 140, 110, 20);
        }
        else {
          this.localKinescopeMenu.setBounds(5, 100, 110, 20);
          if (KVMUtil.isLinuxOS1()) {
            setSize(95, 120);
          }
          else {
            setSize(100, 125);
          } 
        } 
      } 
    }
    private void setBMCBounds() {
      if (Base.local.equalsIgnoreCase("en")) {
        this.poweronMenu.setBounds(5, 0, 180, 20);
        this.poweroffMenu.setBounds(5, 20, 180, 20);
        this.safePowerOffMenu.setBounds(5, 40, 180, 20);
        this.restartMenu.setBounds(5, 60, 180, 20);
        this.safetyRestartMenu.setBounds(5, 80, 180, 20);
        if ("USB".equals(Base.TYPE)) {
          this.usbResetMenu.setBounds(5, 100, 180, 20);
          this.localKinescopeMenu.setBounds(5, 120, 180, 20);
          this.SingleMouseMenu.setBounds(5, 140, 180, 20);
          this.mouseModeSwitchMenu.setBounds(5, 160, 180, 20);
        }
        else {
          this.localKinescopeMenu.setBounds(5, 100, 180, 20);
          if (KVMUtil.isLinuxOS1())
          {
            setSize(165, 118);
          }
          else
          {
            setSize(170, 122);
          }
        } 
      } else {
        this.poweronMenu.setBounds(5, 0, 110, 20);
        this.poweroffMenu.setBounds(5, 20, 110, 20);
        this.safePowerOffMenu.setBounds(5, 40, 110, 20);
        this.restartMenu.setBounds(5, 60, 110, 20);
        this.safetyRestartMenu.setBounds(5, 80, 110, 20);
        if ("USB".equals(Base.TYPE)) {
          this.usbResetMenu.setBounds(5, 100, 110, 20);
          this.localKinescopeMenu.setBounds(5, 120, 110, 20);
          this.SingleMouseMenu.setBounds(5, 140, 110, 20);
          this.mouseModeSwitchMenu.setBounds(5, 160, 110, 20);
        }
        else {
          this.localKinescopeMenu.setBounds(5, 100, 110, 20);
          if (KVMUtil.isLinuxOS1()) {
            setSize(95, 120);
          }
          else {
            setSize(100, 125);
          } 
        } 
      } 
    }
    public void setMouseSelected() {
      if (Base.isSynMouse) {
        this.mouseModeSwitchMenu.setSelected(true);
      }
      else {
        this.mouseModeSwitchMenu.setSelected(false);
      } 
      if (Base.isSingleMouse) {
        this.SingleMouseMenu.setSelected(true);
      }
      else {
        this.SingleMouseMenu.setSelected(false);
      } 
    }
    private JPanel createMainPanle() {
      this.mainPanel = new JPanel();
      if (Base.local.equalsIgnoreCase("en")) {
        this.mainPanel.setSize(105, 180);
      }
      else {
        this.mainPanel.setSize(70, 175);
      } 
      this.mainPanel.setBackground(new Color(204, 227, 242));
      this.mainPanel.setVisible(true);
      this.mainPanel.setLayout((LayoutManager)null);
      this.poweronMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Power_On"));
      this.poweronMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.poweronMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.poweronMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.poweronMenu.setForeground(Color.BLACK);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.fullScreen, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.kvmCmdPowerControl((byte)33, bladeThread.getBladeNO()));
              }
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      if (FullScreen.this.kvmInterface.getProductType().equals("BMC")) {
        this.poweroffMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Power_Off_BMC"));
      }
      else {
        this.poweroffMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Power_Off_OSCA"));
      } 
      this.poweroffMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.poweroffMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.poweroffMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.poweroffMenu.setForeground(Color.BLACK);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.fullScreen, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.kvmCmdPowerControl((byte)32, bladeThread.getBladeNO()));
              }
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.restartMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Cold_Reset"));
      this.restartMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.restartMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.restartMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.restartMenu.setForeground(Color.BLACK);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.fullScreen, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.kvmCmdPowerControl((byte)34, bladeThread.getBladeNO()));
              }
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.safetyRestartMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Graceful_Reboot"));
      this.safetyRestartMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.safetyRestartMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.safetyRestartMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.safetyRestartMenu.setForeground(Color.BLACK);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.fullScreen, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.kvmCmdPowerControl((byte)35, bladeThread.getBladeNO()));
              }
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.usbResetMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Usb_Reset"));
      this.usbResetMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.usbResetMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.usbResetMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.usbResetMenu.setForeground(Color.BLACK);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.fullScreen, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.kvmCmdPowerControl((byte)48, bladeThread.getBladeNO()));
              }
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.safePowerOffMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("Graceful_poweroff"));
      this.safePowerOffMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.safePowerOffMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.safePowerOffMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.safePowerOffMenu.setForeground(Color.BLACK);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.fullScreen, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.kvmCmdPowerControl((byte)37, bladeThread.getBladeNO()));
              }
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.localKinescopeMenu = new JMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("localKinescope"));
      this.localKinescopeMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.localKinescopeMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.localKinescopeMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              FullScreen.PowerPanel.this.localKinescopeMenu.setForeground(Color.BLACK);
              JMenuItem menuItem = (JMenuItem)e.getSource();
              if (null == FullScreen.PowerPanel.this.kineScopeDataCollect) {
                int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.floatToolbar.imagePanel, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
                if (0 == result)
                {
                  String absolutePath = "";
                  JFileChooser choose = new JFileChooser("");
                  choose.addChoosableFileFilter(new FileFilter()
                      {
                        public boolean accept(File f)
                        {
                          if (f.isDirectory())
                          {
                            return true;
                          }
                          String fileName = f.getName();
                          if (fileName.toLowerCase().endsWith("rep".toLowerCase()))
                          {
                            return true;
                          }
                          return false;
                        }
                        public String getDescription() {
                          return "file(*.rep)";
                        }
                      });
                  choose.setFileSelectionMode(0);
                  int returnval = choose.showSaveDialog(null);
                  if (returnval == 0)
                  {
                    absolutePath = choose.getSelectedFile().getAbsolutePath().toLowerCase();
                  }
                  if (null == absolutePath || "".equals(absolutePath)) {
                    return;
                  }
                  if (!absolutePath.toLowerCase().endsWith(".rep"))
                  {
                    absolutePath = absolutePath + ".rep";
                  }
                  File file = new File(absolutePath);
                  if (file.exists())
                  {
                    int file_check = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.floatToolbar.imagePanel, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
                    if (file_check == 0)
                    {
                      boolean isdel = file.delete();
                      if (!isdel) {
                        JOptionPane.showMessageDialog(FullScreen.this.kvmInterface.floatToolbar.imagePanel, FullScreen.this.kvmInterface.kvmUtil.getString("file_used"));
                        return;
                      } 
                      menuItem.setText(FullScreen.this.kvmInterface.kvmUtil.getString("Stop_KinScope"));
                      FullScreen.PowerPanel.this.kineScopeDataCollect = new KinescopeDataCollect(absolutePath);
                      FullScreen.PowerPanel.this.kineScopeDataCollect.setCollect(true);
                      FullScreen.PowerPanel.this.kineScopeDataCollect.startCollectThread();
                      if (FullScreen.this.kvmInterface.isFullScreen && null == FullScreen.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect)
                      {
                        FullScreen.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = FullScreen.PowerPanel.this.kineScopeDataCollect;
                      }
                    }
                  }
                  else
                  {
                    menuItem.setText(FullScreen.this.kvmInterface.kvmUtil.getString("Stop_KinScope"));
                    FullScreen.PowerPanel.this.kineScopeDataCollect = new KinescopeDataCollect(absolutePath);
                    FullScreen.PowerPanel.this.kineScopeDataCollect.setCollect(true);
                    FullScreen.PowerPanel.this.kineScopeDataCollect.startCollectThread();
                    if (FullScreen.this.kvmInterface.isFullScreen && null == FullScreen.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect)
                    {
                      FullScreen.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = FullScreen.PowerPanel.this.kineScopeDataCollect;
                    }
                  }
                }
              }
              else if (FullScreen.PowerPanel.this.kineScopeDataCollect.isCollect()) {
                int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.floatToolbar.imagePanel, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
                if (0 == result) {
                  menuItem.setText(FullScreen.this.kvmInterface.kvmUtil.getString("localKinescope"));
                  FullScreen.PowerPanel.this.kineScopeDataCollect.interrupt();
                  FullScreen.PowerPanel.this.kineScopeDataCollect.setCollect(false);
                  if (FullScreen.PowerPanel.this.kineScopeDataCollect.isAlive()) {
                    try {
                      Thread.sleep(5L);
                    }
                    catch (InterruptedException e1) {
                      e1.printStackTrace();
                    } 
                  }
                  FullScreen.PowerPanel.this.kineScopeDataCollect = null;
                  FullScreen.PowerPanel.this.dissflag = false;
                  FullScreen.PowerPanel.this.isIdiss = false;
                  if (FullScreen.this.kvmInterface.isFullScreen && null == FullScreen.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect)
                  {
                    FullScreen.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = FullScreen.PowerPanel.this.kineScopeDataCollect;
                  }
                } 
              } 
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.mouseModeSwitchMenu = new JCheckBoxMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("mouse_mode_switch"));
      if (Base.isSynMouse) {
        this.mouseModeSwitchMenu.setSelected(true);
      }
      else {
        this.mouseModeSwitchMenu.setSelected(false);
      } 
      this.mouseModeSwitchMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.mouseModeSwitchMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.mouseModeSwitchMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              byte mouseMode = 0;
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.floatToolbar.imagePanel, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result) {
                if (FullScreen.PowerPanel.this.mouseModeSwitchMenu.isSelected()) {
                  mouseMode = 0;
                  Base.isSynMouse = false;
                  FullScreen.PowerPanel.this.mouseModeSwitchMenu.setSelected(false);
                }
                else {
                  mouseMode = 1;
                  FullScreen.PowerPanel.this.mouseModeSwitchMenu.setSelected(true);
                  Base.isSingleMouse = false;
                  Base.isSynMouse = true;
                  FullScreen.PowerPanel.this.SingleMouseMenu.setSelected(false);
                } 
                bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.mouseModeControl((byte)36, mouseMode, bladeThread.getBladeNO()));
              }
              else if (FullScreen.PowerPanel.this.mouseModeSwitchMenu.isSelected()) {
                FullScreen.PowerPanel.this.mouseModeSwitchMenu.setSelected(true);
              }
              else {
                FullScreen.PowerPanel.this.mouseModeSwitchMenu.setSelected(false);
              } 
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.SingleMouseMenu = new JCheckBoxMenuItem(FullScreen.this.kvmInterface.kvmUtil.getString("single_mouse"));
      if (Base.isSingleMouse) {
        this.SingleMouseMenu.setSelected(true);
      }
      else {
        this.SingleMouseMenu.setSelected(false);
      } 
      this.SingleMouseMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FullScreen.PowerPanel.this.SingleMouseMenu.setForeground(new Color(158, 202, 232));
            }
            public void mouseExited(MouseEvent e) {
              FullScreen.PowerPanel.this.SingleMouseMenu.setForeground(Color.BLACK);
            }
            public void mouseClicked(MouseEvent e) {
              FullScreen.this.powerPanelDialog.setVisible(false);
              BladeThread bladeThread = FullScreen.this.kvmInterface.base.threadGroup.get(String.valueOf(FullScreen.this.actionBlade));
              int result = JOptionPane.showConfirmDialog(FullScreen.this.kvmInterface.floatToolbar.imagePanel, FullScreen.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result) {
                if (FullScreen.PowerPanel.this.SingleMouseMenu.isSelected())
                {
                  Base.isSingleMouse = false;
                  FullScreen.PowerPanel.this.SingleMouseMenu.setSelected(false);
                }
                else
                {
                  Base.isSingleMouse = true;
                  FullScreen.PowerPanel.this.SingleMouseMenu.setSelected(true);
                  FullScreen.PowerPanel.this.mouseModeSwitchMenu.setSelected(false);
                  if (Base.isSynMouse)
                  {
                    bladeThread.bladeCommu.sentData((FullScreen.this.kvmInterface.kvmUtil.getImagePane(FullScreen.this.actionBlade)).pack.mouseModeControl((byte)36, (byte)0, bladeThread.getBladeNO()));
                  }
                }
              }
              else if (FullScreen.PowerPanel.this.SingleMouseMenu.isSelected()) {
                FullScreen.PowerPanel.this.SingleMouseMenu.setSelected(true);
              }
              else {
                FullScreen.PowerPanel.this.SingleMouseMenu.setSelected(false);
              } 
            }
            public void mousePressed(MouseEvent e) {
              mouseClicked(e);
            }
          });
      this.mainPanel.add(this.poweronMenu);
      this.mainPanel.add(this.poweroffMenu);
      if (FullScreen.this.kvmInterface.getProductType().equals("BMC"))
      {
        this.mainPanel.add(this.safePowerOffMenu);
      }
      this.mainPanel.add(this.restartMenu);
      this.mainPanel.add(this.safetyRestartMenu);
      if (!"PS2".equals(Base.TYPE))
      {
        this.mainPanel.add(this.usbResetMenu);
      }
      this.mainPanel.add(this.localKinescopeMenu);
      if ("USB".equals(Base.TYPE)) {
        this.mainPanel.add(this.SingleMouseMenu);
        this.mainPanel.add(this.mouseModeSwitchMenu);
      } 
      return this.mainPanel;
    }
  }
}
