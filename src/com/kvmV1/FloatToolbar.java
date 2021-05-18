package com.kvmV1;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
public class FloatToolbar
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  public KVMInterface kvmInterface;
  private VirtualMedia virtualMedia = null;
  public ImagePane imagePanel = null;
  public FloatToolbar floatToolbar = null;
  public int imgwidth = 0;
  public int imgheight = 0;
  public int newimgwidth = 0;
  public int newimgheight = 0;
  public JButton btnCDMenu;
  public JButton btnFlpMenu;
  public JButton btnCreateImage;
  public JButton btnShow;
  public boolean isShowPanel = false;
  public boolean isShowingCD = false;
  public boolean isShowingFlp = false;
  public boolean isShowingImagep = false;
  public static final int KVMMENU_WIDTH = 425;
  public static final int KVMMENU_WIDTH_OTHER = 235;
  public static final int KVMMENU_HEIGTH = 24;
  public boolean flag = true;
  public JButton powerMenuButton;
  public PowerPopupMenu powerMenu;
  public JButton helpButton;
  public JFrame helpFrm;
  public HelpDocument help;
  public HelpDocument helpMM;
  public JFrame helpMMFrm;
  public FloatToolbar(ImagePane imagePanel, VirtualMedia virtualMedia, KVMInterface kvmInterface) {
    this.imagePanel = imagePanel;
    this.virtualMedia = virtualMedia;
    this.kvmInterface = kvmInterface;
    this.floatToolbar = this;
    setLayout((LayoutManager)null);
    if (null != virtualMedia) {
      setSize(425, 24);
    }
    else {
      setSize(235, 24);
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
    createHelpButton();
    if (kvmInterface.getBladeSize() == 1)
    {
      createImageButton();
    }
    init();
    imagePanel.add(this);
    addMenuPanle();
    startStateMenu();
    this.floatToolbar.addMouseListener(kvmMenuMonuseListener());
    if (null != virtualMedia) {
      virtualMedia.flp.addMouseListener(vmmMonuseListener());
      virtualMedia.cdp.addMouseListener(vmmMonuseListener());
    } 
    if (kvmInterface.getBladeSize() == 1)
    {
      kvmInterface.imageFile.addMouseListener(vmmMonuseListener());
    }
    if (KVMUtil.isLinuxOS() && kvmInterface.getBladeSize() == 1)
    {
      this.btnCreateImage.setEnabled(false);
    }
  }
  public JButton createHelpButton() {
    this.helpButton = createButton("help_document", "resource/images/help.gif");
    this.helpButton.addActionListener(createHelpAction());
    this.helpButton.setBackground(new Color(158, 202, 232));
    this.helpButton.setBorder((Border)null);
    return this.helpButton;
  }
  private Action createHelpAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (null == FloatToolbar.this.helpFrm) {
            FloatToolbar.this.getBMCHelpDocument();
          }
          else {
            FloatToolbar.this.helpFrm.setAlwaysOnTop(true);
            FloatToolbar.this.helpFrm.setAlwaysOnTop(false);
          } 
        }
      };
    return action;
  }
  public JButton createPowerButton() {
    this.powerMenuButton = createButton("Power_Management", "resource/images/dev_com_power.gif");
    this.powerMenu = new PowerPopupMenu(this.kvmInterface);
    this.powerMenuButton.setBackground(new Color(158, 202, 232));
    this.powerMenuButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            if (null != FloatToolbar.this.virtualMedia) {
              FloatToolbar.this.virtualMedia.cdp.setVisible(false);
              FloatToolbar.this.virtualMedia.flp.setVisible(false);
              FloatToolbar.this.isShowingCD = false;
              FloatToolbar.this.isShowingFlp = false;
            } 
            if (FloatToolbar.this.kvmInterface.getBladeSize() == 1) {
              FloatToolbar.this.kvmInterface.imageFile.setVisible(false);
              FloatToolbar.this.btnCreateImage.setBorder((Border)null);
              FloatToolbar.this.isShowingImagep = false;
            } 
            if (null != FloatToolbar.this.virtualMedia) {
              FloatToolbar.this.btnCDMenu.setBorder((Border)null);
              FloatToolbar.this.btnFlpMenu.setBorder((Border)null);
            } 
            FloatToolbar.this.powerMenu.show(FloatToolbar.this.imagePanel, FloatToolbar.this.powerMenuButton.getX() + FloatToolbar.this.floatToolbar.getX(), FloatToolbar.this.floatToolbar.getHeight() + FloatToolbar.this.kvmInterface.vv - 1);
          }
        });
    return this.powerMenuButton;
  }
  public void init() {
    if (null != this.virtualMedia) {
      add(this.btnCDMenu);
      add(this.btnFlpMenu);
    } 
    add(this.btnShow);
    if (this.powerMenuButton != null)
    {
      add(this.powerMenuButton);
    }
    this.btnShow.setBounds(22, 2, 20, 16);
    if (this.kvmInterface.getBladeSize() == 1) {
      if (KVMUtil.isWindowsOS())
      {
        if (null != this.virtualMedia) {
          this.btnCDMenu.setBackground(new Color(158, 202, 232));
          this.btnCDMenu.setBounds(250, 0, 23, 23);
          this.btnFlpMenu.setBackground(new Color(158, 202, 232));
          this.btnFlpMenu.setBounds(275, 0, 23, 23);
        } 
        add(this.kvmInterface.toolbar.combineKey);
        add(this.kvmInterface.toolbar.fullButton);
        add(this.kvmInterface.toolbar.numColorButton);
        add(this.kvmInterface.toolbar.capsColorButton);
        add(this.kvmInterface.toolbar.scrollColorButton);
        this.kvmInterface.toolbar.combineKey.setBounds(70, 1, 20, 20);
        this.kvmInterface.toolbar.fullButton.setBounds(95, 1, 20, 20);
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name").toLowerCase();
        if (os.indexOf("2012") > 0 || os.indexOf("8") > 0 || os.indexOf("nt") > 0) {
          JLabel lblnum = new JLabel("num");
          lblnum.setBounds(120, 0, 20, 20);
          add(lblnum);
          this.kvmInterface.toolbar.numColorButton.setBounds(145, 5, 10, 10);
          JLabel lblcaps = new JLabel("caps");
          lblcaps.setBounds(160, 0, 25, 20);
          add(lblcaps);
          this.kvmInterface.toolbar.capsColorButton.setBounds(188, 5, 10, 10);
          JLabel lblscroll = new JLabel("scroll");
          lblscroll.setBounds(203, 0, 27, 20);
          add(lblscroll);
          this.kvmInterface.toolbar.scrollColorButton.setBounds(233, 5, 10, 10);
        }
        else {
          JLabel lblnum = new JLabel("num");
          lblnum.setBounds(120, 0, 20, 20);
          add(lblnum);
          this.kvmInterface.toolbar.numColorButton.setBounds(140, 5, 10, 10);
          JLabel lblcaps = new JLabel("caps");
          lblcaps.setBounds(155, 0, 27, 20);
          add(lblcaps);
          this.kvmInterface.toolbar.capsColorButton.setBounds(182, 5, 10, 10);
          JLabel lblscroll = new JLabel("scroll");
          lblscroll.setBounds(197, 0, 38, 20);
          add(lblscroll);
          this.kvmInterface.toolbar.scrollColorButton.setBounds(235, 5, 10, 10);
        } 
        this.btnCreateImage.setBackground(new Color(158, 202, 232));
        this.btnCreateImage.setBounds(300, 0, 23, 23);
        add(this.btnCreateImage);
        if (this.powerMenuButton != null) {
          this.powerMenuButton.setBackground(new Color(158, 202, 232));
          this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder());
          this.powerMenuButton.setBounds(325, 0, 20, 20);
        } 
        add(this.helpButton);
        this.helpButton.setBackground(new Color(158, 202, 232));
        this.helpButton.setBorder(BorderFactory.createEmptyBorder());
        this.helpButton.setBounds(350, 0, 20, 20);
      }
      else if (KVMUtil.isLinuxOS())
      {
        if (null != this.virtualMedia) {
          this.btnCDMenu.setBackground(new Color(158, 202, 232));
          this.btnCDMenu.setBounds(262, 0, 23, 23);
          this.btnFlpMenu.setBackground(new Color(158, 202, 232));
          this.btnFlpMenu.setBounds(287, 0, 23, 23);
        } 
        add(this.kvmInterface.toolbar.combineKey);
        add(this.kvmInterface.toolbar.fullButton);
        add(this.kvmInterface.toolbar.numColorButton);
        add(this.kvmInterface.toolbar.capsColorButton);
        add(this.kvmInterface.toolbar.scrollColorButton);
        this.kvmInterface.toolbar.combineKey.setBounds(70, 1, 20, 20);
        this.kvmInterface.toolbar.fullButton.setBounds(95, 1, 20, 20);
        JLabel lblnum = new JLabel("num");
        lblnum.setBounds(120, 0, 30, 20);
        add(lblnum);
        this.kvmInterface.toolbar.numColorButton.setBounds(150, 5, 10, 10);
        JLabel lblcaps = new JLabel("caps");
        lblcaps.setBounds(165, 0, 35, 20);
        add(lblcaps);
        this.kvmInterface.toolbar.capsColorButton.setBounds(195, 5, 10, 10);
        JLabel lblscroll = new JLabel("scroll");
        lblscroll.setBounds(210, 0, 40, 20);
        add(lblscroll);
        this.kvmInterface.toolbar.scrollColorButton.setBounds(247, 5, 10, 10);
        this.btnCreateImage.setBackground(new Color(158, 202, 232));
        this.btnCreateImage.setBounds(312, 0, 23, 23);
        add(this.btnCreateImage);
        if (this.powerMenuButton != null) {
          this.powerMenuButton.setBackground(new Color(158, 202, 232));
          this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder());
          this.powerMenuButton.setBounds(337, 0, 20, 20);
        } 
        add(this.helpButton);
        this.helpButton.setBackground(new Color(158, 202, 232));
        this.helpButton.setBorder(BorderFactory.createEmptyBorder());
        this.helpButton.setBounds(362, 0, 20, 20);
      }
    }
    else if (null != this.virtualMedia) {
      this.btnCDMenu.setBorder(BorderFactory.createEmptyBorder());
      this.btnCDMenu.setBounds(215, 0, 23, 23);
      this.btnFlpMenu.setBorder(BorderFactory.createEmptyBorder());
      this.btnFlpMenu.setBounds(240, 0, 23, 23);
      if (this.powerMenuButton != null)
      {
        this.powerMenuButton.setBackground(new Color(158, 202, 232));
        this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder());
        this.powerMenuButton.setBounds(340, 0, 20, 20);
      }
    }
    else if (this.powerMenuButton != null) {
      this.powerMenuButton.setBackground(new Color(158, 202, 232));
      this.powerMenuButton.setBorder(BorderFactory.createEmptyBorder());
      this.powerMenuButton.setBounds(170, 0, 20, 20);
    } 
    if (null != this.virtualMedia) {
      this.btnCDMenu.setContentAreaFilled(false);
      this.btnFlpMenu.setContentAreaFilled(false);
    } 
    if (this.powerMenuButton != null)
    {
      this.powerMenuButton.setContentAreaFilled(false);
    }
    if (this.kvmInterface.getBladeSize() == 1) {
      this.btnCreateImage.setContentAreaFilled(false);
      this.btnCreateImage.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FloatToolbar.this.btnCreateImage.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
              FloatToolbar.this.btnCreateImage.setContentAreaFilled(false);
            }
          });
    } 
    if (null != this.virtualMedia) {
      this.btnCDMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FloatToolbar.this.btnCDMenu.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
              FloatToolbar.this.btnCDMenu.setContentAreaFilled(false);
            }
          });
      this.btnFlpMenu.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FloatToolbar.this.btnFlpMenu.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
              FloatToolbar.this.btnFlpMenu.setContentAreaFilled(false);
            }
          });
    } 
    if (this.powerMenuButton != null)
    {
      this.powerMenuButton.addMouseListener(new MouseAdapter()
          {
            public void mouseEntered(MouseEvent e)
            {
              FloatToolbar.this.powerMenuButton.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
              FloatToolbar.this.powerMenuButton.setContentAreaFilled(false);
            }
          });
    }
    this.helpButton.setContentAreaFilled(false);
    this.helpButton.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            FloatToolbar.this.helpButton.setContentAreaFilled(true);
          }
          public void mouseExited(MouseEvent e) {
            FloatToolbar.this.helpButton.setContentAreaFilled(false);
          }
        });
    this.btnShow.setContentAreaFilled(false);
    this.btnShow.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            FloatToolbar.this.btnShow.setContentAreaFilled(true);
          }
          public void mouseExited(MouseEvent e) {
            FloatToolbar.this.btnShow.setContentAreaFilled(false);
          }
        });
  }
  public MouseListener vmmMonuseListener() {
    MouseAdapter adapter = new MouseAdapter()
      {
        public void mouseEntered(MouseEvent e)
        {
          if (!FloatToolbar.this.kvmInterface.isFullScreen) {
            FloatToolbar.this.imagePanel.kvmInterface.setCursor(FloatToolbar.this.kvmInterface.base.defCursor);
            FloatToolbar.this.imagePanel.setCursor(FloatToolbar.this.kvmInterface.base.defCursor);
          } 
        }
      };
    return adapter;
  }
  public MouseListener kvmMenuMonuseListener() {
    MouseAdapter adapter = new MouseAdapter()
      {
        public void mouseEntered(MouseEvent e)
        {
          if (!FloatToolbar.this.kvmInterface.isFullScreen) {
            FloatToolbar.this.imagePanel.kvmInterface.setCursor(FloatToolbar.this.kvmInterface.base.defCursor);
            FloatToolbar.this.imagePanel.setCursor(FloatToolbar.this.kvmInterface.base.defCursor);
          } 
        }
        public void mousePressed(MouseEvent e) {
          if (!FloatToolbar.this.kvmInterface.isFullScreen) {
            if (null != FloatToolbar.this.virtualMedia) {
              if (FloatToolbar.this.virtualMedia.flp.isShowing())
              {
                if (e.getX() <= FloatToolbar.this.virtualMedia.flp.getX() || e.getX() >= FloatToolbar.this.virtualMedia.flp.getWidth() + FloatToolbar.this.virtualMedia.flp.getX() || e.getY() <= FloatToolbar.this.virtualMedia.flp.getY() || e.getY() >= FloatToolbar.this.virtualMedia.flp.getY() + FloatToolbar.this.virtualMedia.flp.getHeight()) {
                  FloatToolbar.this.virtualMedia.flp.setVisible(false);
                  FloatToolbar.this.floatToolbar.isShowingFlp = false;
                  FloatToolbar.this.floatToolbar.btnFlpMenu.setBorder((Border)null);
                } 
              }
              if (FloatToolbar.this.virtualMedia.cdp.isShowing())
              {
                if (e.getX() <= FloatToolbar.this.virtualMedia.cdp.getX() || e.getX() >= FloatToolbar.this.virtualMedia.cdp.getWidth() + FloatToolbar.this.virtualMedia.cdp.getX() || e.getY() <= FloatToolbar.this.virtualMedia.cdp.getY() || e.getY() >= FloatToolbar.this.virtualMedia.cdp.getY() + FloatToolbar.this.virtualMedia.cdp.getHeight()) {
                  FloatToolbar.this.virtualMedia.cdp.setVisible(false);
                  FloatToolbar.this.kvmInterface.floatToolbar.isShowingCD = false;
                  FloatToolbar.this.kvmInterface.floatToolbar.btnCDMenu.setBorder((Border)null);
                } 
              }
            } 
            if (FloatToolbar.this.kvmInterface.getBladeSize() == 1 && FloatToolbar.this.kvmInterface.imageFile.isShowing())
            {
              if (e.getX() <= FloatToolbar.this.kvmInterface.imageFile.getX() || e.getX() >= FloatToolbar.this.kvmInterface.imageFile.getWidth() + FloatToolbar.this.kvmInterface.imageFile.getX() || e.getY() <= FloatToolbar.this.kvmInterface.imageFile.getY() || e.getY() >= FloatToolbar.this.kvmInterface.imageFile.getY() + FloatToolbar.this.kvmInterface.imageFile.getHeight()) {
                FloatToolbar.this.kvmInterface.imageFile.setVisible(false);
                FloatToolbar.this.floatToolbar.isShowingImagep = false;
                FloatToolbar.this.floatToolbar.btnCreateImage.setBorder((Border)null);
              } 
            }
          } 
        }
      };
    return adapter;
  }
  public void startStateMenu() {
    if (this.kvmInterface.isFullScreen) {
      setVisible(false);
      return;
    } 
    if (!this.kvmInterface.isFullScreen && this.imagePanel.width != 0 && this.imagePanel.width == this.imgwidth) {
      if (this.kvmInterface.isReturnToWin) {
        setVisible(true);
        setLocationToPanel();
        this.kvmInterface.isReturnToWin = false;
      } 
      return;
    } 
    if (this.imagePanel.width != 0 && this.imagePanel.width != this.imgwidth) {
      this.imgwidth = this.imagePanel.width;
      this.imgheight = this.imagePanel.height;
    }
    else {
      this.imgwidth = 1024;
      this.imgheight = 768;
    } 
    setLocationToPanel();
  }
  public void setLocationToPanel() {
    if (this.imgwidth > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
      setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2 + this.kvmInterface.h, -1 + this.kvmInterface.vv);
      if (null != this.virtualMedia) {
        this.virtualMedia.flp.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.virtualMedia.flp.getWidth()) / 2 + this.kvmInterface.h, getHeight() + this.kvmInterface.vv - 1);
        this.virtualMedia.cdp.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.virtualMedia.cdp.getWidth()) / 2 + this.kvmInterface.h, getHeight() + this.kvmInterface.vv - 1);
      } 
      if (this.kvmInterface.getBladeSize() == 1)
      {
        Toolkit.getDefaultToolkit();
        this.kvmInterface.imageFile.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.kvmInterface.imageFile.getWidth()) / 2 + this.kvmInterface.h, getHeight() + this.kvmInterface.vv - 1);
      }
    }
    else {
      setLocation((this.imgwidth - getWidth()) / 2 + this.kvmInterface.h, -1 + this.kvmInterface.vv);
      if (null != this.virtualMedia) {
        this.virtualMedia.flp.setLocation((this.imgwidth - this.virtualMedia.flp.getWidth()) / 2 + this.kvmInterface.h, getHeight() + this.kvmInterface.vv - 1);
        this.virtualMedia.cdp.setLocation((this.imgwidth - this.virtualMedia.cdp.getWidth()) / 2 + this.kvmInterface.h, getHeight() + this.kvmInterface.vv - 1);
      } 
      if (this.kvmInterface.getBladeSize() == 1)
      {
        this.kvmInterface.imageFile.setLocation((this.imgwidth - this.kvmInterface.imageFile.getWidth()) / 2 + this.kvmInterface.h, getHeight() + this.kvmInterface.vv - 1);
      }
    } 
    setVisible(true);
    if (null != this.virtualMedia) {
      this.virtualMedia.flp.setVisible(false);
      this.virtualMedia.cdp.setVisible(false);
      this.btnCDMenu.setBorder((Border)null);
      this.btnFlpMenu.setBorder((Border)null);
    } 
    if (this.kvmInterface.getBladeSize() == 1) {
      this.kvmInterface.imageFile.setVisible(false);
      this.btnCreateImage.setBorder((Border)null);
    } 
    if (null != this.floatToolbar && null != this.floatToolbar.powerMenu)
    {
      this.floatToolbar.powerMenu.setVisible(false);
    }
    this.isShowPanel = true;
  }
  public void fullStateMenu() {
    if (null != this.virtualMedia) {
      this.virtualMedia.cdp.setVisible(false);
      this.virtualMedia.flp.setVisible(false);
    } 
    setVisible(false);
  }
  private void addMenuPanle() {
    if (null != this.virtualMedia) {
      this.imagePanel.add(this.virtualMedia.cdp);
      this.imagePanel.add(this.virtualMedia.flp);
    } 
    if (this.kvmInterface.getBladeSize() == 1)
    {
      this.imagePanel.add(this.kvmInterface.imageFile);
    }
  }
  private JButton createButton(String resCode, String imageURL) {
    JButton button = new JButton(new ImageIcon(getClass().getResource(imageURL)));
    button.setToolTipText(this.kvmInterface.kvmUtil.getString(resCode));
    return button;
  }
  private JButton createFloatButton() {
    this.btnShow = createButton("button_float", "resource/images/float.gif");
    this.btnShow.addActionListener(createFloatAction());
    this.btnShow.setBackground(new Color(158, 202, 232));
    this.btnShow.setBorder((Border)null);
    return this.btnShow;
  }
  private JButton createImageButton() {
    this.btnCreateImage = createButton("create_image", "resource/images/virtualne.gif");
    this.btnCreateImage.addActionListener(createImageAction());
    this.btnShow.setBackground(new Color(158, 202, 232));
    return this.btnCreateImage;
  }
  private JButton createCDMenuButton() {
    this.btnCDMenu = createButton("cd_cdroms", "resource/images/cd.gif");
    this.btnCDMenu.setBorder((Border)null);
    this.btnCDMenu.addActionListener(createCDAction());
    this.btnCDMenu.setBackground(new Color(158, 202, 232));
    return this.btnCDMenu;
  }
  private JButton createFlpMenuButton() {
    this.btnFlpMenu = createButton("flp_floppy", "resource/images/flp.gif");
    this.btnFlpMenu.setBorder((Border)null);
    this.btnFlpMenu.addActionListener(createFlpAction());
    this.btnFlpMenu.setBackground(new Color(158, 202, 232));
    return this.btnFlpMenu;
  }
  private Action createImageAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (FloatToolbar.this.kvmInterface.getBladeSize() == 1)
          {
            if (FloatToolbar.this.kvmInterface.imageFile.isShowing()) {
              FloatToolbar.this.floatToolbar.isShowingImagep = false;
              FloatToolbar.this.kvmInterface.imageFile.setVisible(false);
              FloatToolbar.this.btnCreateImage.setBorder((Border)null);
            }
            else {
              if (null != FloatToolbar.this.virtualMedia) {
                if (FloatToolbar.this.floatToolbar.isShowingCD) {
                  FloatToolbar.this.floatToolbar.isShowingCD = false;
                  FloatToolbar.this.virtualMedia.cdp.setVisible(false);
                  FloatToolbar.this.floatToolbar.btnCDMenu.setBorder((Border)null);
                } 
                if (FloatToolbar.this.floatToolbar.isShowingFlp) {
                  FloatToolbar.this.floatToolbar.isShowingFlp = false;
                  FloatToolbar.this.virtualMedia.flp.setVisible(false);
                  FloatToolbar.this.floatToolbar.btnFlpMenu.setBorder((Border)null);
                } 
              } 
              FloatToolbar.this.floatToolbar.isShowingImagep = true;
              FloatToolbar.this.kvmInterface.imageFile.setVisible(true);
              FloatToolbar.this.btnCreateImage.setBorder(BorderFactory.createBevelBorder(1));
            } 
          }
        }
      };
    return action;
  }
  private Action createCDAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (FloatToolbar.this.virtualMedia.cdp.isShowing()) {
            FloatToolbar.this.isShowingCD = false;
            FloatToolbar.this.virtualMedia.cdp.setVisible(false);
            FloatToolbar.this.btnCDMenu.setBorder((Border)null);
          }
          else {
            if (FloatToolbar.this.isShowingFlp) {
              FloatToolbar.this.isShowingFlp = false;
              FloatToolbar.this.virtualMedia.flp.setVisible(false);
              FloatToolbar.this.btnFlpMenu.setBorder((Border)null);
            } 
            if (FloatToolbar.this.isShowingImagep)
            {
              if (FloatToolbar.this.kvmInterface.getBladeSize() == 1) {
                FloatToolbar.this.isShowingImagep = false;
                FloatToolbar.this.kvmInterface.imageFile.setVisible(false);
                FloatToolbar.this.btnCreateImage.setBorder((Border)null);
              } 
            }
            FloatToolbar.this.isShowingCD = true;
            FloatToolbar.this.virtualMedia.cdp.setVisible(true);
            FloatToolbar.this.btnCDMenu.setBorder(BorderFactory.createBevelBorder(1));
          } 
        }
      };
    return action;
  }
  private Action createFlpAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (FloatToolbar.this.virtualMedia.flp.isShowing()) {
            FloatToolbar.this.isShowingFlp = false;
            FloatToolbar.this.virtualMedia.flp.setVisible(false);
            FloatToolbar.this.btnFlpMenu.setBorder((Border)null);
          }
          else {
            if (FloatToolbar.this.isShowingCD) {
              FloatToolbar.this.isShowingCD = false;
              FloatToolbar.this.virtualMedia.cdp.setVisible(false);
              FloatToolbar.this.btnCDMenu.setBorder((Border)null);
            } 
            if (FloatToolbar.this.isShowingImagep)
            {
              if (FloatToolbar.this.kvmInterface.getBladeSize() == 1) {
                FloatToolbar.this.isShowingImagep = false;
                FloatToolbar.this.kvmInterface.imageFile.setVisible(false);
                FloatToolbar.this.btnCreateImage.setBorder((Border)null);
              } 
            }
            FloatToolbar.this.isShowingFlp = true;
            FloatToolbar.this.virtualMedia.flp.setVisible(true);
            FloatToolbar.this.btnFlpMenu.setBorder(BorderFactory.createBevelBorder(1));
          } 
        }
      };
    return action;
  }
  private Action createFloatAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (!FloatToolbar.this.isShowPanel) {
            FloatToolbar.this.floatToolbar.setVisible(true);
            FloatToolbar.this.isShowPanel = true;
            FloatToolbar.this.btnShow.setIcon(new ImageIcon(getClass().getResource("resource/images/float.gif")));
          }
          else if (FloatToolbar.this.floatToolbar.isShowing()) {
            if (null != FloatToolbar.this.virtualMedia) {
              FloatToolbar.this.virtualMedia.flp.setVisible(false);
              FloatToolbar.this.virtualMedia.cdp.setVisible(false);
              FloatToolbar.this.isShowingCD = false;
              FloatToolbar.this.isShowingFlp = false;
              FloatToolbar.this.btnCDMenu.setBorder((Border)null);
              FloatToolbar.this.btnFlpMenu.setBorder((Border)null);
            } 
            if (FloatToolbar.this.kvmInterface.getBladeSize() == 1) {
              FloatToolbar.this.kvmInterface.imageFile.setVisible(false);
              FloatToolbar.this.isShowingImagep = false;
              FloatToolbar.this.btnCreateImage.setBorder((Border)null);
            } 
            FloatToolbar.this.floatToolbar.setVisible(false);
            FloatToolbar.this.isShowPanel = false;
            FloatToolbar.this.btnShow.setIcon(new ImageIcon(getClass().getResource("resource/images/float2.gif")));
          }
          else {
            FloatToolbar.this.floatToolbar.setVisible(true);
            FloatToolbar.this.isShowPanel = true;
            FloatToolbar.this.btnShow.setIcon(new ImageIcon(getClass().getResource("resource/images/float.gif")));
          } 
        }
      };
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
    this.virtualMedia.cdp.setVisible(cdflag);
    this.virtualMedia.flp.setVisible(flpflag);
  }
  public void setCDVisibleAndLocation(boolean cdflag, int x, int y) {
    this.virtualMedia.cdp.setVisible(cdflag);
    this.virtualMedia.cdp.setLocation(x, y);
  }
  public void setFlpVisibleAndLocation(boolean flpflag, int x, int y) {
    this.virtualMedia.flp.setVisible(flpflag);
    this.virtualMedia.flp.setLocation(x, y);
  }
  public void setCDVisible(boolean cdflag) {
    this.virtualMedia.cdp.setVisible(cdflag);
  }
  public void setFlpVisible(boolean flpflag) {
    this.virtualMedia.flp.setVisible(flpflag);
  }
  public void setCDLocation(int x, int y) {
    this.virtualMedia.cdp.setLocation(x, y);
  }
  public void setFlpLocation(int x, int y) {
    this.virtualMedia.flp.setLocation(x, y);
  }
  public boolean isVirtualMedia() {
    if (null != this.virtualMedia)
    {
      return true;
    }
    return false;
  }
  public int getCDX() {
    return this.virtualMedia.cdp.getX();
  }
  public int getCDY() {
    return this.virtualMedia.cdp.getY();
  }
  public int getCDWidth() {
    return this.virtualMedia.cdp.getWidth();
  }
  public int getCDHeight() {
    return this.virtualMedia.cdp.getHeight();
  }
  public int getFlpX() {
    return this.virtualMedia.flp.getX();
  }
  public int getFlpY() {
    return this.virtualMedia.flp.getY();
  }
  public int getFlpWidth() {
    return this.virtualMedia.flp.getWidth();
  }
  public int getFlpHeight() {
    return this.virtualMedia.flp.getHeight();
  }
  public JPanel getCDPanel() {
    return this.virtualMedia.cdp;
  }
  public JPanel getFlpPanel() {
    return this.virtualMedia.flp;
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
    if (this.kvmInterface.getProductType().equals("OSCA")) {
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
            FloatToolbar.this.helpFrm = null;
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
}
