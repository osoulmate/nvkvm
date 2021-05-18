package com.kvm;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
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
  public int getBladeNumber() {
    return this.bladeNO;
  }
  public void setBladeNumber(int bladeNO) {
    this.bladeNO = bladeNO;
  }
  private transient BladeCommu bladeCommu = null;
  public BladeCommu getBladeCommu() {
    return this.bladeCommu;
  }
  public void setBladeCommu(BladeCommu bladeCommu) {
    this.bladeCommu = bladeCommu;
  }
  private KVMInterface kvmInterface = null;
  private transient ActionListener radioListener;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public ColorBit(JDialog dialog, int bladeNO, KVMInterface kvmInterface2)
  {
    super(dialog);
    this.radioListener = new RadioListener(this); setModal(true);
    this.kvmInterface = kvmInterface2;
    setTitle(kvmInterface2.getKvmUtil().getString("setColorBit"));
    this.bladeNO = bladeNO;
    this.bladeCommu = ((BladeThread)kvmInterface2.getBase().getThreadGroup().get(String.valueOf(bladeNO))).getBladeCommu();
    jbInit(); } private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException { in.defaultReadObject();
    this.radioListener = (ActionListener)in.readObject(); }
  private void jbInit() {
    setSize(200, 100);
    setResizable(false);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = d.width / 2 - 100;
    int y = d.height / 2 - 90;
    setLocation(x, y);
    addWindowListener(new ColorBitWindowAdapter(this));
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(1, 2));
    Box box = new Box(1);
    ButtonGroup group = new ButtonGroup();
    this.radio1 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.getKvmUtil().getString("8-bit")));
    this.radio1.setActionCommand("8");
    this.radio1.addActionListener(this.radioListener);
    group.add(this.radio1);
    this.radio2 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.getKvmUtil().getString("7-bit")));
    this.radio2.setActionCommand("7");
    this.radio2.addActionListener(this.radioListener);
    group.add(this.radio2);
    p.add(box);
    box = new Box(1);
    this.radio3 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.getKvmUtil().getString("6-bit")));
    this.radio3.setActionCommand("6");
    this.radio3.addActionListener(this.radioListener);
    group.add(this.radio3);
    this.radio4 = (JRadioButton)box.add(new JRadioButton(this.kvmInterface.getKvmUtil().getString("4-bit")));
    this.radio4.setActionCommand("4");
    this.radio4.addActionListener(this.radioListener);
    group.add(this.radio4);
    p.add(box);
    switch (this.kvmInterface.getKvmUtil().getImagePane(this.bladeNO).getColorBit()) {
      case 2:
        group.setSelected(this.radio1.getModel(), true);
        break;
      case 1:
        group.setSelected(this.radio2.getModel(), true);
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
    if (!this.kvmInterface.isFullScreen())
      this.kvmInterface.getKvmUtil().getImagePane(this.bladeNO).requestFocus(); 
  }
  public void removeAll() {
    this.radio1.removeActionListener(this.radioListener);
    this.radio2.removeActionListener(this.radioListener);
    this.radio3.removeActionListener(this.radioListener);
    this.radio4.removeActionListener(this.radioListener);
    getContentPane().removeAll();
  }
}
