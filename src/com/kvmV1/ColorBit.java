package com.kvmV1;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
public class ColorBit
  extends JDialog
{
  private static final long serialVersionUID = 1L;
  private int bladeNO;
  private JRadioButton radio1;
  private JRadioButton radio2;
  private JRadioButton radio3;
  private JRadioButton radio4;
  private static final int EIGHT_BIT = 2;
  private static final int SEVEN_BIT = 1;
  private static final int SIX_BIT = 0;
  private static final int FOUR_BIT = 3;
  private BladeCommu bladeCommu = null;
  public KVMInterface kvmInterface = null;
  public ActionListener radioListener;
  public ColorBit(JDialog dialog, int bladeNO, KVMInterface kvmInterface) {
    super(dialog);
    this.radioListener = new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (e.getActionCommand().equals("8")) {
            (ColorBit.this.kvmInterface.kvmUtil.getImagePane(ColorBit.this.bladeNO)).custBit = 2;
            ColorBit.this.bladeCommu.sentData(ColorBit.this.kvmInterface.packData.connectBlade(ColorBit.this.bladeNO, (byte)2));
          } 
          if (e.getActionCommand().equals("7")) {
            (ColorBit.this.kvmInterface.kvmUtil.getImagePane(ColorBit.this.bladeNO)).custBit = 1;
            ColorBit.this.bladeCommu.sentData(ColorBit.this.kvmInterface.packData.connectBlade(ColorBit.this.bladeNO, (byte)1));
          } 
          if (e.getActionCommand().equals("6")) {
            (ColorBit.this.kvmInterface.kvmUtil.getImagePane(ColorBit.this.bladeNO)).custBit = 0;
            ColorBit.this.bladeCommu.sentData(ColorBit.this.kvmInterface.packData.connectBlade(ColorBit.this.bladeNO, (byte)0));
          } 
          if (e.getActionCommand().equals("4")) {
            (ColorBit.this.kvmInterface.kvmUtil.getImagePane(ColorBit.this.bladeNO)).custBit = 3;
            ColorBit.this.bladeCommu.sentData(ColorBit.this.kvmInterface.packData.connectBlade(ColorBit.this.bladeNO, (byte)3));
          } 
          if (ColorBit.this.kvmInterface.kvmUtil.getImagePane(ColorBit.this.bladeNO) != null) {
            ColorBit.this.remove();
          }
          else {
            ColorBit.this.removeAll();
          } 
          ColorBit.this.dispose();
        }
      };
    setModal(true);
    this.kvmInterface = kvmInterface;
    setTitle(kvmInterface.kvmUtil.getString("setColorBit"));
    this.bladeNO = bladeNO;
    this.bladeCommu = ((BladeThread)kvmInterface.base.threadGroup.get(String.valueOf(bladeNO))).bladeCommu;
    jbInit();
  }
  private void jbInit() {
    setSize(200, 100);
    setResizable(false);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = d.width / 2 - 100;
    int y = d.height / 2 - 90;
    setLocation(x, y);
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            if (ColorBit.this.kvmInterface.kvmUtil.getImagePane(ColorBit.this.bladeNO) != null) {
              ColorBit.this.remove();
            } else {
              ColorBit.this.removeAll();
            } 
            ColorBit.this.dispose();
          }
        });
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(1, 2));
    Box box = new Box(1);
    ButtonGroup group = new ButtonGroup();
    this.radio1 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.kvmUtil.getString("8-bit")));
    this.radio1.setActionCommand("8");
    this.radio1.addActionListener(this.radioListener);
    group.add(this.radio1);
    this.radio2 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.kvmUtil.getString("7-bit")));
    this.radio2.setActionCommand("7");
    this.radio2.addActionListener(this.radioListener);
    group.add(this.radio2);
    p.add(box);
    box = new Box(1);
    this.radio3 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.kvmUtil.getString("6-bit")));
    this.radio3.setActionCommand("6");
    this.radio3.addActionListener(this.radioListener);
    group.add(this.radio3);
    this.radio4 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.kvmUtil.getString("4-bit")));
    this.radio4.setActionCommand("4");
    this.radio4.addActionListener(this.radioListener);
    group.add(this.radio4);
    p.add(box);
    switch ((this.kvmInterface.kvmUtil.getImagePane(this.bladeNO)).colorBit) {
      case 2:
        group.setSelected(this.radio1.getModel(), true);
        break;
      case 1:
        group.setSelected(this.radio2.getModel(), true);
        break;
      case 0:
        group.setSelected(this.radio3.getModel(), true);
        break;
      case 3:
        group.setSelected(this.radio4.getModel(), true);
        break;
      default:
        group.setSelected(this.radio3.getModel(), true);
        break;
    } 
    getContentPane().add(p);
    setVisible(true);
  }
  public void remove() {
    this.radio1.removeActionListener(this.radioListener);
    this.radio2.removeActionListener(this.radioListener);
    this.radio3.removeActionListener(this.radioListener);
    this.radio4.removeActionListener(this.radioListener);
    getContentPane().removeAll();
    if (!this.kvmInterface.isFullScreen)
      this.kvmInterface.kvmUtil.getImagePane(this.bladeNO).requestFocus(); 
  }
  public void removeAll() {
    this.radio1.removeActionListener(this.radioListener);
    this.radio2.removeActionListener(this.radioListener);
    this.radio3.removeActionListener(this.radioListener);
    this.radio4.removeActionListener(this.radioListener);
    getContentPane().removeAll();
  }
}
