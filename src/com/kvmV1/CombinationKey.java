package com.kvmV1;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
public class CombinationKey
  extends JDialog
{
  private static final long serialVersionUID = 1L;
  public int bladeNO;
  public PackData pack;
  public JPanel bPanel = new JPanel(new BorderLayout());
  public JPanel bPanel1 = new JPanel(new BorderLayout());
  public JPanel bPanel2 = new JPanel(new BorderLayout());
  public JButton b1 = new JButton("Ctrl+Shift");
  public JButton b2 = new JButton("Ctrl+Esc");
  public JButton b3 = new JButton("Ctrl+Alt+Del");
  public JButton b4 = new JButton("Alt+Tab");
  public JButton b5 = new JButton("Ctrl+Space");
  public JButton b6 = new JButton("ResetKeyboard");
  public JPanel tPanel = new JPanel(new BorderLayout());
  public JTextField textField1 = new JTextField();
  public JTextField textField2 = new JTextField();
  public JTextField textField3 = new JTextField();
  public JTextField textField4 = new JTextField();
  public JTextField textField5 = new JTextField();
  public JTextField textField6 = new JTextField();
  public int keyValue1 = 0;
  public int keyValue2 = 0;
  public int keyValue3 = 0;
  public int keyValue4 = 0;
  public int keyValue5 = 0;
  public int keyValue6 = 0;
  public JButton send = null;
  protected Timer timer = null;
  public KVMInterface kvmInterface = null;
  public BladeThread bladeThread = null;
  private CombinationKey(JFrame frame) {
    super(frame, true);
  }
  public CombinationKey(JFrame frame, int bladeNO, PackData pack) {
    this(frame);
    this.bladeNO = bladeNO;
    this.pack = pack;
    jbInit();
  }
  public CombinationKey(JDialog dialog, int bladeNO, PackData pack, KVMInterface kvmInterface) {
    this.bladeNO = bladeNO;
    this.pack = pack;
    this.kvmInterface = kvmInterface;
    this.send = new JButton(kvmInterface.kvmUtil.getString("send"));
    jbInit();
  }
  private void jbInit() {
    this.bladeThread = this.kvmInterface.base.threadGroup.get(String.valueOf(this.bladeNO));
    setSize(550, 120);
    setResizable(false);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = d.width / 2 - 250;
    int y = d.height / 2 - 90;
    setLocation(x, y);
    CombineKeyListener combineKeyListener = new CombineKeyListener(this);
    addWindowListener(new WindowAdapter()
        {
          public void windowClosing(WindowEvent e)
          {
            CombinationKey.this.dispose();
          }
        });
    addWindowListener(new WindowAdapter()
        {
          public void windowActivated(WindowEvent e)
          {
            if (CombinationKey.this.kvmInterface.base.getHookNum() == 0)
            {
              CombinationKey.this.kvmInterface.base.setHookNum(MouseDisplacementImpl.installHook());
            }
            CombinationKey.this.kvmInterface.kvmUtil.setiWindosFocus(1);
          }
          public void windowDeactivated(WindowEvent e) {
            CombinationKey.this.kvmInterface.kvmUtil.setiWindosFocus(0);
            if (CombinationKey.this.kvmInterface.base.getHookNum() != 0) {
              MouseDisplacementImpl.removeHook(CombinationKey.this.kvmInterface.base.getHookNum());
              CombinationKey.this.kvmInterface.base.setHookNum(0);
            } 
          }
        });
    getContentPane().setLayout(new BorderLayout());
    ComKeyHandler listener = new ComKeyHandler(this);
    this.textField1.addKeyListener(listener);
    this.textField2.addKeyListener(listener);
    this.textField3.addKeyListener(listener);
    this.textField4.addKeyListener(listener);
    this.textField5.addKeyListener(listener);
    this.textField6.addKeyListener(listener);
    this.bPanel1.setLayout(new GridLayout(1, 3));
    this.bPanel1.add(this.b1, (Object)null);
    this.bPanel1.add(this.b2, (Object)null);
    this.bPanel1.add(this.b3, (Object)null);
    this.bPanel2.setLayout(new GridLayout(1, 3));
    this.bPanel2.add(this.b4, (Object)null);
    this.bPanel2.add(this.b5, (Object)null);
    this.bPanel2.add(this.b6, (Object)null);
    this.bPanel.setLayout(new GridLayout(2, 1));
    this.bPanel.add(this.bPanel1, (Object)null);
    this.bPanel.add(this.bPanel2, (Object)null);
    this.tPanel.setLayout(new GridLayout(1, 8));
    this.tPanel.add(new JLabel(this.kvmInterface.kvmUtil.getString("custom"), 0), (Object)null);
    this.tPanel.add(this.textField1, (Object)null);
    this.tPanel.add(this.textField2, (Object)null);
    this.tPanel.add(this.textField3, (Object)null);
    this.tPanel.add(this.textField4, (Object)null);
    this.tPanel.add(this.textField5, (Object)null);
    this.tPanel.add(this.textField6, (Object)null);
    this.tPanel.add(this.send, (Object)null);
    getContentPane().add(this.bPanel, "North");
    getContentPane().add(this.tPanel, "Center");
    this.b1.setActionCommand(this.b1.getText());
    this.b1.addActionListener(combineKeyListener);
    this.b2.setActionCommand(this.b2.getText());
    this.b2.addActionListener(combineKeyListener);
    this.b3.setActionCommand(this.b3.getText());
    this.b3.addActionListener(combineKeyListener);
    this.b4.setActionCommand(this.b4.getText());
    this.b4.addActionListener(combineKeyListener);
    this.b5.setActionCommand(this.b5.getText());
    this.b5.addActionListener(combineKeyListener);
    this.b6.setActionCommand(this.b6.getText());
    this.b6.addActionListener(combineKeyListener);
    this.send.setActionCommand("send");
    this.send.addActionListener(combineKeyListener);
    setModal(true);
    setAlwaysOnTop(true);
    setVisible(true);
  }
}
