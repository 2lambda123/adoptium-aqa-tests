package net.adoptopenjdk.test.build.common;

import java.util.ArrayList;
import java.util.Map;

public class BuildIs
{
    /**
     * Returns whether the jdk build we're running 
     * on was built by the vendor that matches the
     * supplied string.
     * 
     * E.g. "AdoptOpenJDK"
     */
    public static boolean createdByThisVendor(String vendorName)
    {
        String lowercaseVendorName = vendorName.toLowerCase();
        if (lowercaseVendorName.equals("adopt") || lowercaseVendorName.equals("adoptopenjdk")) {
            return System.getProperty("java.vendor").equals("AdoptOpenJDK");
        }

        return System.getProperty("java.vendor").toLowerCase().equals(lowercaseVendorName);
    }
    
    /**
     * Returns true if we're using a Hotspot VM.
     */
    public static boolean usingAHotspotVM() {
        //TODO: Figure out a better way of doing this. System properties don't seem to say 
        //Hotspot anywhere except sun.management.compiler, which looks a bit "legacy" to me.
        return !usingAnOpenJ9VM();
    }
     
    /**
     * Returns true if we're using an OpenJ9 VM.
     */
    public static boolean usingAnOpenJ9VM() {
        return System.getProperty("java.vm.name").contains("OpenJ9");
    }

    /** 
     * Takes a java.version value representing the oldest 
     * acceptable version, and returns a boolean 
     * indicating if this code is running on a JDK more 
     * recent than (or equal to) the version supplied. 
     * E.g. "1.8.0_275 or 11.0.2 or 11.0.2.1"
     * Note: Omitting trailing characters defaults them to 0.
     * (So 1.8.0 is treated as 1.8.0_0-b0)
     * Also note: This method assumes all JDK11 builds
     * are older than JDK8 builds, so compare major java
     * versions separately if this concerns you.
     */
    public static boolean moreRecentThanOrEqualToVersion(String version)
    {
        int[] currentVersionArray = turnStringVersionIntoIntArray(System.getProperty("java.runtime.version"));
        int[] usersVersionArray = turnStringVersionIntoIntArray(version);
        
        for (int x = 0 ; x < currentVersionArray.length ; x++ ) {
            if (currentVersionArray[x] > usersVersionArray[x]) {
                // The current build is newer than the supplied build version.
                return true;
            } else if (usersVersionArray[x] > currentVersionArray[x]) {
                // The current build is older than the supplied build version.
                return false;
            }
        }
        
        // The current build matches the supplied build version.
        return true;
    }

    /**
     * Takes an java.version value representing the most recent 
     * acceptable version, and returns a boolean 
     * indicating if this code is running on a JDK older
     * than the version supplied: 
     * E.g. "1.8.0_275 or 11.0.2 or 11.0.2.1"
     * Note: Omitting trailing characters defaults them to 0.
     * (So 1.8.0 is treated as 1.8.0_0-b0)
     * Also note: This method assumes all JDK11 builds
     * are older than JDK8 builds, so compare major java
     * versions separately if this concerns you.
     */
    public static boolean olderThanVersion(String version)
    {
        int[] currentVersionArray = turnStringVersionIntoIntArray(System.getProperty("java.runtime.version"));
        int[] usersVersionArray = turnStringVersionIntoIntArray(version);
        
        for (int x = 0 ; x < currentVersionArray.length ; x++ ) {
            if (currentVersionArray[x] < usersVersionArray[x]) {
                // The current build is newer than the supplied build version.
                return true;
            } else if (usersVersionArray[x] < currentVersionArray[x]) {
                // The current build is older than the supplied build version.
                return false;
            }
        }
        
        // The current build matches the supplied build version.
        return false;
    }

    /**
     * Returns whether the major version of the jdk 
     * build we're running on matches the supplied 
     * int (for jdk8, use 8).
     */
    public static boolean thisMajorVersion(int majorVersion)
    {
        int[] currentVersionArray = turnStringVersionIntoIntArray(System.getProperty("java.runtime.version"));
        if(currentVersionArray[0] == majorVersion) return true;
        return false;
    }

    /**
     * Returns whether the major version of the jdk 
     * build we're running on is above the supplied 
     * int (for jdk8, use 8).
     */
    public static boolean aboveThisMajorVersion(int majorVersion)
    {
        int[] currentVersionArray = turnStringVersionIntoIntArray(System.getProperty("java.runtime.version"));
        if(currentVersionArray[0] > majorVersion) return true;
        return false;
    }

    /**
     * Returns whether the major version of the jdk 
     * build we're running on is below the supplied 
     * int (for jdk8, use 8).
     */
    public static boolean belowThisMajorVersion(int majorVersion)
    {
        int[] currentVersionArray = turnStringVersionIntoIntArray(System.getProperty("java.runtime.version"));
        if(currentVersionArray[0] < majorVersion) return true;
        return false;
    }
    
    /**
     * Takes a runtime version string and returns it as an
     * int array that we can easily compare to other versions.
     * E.g. "1.8.0_275-b01" or "11.0.2+9" or "11.0.2.1+9"
     * Note: Omitting trailing characters defaults them to 0.
     * (So 1.8.0 and 1.8.potato are treated as 1.8.0_0-b0)
     */
    private static int[] turnStringVersionIntoIntArray(String version)
    {
    	try {
            //First we turn it into a String that only has ints and dots.
            String formattedVersion = version;
            boolean hasBuildNumber = formattedVersion.contains("b") || formattedVersion.contains("+");
	        formattedVersion = formattedVersion.replaceAll("\\+", ".");
	        formattedVersion = formattedVersion.replaceAll("-", ".");
	        formattedVersion = formattedVersion.replaceAll("_", ".");
	        formattedVersion = formattedVersion.replaceAll("b", ".");
	        formattedVersion = formattedVersion.replaceAll("\\.\\.", ".");
	        
	        //And we also need to accommodate 8's occasional lack of a minor version number between 
	        //the major version and the maintenance version numbers.
	        if (formattedVersion.startsWith("1.8.")) {
	            formattedVersion = formattedVersion.substring(2);
	        }
	        if (formattedVersion.startsWith("8.") && !formattedVersion.startsWith("8.0.")) {
	            formattedVersion = formattedVersion.substring(2);
	            formattedVersion = "8.0." + formattedVersion;
	        }
        
	        //Then we turn it into an array, adding any missing zeros and stripping out
	        //any too-long numbers that are likely the build system putting the date into
	        //the version string of nightlies.
	        int[] returnVersionArray = {0,0,0,0,0};
	        ArrayList<String> formattedVersionArray = new ArrayList(formattedVersion.split("\\."));
	        for (int x = 0; x < formattedVersionArray.size() ; ) {
	        	if (formattedVersionArray.get(x).length() > 5) {
	        	    formattedVersionArray.remove(x);
	        	} else {
	        	    x++;
	        	}
	        }
	        for (int x = 0; x < formattedVersionArray.size() ; x++ ) {
	            if (hasBuildNumber && (x > (returnVersionArray.length - 3))) break;
	            if (!hasBuildNumber && (x > (returnVersionArray.length - 2))) break;
	            returnVersionArray[x] = Integer.parseInt(formattedVersionArray.get(x));
	        }
	        
	        if (hasBuildNumber) {
	            returnVersionArray[returnVersionArray.length - 1] = Integer.parseInt(formattedVersionArray.get(formattedVersionArray.size() - 1));
	        }
	        
	        return returnVersionArray;
    	} catch(Exception e) {
    	    throw new Error("The BuildIs class has tried and failed to parse the java version string \"" + version + "\".", e);
    	}
    }
}