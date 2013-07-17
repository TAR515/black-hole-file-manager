package ro.edu.ubb.blackhole.dal.communicationservice;

import ro.edu.ubb.blackhole.dal.communicationservice.pcinfoservice.PCInfoProvider;
import ro.edu.ubb.blackhole.dal.communicationservice.pcinfoservice.PCInfoService;
import ro.edu.ubb.blackhole.datastructures.CommandToGui;
import ro.edu.ubb.blackhole.datastructures.GuiRequest;

public class CommunicationProvider implements CommunicationService {

	private PCInfoService pcInfoService = null;

	@Override
	public CommandToGui passwordCheck(GuiRequest request) {
		this.pcInfoService = new PCInfoProvider();
		return this.pcInfoService.passwordCheck(request);
	}

}
