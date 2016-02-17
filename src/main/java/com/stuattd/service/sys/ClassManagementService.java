package com.stuattd.service.sys;

import java.util.List;

import jit.gwm.stuattd.model.Sbjjbxx;
import core.service.Service;

public interface ClassManagementService extends Service<Sbjjbxx> {
	List<Sbjjbxx> getClassType();

	boolean saveClass(Sbjjbxx entity);
}
