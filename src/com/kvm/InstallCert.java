package com.kvm;
import java.awt.Font;
import java.awt.Panel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
class InstallCert
{
  public void importExceptionReport(LoginUtil loginUtil) {
    Object[] SureOption = { "" };
    SureOption[0] = loginUtil.getString("RemindOption");
    JLabel Error = new JLabel(loginUtil.getString("Import_Exception"));
    Error.setFont(new Font(loginUtil.getString("font_name"), 0, Main.wordSize));
    JOptionPane.showOptionDialog(new Panel(), Error, loginUtil
        .getString("Remind_title"), 0, 3, null, SureOption, SureOption[0]);
  }
  public void installKeyStore(String host, int port, LoginUtil loginUtil) throws Exception {
    String p = "changeit";
    Object[] SureOption = { "" };
    SureOption[0] = loginUtil.getString("RemindOption");
    char[] passphrase = p.toCharArray();
    File file = new File("jssecacerts");
    if (!file.isFile()) {
      char c = File.separatorChar;
      File file2 = new File(System.getProperty("java.home") + c + "lib" + c + "security");
      file = new File(file2, "jssecacerts");
      if (!file.isFile())
      {
        file = new File(file2, "cacerts");
      }
    } 
    InputStream in = null;
    try {
      in = new FileInputStream(file);
    }
    catch (IOException e) {
      importExceptionReport(loginUtil);
      return;
    } 
    KeyStore ks = null;
    try {
      ks = KeyStore.getInstance(KeyStore.getDefaultType());
    }
    catch (KeyStoreException e) {
      try {
        in.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
        return;
      } 
      importExceptionReport(loginUtil);
      return;
    } 
    try {
      ks.load(in, passphrase);
    }
    catch (IOException e) {
      in.close();
      importExceptionReport(loginUtil);
      return;
    } catch (NoSuchAlgorithmException e) {
      in.close();
      importExceptionReport(loginUtil);
      return;
    } catch (CertificateException e) {
      in.close();
      importExceptionReport(loginUtil);
      return;
    } 
    try {
      in.close();
    }
    catch (IOException e) {
      importExceptionReport(loginUtil);
      return;
    } 
    UIManager.put("FileChooser.cancelButtonText", loginUtil.getString("Cancel_Text"));
    JFileChooser ChainChoose = new JFileChooser();
    ChainChoose.setFileFilter(new JAVAFileFilter());
    int ResultChoose = 0;
    ChainChoose.setDialogTitle(loginUtil.getString("Choose_Title"));
    ChainChoose.setApproveButtonText(loginUtil.getString("Choose_Text"));
    ResultChoose = ChainChoose.showDialog(new JLabel(), null);
    if (ResultChoose != 0) {
      return;
    }
    File Cerfile = ChainChoose.getSelectedFile();
    String ChainFullName = Cerfile.getName();
    boolean CheckNameSuffix = false;
    if (ChainFullName.toLowerCase(Locale.getDefault()).endsWith(".cer") || ChainFullName.toLowerCase(Locale.getDefault()).endsWith(".crt") || ChainFullName
      .toLowerCase(Locale.getDefault()).endsWith(".pem")) {
      CheckNameSuffix = true;
    }
    else {
      CheckNameSuffix = false;
    } 
    if (!CheckNameSuffix) {
      JLabel WrongSuffix = new JLabel(loginUtil.getString("WrongSuffix"));
      WrongSuffix.setFont(new Font(loginUtil.getString("font_name"), 0, Main.wordSize));
      JOptionPane.showOptionDialog(new Panel(), WrongSuffix, loginUtil
          .getString("Remind_title"), 0, 3, null, SureOption, SureOption[0]);
      return;
    } 
    double ChainFileSize = Cerfile.length() / 1024.0D / 1024.0D;
    if (ChainFileSize > 1.0D) {
      JLabel ChainOverSize = new JLabel(loginUtil.getString("ChainOverSize"));
      ChainOverSize.setFont(new Font(loginUtil.getString("font_name"), 0, Main.wordSize));
      JOptionPane.showOptionDialog(new Panel(), ChainOverSize, loginUtil
          .getString("Remind_title"), 0, 3, null, SureOption, SureOption[0]);
      return;
    } 
    InputStream CertIn = new FileInputStream(Cerfile);
    CertificateFactory cf = null;
    try {
      cf = CertificateFactory.getInstance("X.509");
    }
    catch (CertificateException e) {
      try {
        CertIn.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
        return;
      } 
      importExceptionReport(loginUtil);
      return;
    } 
    X509Certificate Cert = null;
    try {
      Cert = (X509Certificate)cf.generateCertificate(CertIn);
    }
    catch (Exception e) {
      JLabel ChainWrong = new JLabel(loginUtil.getString("ChainWrong"));
      ChainWrong.setFont(new Font(loginUtil.getString("font_name"), 0, Main.wordSize));
      JOptionPane.showOptionDialog(new Panel(), ChainWrong, loginUtil
          .getString("Remind_title"), 0, 3, null, SureOption, SureOption[0]);
      return;
    } finally {
      try {
        CertIn.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
      } 
    } 
    String alias = host + "-CA";
    ks.setCertificateEntry(alias, Cert);
    char SEP = File.separatorChar;
    File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
    File file1 = new File(dir, "jssecacerts");
    OutputStream out = null;
    try {
      out = new FileOutputStream(file1);
    }
    catch (IOException e) {
      return;
    } 
    try {
      ks.store(out, passphrase);
    }
    catch (KeyStoreException e) {
      try {
        out.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
        return;
      } 
      importExceptionReport(loginUtil);
      return;
    } catch (IOException e) {
      try {
        out.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
        return;
      } 
      importExceptionReport(loginUtil);
      return;
    } catch (NoSuchAlgorithmException e) {
      try {
        out.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
        return;
      } 
      importExceptionReport(loginUtil);
      return;
    } catch (CertificateException e) {
      try {
        out.close();
      }
      catch (IOException e2) {
        importExceptionReport(loginUtil);
        return;
      } 
      importExceptionReport(loginUtil);
      return;
    } 
    out.close();
    JLabel restartRemind = new JLabel(loginUtil.getString("restartRemind"));
    restartRemind.setFont(new Font(loginUtil.getString("font_name"), 0, Main.wordSize));
    JOptionPane.showOptionDialog(new Panel(), restartRemind, loginUtil
        .getString("Remind_title"), 0, 3, null, SureOption, SureOption[0]);
  }
  private static class JAVAFileFilter
    extends FileFilter
  {
    private JAVAFileFilter() {}
    public boolean accept(File file) {
      String name = file.getName();
      return (file.isDirectory() || name.toLowerCase(Locale.getDefault()).endsWith(".cer") || name.toLowerCase(Locale.getDefault()).endsWith(".crt") || name
        .toLowerCase(Locale.getDefault()).endsWith(".pem"));
    }
    public String getDescription() {
      return "*.cer;*.crt;*.pem";
    }
  }
}
