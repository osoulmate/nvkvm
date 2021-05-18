package com.kvm;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
class Floppy
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_Floppy;
  public Floppy(VirtualMedia refer_Floppy) {
    this.refer_Floppy = refer_Floppy;
    VirtualMedia.uiManager();
    setLayout((LayoutManager)null);
    setBackground(new Color(204, 227, 242));
    setSize(457, 49);
    if (KVMUtil.isLinuxOS() && Base.getLocal().equalsIgnoreCase("en"))
    {
      setSize(500, 49);
    }
    refer_Floppy.setFc(refer_Floppy.createChekBox(refer_Floppy.getUtil().getResource("flp_setwrite"), refer_Floppy
          .fCheckClick(), true));
    refer_Floppy.setFd(refer_Floppy.createRadioButton(refer_Floppy.getUtil().getResource("flp_floppy"), true, refer_Floppy
          .floppyItemsUpdate()));
    refer_Floppy.setFdSelect(flpcreateComboBox());
    refer_Floppy.getFdSelect().addMouseListener(refer_Floppy.fdmouseClicked());
    Component c = refer_Floppy.getFdSelect().getComponent(0);
    c.addMouseListener(refer_Floppy.fdmouseClicked());
    refer_Floppy.setFcb(refer_Floppy.createButton(refer_Floppy.getUtil().getResource("flp_cd_connection"), true, refer_Floppy
          .createVFloppy()));
    refer_Floppy.setFr(refer_Floppy.createRadioButton(refer_Floppy.getUtil().getResource("flp_cd_image_file"), false, refer_Floppy
          .floppyItemsUpdate()));
    refer_Floppy.setFitext(refer_Floppy.createTextFiele(12, false));
    refer_Floppy.setFlpSkim(refer_Floppy.createButton(refer_Floppy.getUtil().getResource("flp_cd_skim_over"), false, refer_Floppy
          .fileChoose()));
    refer_Floppy.setFie(refer_Floppy.createButton(refer_Floppy.getUtil().getResource("flp_cd_pop_up_program"), false, refer_Floppy
          .floppyEject()));
    if (KVMUtil.isLinuxOS()) {
      if (Base.getLocal().equalsIgnoreCase("en"))
      {
        refer_Floppy.getFd().setBounds(0, 4, 150, 20);
        refer_Floppy.getFdSelect().setBounds(153, 4, 85, 20);
        refer_Floppy.getFc().setBounds(240, 4, 164, 20);
        refer_Floppy.getFcb().setBounds(400, 4, 95, 20);
        refer_Floppy.getFr().setBounds(0, 23, 160, 20);
        refer_Floppy.getFitext().setBounds(153, 25, 82, 18);
        refer_Floppy.getFlpSkim().setBounds(235, 25, 100, 18);
        refer_Floppy.getFie().setBounds(400, 25, 95, 18);
      }
      else
      {
        refer_Floppy.getFd().setBounds(3, 4, 148, 20);
        refer_Floppy.getFdSelect().setBounds(148, 4, 65, 20);
        refer_Floppy.getFc().setBounds(213, 4, 150, 20);
        refer_Floppy.getFcb().setBounds(372, 4, 75, 20);
        refer_Floppy.getFr().setBounds(3, 23, 148, 20);
        refer_Floppy.getFitext().setBounds(148, 25, 75, 18);
        refer_Floppy.getFlpSkim().setBounds(223, 25, 78, 18);
        refer_Floppy.getFie().setBounds(372, 25, 75, 18);
      }
    } else {
      refer_Floppy.getFd().setBounds(3, 4, 145, 20);
      refer_Floppy.getFdSelect().setBounds(148, 4, 50, 20);
      refer_Floppy.getFc().setBounds(198, 4, 153, 20);
      refer_Floppy.getFcb().setBounds(360, 4, 92, 20);
      refer_Floppy.getFr().setBounds(3, 23, 145, 20);
      refer_Floppy.getFitext().setBounds(148, 25, 75, 18);
      refer_Floppy.getFlpSkim().setBounds(223, 25, 78, 18);
      refer_Floppy.getFie().setBounds(360, 25, 92, 18);
    } 
    refer_Floppy.getFd().setBackground(new Color(204, 227, 242));
    refer_Floppy.getFc().setBackground(new Color(204, 227, 242));
    refer_Floppy.getFcb().setBackground(new Color(204, 227, 242));
    refer_Floppy.getFr().setBackground(new Color(204, 227, 242));
    refer_Floppy.getFitext().setBackground(new Color(204, 227, 242));
    refer_Floppy.getFlpSkim().setBackground(new Color(204, 227, 242));
    refer_Floppy.getFie().setBackground(new Color(204, 227, 242));
    add(refer_Floppy.getFc());
    add(refer_Floppy.getFd());
    add(refer_Floppy.getFdSelect());
    add(refer_Floppy.getFitext());
    add(refer_Floppy.getFr());
    add(refer_Floppy.getFcb());
    add(refer_Floppy.getFlpSkim());
    add(refer_Floppy.getFie());
    setBorder(BorderFactory.createLineBorder(new Color(153, 199, 230), 1));
  }
  private JComboBox flpcreateComboBox() {
    JComboBox<String> comboBox = new JComboBox();
    this.refer_Floppy.setFloppys(this.refer_Floppy.getVmApplet().getFloppyDevices());
    if ("".equals(this.refer_Floppy.getFloppys()) || null == this.refer_Floppy.getFloppys()) {
      comboBox.addItem(this.refer_Floppy.getUtil().getResource("flp_cd_none"));
      this.refer_Floppy.setFloppys(null);
    }
    else {
      String[] flplit = this.refer_Floppy.getFloppys().split(":");
      for (int i = 0; i < flplit.length; i++) {
        if (!"".equals(flplit[i]))
        {
          comboBox.addItem(flplit[i] + ':');
        }
      } 
    } 
    return comboBox;
  }
}
