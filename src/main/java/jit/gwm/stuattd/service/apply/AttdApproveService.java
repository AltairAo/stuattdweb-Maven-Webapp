package jit.gwm.stuattd.service.apply;

import java.util.List;

import jit.gwm.stuattd.model.Aqjxx;
import jit.gwm.stuattd.model.Sxsjbxx;
import core.service.Service;

public interface AttdApproveService extends Service<Aqjxx> {
	List<Aqjxx> getTotalFromCgx();

	List<Aqjxx> getInfoByClass(int classId);

	List<Aqjxx> getInfoByCondition(int classId, String stuName);

	List<Sxsjbxx> getStuName(Long xsjbxxId);

	boolean Withdraw(int id);

	boolean Negative(int id, String bpyy);

	boolean Pass(int id);
}
