package fi.metropolia.villevra.util;

/*
 * Simple wrapper to hold filenames and full file paths of same videofile
 */
public class URLStringWrapper {

	private String fullPath;
	private String fileName;
	
	public URLStringWrapper(String fullPath, String fileName) {
		this.fullPath = fullPath;
		this.fileName = fileName;
	}

	public String getFullPath() {
		return fullPath;
	}

	public String getFileName() {
		return fileName;
	}
	
	

}
