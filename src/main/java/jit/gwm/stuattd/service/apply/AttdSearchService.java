package jit.gwm.stuattd.service.apply;

import java.util.List;

import jit.gwm.stuattd.model.Aqjxx;

public interface AttdSearchService {

	List<Aqjxx> getTotalFromCgx(Long xsjbxxId);
	
	List<Aqjxx> getInfoByCondition(Long xsjbxxId, String kssj, String jssj);
}
