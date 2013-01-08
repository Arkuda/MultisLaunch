////////////////////////
//THIS FILE BEEN MODED//
////////////////////////

package net.ftb.data;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import net.ftb.log.Logger;
import net.ftb.util.OSUtils;

public class LauncherStyle extends Properties {

	private static final long serialVersionUID = 6370446700503387209L;
	
	private static LauncherStyle currentStyle;
	
	private static File baseStylePath;
	
	static {
		baseStylePath = new File(OSUtils.getDynamicStorageLocation(), "launcher_styles");
		
		if (!baseStylePath.exists()) {
			baseStylePath.mkdir();
		}
		
		currentStyle = new LauncherStyle();
		currentStyle.load();
	}
	
	public Color control = new Color(153, 149, 140);
	public Color text = new Color(0, 0, 0);//.brighter().brighter().brighter().brighter().brighter();
	public Color nimbusBase = new Color(153, 149, 140);
	public Color nimbusFocus = new Color(153, 149, 140);
	public Color nimbusBorder = new Color(153, 149, 140);
	public Color nimbusLightBackground = new Color(153, 149, 140);
	public Color info = new Color(125, 249, 255);//.brighter().brighter();
	public Color nimbusSelectionBackground = new Color(125, 249, 255);//.brighter().brighter();
	public Color footerColor = new Color(80, 80, 80);
	public Color filterTextColor = new Color(153, 149, 140);
	public Color filterInnerTextColor = new Color(153, 149, 140);

	public void load() {
		this.load(Settings.getSettings().getStyle());
	}
	
	public void load(String file) {
		File filename = new File(baseStylePath, file);
		if (new File(baseStylePath, file).exists()) {
			try {
				this.load(new FileReader(filename));
			} catch (FileNotFoundException e) {
				Logger.logWarn("Could not load style", e);
			} catch (IOException e) {
				Logger.logWarn("Could not load style", e);
			}
		}
		
		this.control = loadColor("control", this.control);
		this.text = loadColor("text", this.text);
		this.nimbusBase = loadColor("nimbusBase", this.nimbusBase);
		this.nimbusFocus = loadColor("nimbusFocus", this.nimbusFocus);
		this.nimbusBorder = loadColor("nimbusBorder", this.nimbusBorder);
		this.nimbusLightBackground = loadColor("nimbusLightBackground", this.nimbusLightBackground);
		this.info = loadColor("info", this.info);
		this.nimbusSelectionBackground = loadColor("nimbusSelectionBackground", this.nimbusSelectionBackground);
		this.footerColor = loadColor("footerColor", this.footerColor);
		this.filterTextColor = loadColor("filterTextColor", this.filterTextColor);
		this.filterInnerTextColor = loadColor("filterInnerTextColor", this.filterInnerTextColor);
		
		try {
			this.store(new FileOutputStream(filename), "FTB Style File");
		} catch (FileNotFoundException e) {
			Logger.logWarn("Could not save style", e);
		} catch (IOException e) {
			Logger.logWarn("Could not save style", e);
		}
	}
	
	public static String getColorAsString(Color col) {
		return col.getRed() + "," + col.getGreen() + "," + col.getBlue();
	}
	
	public static Color getStringAsColor(String str) {
		String[] tokens = str.split(",");
		return new Color(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
	}

	private Color loadColor(String string, Color defaultColor) {
		String defaultColorStr = getColorAsString(defaultColor);
		if (!this.containsKey(string)) {
			this.setProperty(string, defaultColorStr);
		}
		return getStringAsColor(this.getProperty(string, defaultColorStr));
	}

	public static LauncherStyle getCurrentStyle() {
		return currentStyle;
	}

	public static void setCurrentStyle(LauncherStyle currentStyle) {
		LauncherStyle.currentStyle = currentStyle;
	}
	
}
