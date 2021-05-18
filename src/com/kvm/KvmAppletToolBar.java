package com.kvm;
import com.library.LibException;
import com.library.LoggerUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
class KvmAppletToolBar
  extends JToolBar
  implements ActionListener
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null; private JButton disConnectBladeButton; private JButton mouseSynButton; private JButton combineKey; private JButton setColorBit; private JButton fullButton; private BackColorButton numColorButton; private BackColorButton capsColorButton; private BackColorButton scrollColorButton; private JButton refreshButton; private boolean dynamicBlade; private JButton imageButton; private JFrame frmFr; private JButton helpButton; private JFrame helpFrm; private transient HelpDocument help; private Color numColor; private Color capsColor;
  private Color scrollColor;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public JButton getDisConnectBladeButton() {
    return this.disConnectBladeButton;
  }
  public void setDisConnectBladeButton(JButton disConnectBladeButton) {
    this.disConnectBladeButton = disConnectBladeButton;
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
  public JButton getSetColorBit() {
    return this.setColorBit;
  }
  public void setSetColorBit(JButton setColorBit) {
    this.setColorBit = setColorBit;
  }
  public JButton getFullButton() {
    return this.fullButton;
  }
  public void setFullButton(JButton fullButton) {
    this.fullButton = fullButton;
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
  public JButton getRefreshButton() {
    return this.refreshButton;
  }
  public void setRefreshButton(JButton refreshButton) {
    this.refreshButton = refreshButton;
  }
  public KvmAppletToolBar(KVMInterface refer) {
    this.dynamicBlade = true;
    this.numColor = Base.LIGHT_OFF;
    this.capsColor = Base.LIGHT_OFF;
    this.scrollColor = Base.LIGHT_OFF; this.kvmInterface = refer; } public boolean isDynamicBlade() { return this.dynamicBlade; } public void setDynamicBlade(boolean dynamicBlade) { this.dynamicBlade = dynamicBlade; } public JButton getImageButton() { return this.imageButton; } public void setImageButton(JButton imageButton) { this.imageButton = imageButton; } public KvmAppletToolBar(KVMInterface refer, int bladeSize) { this.dynamicBlade = true; this.numColor = Base.LIGHT_OFF; this.capsColor = Base.LIGHT_OFF; this.scrollColor = Base.LIGHT_OFF; this.kvmInterface = refer;
    createBladeButton(); }
  public JFrame getFrmFr() { return this.frmFr; }
  public void setFrmFr(JFrame frmFr) { this.frmFr = frmFr; }
  public JButton getHelpButton() { return this.helpButton; } public Color getScrollColor() { return this.scrollColor; } public void setHelpButton(JButton helpButton) { this.helpButton = helpButton; } public JFrame getHelpFrm() { return this.helpFrm; } public void setHelpFrm(JFrame helpFrm) { this.helpFrm = helpFrm; }
  public Color getNumColor() { return this.numColor; }
  public void setNumColor(Color numColor) { this.numColor = numColor; }
  public Color getCapsColor() { return this.capsColor; }
  public void setCapsColor(Color capsColor) { this.capsColor = capsColor; }
  public void setScrollColor(Color scrollColor) { this.scrollColor = scrollColor; }
  void createBladeButton() {
    String imagesRes = "resource/images/blade1.gif";
    for (int i = 0; i < this.kvmInterface.getBladeSize(); i++) {
      imagesRes = "resource/images/blade" + (i + 1) + ".gif";
      JButton blade = new JButton(new ImageIcon(getClass().getResource(imagesRes.trim())));
      this.kvmInterface.getBladeList().add(blade);
    } 
  }
  JButton createButton(String resCode, String imageURL) {
    JButton button = new JButton(new ImageIcon(getClass().getResource(imageURL)));
    button.setToolTipText(this.kvmInterface.getKvmUtil().getString(resCode));
    return button;
  }
  void createDisConnectBladeButton() {
    this.disConnectBladeButton = createButton("DisconnectBlade.Tip", "resource/images/disconnectblade.gif");
    this.disConnectBladeButton.addActionListener(this.kvmInterface.disConnBladeAction());
    add(this.disConnectBladeButton);
    if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.setLinuxSize(this.disConnectBladeButton, 5, 5, 30, 35);
    }
    if (this.kvmInterface.getBladeSize() == 1)
    {
      this.disConnectBladeButton.setVisible(false);
    }
  }
  void createMouseSynButton() {
    this.mouseSynButton = createButton("MouseSyn.Tip", "resource/images/mousesyn.gif");
    this.mouseSynButton.addActionListener(this.kvmInterface.synMouseAction());
    add(this.mouseSynButton);
    if (this.kvmInterface.getBladeSize() == 1)
    {
      this.mouseSynButton.setVisible(false);
    }
  }
  void createComKeyButton() {
    this.combineKey = createButton("keycombination.Tip", "resource/images/combinekey.gif");
    this.combineKey.addActionListener(this.kvmInterface.shortCutAction());
    add(this.combineKey);
    if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.setLinuxSize(this.combineKey, 38, 5, 30, 35);
    }
  }
  void createColorBitButton() {
    this.setColorBit = createButton("setColorBit", "resource/images/color.gif");
    this.setColorBit.addActionListener(this.kvmInterface.colorBitAction());
    add(this.setColorBit);
    if (this.kvmInterface.getBladeSize() == 1)
    {
      this.setColorBit.setVisible(false);
    }
  }
  void createFullButton() {
    this.fullButton = createButton("Full.Tip", "resource/images/fullscreen.gif");
    this.fullButton.addActionListener(this.kvmInterface.fullScreenAction());
    add(this.fullButton);
    if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.setLinuxSize(this.fullButton, 71, 5, 30, 35);
    }
  }
  void createRefreshButton() {
    this.refreshButton = createButton("Refresh.Tip", "resource/images/refresh.gif");
    this.refreshButton.addActionListener(this.kvmInterface.refreshAction());
    add(this.refreshButton);
    if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.setLinuxSize(this.refreshButton, 137, 5, 30, 35);
    }
    if (this.kvmInterface.getBladeSize() == 1)
    {
      this.refreshButton.setVisible(false);
    }
  }
  void createNumColorButton() {
    this.numColorButton = new BackColorButton(this.numColor);
    this.numColorButton.setEnabled(false);
    if (this.kvmInterface.getBladeSize() > 1) {
      JLabel labelnum = new JLabel(" num ");
      add(labelnum);
      if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1) {
        this.kvmInterface.setLinuxSize(labelnum, 170 + this.kvmInterface.getBladeSize() * 31, 15, 37, 20);
        this.kvmInterface.setLinuxSize(this.numColorButton, 207 + this.kvmInterface.getBladeSize() * 31, 20, 10, 10);
      } 
    } 
    add(this.numColorButton);
  }
  void createCapsColorButton() {
    this.capsColorButton = new BackColorButton(this.capsColor);
    this.capsColorButton.setEnabled(false);
    if (this.kvmInterface.getBladeSize() > 1) {
      JLabel labelcaps = new JLabel(" caps ");
      add(labelcaps);
      if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1) {
        this.kvmInterface.setLinuxSize(labelcaps, 220 + this.kvmInterface.getBladeSize() * 31, 15, 37, 20);
        this.kvmInterface.setLinuxSize(this.capsColorButton, 257 + this.kvmInterface.getBladeSize() * 31, 20, 10, 10);
      } 
    } 
    add(this.capsColorButton);
  }
  void createScrollColorButton() {
    this.scrollColorButton = new BackColorButton(this.scrollColor);
    this.scrollColorButton.setEnabled(false);
    if (this.kvmInterface.getBladeSize() > 1) {
      JLabel labelscroll = new JLabel(" scroll ");
      add(labelscroll);
      if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1) {
        this.kvmInterface.setLinuxSize(labelscroll, 270 + this.kvmInterface.getBladeSize() * 31, 15, 43, 20);
        this.kvmInterface.setLinuxSize(this.scrollColorButton, 313 + this.kvmInterface.getBladeSize() * 31, 20, 10, 10);
      } 
    } 
    add(this.scrollColorButton);
  }
  void creatBladeButton() {
    if (this.dynamicBlade) {
      JButton blade = null;
      for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
        blade = ((InterfaceContainer)this.kvmInterface.getBladeList().get(i)).getBladeButton();
        blade.setActionCommand("blade" + (i + 1));
        if (this.kvmInterface.getBladeSize() == 1)
        {
          blade.setVisible(false);
        }
        blade.addActionListener(this);
        add(blade);
        if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1)
        {
          this.kvmInterface.setLinuxSize(blade, 170 + i * 31, 0, 30, 48);
        }
      } 
    } 
  }
  protected void createHelpButton() {
    this.helpButton = createButton("help_document", "resource/images/help.gif");
    this.helpButton.addActionListener(createHelpAction());
  }
  private Action createHelpAction() {
    Action action = new KVMAppletCreatHelpAction(this);
    return action;
  }
  public JFrame getMMHelpDocument() {
    String path = "";
    if (!Base.getLocal().equalsIgnoreCase("en")) {
      path = "resource/helpdoc/help/kvmvmm_zh.html";
    }
    else {
      path = "resource/helpdoc/help/kvmvmm_en.html";
    } 
    if (this.help == null)
    {
      this.help = new HelpDocument(path);
    }
    this.helpFrm = new JFrame();
    this.helpFrm.addWindowListener(new KVMWindowAdapter(this));
    this.helpFrm.setSize(800, 650);
    int y = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.helpFrm.getHeight()) / 2;
    int x = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.helpFrm.getWidth()) / 2;
    this.helpFrm.setLocation(x, y);
    this.helpFrm.setLayout(new BorderLayout());
    this.helpFrm.getContentPane().add(this.help.getScroller());
    this.helpFrm.setVisible(true);
    return this.helpFrm;
  }
  protected void createMenuButton() {
    this.imageButton = createButton("create_image", "resource/images/virtualne.gif");
    this.imageButton.addActionListener(createMenuAction());
    if (this.kvmInterface.getBladeSize() > 1)
    {
      add(this.imageButton);
    }
    if (KVMUtil.isLinuxOS() && this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.setLinuxSize(this.imageButton, 326 + this.kvmInterface.getBladeSize() * 31, 5, 35, 35);
    }
  }
  protected JFrame createFrFrame() {
    this.frmFr = new JFrame(this.kvmInterface.getKvmUtil().getString("create_image"));
    this.frmFr.setDefaultCloseOperation(0);
    this.frmFr.setContentPane(this.kvmInterface.getImageFile());
    this.frmFr.setSize(337, 79);
    this.kvmInterface.getImageFile().setVisible(true);
    this.frmFr.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.frmFr.getWidth()) / 2, 
        (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.frmFr.getHeight()) / 2);
    this.frmFr.addWindowListener(new KVMAppletCloseAction(this));
    this.frmFr.setResizable(false);
    this.frmFr.setVisible(true);
    return this.frmFr;
  }
  private Action createMenuAction() {
    Action action = new KVMAppletCreatMenuAction(this);
    return action;
  }
  public void actionPerformed(ActionEvent e) {
    Debug.println(" actionPerformed...");
    LoggerUtil.info( "actionPerformed... ");
    this.kvmInterface.setClickFlag(true);
    this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    BladeState bladeState = null;
    try {
      if (this.dynamicBlade)
      {
        for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
          JButton blade = ((InterfaceContainer)this.kvmInterface.getBladeList().get(i)).getBladeButton();
          if (blade != null)
          {
            if (e.getActionCommand().equals(blade.getActionCommand())) {
              int bladeNO = i + 1;
              Debug.println(" blade1..." + bladeNO);
              if (bladeNO == 1 && this.kvmInterface.getBladeList().size() == 1) {
                bladeState = this.kvmInterface.getKvmUtil().getBladStateBmc(bladeNO);
              }
              else {
                bladeState = this.kvmInterface.getKvmUtil().getBladeState(bladeNO);
              }
              LoggerUtil.info( "bladeState: "+bladeState);
              if (bladeState.isEnable())
              {
            	LoggerUtil.info( "connectNewBlade: ip"+bladeState.getBladeIP()+" port: "+bladeState.getBladePort()+" isNew:"+bladeState.isNew());
                this.kvmInterface.getKvmUtil().connectNewBlade(bladeNO, true, bladeState
                    .getBladeIP(), bladeState
                    .getBladePort(), bladeState
                    .isNew());
              }
            } 
          }
        } 
      }
    } catch (LibException ex) {
      this.kvmInterface.setClickFlag(false);
      if ("IO_ERRCODE".equals(ex.getErrCode()))
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
            .getKvmUtil().getString("Network_interrupt_message"));
      }
    }
    catch (Exception ex1) {
      this.kvmInterface.setClickFlag(false);
      LoggerUtil.error(ex1.getClass().getName());
      return;
    } 
    this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
  }
  public void releaseKVMToolBar() {
    if (this.dynamicBlade) {
      if (this.kvmInterface.getBladeList() == null) {
        return;
      }
      JButton blade = null;
      Iterator<Object> iter = this.kvmInterface.getBladeList().iterator();
      while (iter.hasNext()) {
        blade = ((InterfaceContainer)iter.next()).getBladeButton();
        if (blade == null) {
          continue;
        }
        blade.removeActionListener(this);
      } 
      this.kvmInterface.getBladeList().clear();
      this.kvmInterface.setBladeList(null);
    } 
    this.disConnectBladeButton.removeActionListener(this.kvmInterface.disConnBladeAction());
    if (this.mouseSynButton != null)
      this.mouseSynButton.removeActionListener(this.kvmInterface.synMouseAction()); 
    this.fullButton.removeActionListener(this.kvmInterface.fullScreenAction());
    this.refreshButton.removeActionListener(this.kvmInterface.refreshAction());
    this.combineKey.removeActionListener(this.kvmInterface.shortCutAction());
    if (this.setColorBit != null) {
      this.setColorBit.removeActionListener(this.kvmInterface.colorBitAction());
    }
    this.numColorButton = null;
    this.capsColorButton = null;
    this.scrollColorButton = null;
    this.disConnectBladeButton = null;
    this.fullButton = null;
    this.refreshButton = null;
    this.combineKey = null;
    this.setColorBit = null;
  }
}
