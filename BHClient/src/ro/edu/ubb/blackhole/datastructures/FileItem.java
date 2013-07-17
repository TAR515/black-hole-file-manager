package ro.edu.ubb.blackhole.datastructures;

import java.io.File;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ro.edu.ubb.blackhole.R;
import ro.edu.ubb.blackhole.gui.activityes.FileManager;

/**
 * This object contains all informations about a file or folder in context of {@link FileManager}. This is more than a simple
 * {@link File} format. It contains the file icon, file access rights and and isSelected attribute which indicates that the user
 * selected the current file or not.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class FileItem implements Comparable<FileItem>, Serializable {

	/**
	 * Default serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Icon code.
	 */
	private int fileIcon = -1;

	/**
	 * Path of the file.
	 */
	private String filePath = null;

	/**
	 * Name of the file.
	 */
	private String fileName = null;

	/**
	 * Size of the file.
	 */
	private String fileSize = null;

	/**
	 * Extension of the file.
	 */
	private String fileExtension = null;

	/**
	 * Last modified date of the file.
	 */
	private Date lastModifiedDate = null;

	/**
	 * Indicates the file permissions.
	 */
	private String filePermissions = null;

	/**
	 * True - if this file is a folder / False - otherwise.
	 */
	private boolean isThisFolder = false;

	/**
	 * True - if this file is selected my the user / False - otherwise.
	 */
	private boolean isSelected = false;

	/**
	 * This {@link HashMap} contains the Black Hole supported icons associated to a file icon ID.
	 */
	private Map<String, Integer> FILE_EXTRACTION_RESOURCES = new HashMap<String, Integer>();

	public FileItem(File file) {
		super();
		this.filePath = file.getPath();
		this.isThisFolder = file.isDirectory();

		boolean isWriteable = file.canWrite();
		boolean isReadable = file.canRead();
		boolean isExecutable = file.canExecute();

		setFileSize(file.length());
		setLastModifiedDate(file.lastModified());
		setFilePermissions(isExecutable, isReadable, isWriteable);

		init();
	}

	public FileItem(String filePath, boolean isThisFolder) {
		super();
		this.filePath = filePath;
		this.isThisFolder = isThisFolder;
		this.lastModifiedDate = new Date(0);
		this.fileSize = 0 + " mb";
		this.filePermissions = new String("Unknown!");

		init();
	}

	/**
	 * Initialize all components.
	 */
	private void init() {
		this.FILE_EXTRACTION_RESOURCES.put("avi", R.drawable.avi);
		this.FILE_EXTRACTION_RESOURCES.put("doc", R.drawable.doc);
		this.FILE_EXTRACTION_RESOURCES.put("docx", R.drawable.doc);
		this.FILE_EXTRACTION_RESOURCES.put("exe", R.drawable.exe);
		this.FILE_EXTRACTION_RESOURCES.put("", R.drawable.folder);
		this.FILE_EXTRACTION_RESOURCES.put("html", R.drawable.html);
		this.FILE_EXTRACTION_RESOURCES.put("jpg", R.drawable.jpg);
		this.FILE_EXTRACTION_RESOURCES.put("mp3", R.drawable.mp3);
		this.FILE_EXTRACTION_RESOURCES.put("mp4", R.drawable.mp4);
		this.FILE_EXTRACTION_RESOURCES.put("mpg", R.drawable.mpg);
		this.FILE_EXTRACTION_RESOURCES.put("pdf", R.drawable.pdf);
		this.FILE_EXTRACTION_RESOURCES.put("png", R.drawable.png);
		this.FILE_EXTRACTION_RESOURCES.put("rar", R.drawable.rar);
		this.FILE_EXTRACTION_RESOURCES.put("tgz", R.drawable.tgz);
		this.FILE_EXTRACTION_RESOURCES.put("txt", R.drawable.txt);
		this.FILE_EXTRACTION_RESOURCES.put("wav", R.drawable.wav);
		this.FILE_EXTRACTION_RESOURCES.put("xml", R.drawable.xml);
		this.FILE_EXTRACTION_RESOURCES.put("zip", R.drawable.zip);
		this.FILE_EXTRACTION_RESOURCES.put("selected", R.drawable.selected);

		setFileName();
		setFileExtension();
		setFileIcon();
	}

	public int getFileIcon() {
		return fileIcon;
	}

	/**
	 * At your own risk!
	 * 
	 * @param fileIcon
	 *            Id from R.
	 */
	public void setFileIcon(int fileIcon) {
		this.fileIcon = fileIcon;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;

		setFileName();
		setFileExtension();
		setFileIcon();
	}

	/**
	 * At your own risk! It does not change the path, just the file name!
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * In bytes.
	 * 
	 * @param fileSize
	 */
	public void setFileSize(long fileSize) {
		if (this.isThisFolder) {
			this.fileSize = "";
		} else {
			this.fileSize = calculateFileSize(fileSize);
		}
	}

	/**
	 * Use this method if you want to set a directory size.
	 * 
	 * @param directorySize
	 */
	public void setDirectorySize(long directorySize) {
		this.fileSize = calculateFileSize(directorySize);
	}

	private String calculateFileSize(long fileSize) {
		if (fileSize <= 1024) {
			return fileSize + " b";
		}

		if (fileSize > 1024 && fileSize <= 1048576) {
			double size = fileSize / 1024.0;
			return getStringWithPrecision(size, 1) + " kb";
		}

		if (fileSize > 1048576 && fileSize <= 1073741824) {
			double size = fileSize / 1048576.0;
			return getStringWithPrecision(size, 1) + " mb";
		}

		if (fileSize > 1073741824 && fileSize <= 1099511627776L) {
			double size = fileSize / 1073741824.0;
			return getStringWithPrecision(size, 1) + " gb";
		}

		if (fileSize > 1099511627776L) {
			double size = fileSize / 1099511627776.0;
			return getStringWithPrecision(size, 1) + " tb";
		}

		return "";
	}

	private String getStringWithPrecision(double num, int prec) {
		String tmp = Double.toString(num);
		int lastSpecialChar = tmp.lastIndexOf(".");

		return tmp.substring(0, lastSpecialChar + prec + 1);
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public String getLastModifiedDateUserFriendly() {
		Format formatter = new SimpleDateFormat("yyyy/MM/dd - HH:mm");
		return formatter.format(this.lastModifiedDate);
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setLastModifiedDate(long lastModifiedDate) {
		this.lastModifiedDate = new Date(lastModifiedDate);
	}

	public boolean isThisFolder() {
		return isThisFolder;
	}

	public void setThisFolder(boolean isThisFolder) {
		this.isThisFolder = isThisFolder;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;

		setFileIcon();
	}

	public String getFilePermissions() {
		return filePermissions;
	}

	public void setFilePermissions(String filePermissions) {
		this.filePermissions = filePermissions;
	}

	public void setFilePermissions(boolean isExecutable, boolean isReadable, boolean isWriteable) {
		this.filePermissions = new String("");

		if (!isExecutable && !isReadable && !isWriteable) {
			this.filePermissions += "No permissions!";
		} else {

			if (isExecutable) {
				this.filePermissions += " +execute";
			}

			if (isReadable) {
				this.filePermissions += " +read";
			}

			if (isWriteable) {
				this.filePermissions += " +write";
			}
		}
	}

	/**
	 * If the file path is already configured than this method will set the file name.
	 */
	private void setFileName() {
		if (this.filePath != null) {
			int lastIndOfSlash = this.filePath.lastIndexOf("/");
			int lastIndOfBackSlash = this.filePath.lastIndexOf("\\");

			int lastSpecialChar = (lastIndOfSlash > lastIndOfBackSlash) ? lastIndOfSlash : lastIndOfBackSlash;
			this.fileName = this.filePath.substring(lastSpecialChar + 1, this.filePath.length());
		} else {
			this.fileName = null;
		}
	}

	/**
	 * If the file path is already configured than this method will set the file extension.
	 */
	private void setFileExtension() {
		if (this.filePath != null) {
			int lastSpecialChar = this.filePath.lastIndexOf(".");

			if (this.isThisFolder) {
				this.fileExtension = "";
			} else {
				this.fileExtension = this.filePath.substring(lastSpecialChar + 1, this.filePath.length());
			}
		} else {
			this.fileExtension = null;
		}
	}

	/**
	 * If the file extraction is already configured than this method will set the file icon else the file icon will be -1;
	 */
	private void setFileIcon() {
		if (this.fileExtension != null) {
			String fileExtension = this.getFileExtension();

			if (this.isSelected) {
				fileExtension = "selected";
			}

			if (this.FILE_EXTRACTION_RESOURCES.containsKey(fileExtension)) {
				this.fileIcon = this.FILE_EXTRACTION_RESOURCES.get(fileExtension);
			} else {
				this.fileIcon = R.drawable.unknown;
			}
		} else {
			this.fileIcon = -1;
		}
	}

	/**
	 * @see #hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((FILE_EXTRACTION_RESOURCES == null) ? 0 : FILE_EXTRACTION_RESOURCES.hashCode());
		result = prime * result + ((fileExtension == null) ? 0 : fileExtension.hashCode());
		result = prime * result + fileIcon;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((filePermissions == null) ? 0 : filePermissions.hashCode());
		result = prime * result + ((fileSize == null) ? 0 : fileSize.hashCode());
		result = prime * result + (isSelected ? 1231 : 1237);
		result = prime * result + (isThisFolder ? 1231 : 1237);
		result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
		return result;
	}

	/**
	 * @see #equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileItem other = (FileItem) obj;
		if (FILE_EXTRACTION_RESOURCES == null) {
			if (other.FILE_EXTRACTION_RESOURCES != null)
				return false;
		} else if (!FILE_EXTRACTION_RESOURCES.equals(other.FILE_EXTRACTION_RESOURCES))
			return false;
		if (fileExtension == null) {
			if (other.fileExtension != null)
				return false;
		} else if (!fileExtension.equals(other.fileExtension))
			return false;
		if (fileIcon != other.fileIcon)
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (filePermissions == null) {
			if (other.filePermissions != null)
				return false;
		} else if (!filePermissions.equals(other.filePermissions))
			return false;
		if (fileSize == null) {
			if (other.fileSize != null)
				return false;
		} else if (!fileSize.equals(other.fileSize))
			return false;
		if (isSelected != other.isSelected)
			return false;
		if (isThisFolder != other.isThisFolder)
			return false;
		if (lastModifiedDate == null) {
			if (other.lastModifiedDate != null)
				return false;
		} else if (!lastModifiedDate.equals(other.lastModifiedDate))
			return false;
		return true;
	}

	/**
	 * @see #toString()
	 */
	@Override
	public String toString() {
		return "FileItem [fileIcon=" + fileIcon + ", filePath=" + filePath + ", fileName=" + fileName
				+ ", fileSize=" + fileSize + ", fileExtension=" + fileExtension + ", lastModifiedDate="
				+ lastModifiedDate + ", filePermissions=" + filePermissions + ", isThisFolder="
				+ isThisFolder + ", isSelected=" + isSelected + "]";
	}

	/**
	 * @see #compareTo(FileItem) The folders enjoys the benefits at all.
	 */
	@Override
	public int compareTo(FileItem another) {
		if (this.isThisFolder != another.isThisFolder) {
			if (this.isThisFolder) {
				return -1;
			} else {
				return 1;
			}
		}

		return this.fileName.compareToIgnoreCase(another.fileName);
	}
}
