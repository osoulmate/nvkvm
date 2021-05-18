package com.kvm;
import com.huawei.vm.console.utils.VMException;
import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.JOptionPane;
class FileChooseTimer
  extends TimerTask
{
  private transient VirtualMedia refer_FileChooseTimer;
  public FileChooseTimer(VirtualMedia refer_FileChooseTimer) {
    this.refer_FileChooseTimer = refer_FileChooseTimer;
  }
  public void run() {
    try {
      this.refer_FileChooseTimer.getCdCon().setEnabled(false);
      this.refer_FileChooseTimer.getCdLocalSkim().setEnabled(false);
      this.refer_FileChooseTimer.getCdLocalText().setEnabled(false);
      this.refer_FileChooseTimer.getCdLocalText().setEditable(false);
      this.refer_FileChooseTimer.getCd().setEnabled(false);
      this.refer_FileChooseTimer.getCr().setEnabled(false);
      UDFImageBuilder mySabreUDF = new UDFImageBuilder();
      File dirFile = new File(this.refer_FileChooseTimer.getCdLocalDir());
      try {
        this.refer_FileChooseTimer.getDir(dirFile);
      }
      catch (VMException ex) {
        this.refer_FileChooseTimer.getCdCon().setEnabled(true);
        this.refer_FileChooseTimer.getCdLocalSkim().setEnabled(true);
        this.refer_FileChooseTimer.getCdLocalText().setEnabled(false);
        this.refer_FileChooseTimer.getCdLocalText().setEditable(false);
        this.refer_FileChooseTimer.getCd().setEnabled(true);
        this.refer_FileChooseTimer.getCr().setEnabled(true);
        JOptionPane.showMessageDialog(this.refer_FileChooseTimer.getCdp(), this.refer_FileChooseTimer.getUtil()
            .getResource("com.huawei.vm.console.out.431"));
        this.refer_FileChooseTimer.getCdLocalText().setText("");
        return;
      } 
      mySabreUDF.setRootPath(dirFile);
      if (dirFile.getName().length() >= 30) {
        mySabreUDF.setImageIdentifier(dirFile.getName().substring(0, 28) + '~');
      }
      else {
        mySabreUDF.setImageIdentifier(dirFile.getName());
      } 
      ArrayList<UDFImageBuilderFile> list = mySabreUDF.getRootUDFImageBuilderFile().getChilds();
      UDFImageBuilderFile[] childs = new UDFImageBuilderFile[list.size()];
      int j = 0;
      for (UDFImageBuilderFile udf : list) {
        childs[j++] = udf;
      }
      this.refer_FileChooseTimer.setBeforeLocalDir(childs);
      mySabreUDF.excute();
      this.refer_FileChooseTimer.setMemoryStruct(mySabreUDF.getExtendMap());
      this.refer_FileChooseTimer.getCdCon().setEnabled(true);
      this.refer_FileChooseTimer.getCdLocalSkim().setEnabled(true);
      this.refer_FileChooseTimer.getCdLocalText().setEnabled(false);
      this.refer_FileChooseTimer.getCdLocalText().setEditable(false);
      if (this.refer_FileChooseTimer.isCdBtnForCon)
      {
        this.refer_FileChooseTimer.getCd().setEnabled(true);
        this.refer_FileChooseTimer.getCr().setEnabled(true);
      }
      else
      {
        this.refer_FileChooseTimer.getCd().setEnabled(false);
        this.refer_FileChooseTimer.getCr().setEnabled(false);
      }
    }
    catch (RuntimeException re) {
      this.refer_FileChooseTimer.getCdCon().setEnabled(true);
      this.refer_FileChooseTimer.getCdLocalSkim().setEnabled(true);
      this.refer_FileChooseTimer.getCdLocalText().setEnabled(false);
      this.refer_FileChooseTimer.getCdLocalText().setEditable(false);
      this.refer_FileChooseTimer.getCd().setEnabled(true);
      this.refer_FileChooseTimer.getCr().setEnabled(true);
      JOptionPane.showMessageDialog(this.refer_FileChooseTimer.getCdp(), this.refer_FileChooseTimer
          .getUtil().getResource("com.huawei.vm.console.out.428"));
      return;
    } catch (Exception e) {
      this.refer_FileChooseTimer.getCdCon().setEnabled(true);
      this.refer_FileChooseTimer.getCdLocalSkim().setEnabled(true);
      this.refer_FileChooseTimer.getCdLocalText().setEnabled(false);
      this.refer_FileChooseTimer.getCdLocalText().setEditable(false);
      this.refer_FileChooseTimer.getCd().setEnabled(true);
      this.refer_FileChooseTimer.getCr().setEnabled(true);
      JOptionPane.showMessageDialog(this.refer_FileChooseTimer.getCdp(), this.refer_FileChooseTimer
          .getUtil().getResource("com.huawei.vm.console.out.428"));
      return;
    } 
  }
}
