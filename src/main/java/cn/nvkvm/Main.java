package cn.nvkvm;
import cn.library.TextAreaLogAppender;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory; 
public class Main extends JFrame
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
  private JComboBox<String> ianguageComboBox;
  private JButton kvmButton;
  private String ianguage = "";
  private JLabel noNetworkAddrRemind;
  private JLabel noUsernameRemind;
  private JLabel noPassword;
  private JLabel unSupportVendor;
  private JLabel networkAddrError;
  private JLabel loginFailed;
  //private ConsoleTextArea consoleTextArea;
  private JScrollPane scrollPane; 
  private JTextArea textArea;
  static int wordSize = 0;
  private final static Log log = LogFactory.getLog(Main.class);
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
    setBounds(0, 0, 400, 300);
    //URL url = Main.class.getResource("resource/images/vconsole.png");
    URL url = this.getClass().getClassLoader().getResource("images/vconsole.png");
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
  public void initLog() {  
      try {  
          Thread t;  
          t = new TextAreaLogAppender(this.textArea, this.scrollPane);  
          t.start();  
      } catch (Exception e) {  
          JOptionPane.showMessageDialog(this, e, "绑定日志输出组件错误", JOptionPane.ERROR_MESSAGE);  
      }  
  }  
  public void init() {
    Locale locale = Locale.getDefault();
    this.ianguage = locale.getLanguage();
    int IanguageIndex = 0;
    String IpAddrLabelName = "";
    String UsernameLabelName = "";
    String PwdLabelName = "";
    String[] IanguageSet = new String[2];
    String KVMButtonName = "";
    this.loginUtil = new LoginUtil();
    this.loginUtil.setBundle(this.ianguage);
    this.remindOption[0] = this.loginUtil.getString("RemindOption");
    IpAddrLabelName = this.loginUtil.getString("IpAddr_Label_Name");
    UsernameLabelName = this.loginUtil.getString("Username_Label_Name");
    PwdLabelName = this.loginUtil.getString("Pwd_Label_Name");
    KVMButtonName = this.loginUtil.getString("KVM_Button_Name");
    IanguageSet[0] = this.loginUtil.getString("Ianguage_en");
    IanguageSet[1] = this.loginUtil.getString("Ianguage_zh");
    GetDiffOSPara.osParaInit();
    this.fonts = this.loginUtil.getString("font_name");
    setWordSize(Integer.parseInt(this.loginUtil.getString("Word_Size")));
    Container container = getContentPane();
    this.loginPanel = new JPanel();
    //bak.JPG 426px x 268px
    //Image image = (new ImageIcon(Main.class.getResource("resource/images/bak.JPG"))).getImage();
    Image image = (new ImageIcon(this.getClass().getClassLoader().getResource("images/bak.JPG"))).getImage();
    this.loginPanel = new BackgroundPanel(image);
    this.loginPanel.setLayout((LayoutManager)null);
    this.loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
    //this.loginPanel.setBounds(5, 0, 338, 228);
    this.loginPanel.setBounds(1, 0, 400, 300);
    this.loginPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 1), "", 1, 2, new Font("Times new roman", 0, 12)));
    this.loginPanel.setName("LoginPanel");
    //Image image1 = (new ImageIcon(Main.class.getResource("resource/images/floor.JPG"))).getImage();
    Image image1 = (new ImageIcon(this.getClass().getClassLoader().getResource("images/floor.JPG"))).getImage();
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
    this.kvmButton.setBounds(285, 155, 75, 25);
    this.kvmButton.setForeground(Color.black);
    this.kvmButton.setBackground(new Color(240,240,240));
    this.kvmButton.setBorder(BorderFactory.createLineBorder(Color.gray));
    this.kvmButton.setFont(new Font(this.fonts, 0, wordSize));
    this.kvmButton.setName("connectButton");
    this.kvmButton.setFocusPainted(false);
    remindMsgSet();
    this.copyrightImagebutton = new JButton();
    //Image crImage = (new ImageIcon(Main.class.getResource("resource/images/CRhelp.png"))).getImage();
    Image crImage = (new ImageIcon(this.getClass().getClassLoader().getResource("images/CRhelp.png"))).getImage();
    this.copyrightImagebutton.setIcon(new ImageIcon(crImage));
    this.copyrightImagebutton.setBounds(15, 10, 20, 20);
    this.copyrightImagebutton.setBorder((Border)null);
    this.copyrightImagebutton.setContentAreaFilled(false);
    this.copyrightImagebutton.setName("CopyrightImagebuttonInput");
    this.loginPanel.add(this.ipAddrLabel);
    this.loginPanel.add(this.passwordLabel);
    this.backgroundLable.add(this.ianguageComboBox);
    this.backgroundLable.add(this.copyrightImagebutton);
    this.backgroundLable.add(this.kvmButton);
    this.loginPanel.add(this.ip_addr);
    this.loginPanel.add(this.username);
    this.loginPanel.add(this.pwdJField);
    this.loginPanel.add(this.usernameLabel);
    this.scrollPane = new JScrollPane();
    this.textArea = new JTextArea();
    this.textArea.setColumns(20); 
    this.textArea.setRows(5);
    this.scrollPane.setViewportView(this.textArea);
    this.scrollPane.setBounds(2, 182, 379, 80);
    this.loginPanel.add(this.scrollPane);
    //System.setOut(new GUIPrintStream(System.out, textArea));
    this.backgroundLable.add(this.loginPanel);
    getRootPane().setDefaultButton(this.kvmButton);
    this.ip_addr.requestFocus();
    container.add(this.backgroundLable);
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
    this.kvmButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            Main.this.kvmButton.setBackground(new Color(245,245,245));
            Main.this.kvmButton.setBorder(BorderFactory.createLineBorder(Color.blue,1));
        }
        @Override
        public void mouseExited(MouseEvent e) {
            Main.this.kvmButton.setBackground(Color.white);
            Main.this.kvmButton.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        }
    });
    this.kvmButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            String Addr = Main.this.ip_addr.getText();
            String user_name = Main.this.username.getText();
            String PwdStrIPMI = "";
            String PwdStrHTTPS = "";
            PwdStrIPMI = String.valueOf(Main.this.pwdJField.getPassword());
            //System.out.println("USERNAME:"+user_name+" PASSWORD:"+PwdStrIPMI);
            try {
              PwdStrHTTPS = URLEncoder.encode(String.valueOf(Main.this.pwdJField.getPassword()), "UTF-8");
            }
            catch (UnsupportedEncodingException e2) {
              log.error(e.getClass().getName());
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
            String host = Main.this.getHostFromAddr(Addr);
            String hostIpmi = Main.this.getIpmiHostFromAddr(Addr);
            try {
                if (Main.this.isAliveCheck(host)) {
                    Collect mycollect = new Collect(hostIpmi,user_name,PwdStrIPMI);
                    String vendor = mycollect.getVendor();
                    String model = mycollect.getModel();
                    String bmc_version = mycollect.getBmcVersion();
                    HashMap<String, String> extra = mycollect.getExtra();
                    ClientSubmitLoginCommon commonLogin = new ClientSubmitLoginCommon();
                    log.info( "vendor:"+ vendor+", model:"+model);
                    String [] loginResult = commonLogin.doLogin(vendor, model, bmc_version,extra,hostIpmi,user_name,PwdStrIPMI, KvmMode);
                    //log.info( "loginResult:"+ loginResult[0]+","+loginResult[1]);
                    if(loginResult[0].equals("400")) {
                    	if (!vendor.toLowerCase().contains("huawei")) {
                        JOptionPane.showOptionDialog(Main.this.loginPanel, loginResult[1], Main.this
                                .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                                .remindOption, Main.this
                                .remindOption[0]);
                        return;
                    	}
                        
                    }
                    if (loginResult[0].equals("200")) {
                    	String []  rs=commonLogin.doDownLoad();
                        if(rs[0].equals("400")) {
                          if (!vendor.toLowerCase().contains("huawei")) {
                            JOptionPane.showOptionDialog(Main.this.loginPanel,rs[1] , Main.this
                                    .loginUtil.getString("Remind_title"), 0, 3, null, Main.this
                                    .remindOption, Main.this
                                    .remindOption[0]);
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
                log.error(e1.getClass().getName());
                return;
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
            Main.this.kvmButton.setText(Main.this.loginUtil.getString("KVM_Button_Name"));
            Main.this.remindOption[0] = Main.this.loginUtil.getString("RemindOption");
            Main.this.widgetMsgUpdate();
            Main.this.remindMsgSet();
            Main.this.changeTitle();
          }
        });
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
    this.loginFailed = new JLabel(this.loginUtil.getString("login_Failed"));
    this.loginFailed.setFont(new Font(this.loginUtil.getString("font_name"), 0, wordSize));
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
               //System.out.println(ip + " is ipv4 address"); 
            }else
            if(address instanceof java.net.Inet6Address){ 
               //System.out.println(ip + " is ipv6 address"); 
            }else{ 
               //System.out.println(ip + " is unrecongized"); 
            } 
            if(address.isReachable(5000)){ 
              log.info("host "+ip+" is REACHABLE");
              return true; 
            }
            else{ 
            	log.info("host "+ip+" is UNREACHABLE"); 
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
  public static void main(String[] args) {
	    Main login = new Main();
	    login.initLog();
	    try {
	      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (Exception e) {
	      log.error(e.getClass().getName());
	    } 
	    Action emptyAction = new JFrameF10Action();
	    login.getRootPane().getInputMap(1).put(KeyStroke.getKeyStroke("F10"), "F10");
	    login.getRootPane().getActionMap().put("F10", emptyAction);
	  }
}
