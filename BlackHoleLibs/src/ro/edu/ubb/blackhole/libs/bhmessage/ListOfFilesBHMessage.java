package ro.edu.ubb.blackhole.libs.bhmessage;

import java.util.ArrayList;
import java.util.List;

import ro.edu.ubb.blackhole.libs.datastructures.FileItem;

/**
 * Contains all files and her informations located into a specified directory.
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public class ListOfFilesBHMessage extends BlackHoleMessage {
	
	/**
	 * All files located into a specified directory.
	 */
	private List<FileItem> listOfFiles = new ArrayList<FileItem>();
	
	public ListOfFilesBHMessage() {
		super(BHMessageType.BH_LIST_OF_FILES_MESSAGE);
	}
	
	public ListOfFilesBHMessage(int requestMessageType) {
		super(BHMessageType.BH_LIST_OF_FILES_MESSAGE, requestMessageType);
	}

	public List<FileItem> getListOfFiles() {
		return listOfFiles;
	}

	public void setListOfFiles(List<FileItem> listOfFiles) {
		this.listOfFiles = listOfFiles;
	}
	
	public void appendListOfFiles(FileItem newFileItem) {
		if (newFileItem != null) {
			this.listOfFiles.add(newFileItem);
		}
	}

}
