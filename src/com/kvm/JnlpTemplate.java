package com.kvm;

import java.util.HashMap;

import com.library.LoggerUtil;

public class JnlpTemplate {
	private String host;
	private String vendor;
	private String model;
	HashMap<String, String> myJson;
	public JnlpTemplate(String vendor, String model, String host, Boolean isHttps, String loginRes,String secondAuthRes, String downloadRes) {
		this.host = host;
		this.vendor = vendor;
		this.model = model;
		this.myJson = getJson(loginRes);
		HashMap<String, String> myJson2 = getJson(secondAuthRes);
		HashMap<String, String> myJson3 = getJson(downloadRes);
		this.myJson.putAll(myJson2);
		this.myJson.putAll(myJson3);
		LoggerUtil.info("myJson:"+this.myJson);
	}
	private HashMap<String, String> getJson(String input) {
		HashMap<String, String> sites = new HashMap<String, String>();
		if (input.charAt(0) == '{' && input.charAt(input.length()-1) == '}'){
		   String[] kv = input.replace("}","").replace("{", "").split(",");
		   for(String k : kv) {
		   String[] subKv = k.split(":");
		   if (subKv.length >= 2) {
		     sites.put(subKv[0].replace("\"", "").replace(" ", ""), subKv[1].replace("\"", "").replace(" ", ""));
		     LoggerUtil.info("k:"+subKv[0].replace("\"", "")+" v:"+subKv[1].replace("\"", ""));
		     }
		   }
		}
		return sites;
	}

	public String getTemplate() {
		String jnlpTemplate = "";
		if(this.vendor.equalsIgnoreCase("h3c") && this.model.equalsIgnoreCase("uniserverr4900g3")) {
			String newh3cJnlp="<?xml version='1.0' encoding='UTF-8'?>"
		            +"<jnlp spec='1.0+' codebase='https://"+this.host+"/remote_console'>"
		            +"<information>"
		            +"<title>JViewer</title>"
		            +"<vendor>American Megatrends, Inc.</vendor>"
		            +"<description kind='one-line'>JViewer Console Redirection Application</description>"
		            +"<description kind='tooltip'>JViewer Console Redirection Application</description>"
		            +"</information>"
		            +"<security><all-permissions/></security>"
		            +"<resources><j2se version='1.5* 1.6*' initial-heap-size='100m' max-heap-size='256m' java-vm-args='-XX:+DisableExplicitGC' /></resources>"
		            +"<resources><j2se version='1.5+'/><jar href='release/JViewer.jar'/></resources>"
		            +"<resources os='Windows' arch='x86'><j2se version='1.5+'/><nativelib href='release/Win32.jar'/></resources>"
		            +"<resources os='Windows' arch='amd64'><j2se version='1.5+'/><nativelib href='release/Win64.jar'/></resources>"
		            +"<resources os='Linux' arch='x86'><j2se version='1.5+'/><nativelib href='release/Linux_x86_32.jar'/></resources>"
		            +"<resources os='Linux' arch='i386'><j2se version='1.5+'/><nativelib href='release/Linux_x86_32.jar'/></resources>"
		            +"<resources os='Linux' arch='x86_64'><j2se version='1.5+'/><nativelib href='release/Linux_x86_64.jar'/></resources>"
		            +"<resources os='Linux' arch='amd64'><j2se version='1.5+'/><nativelib href='release/Linux_x86_64.jar'/></resources>"
		            +"<resources os='Mac OS X' arch='i386'><j2se version='1.5+'/><nativelib href='release/Mac32.jar'/></resources>"
		            +"<resources os='Mac OS X' arch='x86_64'><j2se version='1.5+'/><nativelib href='release/Mac64.jar'/></resources>"
		            +"<application-desc>"
		            +"<argument>-apptype</argument>"
		            +"<argument>JViewer</argument>"
		            +"<argument>-hostname</argument>"
		            +"<argument>"+this.host+"</argument>"
		            +"<argument>-kvmtoken</argument>"
		            +"<argument>"+this.myJson.get("token")+"</argument>"
		            +"<argument>-kvmsecure</argument>"
		            +"<argument>"+this.myJson.get("kvmsecure")+"</argument>"
		            +"<argument>-kvmport</argument>"
		            +"<argument>"+this.myJson.get("kvm_port")+"</argument>"
		            +"<argument>-vmsecure</argument>"
		            +"<argument>"+this.myJson.get("vmsecure")+"</argument>"
		            +"<argument>-cdstate</argument>"
		            +"<argument>"+this.myJson.get("cd_status")+"</argument>"
		            +"<argument>-fdstate</argument>"
		            +"<argument>"+this.myJson.get("fd_status")+"</argument>"
		            +"<argument>-hdstate</argument>"
		            +"<argument>"+this.myJson.get("hd_status")+"</argument>"
		            +"<argument>-cdport</argument>"
		            +"<argument>"+this.myJson.get("cd_port")+"</argument>"
		            +"<argument>-fdport</argument>"
		            +"<argument>"+this.myJson.get("fd_port")+"</argument>"
		            +"<argument>-hdport</argument>"
		            +"<argument>"+this.myJson.get("hd_port")+"</argument>"
		            +"<argument>-cdnum</argument>"
		            +"<argument>"+this.myJson.get("num_cd")+"</argument>"
		            +"<argument>-fdnum</argument>"
		            +"<argument>"+this.myJson.get("num_fd")+"</argument>"
		            +"<argument>-hdnum</argument>"
		            +"<argument>"+this.myJson.get("num_hd")+"</argument>"
		            +"<argument>-singleportenabled</argument>"
		            +"<argument>"+this.myJson.get("single_port_enabled")+"</argument>"
		            +"<argument>-ldapuser</argument>"
		            +"<argument>"+this.myJson.get("ldap_user")+"</argument>"
		            +"<argument>-extendedpriv</argument>"
		            +"<argument>"+this.myJson.get("extendedpriv")+"</argument>"
		            +"<argument>-keyboardlayout</argument>"
		            +"<argument>AD</argument>"
		            +"<argument>-localization</argument>"
		            +"<argument>cn</argument>"
		            +"<argument>-retrycount</argument>"
		            +"<argument>"+this.myJson.get("retry_count")+"</argument>"
		            +"<argument>-retryinterval</argument>"
		            +"<argument>"+this.myJson.get("retry_interval")+"</argument>"
		            +"<argument>-kvmstatus</argument>"
		            +"<argument>"+this.myJson.get("kvm_status")+"</argument>"
		            +"<argument>-kvmcdnum</argument>"
		            +"<argument>"+this.myJson.get("kvm_num_cd")+"</argument>"
		            +"<argument>-kvmfdnum</argument>"
		            +"<argument>"+this.myJson.get("kvm_num_fd")+"</argument>"
		            +"<argument>-kvmhdnum</argument>"
		            +"<argument>"+this.myJson.get("kvm_num_hd")+"</argument>"
		            +"<argument>-webcookie</argument>"
		            +"<argument>"+this.myJson.get("session")+"</argument>"
		            +"<argument>-isUIS</argument>"
		            +"<argument>0</argument>"
		            +"<argument>-isSpe</argument>"
		            +"<argument>16978692</argument>"
		            +"<argument>-oemfeatures</argument>"
		            +"<argument>"+this.myJson.get("oemFeature")+"</argument>"
		            +"</application-desc>"
		            +"</jnlp>";
			jnlpTemplate = newh3cJnlp;
			LoggerUtil.info("newh3cjnlp:"+newh3cJnlp);
		}
		return jnlpTemplate;

	}

}
