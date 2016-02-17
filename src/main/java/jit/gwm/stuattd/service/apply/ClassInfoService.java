package jit.gwm.stuattd.service.apply;

import java.util.List;

import jit.gwm.stuattd.model.Sbjjbxx;
import core.service.Service;

public interface ClassInfoService extends Service<Sbjjbxx> {

	List<Sbjjbxx> getClassInfo();
}
