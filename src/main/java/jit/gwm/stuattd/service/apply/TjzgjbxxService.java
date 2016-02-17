package jit.gwm.stuattd.service.apply;

import java.util.List;

import jit.gwm.stuattd.model.Tjzgjbxx;
import core.service.Service;

/**
 * @开发商:jit.gwm
 */
public interface TjzgjbxxService extends Service<Tjzgjbxx> {

	List<Tjzgjbxx> getApproveTeachers(Long xsjbxxId);

}
