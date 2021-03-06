package com.gst.organisation.supplier.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gst.commands.annotation.CommandType;
import com.gst.commands.handler.NewCommandSourceHandler;
import com.gst.infrastructure.core.api.JsonCommand;
import com.gst.infrastructure.core.data.CommandProcessingResult;
import com.gst.organisation.supplier.service.SupplierWritePlatformService;


@Service
@CommandType(entity = "SUPPLIER", action = "CREATE")
public class CreateSupplierCommandHandler implements NewCommandSourceHandler{
	
	private final SupplierWritePlatformService writePlatformService;
	
	@Autowired
    public CreateSupplierCommandHandler(final SupplierWritePlatformService writePlatformService) {
        this.writePlatformService = writePlatformService;
    }
	@Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {

        return this.writePlatformService.createSupplier(command);
    }

}
