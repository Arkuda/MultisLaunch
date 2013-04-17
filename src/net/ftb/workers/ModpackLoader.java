/*
 * This file is part of FTB Launcher.
 *
 * Copyright © 2012-2013, FTB Launcher Contributors <https://github.com/Slowpoke101/FTBLaunch/>
 * FTB Launcher is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ftb.workers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import net.ftb.data.Map;
import net.ftb.data.ModPack;
import net.ftb.data.TexturePack;
import net.ftb.gui.panes.ModpacksPane;
import net.ftb.log.Logger;
import net.ftb.util.AppUtils;
import net.ftb.util.DownloadUtils;
import net.ftb.util.OSUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModpackLoader extends Thread {
	private ArrayList<String> xmlFiles = new ArrayList<String>();
	private static int counter = 0;

	public ModpackLoader(ArrayList<String> xmlFiles) {
		this.xmlFiles = xmlFiles;
	}

	@Override
	public void run() {
		for(String xmlFile : xmlFiles) {
			boolean privatePack = !xmlFile.equalsIgnoreCase("modpacks.xml");
			File modPackFile = new File(OSUtils.getDynamicStorageLocation(), "ModPacks" + File.separator + xmlFile);
			File addmodPackFile = new File(OSUtils.getDynamicStorageLocation(), "ModPacks" + File.separator +"add" + xmlFile);
			try {
				modPackFile.getParentFile().mkdirs();
				DownloadUtils.downloadToFile(new URL(DownloadUtils.getStaticCreeperhostLink(xmlFile)), modPackFile);
				DownloadUtils.downloadToFile(new URL(DownloadUtils.getStaticDropboxLink(xmlFile)), addmodPackFile);
			} catch (IOException e) {
				Logger.logWarn("Failed to load modpacks, loading from backup", e);
			}
			Logger.logInfo("Loading modpack information for " + xmlFile + "...");
			InputStream modPackStream = null;
			InputStream modPackSubSrtring = null;
			try {
				modPackStream = new FileInputStream(modPackFile);
				modPackSubSrtring = new FileInputStream(addmodPackFile);
			} catch (IOException e) {
				Logger.logWarn("Failed to read modpack file - falling back to direct download", e);
			}
			if(modPackStream == null) {
				try {
					modPackStream = new URL(DownloadUtils.getStaticCreeperhostLink(xmlFile)).openStream();
					modPackSubSrtring = new URL(DownloadUtils.getStaticDropboxLink(xmlFile)).openStream();
				} catch (IOException e) {
					Logger.logError("Completely unable to download the modpack file - check your connection", e);
				}
			}
			if(modPackStream != null) {
				Document doc;
				Document docA;
				try {
					doc = AppUtils.getXML(modPackStream);
					docA = AppUtils.getXML(modPackSubSrtring);
				} catch (Exception e) {
					Logger.logError("Exception reading modpack file", e);
					return;
				}
				if(doc == null) {
					Logger.logError("Error: could not load modpack data!");
					return;
				}
				NodeList modPacks = doc.getElementsByTagName("modpack");
				NodeList modPacksAdd = docA.getElementsByTagName("modpack");
				
				for(int i = 0; i < modPacks.getLength(); i++) {
					Node modPackNode = modPacks.item(i);
					NamedNodeMap modPackAttr = modPackNode.getAttributes();
					try {
						ModPack.addPack(new ModPack(modPackAttr.getNamedItem("name").getTextContent(), modPackAttr.getNamedItem("author").getTextContent(),
								modPackAttr.getNamedItem("version").getTextContent(), modPackAttr.getNamedItem("logo").getTextContent(),
								modPackAttr.getNamedItem("url").getTextContent(), modPackAttr.getNamedItem("image").getTextContent(),
								modPackAttr.getNamedItem("dir").getTextContent(), modPackAttr.getNamedItem("mcVersion").getTextContent(), 
								modPackAttr.getNamedItem("serverPack").getTextContent(), modPackAttr.getNamedItem("description").getTextContent(),
								modPackAttr.getNamedItem("mods") != null ? modPackAttr.getNamedItem("mods").getTextContent() : "", 
								modPackAttr.getNamedItem("oldVersions") != null ? modPackAttr.getNamedItem("oldVersions").getTextContent() : "",
								modPackAttr.getNamedItem("animation") != null ? modPackAttr.getNamedItem("animation").getTextContent() : "", counter, privatePack, xmlFile, "false"));
						counter++;
					} catch (Exception e) {
						Logger.logError(e.getMessage(), e);
					}
				}
				try {
					modPackStream.close();
				} catch (IOException e) { }
			
			
				for(int i = 0; i < modPacksAdd.getLength(); i++) {
					Node modPackNode = modPacksAdd.item(i);
					NamedNodeMap modPackAttr = modPackNode.getAttributes();
					try {
						ModPack.addPack(
								new ModPack(
										modPackAttr.getNamedItem("name").getTextContent(), 
										modPackAttr.getNamedItem("author").getTextContent(),
								modPackAttr.getNamedItem("version").getTextContent(), modPackAttr.getNamedItem("logo").getTextContent(),
								modPackAttr.getNamedItem("url").getTextContent(), modPackAttr.getNamedItem("image").getTextContent(),
								modPackAttr.getNamedItem("dir").getTextContent(), modPackAttr.getNamedItem("mcVersion").getTextContent(), 
								modPackAttr.getNamedItem("serverPack").getTextContent(), modPackAttr.getNamedItem("description").getTextContent(),
								modPackAttr.getNamedItem("mods") != null ? modPackAttr.getNamedItem("mods").getTextContent() : "", 
								modPackAttr.getNamedItem("oldVersions") != null ? modPackAttr.getNamedItem("oldVersions").getTextContent() : "",
								modPackAttr.getNamedItem("animation") != null ? modPackAttr.getNamedItem("animation").getTextContent() : "", counter, privatePack, xmlFile,"true"));
						counter++;
					} catch (Exception e) {
						Logger.logError(e.getMessage(), e);
						getStackTrace();
					}
				}
				try {
					modPackStream.close();
				} catch (IOException e) { }
			
			
			}
		}
		if(!ModpacksPane.loaded) {
			ModpacksPane.loaded = true;
			Map.loadAll();
			TexturePack.loadAll();
		}
	}
}
