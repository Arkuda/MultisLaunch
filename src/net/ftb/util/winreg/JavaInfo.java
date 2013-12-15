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
package net.ftb.util.winreg;

import net.ftb.log.Logger;

/**
 * Java Finder by petrucio@stackoverflow(828681) is licensed under a Creative Commons Attribution 3.0 Unported License.
 * Needs WinRegistry.java. Get it at: http://stackoverflow.com/questions/62289/read-write-to-windows-registry-using-java
 *
 * JavaFinder - Windows-specific classes to search for all installed versions of java on this system
 * Author: petrucio@stackoverflow (828681)
 *****************************************************************************/
/**
 * Helper struct to hold information about one installed java version
 ****************************************************************************/
public class JavaInfo implements Comparable<JavaInfo> {
    public String  path;        //! Full path to java.exe executable file
    public String  version;     //! Version string. "Unkown" if the java process returned non-standard version string
    public boolean is64bits;    //! true for 64-bit javas, false for 32
    private int major, minor, revision, build;

    /**
     * Calls 'javaPath -version' and parses the results
     * @param javaPath: path to a java.exe executable
     ****************************************************************************/
    public JavaInfo(String javaPath) {
        String versionInfo = RuntimeStreamer.execute( new String[] { javaPath, "-version" } );
        String[] tokens = versionInfo.split("\"");
        if (tokens.length < 2) this.version = "0.0.0_00";
        else this.version = tokens[1];
        if (version.contains("-ea"))
            version = version.replace("-ea", "_00");
        this.is64bits = versionInfo.toUpperCase().contains("64-BIT");
        this.path     = javaPath;
        
        if (this.version.indexOf('_') != -1)
        {
            this.build = Integer.parseInt(this.version.substring(version.indexOf('_') + 1));
            this.version = version.substring(0, this.version.indexOf('_'));

        }
        String[] s = this.version.split("\\.");
        this.major = Integer.parseInt(s[0]);
        this.minor = Integer.parseInt(s[1]);
        this.revision = Integer.parseInt(s[2]);

    }

    /**
     * @return Human-readable contents of this JavaInfo instance
     ****************************************************************************/
    public String toString() {
        return this.path + ":\n  Version: " + this.verToString() + "\n  Bitness: " + (this.is64bits ? "64-bits" : "32-bits");
    }
    public String verToString()
    {
        return major + "." + minor + "." + revision + "_" + build;
    }

    @Override
    public int compareTo (JavaInfo o)
    {
        if (o.major > major) return -1;
        if (o.major < major) return 1;
        if (o.minor > minor) return -1;
        if (o.minor < minor) return 1;
        if (o.revision > revision) return -1;
        if (o.revision < revision) return 1;
        if (o.build > build) return -1;
        if (o.build < build) return 1;
        return 0;
    }


}
