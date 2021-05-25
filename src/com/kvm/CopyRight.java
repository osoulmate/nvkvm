package com.kvm;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
class CopyRight
  extends JFrame
{
  private static final long serialVersionUID = 1L;
  private JPanel loginPanel;
  private JLabel backgroundLable;
  private JLabel versionsLabel;
  private JLabel rightLabel;
  private JLabel titleLabel;
  private JLabel contentLabel;
  private JButton sureButton;
  private JLabel jLogo;
  public CopyRight() {
    init();
    setTitle("About");
    setDefaultCloseOperation(2);
    setLayout((LayoutManager)null);
    setBounds(0, 0, GetDiffOSPara.getCopyrightLength(), GetDiffOSPara.getCopyrightWidth());
    Image image = (new ImageIcon(getClass().getResource("resource/images/vconsole.png"))).getImage();
    setIconImage(image);
    setResizable(false);
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int screenWidth = screenSize.width / 2;
    int screenHeight = screenSize.height / 2;
    int height = getHeight();
    int width = getWidth();
    setLocation(screenWidth - width / 2, screenHeight - height / 2);
    setVisible(true);
  }
  public void init() {
    String KVMButtonName = "OK";
    Container container = getContentPane();
    this.loginPanel = new JPanel();
    Image image = (new ImageIcon(getClass().getResource("resource/images/bak.JPG"))).getImage();
    this.loginPanel = new BackgroundPanel(image);
    this.loginPanel.setLayout((LayoutManager)null);
    this.loginPanel.setBounds(0, 0, GetDiffOSPara.getCopyrightLength(), GetDiffOSPara.getCopyrightWidth());
    this.loginPanel.setName("LoginPanel_CR");
    this.backgroundLable = new JLabel();
    this.backgroundLable.setBounds(0, 0, GetDiffOSPara.getCopyrightLength(), GetDiffOSPara.getCopyrightWidth());
    this.backgroundLable.setName("BackgroundLable_CR");
    this.jLogo = new JLabel();
    Image image2 = (new ImageIcon(getClass().getResource("resource/images/logo.jpg"))).getImage();
    this.jLogo.setIcon(new ImageIcon(image2));
    this.jLogo.setBounds(18, 14, 45, 45);
    this.titleLabel = new JLabel("nvkvm");
    this.titleLabel.setBounds(65, 28, 220, 20);
    this.titleLabel.setFont(new Font("Times New Roman", 1, 11));
    this.versionsLabel = new JLabel("V1.0.0");
    this.versionsLabel.setBounds(70, 58, 200, 20);
    this.versionsLabel.setFont(new Font("Times New Roman", 0, 10));
    this.rightLabel = new JLabel("Copyright @ 2021");
    this.rightLabel.setBounds(70, 78, 200, 20);
    this.rightLabel.setFont(new Font("Times New Roman", 0, 10));
    this.contentLabel = new JLabel("Author by osoulmate");
    this.contentLabel.setBounds(70, 98, 200, 20);
    this.contentLabel.setFont(new Font("Times New Roman", 0, 10));
    this.sureButton = new JButton(KVMButtonName);
    this.sureButton.setBounds(190, 135, 60, 20);
    this.sureButton.setFocusPainted(false);
    this.sureButton.setFont(new Font("Times new roman", 0, 12));
    this.sureButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            CopyRight.this.disposeFrame();
          }
        });
    this.loginPanel.add(this.sureButton);
    this.loginPanel.add(this.titleLabel);
    this.loginPanel.add(this.versionsLabel);
    this.loginPanel.add(this.rightLabel);
    this.loginPanel.add(this.contentLabel);
    this.loginPanel.add(this.jLogo);
    this.backgroundLable.add(this.loginPanel);
    container.add(this.backgroundLable);
  }
  private void disposeFrame() {
    dispose();
  }
}
