package jit.gwm.stuattd.service.apply;

import jit.gwm.stuattd.model.Aqjxx;
import core.service.Service;

public interface AqjxxService extends Service<Aqjxx> {
	boolean doSave(Aqjxx aqjxx);
	boolean doUpdate(Aqjxx aqjxx);
}
