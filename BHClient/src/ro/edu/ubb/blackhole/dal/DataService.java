package ro.edu.ubb.blackhole.dal;

import ro.edu.ubb.blackhole.dal.localdatabase.AndroidDatabaseService;
import ro.edu.ubb.blackhole.dal.localfilemanager.LocalFileService;
import ro.edu.ubb.blackhole.dal.pcfilemanager.PCFileService;

/**
 * Provides an interface to the module. This is the entry point of the "Data Access Layer" component.
 * 
 * @author Turdean Arnold Robert
 * @version 1.0
 */
public interface DataService extends AndroidDatabaseService, LocalFileService, PCFileService {

}
