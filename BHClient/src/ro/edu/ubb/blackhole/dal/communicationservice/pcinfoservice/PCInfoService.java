package ro.edu.ubb.blackhole.dal.communicationservice.pcinfoservice;

import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.GuiRequest;

public interface PCInfoService {

	public CommandToGui passwordCheck(GuiRequest request);

}
