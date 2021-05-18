package com.kvm;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
class CdRom
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_CdRom;
  public CdRom(VirtualMedia refer_CdRom) {
    this.refer_CdRom = refer_CdRom;
    VirtualMedia.uiManager();
    setLayout((LayoutManager)null);
    if (KVMUtil.isLinuxOS() && Base.getLocal().equalsIgnoreCase("en")) {
      setSize(450, 69);
    }
    else {
      setSize(407, 69);
    } 
    setBackground(new Color(204, 227, 242));
    refer_CdRom.setCr(refer_CdRom.createRadioButton(refer_CdRom.getUtil().getResource("cd_cdroms"), true, refer_CdRom
          .cdromItemsUpdate()));
    refer_CdRom.setCdSelect(cdcreateCombobox());
    refer_CdRom.getCdSelect().addMouseListener(refer_CdRom.cdmouseClicked());
    Component c = refer_CdRom.getCdSelect().getComponent(0);
    c.addMouseListener(refer_CdRom.cdmouseClicked());
    refer_CdRom.setCdCon(refer_CdRom.createButton(refer_CdRom.getUtil().getResource("flp_cd_connection"), true, refer_CdRom
          .createVCdrom()));
    refer_CdRom.setCd(refer_CdRom.createRadioButton(refer_CdRom.getUtil().getResource("flp_cd_image_file"), false, refer_CdRom
          .cdromItemsUpdate()));
    refer_CdRom.setCdText(refer_CdRom.createTextFiele(12, false));
    refer_CdRom.setCdSkim(refer_CdRom.createButton(refer_CdRom.getUtil().getResource("flp_cd_skim_over"), false, refer_CdRom
          .fileChoose()));
    refer_CdRom.setCie(refer_CdRom.createButton(refer_CdRom.getUtil().getResource("flp_cd_pop_up_program"), false, refer_CdRom
          .cdromEject()));
    refer_CdRom.setCdLocalText(refer_CdRom.createTextFiele(12, false));
    refer_CdRom.setCdLocalSkim(refer_CdRom.createButton(refer_CdRom.getUtil().getResource("flp_cd_skim_over"), false, refer_CdRom
          .fileChoose()));
    refer_CdRom.setCdLocal(refer_CdRom.createRadioButton(refer_CdRom.getUtil().getResource("flp_cd_local_file"), false, refer_CdRom
          .cdromItemsUpdate()));
    if (KVMUtil.isLinuxOS()) {
      if (Base.getLocal().equalsIgnoreCase("en"))
      {
        refer_CdRom.getCr().setBounds(0, 4, 150, 20);
        refer_CdRom.getCdSelect().setBounds(153, 4, 85, 20);
        refer_CdRom.getCdCon().setBounds(350, 4, 95, 20);
        refer_CdRom.getCd().setBounds(0, 23, 160, 20);
        refer_CdRom.getCdText().setBounds(153, 25, 82, 18);
        refer_CdRom.getCdSkim().setBounds(245, 25, 100, 18);
        refer_CdRom.getCie().setBounds(350, 25, 95, 18);
        refer_CdRom.getCdLocal().setBounds(0, 42, 160, 20);
        refer_CdRom.getCdLocalText().setBounds(153, 46, 82, 18);
        refer_CdRom.getCdLocalSkim().setBounds(245, 46, 100, 18);
      }
      else
      {
        refer_CdRom.getCr().setBounds(0, 4, 145, 20);
        refer_CdRom.getCdSelect().setBounds(148, 4, 65, 20);
        refer_CdRom.getCdCon().setBounds(322, 4, 75, 20);
        refer_CdRom.getCd().setBounds(0, 23, 155, 20);
        refer_CdRom.getCdText().setBounds(148, 25, 75, 18);
        refer_CdRom.getCdSkim().setBounds(233, 25, 85, 18);
        refer_CdRom.getCie().setBounds(322, 25, 75, 18);
        refer_CdRom.getCdLocal().setBounds(0, 42, 157, 20);
        refer_CdRom.getCdLocalText().setBounds(148, 46, 75, 18);
        refer_CdRom.getCdLocalSkim().setBounds(233, 46, 85, 18);
      }
    }
    else {
      refer_CdRom.getCr().setBounds(3, 4, 145, 20);
      refer_CdRom.getCdSelect().setBounds(148, 4, 50, 20);
      refer_CdRom.getCdCon().setBounds(310, 4, 92, 20);
      refer_CdRom.getCd().setBounds(3, 23, 145, 20);
      refer_CdRom.getCdText().setBounds(148, 25, 75, 18);
      refer_CdRom.getCdSkim().setBounds(223, 25, 78, 18);
      refer_CdRom.getCie().setBounds(310, 25, 92, 18);
      refer_CdRom.getCdLocal().setBounds(3, 42, 145, 18);
      refer_CdRom.getCdLocalText().setBounds(148, 46, 75, 18);
      refer_CdRom.getCdLocalSkim().setBounds(223, 46, 78, 18);
    } 
    refer_CdRom.getCd().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCdCon().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCr().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCdText().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCdSkim().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCie().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCdLocal().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCdLocalText().setBackground(new Color(204, 227, 242));
    refer_CdRom.getCdLocalSkim().setBackground(new Color(204, 227, 242));
    add(refer_CdRom.getCd());
    add(refer_CdRom.getCdSelect());
    add(refer_CdRom.getCdCon());
    add(refer_CdRom.getCr());
    add(refer_CdRom.getCdText());
    add(refer_CdRom.getCdSkim());
    add(refer_CdRom.getCie());
    add(refer_CdRom.getCdLocal());
    add(refer_CdRom.getCdLocalText());
    add(refer_CdRom.getCdLocalSkim());
    setBorder(BorderFactory.createLineBorder(new Color(153, 199, 230), 1));
    setBackground(new Color(204, 227, 242));
  }
  private JComboBox cdcreateCombobox() {
    JComboBox<String> box = new JComboBox();
    this.refer_CdRom.setCdroms(this.refer_CdRom.getVmApplet().getCDROMDevices());
    if ("".equals(this.refer_CdRom.getCdroms()) || null == this.refer_CdRom.getCdroms()) {
      box.addItem(this.refer_CdRom.getUtil().getResource("flp_cd_none"));
      this.refer_CdRom.setCdroms(null);
    }
    else {
      String[] cdsplit = this.refer_CdRom.getCdroms().split(":");
      for (int i = 0; i < cdsplit.length; i++) {
        if (!"".equals(cdsplit[i]))
        {
          box.addItem(cdsplit[i] + ':');
        }
      } 
    } 
    return box;
  }
}
