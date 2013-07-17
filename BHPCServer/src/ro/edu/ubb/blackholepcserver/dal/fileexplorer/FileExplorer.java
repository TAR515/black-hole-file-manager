package ro.edu.ubb.blackholepcserver.dal.fileexplorer;

import java.util.List;

import ro.edu.ubb.blackhole.libs.datastructures.FileItem;

public interface FileExplorer {
	
	public List<FileItem> getListOfFiles(String path);
	
	public long getDirectorySize(String path);
	
	public boolean deleteFiles(List<String> paths);

}
