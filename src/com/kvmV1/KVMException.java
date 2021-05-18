package com.kvmV1;
public class KVMException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  protected String m_sErrCode;
  protected String m_sErrDesc;
  protected String m_sErrLevl;
  protected String m_sErrModu;
  protected Throwable m_throwable;
  public static final String IO_ERRCODE = "IO_ERRCODE";
  public KVMException() {}
  public KVMException(String sErrCode) {
    super(sErrCode);
    this.m_sErrCode = sErrCode;
    this.m_sErrDesc = sErrCode;
  }
  public KVMException(String sErrCode, String sErrDesc) {
    super(sErrDesc);
    this.m_sErrCode = sErrCode;
    this.m_sErrDesc = sErrDesc;
  }
  public KVMException(String sErrCode, String sErrDesc, Throwable throwable) {
    super(sErrDesc);
    this.m_sErrCode = sErrCode;
    this.m_sErrDesc = sErrDesc;
    this.m_throwable = throwable;
  }
  public KVMException(String sErrCode, String sErrDesc, String sErrModu, String sErrLevl) {
    super(sErrDesc);
    this.m_sErrCode = sErrCode;
    this.m_sErrDesc = sErrDesc;
    this.m_sErrModu = sErrModu;
    this.m_sErrLevl = sErrLevl;
  }
  public String getErrCode() {
    return this.m_sErrCode;
  }
  public String getErrDesc() {
    return this.m_sErrDesc;
  }
  public String getErrLevl() {
    return this.m_sErrLevl;
  }
  public String getErrModu() {
    return this.m_sErrModu;
  }
}
