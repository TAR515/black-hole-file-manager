package ro.edu.ubb.blackholepcserver.dal;

import java.util.List;

import ro.edu.ubb.blackhole.libs.datastructures.FileItem;
import ro.edu.ubb.blackholepcserver.dal.fileexplorer.FileExplorer;
import ro.edu.ubb.blackholepcserver.dal.fileexplorer.WindowsFileExplorer;

public class DataProvider implements DataService {

	/**
	 * File Explorer.
	 */
	private static FileExplorer fileExplorer = null;
	
	
	public DataProvider() {
		DataProvider.fileExplorer = new WindowsFileExplorer();
	}
	
	@Override
	public List<FileItem> getListOfFiles(String path) {
		return DataProvider.fileExplorer.getListOfFiles(path);
	}

	@Override
	public long getDirectorySize(String path) {
		return DataProvider.fileExplorer.getDirectorySize(path);
	}

	@Override
	public boolean deleteFiles(List<String> paths) {
		return DataProvider.fileExplorer.deleteFiles(paths);
	}

}
