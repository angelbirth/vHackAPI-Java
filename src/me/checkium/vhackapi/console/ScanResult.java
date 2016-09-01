package me.checkium.vhackapi.console;

import java.util.Objects;

public class ScanResult {

	private boolean success = true;
	private String fw = "1";
	protected String username = "???";
	private String av = "1";
	private String scan = "1";
	private String sdk = "1";
	private String spam = "1";
	private String money = "350000";
	private String anonymous = "NO";
	private String repsuccess = "1";
	private String repfail = "1";
	private String successrate = "90";
	
	
	public ScanResult(String[] result) {
		if (result[1] == null) {
			success = false;
		} else {
		username = result[1].substring(26);
		fw = result[2].substring(26);
		av = result[3].substring(27);
		scan = result[4].substring(22);
		sdk = result[5].substring(21);
		spam = result[6].substring(22);
		money = result[7].substring(23);
		anonymous = result[9].substring(27);
		repsuccess = result[11].substring(32);
		repfail = result[12].substring(29);
		successrate = result[13].substring(39);
		}
	}
	
	/**
	 * 
	 * Get some stat about the scan
	 * 
	 * @param stat The stat to get, only av, fw, money, scan, sdk and spam.
	 * @return String The stat.
	 */
	public String getStat(Statistic stat) {
		if (!success) { return null; }
		switch (stat) {
		case av:
			return av;
		case fw:
			return fw;
		case money:
			return money;
		case scan:
			return scan;
		case sdk:
			return sdk;
		case spam:
			return spam;
		default:
			return null;
		}
	}
	
	public String getTransferedIP(){
		return "";
	}
	
	public int getMoney(){
		return Integer.parseInt(money);
	}
	
	public int getReputation(){
		return 0;
	}
	
	public int getSucess(){
		return 0;
	}
	
	public String getUsername() {
		if (!success) { return null; }
		return username;
	}
	
	public String getRepOnSuccess() {
		if (!success) { return null; }
		return repsuccess;
	}
	
	public String getRepOnFail() {
		if (!success) { return null; }
	    return repfail;
	}
	
	public boolean anonymous() {
        return success && Objects.equals(anonymous, "YES");
    }
	
	public int getSuccessRate() {
		if (!success) { return 0; }
		if (successrate == "???") {
			return 0;
		}
		return Integer.valueOf(successrate);
		
	}
	public enum Statistic {

		av, fw, money, spam, scan, sdk

	}
}
