package com.kvm;
import com.library.LoggerUtil;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
public class Main
  extends JFrame
{
  private static final long serialVersionUID = 1L;
  private JTextField username;
  private JTextField ip_addr;
  private JPasswordField pwdJField;
  private JPanel loginPanel;
  private JLabel backgroundLable;
  private JLabel ipAddrLabel;
  private JLabel passwordLabel;
  private JLabel usernameLabel;
  private JComboBox ianguageComboBox;
  private JButton kvmButton;
  private static String getStrFromWeb = "";
  private String ianguage = "";
  private JRadioButton shareButton;
  private JRadioButton onlyButton;
  private JLabel noNetworkAddrRemind;
  private JLabel noUsernameRemind;
  private JLabel noPassword;
  private JLabel unSupportVendor;
  private JLabel networkAddrError;
  private JLabel wrongBMCversion;
  private JLabel loginRestricted;
  private JLabel userNoJurisdiction;
  private JLabel passwordOverdued;
  private JLabel userLocked;
  private JLabel loginFailed;
  private JLabel obtainProductInfoError;
  private ConsoleTextArea consoleTextArea;
  static int wordSize = 0;
  public static int getWordSize() {
    return wordSize;
  }
  public static void setWordSize(int wordSize) {
    Main.wordSize = wordSize;
  }
  private String fonts = "";
  private Object[] remindOption = new Object[] { "" };
  private JButton copyrightImagebutton;
  private LoginUtil loginUtil;
  public Main() {
    init();
    setTitle(this.loginUtil.getString("Login_Name"));
    setDefaultCloseOperation(3);
    setLayout((LayoutManager)null);
    //setBounds(0, 0, GetDiffOSPara.getLoginLength(), GetDiffOSPara.getLoginWidth());
    setBounds(0, 0, 400, 300);
    URL url = Main.class.getResource("resource/images/vconsole.png");
    Image image = (new ImageIcon(url)).getImage();
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
    Locale locale = Locale.getDefault();
    this.ianguage = locale.getLanguage();
    int IanguageIndex = 0;
    String IpAddrLabelName = "";
    String UsernameLabelName = "";
    String PwdLabelName = "";
    String[] IanguageSet = new String[2];
    String shareButtonName = "";
    String onlyButtonName = "";
    String KVMButtonName = "";
    this.loginUtil = new LoginUtil();
    this.loginUtil.setBundle(this.ianguage);
    this.remindOption[0] = this.loginUtil.getString("RemindOption");
    IpAddrLabelName = this.loginUtil.getString("IpAddr_Label_Name");
    UsernameLabelName = this.loginUtil.getString("Username_Label_Name");
    PwdLabelName = this.loginUtil.getString("Pwd_Label_Name");
    shareButtonName = this.loginUtil.getString("share_Button_Name");
    onlyButtonName = this.loginUtil.getString("only_Button_Name");
    KVMButtonName = this.loginUtil.getString("KVM_Button_Name");
    IanguageSet[0] = this.loginUtil.getString("Ianguage_en");
    IanguageSet[1] = this.loginUtil.getString("Ianguage_zh");
    GetDiffOSPara.osParaInit();
    this.fonts = this.loginUtil.getString("font_name");
    setWordSize(Integer.parseInt(this.loginUtil.getString("Word_Size")));
    Container container = getContentPane();
    this.loginPanel = new JPanel();
    //bak.JPG 426px x 268px
    Image image = (new ImageIcon(Main.class.getResource("resource/images/bak.JPG"))).getImage();
    this.loginPanel = new BackgroundPanel(image);
    this.loginPanel.setLayout((LayoutManager)null);
    this.loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
    //this.loginPanel.setBounds(5, 0, 338, 228);
    this.loginPanel.setBounds(1, 0, 400, 300);
    this.loginPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 1), "", 1, 2, new Font("Times new roman", 0, 12)));
    this.loginPanel.setName("LoginPanel");
    Image image1 = (new ImageIcon(Main.class.getResource("resource/images/floor.JPG"))).getImage();
    this.backgroundLable = new JLabel();
    this.backgroundLable.setIcon(new ImageIcon(image1));
    //this.backgroundLable.setBounds(0, 0, 348, 240);
    this.backgroundLable.setBounds(0, 0, 400, 300);
    this.backgroundLable.setName("BackgroundLable");
    this.ip_addr = new JTextField();
    this.ip_addr.setBounds(150, 50, 210, 20);
    this.ip_addr.setFont(new Font(this.fonts, 0, wordSize));
    this.ip_addr.setName("ipInput");
    this.ipAddrLabel = new JLabel(IpAddrLabelName);
    this.ipAddrLabel.setBounds(30, 50, 210, 20);
    this.ipAddrLabel.setFont(new Font(this.fonts, 0, wordSize));
    this.username = new JTextField();
    this.username.setBounds(150, 85, 210, 20);
    this.username.setFont(new Font(this.fonts, 0, wordSize));
    this.username.setForeground(Color.GRAY);
    this.username.setName("usernameInput");
    this.usernameLabel = new JLabel(UsernameLabelName);
    this.usernameLabel.setBounds(30, 85, 150, 20);
    this.usernameLabel.setFont(new Font(this.fonts, 0, wordSize));
    this.pwdJField = new JPasswordField();
    this.pwdJField.setBounds(150, 120, 210, 20);
    this.pwdJField.setName("pwdInput");
    this.passwordLabel = new JLabel(PwdLabelName);
    this.passwordLabel.setBounds(30, 120, 150, 20);
    this.passwordLabel.setFont(new Font(this.fonts, 0, wordSize));
    if (this.ianguage.equals("zh")) {
      IanguageIndex = 1;
    }
    else {
      IanguageIndex = 0;
    } 
    this.ianguageComboBox = new JComboBox<>(IanguageSet);
    if (KVMUtil.isMacOS()) {
      this.ianguageComboBox.setBounds(265, 10, 70, 24);
    }
    else {
      this.ianguageComboBox.setBounds(285, 10, 70, 24);
    } 
    this.ianguageComboBox.setOpaque(false);
    this.ianguageComboBox.setBackground(Color.white);
    this.ianguageComboBox.setSelectedIndex(IanguageIndex);
    this.kvmButton = new JButton(KVMButtonName);
    //this.kvmButton.setBounds(235, 190, 85, 25);
    this.kvmButton.setBounds(285, 155, 75, 25);
    this.kvmButton.setFont(new Font(this.fonts, 0, wordSize));
    this.kvmButton.setName("connectButton");
    this.shareButton = new JRadioButton(shareButtonName);
    this.shareButton.setBounds(28, 160, 100, 20);
    this.shareButton.setBorder((Border)null);
    this.shareButton.setContentAreaFilled(false);
    this.shareButton.setSelected(true);
    this.shareButton.setFont(new Font(this.fonts, 0, wordSize));
    this.shareButton.setName("shareButtonInput");
    this.onlyButton = new JRadioButton(onlyButtonName);
    this.onlyButton.setBounds(150, 160, 100, 20);
    this.onlyButton.setBorder((Border)null);
    this.onlyButton.setContentAreaFilled(false);
    this.onlyButton.setFont(new Font(this.fonts, 0, wordSize));
    this.onlyButton.setName("onlyButtonInput");
    ButtonGroup radioButtonGroup = new ButtonGroup();
    radioButtonGroup.add(this.shareButton);
    radioButtonGroup.add(this.onlyButton);
    remindMsgSet();
    this.copyrightImagebutton = new JButton();
    Image crImage = (new ImageIcon(Main.class.getResource("resource/images/CRhelp.png"))).getImage();
    this.copyrightImagebutton.setIcon(new ImageIcon(crImage));
    this.copyrightImagebutton.setBounds(15, 10, 20, 20);
    this.copyrightImagebutton.setBorder((Border)null);
    this.copyrightImagebutton.setContentAreaFilled(false);
    this.copyrightImagebutton.setName("CopyrightImagebuttonInput");
    /*
    try {
        this.consoleTextArea = new ConsoleTextArea();
        this.consoleTextArea.setFont(java.awt.Font.decode("monospaced"));
        //this.consoleTextArea.setBounds(2, 182, 379, 80);
        this.consoleTextArea.setForeground(Color.gray);
    }
    catch(IOException e) {
        System.err.println(
            "不能创建LoopedStreams：" + e);
        //System.exit(1);
    }*/
    this.ip_addr.addFocusListener(new FocusListener()
        {
          public void focusLost(FocusEvent e)
          {
            if (Main.this.ip_addr.getText().trim().equals("")) {
              Main.this.ip_addr.setText(Main.this.loginUtil.getString("Ip_addr_remind_text"));
              Main.this.ip_addr.setFont(new Font(Main.this.loginUtil.getString("font_name"), 0, Main.wordSize));
              Main.this.ip_addr.setForeground(Color.GRAY);
            } 
          }
          public void focusGained(FocusEvent e) {
            if (Main.this.ip_addr.getText().trim().equals(Main.this.loginUtil.getString("Ip_addr_remind_text"))) {
              Main.this.ip_addr.setText("");
              Main.this.ip_addr.setForeground(Color.BLACK);
              Main.this.ip_addr.setFont(new Font(Main.this.loginUtil.getString("font_name"), 0, Main.wordSize));
            } 
          }
        });
    this.username.addFocusListener(new FocusListener()
        {
          public void focusLost(FocusEvent e)
          {
            if (Main.this.username.getText().trim().equals("")) {
              Main.this.username.setText("");
              Main.this.username.setFont(new Font(Main.this.loginUtil.getString("font_name"), 0, Main.wordSize));
              Main.this.username.setForeground(Color.GRAY);
            } 
          }
          public void focusGained(FocusEvent e) {
        	//重新获取焦点不重置已输入的用户名
            //Main.this.username.setText("");
            Main.this.username.setForeground(Color.BLACK);
            Main.this.username.setFont(new Font(Main.this.loginUtil.getString("font_name"), 0, Main.wordSize));
          }
        });
    this.copyrightImagebutton.addActionListener(new CopyrightButton());
    this.kvmButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            String Addr = Main.this.ip_addr.getText();
            String user_name = Main.this.username.getText();
            String PwdStrIPMI = "";
            String PwdStrHTTPS = "";
            PwdStrIPMI = String.valueOf(Main.this.pwdJField.getPassword());
            LoggerUtil.info( "PwdStrIPMI: "+ PwdStrIPMI );
            System.out.println(user_name+" "+PwdStrIPMI);
            try {
              PwdStrHTTPS = URLEncoder.encode(String.valueOf(Main.this.pwdJField.getPassword()), "UTF-8");
              LoggerUtil.info( "PwdStrHTTPS: "+ PwdStrHTTPS );
            }
            catch (UnsupportedEncodingException e2) {
              LoggerUtil.error(e.getClass().getName());
            } 
            String KvmMode = "";
            if (Addr.equals("") || Addr.trim().equals(Main.this.loginUtil.getString("Ip_addr_remind_text"))) {
              JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                  .noNetworkAddrRemind, Main.this
                  .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                  .remindOption, Main.this
                  .remindOption[0]);
              return;
            } 
            if (user_name.equals("")) {
              JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                  .noUsernameRemind, Main.this
                  .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                  .remindOption, Main.this
                  .remindOption[0]);
              return;
            } 
            if (PwdStrIPMI.equals("") || PwdStrHTTPS.equals("")) {
              JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                  .noPassword, Main.this
                  .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                  .remindOption, Main.this
                  .remindOption[0]);
              return;
            } 
            if (Main.this.shareButton.isSelected()) {
              KvmMode = "0";
            }
            else {
              KvmMode = "1";
            } 
            int port = Main.this.getPortFromAddr(Addr);
            int portIpmi = Main.this.getIpmiPortFromAddr(Addr);
            String host = Main.this.getHostFromAddr(Addr);
            String hostIpmi = Main.this.getIpmiHostFromAddr(Addr);
            int firmVersion = 1;
            try {
            	if (Main.this.isAliveCheck(host)) {
            		Collect mycollect = new Collect(hostIpmi,user_name,PwdStrIPMI);
            		String vendor = mycollect.getVendor();
            		String model = mycollect.getModel();
            		String bmc_version = mycollect.getBmcVersion();
            		HashMap<String, String> extra = mycollect.getExtra();
            		LoggerUtil.info( "vendor:"+ vendor + ",model:"+model+",bmc_version:"+bmc_version+",extra:"+extra );
            		System.out.println("vendor:"+ vendor + ",model:"+model+",bmc_version:"+bmc_version+",extra:"+extra );
            		//非华为机型执行下述登陆方法
            		if (!vendor.equalsIgnoreCase("huawei")) {
            			ClientSubmitLoginCommon commonLogin = new ClientSubmitLoginCommon();
            			String [] loginResult = commonLogin.doLogin(vendor, model, bmc_version,extra,hostIpmi,user_name,PwdStrIPMI, KvmMode);
            			LoggerUtil.info( "loginResult:"+ loginResult[0]+","+loginResult[1]);
            			if(loginResult[0].equals("0")) {
            				//no match vendor
                            JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                                    .unSupportVendor, Main.this
                                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                                    .remindOption, Main.this
                                    .remindOption[0]);
                    		return;
            				
            			}else if (loginResult[0].equals("200")) {
            				System.out.println("Login in "+host+" Success!");
            				if(commonLogin.run()[0].equals("0")) {
                                JOptionPane.showOptionDialog(Main.this.loginPanel,loginResult[1] , Main.this
                                        .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                                        .remindOption, Main.this
                                        .remindOption[0]);
                        		return;
            				}
            			}
            		  return;
            		}
            	}else {
                    JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                            .networkAddrError, Main.this
                            .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                            .remindOption, Main.this
                            .remindOption[0]);
            		return;
            	}
            }catch (Exception e1) {
            	LoggerUtil.error(e1.getClass().getName());
            	return;
            }
            ClientSubmitLoginIofo clientformloginoforosc = new ClientSubmitLoginIofo();
            try {
              Main.getStrFromWeb = clientformloginoforosc.getParaFromWeb(host, user_name, PwdStrHTTPS, KvmMode, port, Main.this
                  .loginUtil);
            }catch (Exception e1) {
              JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                  .networkAddrError, Main.this
                  .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                  .remindOption, Main.this
                  .remindOption[0]);
              return;
            } 
            if (Main.getStrFromWeb.equals("gologin")) {
              return;
            }
            LoggerUtil.info( "Main.getStrFromWeb: "+ Main.getStrFromWeb );
            if (Main.getStrFromWeb.equals("")) {
              LoginAuthentication clientformlogininforosc = new LoginAuthentication();
              Main.this.ipmiLoginMethod(hostIpmi, user_name, PwdStrIPMI, KvmMode, portIpmi, clientformlogininforosc, Main.this
                  .loginUtil);
              FirmwareRevision firmwareRevision = new FirmwareRevision();
              try {
                firmVersion = firmwareRevision.getRevision(hostIpmi, user_name, PwdStrIPMI, portIpmi);
                LoggerUtil.info( "firmVersion: "+ firmVersion );
              }catch (Exception e3) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .obtainProductInfoError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              if (firmVersion == -1) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .networkAddrError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              if (firmVersion != 0) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .networkAddrError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              String local = Main.this.ianguage;
              String secureKvm = null;
              String productType = "BMC";
              String verifyValue = String.valueOf(clientformlogininforosc.getKeyNumber());
              String mmverifyValue = verifyValue;
              String portNum = "";
              String vmmPort = "";
              String compress = "";
              String vmm_compress = "";
              LoggerUtil.info( "verifyValue: "+ verifyValue );
              try {
                compress = clientformlogininforosc.getEncryInfo(hostIpmi, user_name, PwdStrIPMI, portIpmi, "0x02");
                vmm_compress = clientformlogininforosc.getEncryInfo(hostIpmi, user_name, PwdStrIPMI, portIpmi, "0x03");
                LoggerUtil.info( "compress: "+ compress );
                LoggerUtil.info( "vmm_compress: "+ vmm_compress );
                if (compress.equals("0") && vmm_compress.equals("0"))
                {
                  secureKvm = null;
                }
                else if (compress.equals("1") && vmm_compress.equals("0"))
                {
                  secureKvm = "1";
                }
                else if (compress.equals("0") && vmm_compress.equals("1"))
                {
                  secureKvm = "2";
                }
                else if (compress.equals("1") && vmm_compress.equals("1"))
                {
                  secureKvm = "3";
                }
              } catch (RuntimeException e1) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .obtainProductInfoError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } catch (Exception e1) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .obtainProductInfoError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              try {
                portNum = clientformlogininforosc.getKVMPort(hostIpmi, user_name, PwdStrIPMI, portIpmi, firmVersion, Main.this
                    .loginUtil);
                vmmPort = clientformlogininforosc.getVMMPort(hostIpmi, user_name, PwdStrIPMI, portIpmi, firmVersion, Main.this
                    .loginUtil);
              }
              catch (Exception e1) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .obtainProductInfoError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              if (portNum.equals("fal") || vmmPort.equals("fal")) {
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .obtainProductInfoError, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              String privilege = "4";
              String SerialNumber = "";
              String[] paraInput = { local, secureKvm, productType, verifyValue, mmverifyValue, portNum, vmmPort, privilege, hostIpmi };
              PwdStrIPMI = "";
              PwdStrHTTPS = "";
              Main.this.pwdJField.setText("");
              Main.this.disposeLoginFrame();
              OpenKvmWindow openKvm = new OpenKvmWindow();
              LoggerUtil.info( "paraInput: "+ local + ","+secureKvm+ ","+productType+ ","+verifyValue+ ","+mmverifyValue+ ","+portNum+ ","+vmmPort+ ","+privilege+ ","+hostIpmi );
              LoggerUtil.info( "hostIpmi, SerialNumber, firmVersion: "+ hostIpmi+ ","+SerialNumber+ ","+firmVersion );
              openKvm.setPara(paraInput, hostIpmi, SerialNumber, firmVersion);
              openKvm.start();
              verifyValue = "";
              mmverifyValue = "";
              privilege = "";
            }
            else {
              FindParaFromMsg FindEntity = new FindParaFromMsg();
              FindEntity.decodeJsonValue(Main.getStrFromWeb);
              FindEntity.decodeAddValue(Main.getStrFromWeb);
              String reqJson = FindEntity.findErrorCode();
              if (!reqJson.equals("0")) {
                if (reqJson.equals("144")) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .loginRestricted, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } 
                if (reqJson.equals("136")) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .userNoJurisdiction, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } 
                if (reqJson.equals("137")) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .passwordOverdued, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } 
                if (reqJson.equals("131")) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .userLocked, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } 
                JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                    .loginFailed, Main.this
                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                    .remindOption, Main.this
                    .remindOption[0]);
                return;
              } 
              String verifyValue = "";
              String mmVerifyValue = "";
              String decrykey = "";
              String privilege = "";
              String compress = "";
              String vmm_compress = "";
              String kvm_Port = "";
              String vmm_Port = "";
              String SerialNumber = "";
              String verifyValueExt = "";
              LoggerUtil.info( "firmVersion: "+ firmVersion );
              int flag = 0;
              try {
                verifyValue = FindEntity.findVerifyValue();
                mmVerifyValue = verifyValue;
                decrykey = FindEntity.findDecrykey();
                privilege = FindEntity.findPrivilege();
                compress = FindEntity.findCompress();
                vmm_compress = FindEntity.findVmmCompress();
                kvm_Port = FindEntity.findKvmPort();
                vmm_Port = FindEntity.findVmmPort();
                SerialNumber = FindEntity.findSerialNumber();
                verifyValueExt = FindEntity.findVerifyValueExt();
              }
              catch (RuntimeException e2) {
                flag = 1;
                LoginAuthentication clientformlogininforosc = new LoginAuthentication();
                Main.this.ipmiLoginMethod(hostIpmi, user_name, PwdStrIPMI, KvmMode, portIpmi, clientformlogininforosc, Main.this
                    .loginUtil);
                verifyValue = String.valueOf(clientformlogininforosc.getKeyNumber());
                mmVerifyValue = verifyValue;
                decrykey = "0000000000000000000000000000000000000000000000000000000000000000";
                privilege = "4";
                try {
                  compress = clientformlogininforosc.getEncryInfo(hostIpmi, user_name, PwdStrIPMI, portIpmi, "0x02");
                  if (compress.equals("fal")) {
                    JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                        .obtainProductInfoError, Main.this
                        .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                        .remindOption, Main.this
                        .remindOption[0]);
                    return;
                  } 
                  if (compress.equals("0")) {
                    vmm_compress = "0";
                  }
                  else {
                    vmm_compress = clientformlogininforosc.getEncryInfo(hostIpmi, user_name, PwdStrIPMI, portIpmi, "0x03");
                  } 
                  kvm_Port = clientformlogininforosc.getKVMPort(hostIpmi, user_name, PwdStrIPMI, portIpmi, firmVersion, Main.this
                      .loginUtil);
                  vmm_Port = clientformlogininforosc.getVMMPort(hostIpmi, user_name, PwdStrIPMI, portIpmi, firmVersion, Main.this
                      .loginUtil);
                }
                catch (RuntimeException e1) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .obtainProductInfoError, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } catch (Exception e1) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .obtainProductInfoError, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } 
                if (kvm_Port.equals("fal") || vmm_Port.equals("fal")) {
                  JOptionPane.showOptionDialog(Main.this.loginPanel, Main.this
                      .obtainProductInfoError, Main.this
                      .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                      .remindOption, Main.this
                      .remindOption[0]);
                  return;
                } 
                SerialNumber = "";
                verifyValueExt = "";
              }
              catch (Exception e2) {
                LoggerUtil.error(e.getClass().getName());
              } 
              String[] ParaInput = {verifyValue, mmVerifyValue, decrykey, Main.this.ianguage, 
            		  compress, vmm_compress, kvm_Port, vmm_Port, privilege, host, host, verifyValueExt};
              PwdStrHTTPS = "";
              PwdStrIPMI = "";
              Main.this.pwdJField.setText("");
              Main.this.disposeLoginFrame();
              OpenKvmWindow openKvm = new OpenKvmWindow();
              LoggerUtil.info( "paraInput: "+ ParaInput );
              openKvm.setPara(ParaInput, host, SerialNumber, firmVersion);
              openKvm.start();
              verifyValue = "";
              mmVerifyValue = "";
              decrykey = "";
              privilege = "";
            } 
          }
        });
    this.ianguageComboBox.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            int index = Main.this.ianguageComboBox.getSelectedIndex();
            if (1 == index) {
                Main.this.ianguage = "zh";
                JOptionPane.showMessageDialog(null, "If the current OS does not contain the Chinese character set, \n"
                		+ "garbled characters will be displayed after you switch to Chinese. ");
            }
            else {
            	 Main.this.ianguage = "en";
            } 
            String ipTempString = Main.this.loginUtil.getString("Ip_addr_remind_text");
            Main.this.loginUtil.setBundle(Main.this.ianguage);
            Main.setWordSize(Integer.parseInt(Main.this.loginUtil.getString("Word_Size")));
            if (Main.this.ip_addr.getText().trim().equals(ipTempString)) {
              Main.this.ip_addr.setText(Main.this.loginUtil.getString("Ip_addr_remind_text"));
              Main.this.ip_addr.setForeground(Color.GRAY);
              Main.this.ip_addr.setFont(new Font(Main.this.loginUtil.getString("font_name"), 0, Main.wordSize));
            } 
            Main.this.remindOption[0] = Main.this.loginUtil.getString("RemindOption");
            Main.this.widgetMsgUpdate();
            Main.this.remindMsgSet();
            Main.this.changeTitle();
          }
        });
    this.loginPanel.add(this.ipAddrLabel);
    this.loginPanel.add(this.passwordLabel);
    this.backgroundLable.add(this.ianguageComboBox);
    this.backgroundLable.add(this.copyrightImagebutton);
    this.backgroundLable.add(this.kvmButton);
    this.loginPanel.add(this.ip_addr);
    this.loginPanel.add(this.username);
    this.loginPanel.add(this.pwdJField);
    this.loginPanel.add(this.usernameLabel);
    this.loginPanel.add(this.shareButton);
    this.loginPanel.add(this.onlyButton);
    //this.loginPanel.add(this.consoleTextArea);
    /*
    ScrollPane sp=new ScrollPane();
    sp.add(this.consoleTextArea);
    sp.setBounds(2, 182, 379, 80);
    this.loginPanel.add(sp);
    */
    javax.swing.JScrollPane scrollPane; 
    javax.swing.JTextArea textArea;
    scrollPane = new javax.swing.JScrollPane();
    textArea = new javax.swing.JTextArea();
    textArea.setColumns(20); 
    textArea.setRows(5);
    scrollPane.setViewportView(textArea);
    scrollPane.setBounds(2, 182, 379, 80);
    this.loginPanel.add(scrollPane);
    this.backgroundLable.add(this.loginPanel);
    getRootPane().setDefaultButton(this.kvmButton);
    this.ip_addr.requestFocus();
    container.add(this.backgroundLable);
    System.setOut(new GUIPrintStream(System.out, textArea));
  }
  private static class CopyrightButton
    implements ActionListener {
    private CopyrightButton() {}
    public void actionPerformed(ActionEvent e) {
      OpenCopyRight openCR = new OpenCopyRight();
      openCR.start();
    }
  }
  private void widgetMsgUpdate() {
    this.ipAddrLabel.setText(this.loginUtil.getString("IpAddr_Label_Name"));
    this.ipAddrLabel.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.usernameLabel.setText(this.loginUtil.getString("Username_Label_Name"));
    this.usernameLabel.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.passwordLabel.setText(this.loginUtil.getString("Pwd_Label_Name"));
    this.passwordLabel.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.shareButton.setText(this.loginUtil.getString("share_Button_Name"));
    this.shareButton.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.onlyButton.setText(this.loginUtil.getString("only_Button_Name"));
    this.onlyButton.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.kvmButton.setText(this.loginUtil.getString("KVM_Button_Name"));
    this.kvmButton.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
  }
  private void remindMsgSet() {
    this.noNetworkAddrRemind = new JLabel(this.loginUtil.getString("NoNetwork_Addr_Remind"));
    this.noNetworkAddrRemind.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.noUsernameRemind = new JLabel(this.loginUtil.getString("NoUsername_Remind"));
    this.noUsernameRemind.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.noPassword = new JLabel(this.loginUtil.getString("NoPassword"));
    this.noPassword.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.unSupportVendor = new JLabel(this.loginUtil.getString("unsupport_vendor"));
    this.unSupportVendor.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.networkAddrError = new JLabel(this.loginUtil.getString("network_Addr_Error"));
    this.networkAddrError.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.wrongBMCversion = new JLabel(this.loginUtil.getString("wrong_BMC_version"));
    this.wrongBMCversion.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.loginRestricted = new JLabel(this.loginUtil.getString("login_Restricted"));
    this.loginRestricted.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.userNoJurisdiction = new JLabel(this.loginUtil.getString("user_NoJurisdiction"));
    this.userNoJurisdiction.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.passwordOverdued = new JLabel(this.loginUtil.getString("password_Overdued"));
    this.passwordOverdued.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.userLocked = new JLabel(this.loginUtil.getString("user_Locked"));
    this.userLocked.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.loginFailed = new JLabel(this.loginUtil.getString("login_Failed"));
    this.loginFailed.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
    this.obtainProductInfoError = new JLabel(this.loginUtil.getString("obtain_product_information_Error"));
    this.obtainProductInfoError.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
  }
  private void disposeLoginFrame() {
    dispose();
  }
  private void changeTitle() {
    setTitle(this.loginUtil.getString("Login_Name"));
  }
  public String getHostFromAddr(String Addr) {
    String strTemp = Addr.replace(" ", "");
    String host = "";
    int length = strTemp.length();
    int index = -1;
    for (int i = length - 1; i >= 0; i--) {
      if (strTemp.charAt(i) == ']') {
        break;
      }
      if (strTemp.charAt(i) == ':') {
        index = i;
        break;
      } 
    } 
    if (index == -1) {
      host = strTemp;
    }
    else {
      host = strTemp.substring(0, index);
    } 
    return host;
  }
  public int getPortFromAddr(String Addr) {
    String strTemp = Addr.replace(" ", "");
    int length = strTemp.length();
    int index = -1;
    int port = 0;
    for (int i = length - 1; i >= 0; i--) {
      if (strTemp.charAt(i) == ']') {
        break;
      }
      if (strTemp.charAt(i) == ':') {
        index = i;
        break;
      } 
    } 
    if (index == -1) {
      port = 443;
    }
    else {
      String portStr = strTemp.substring(index + 1, length);
      try {
        port = Integer.parseInt(portStr);
      }
      catch (NumberFormatException e) {
        port = 443;
      } 
    } 
    return port;
  }
  private String getIpmiHostFromAddr(String Addr) {
    String strTemp = Addr.replace(" ", "");
    String host = "";
    int length = strTemp.length();
    int index = -1;
    boolean ipv6Addr = false;
    for (int i = length - 1; i >= 0; i--) {
      if (strTemp.charAt(i) == ']') {
        break;
      }
      if (strTemp.charAt(i) == ':') {
        index = i;
        break;
      } 
    } 
    if (strTemp.charAt(0) == '[')
    {
      ipv6Addr = true;
    }
    if (ipv6Addr) {
      if (index == -1)
      {
        host = strTemp.substring(1, length - 1);
      }
      else
      {
        host = strTemp.substring(1, index - 1);
      }
    }
    else if (index == -1) {
      host = strTemp;
    }
    else {
      host = strTemp.substring(0, index);
    } 
    return host;
  }
  private int getIpmiPortFromAddr(String Addr) {
    String strTemp = Addr.replace(" ", "");
    int length = strTemp.length();
    int index = -1;
    int port = 0;
    for (int i = length - 1; i >= 0; i--) {
      if (strTemp.charAt(i) == ']') {
        break;
      }
      if (strTemp.charAt(i) == ':') {
        index = i;
        break;
      } 
    } 
    if (index == -1) {
      port = 623;
    }
    else {
      String portStr = strTemp.substring(index + 1, length);
      try {
        port = Integer.parseInt(portStr);
      }
      catch (NumberFormatException e) {
        port = 623;
      } 
    } 
    return port;
  }
  public boolean ipCheck(String InputIP) {
    if (InputIP != null && !InputIP.isEmpty()) {
      String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
      if (InputIP.matches(regex))
      {
        return true;
      }
      return false;
    } 
    return false;
  }
  public boolean isAliveCheck(String ip) {
	    try{ 
	        InetAddress address = InetAddress.getByName(ip);
	        if(address instanceof java.net.Inet4Address){ 
	           System.out.println(ip + " is ipv4 address"); 
	        }else
	        if(address instanceof java.net.Inet6Address){ 
	           System.out.println(ip + " is ipv6 address"); 
	        }else{ 
	           System.out.println(ip + " is unrecongized"); 
	        } 
	        if(address.isReachable(5000)){ 
	          System.out.println("SUCCESS - ping " + ip + " with no interface specified");
	          return true; 
	        }
	        else{ 
	           System.out.println("FAILURE - ping " + ip + " with no interface specified"); 
	           return false;
	        } 
	        /*
	        System.out.println("\n-------Trying different interfaces--------\n"); 
	        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
	        while(netInterfaces.hasMoreElements()) {    
	             NetworkInterface ni = netInterfaces.nextElement();    
	             System.out.println("Checking interface, DisplayName:" + ni.getDisplayName() + ", Name:" + ni.getName());
	              if(address.isReachable(ni, 0, 5000)){ 
	                  System.out.println("SUCCESS - ping " + ip); 
	              }else{ 
	                  System.out.println("FAILURE - ping " + ip); 
	              } 
	              
	              Enumeration<InetAddress> ips = ni.getInetAddresses();    
	              while(ips.hasMoreElements()) {    
	                System.out.println("IP: " + ips.nextElement().getHostAddress());   
	              } 
	            System.out.println("-------------------------------------------"); 
	          } 
	          */
	      }catch(Exception e){ 
	        System.out.println("error occurs."); 
	        e.printStackTrace();
	        return false;
	      }       
  }
  private void ipmiLoginMethod(String host, String user_name, String PwdStr, String KvmMode, int port, LoginAuthentication clientformlogininforosc, LoginUtil loginutil) {
    String loginAuthResult = "";
    try {
      loginAuthResult = clientformlogininforosc.doLoginAuth(host, user_name, PwdStr, KvmMode, port, this.loginUtil);
      LoggerUtil.info( "loginAuthResult: "+ loginAuthResult );
    }
    catch (Exception e1) {
      JOptionPane.showOptionDialog(this.loginPanel, this.networkAddrError, this.loginUtil
          .getString("Remind_title"), 0, 3, null, this.remindOption, this.remindOption[0]);
      return;
    } 
    if (loginAuthResult.equals("gologin")) {
      JOptionPane.showOptionDialog(this.loginPanel, this.networkAddrError, this.loginUtil
          .getString("Remind_title"), 0, 3, null, this.remindOption, this.remindOption[0]);
      return;
    } 
  }
  public static void main(String[] args) {
    Main login = new Main();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    Action emptyAction = new JFrameF10Action();
    login.getRootPane().getInputMap(1).put(KeyStroke.getKeyStroke("F10"), "F10");
    login.getRootPane().getActionMap().put("F10", emptyAction);
  }
}
