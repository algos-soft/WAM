package it.algos.wam.settings;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.domain.pref.PrefType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * General application preferences.
 * <p>
 * (Company preferences are defined in the CompanyPrefs enum)<br>
 * Defines the preferences and the methods to access them.<br>
 * Each preference has a key, a type and a default value.
 */

public enum ManagerPrefs  {

	smtpServer("smtpServer", PrefType.string, ""),

	smtpPort("smtpPort", PrefType.integer, 25),

	smtpUseAuth("smtpUseAuth", PrefType.bool, false),

	smtpPassword("smtpPassword", PrefType.string, ""),

	smtpUserName("smtpUser", PrefType.string, ""),

	startDaemonAtStartup("startDaemonAtStartup", PrefType.bool, false),

	;

	private String code;
	private PrefType type;
	private Object defaultValue;

	private ManagerPrefs(String key, PrefType type, Object defaultValue) {
		this.code = key;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	/**
	 * Retrieves this preference's value as boolean
	 */
	public boolean getBool(){
		return Pref.getBool(code, null, defaultValue);
	}

	/**
	 * Retrieves this preference's value as byte[]
	 */
	public byte[] getBytes(){
		return Pref.getBytes(code, null,defaultValue);
	}

	/**
	 * Retrieves this preference's value as Date
	 */
	public Date getDate(){
		return Pref.getDate(code,null, defaultValue);
	}
	
	/**
	 * Retrieves this preference's value as BigDecimal
	 */
	public BigDecimal getDecimal(){
		return Pref.getDecimal(code, null,defaultValue);
	}

	/**
	 * Retrieves this preference's value as int
	 */
	public int getInt(){
		return Pref.getInt(code,null, defaultValue);
	}

	/**
	 * Retrieves this preference's value as String
	 */
	public String getString(){
		return Pref.getString(code, null,defaultValue);
	}
	
	/**
	 * Retrieves this preference's value as Image
	 */
	public Image getImage(){
		return Pref.getImage(code, null,defaultValue);
	}

	/**
	 * Retrieves this preference's value as Resource
	 */
	public Resource getResource() {
		return Pref.getResource(code, null,defaultValue);
	}

	/**
	 * Writes a value in the storage for this preference
	 * <p>
	 * If the preference does not exist it is created now.
	 *
	 * @param value
	 *            the value
	 */
	public void put(Object value) {
		Pref.put(code, value, type);
	}

	/**
	 * Removes this preference from the storage.
	 * <p>
	 */
	public void remove(){
		Pref.remove(code, null);
	}
	

}
