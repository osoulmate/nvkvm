package com.kvm;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.library.LoggerUtil;

public class JnlpTemplate {
	private String host;
	private String vendor;
	private String model;
	HashMap<String, String> myJson;
	private String myJnlp;
	public JnlpTemplate(String vendor, String model, String host, Boolean isHttps, String loginRes,String secondAuthRes, String downloadRes) {
		this.host = host;
		this.vendor = vendor;
		this.model = model;
		this.myJson = getJson(loginRes);
		if(this.vendor.equalsIgnoreCase("h3c")){
		  HashMap<String, String> myJson2 = getJson(secondAuthRes);
		  HashMap<String, String> myJson3 = getJson(downloadRes);
		  this.myJson.putAll(myJson2);
		  this.myJson.putAll(myJson3);
		}else if(this.vendor.equalsIgnoreCase("hp")) {
	    	  String jarPattern = "\"archive=(.*)\s+width=";
	    	  String info0Pattern = "\"INFO0\\\\=\\\\(.*)\\\\\"";
	    	  LoggerUtil.info("info0Pattern:"+info0Pattern);
	    	  String info1Pattern = "\"INFO1\\\\=\\\\(.*)\\\\\"";
	    	  String info2Pattern = "\"INFO2\\\\=\\\\(.*)\\\\\"";
	    	  Pattern jar = Pattern.compile(jarPattern);
	    	  Pattern info0 = Pattern.compile(info0Pattern);
	    	  Pattern info1 = Pattern.compile(info1Pattern);
	    	  Pattern info2 = Pattern.compile(info2Pattern);
	    	  Matcher m1 = jar.matcher(downloadRes);
	    	  Matcher m2 = info0.matcher(downloadRes);
	    	  Matcher m3 = info1.matcher(downloadRes);
	    	  Matcher m4 = info2.matcher(downloadRes);
	    	  if(m1.find()) {this.myJson.put("jar", m1.group(1));}
	    	  if(m2.find()) {this.myJson.put("info0", m2.group(1).replace("\"", ""));}
	    	  if(m3.find()) {this.myJson.put("info1", m3.group(1).replace("\"", ""));}
	    	  if(m4.find()) {this.myJson.put("info2", m4.group(1).replace("\"", ""));}
		}else if(this.vendor.equalsIgnoreCase("inspur")) {
			this.myJnlp = downloadRes.replace("https", "http").replace(":443", "");
		}else if (this.vendor.equalsIgnoreCase("sugon")) {
			this.myJnlp = downloadRes.replace("https", "http").replace(":443", "");
		}
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
		if(this.vendor.equalsIgnoreCase("h3c")) {
			String codebase = this.host+"/remote_console";
			if(this.model.equalsIgnoreCase("uniserverr4900g3")) {
				codebase = this.host+"/remote_console";
			}else if (this.model.equalsIgnoreCase("uniserverr4700g3")) {
				codebase = this.host+"/Java";
			}
			String newh3cJnlp="<?xml version='1.0' encoding='UTF-8'?>"
		            +"<jnlp spec='1.0+' codebase='https://"+codebase+"'>"
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
		            +"<argument>"+this.myJson.getOrDefault("kvmsecure","0")+"</argument>"
		            +"<argument>-kvmport</argument>"
		            +"<argument>"+this.myJson.getOrDefault("kvm_port","7578")+"</argument>"
		            +"<argument>-vmsecure</argument>"
		            +"<argument>"+this.myJson.getOrDefault("vmsecure","0")+"</argument>"
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
		}else if(this.vendor.equalsIgnoreCase("hp")) {
			String hpJnlp="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+"<jnlp spec=\"1.0+\" codebase=\"https://"+this.host+"/\" href=\"\">"
					+"<information><title>Integrated Remote Console</title>"
					+"<vendor>HPE</vendor>"
					+"<offline-allowed>"
					+"</offline-allowed>"
					+"</information>"
					+"<security>"
					+"<all-permissions>"
					+"</all-permissions>"
					+"</security>"
					+"<resources>"
					+"<j2se version=\"1.5+\" href=\"http://java.sun.com/products/autodl/j2se\">"
					+"</j2se><jar href=\"https://"+this.host+this.myJson.get("jar")+"\" main=\"false\" />"
					+"</resources>"
					+"<property name=\"deployment.trace.level property\" value=\"basic\">"
					+"</property>"
					+"<applet-desc main-class=\"com.hp.ilo2.intgapp.intgapp\" name=\"iLOJIRC\" documentbase=\"https://"+this.host
					+"/html/java_irc.html\" width=\"1\" height=\"1\">"
					+"<param name=\"RCINFO1\" value=\""+this.myJson.get("session_key")+"\"/>"
					+"<param name=\"RCINFOLANG\" value=\"en\"/>"
					+"<param name=\"INFO0\" value=\""+this.myJson.get("info0")+"\"/>"
					+"<param name=\"INFO1\" value=\""+this.myJson.get("info1")+"\"/>"
					+"<param name=\"INFO2\" value=\""+this.myJson.get("info2")+"\"/>"
					+"</applet-desc>"
					+"<update check=\"background\">"
					+"</update>"
					+"</jnlp>";
			jnlpTemplate = hpJnlp;
			LoggerUtil.info("hpjnlp:"+hpJnlp);
		}else if(this.vendor.equalsIgnoreCase("inspur")) {
			jnlpTemplate = this.myJnlp;
		}else if (this.vendor.equalsIgnoreCase("sugon")) {
			jnlpTemplate = this.myJnlp;
		}
		return jnlpTemplate;

	}

}
