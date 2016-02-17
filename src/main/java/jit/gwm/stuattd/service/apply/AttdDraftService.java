package jit.gwm.stuattd.service.apply;

import java.util.List;

import com.stuattd.model.sys.Stuattd;

import jit.gwm.stuattd.model.Aqjxx;
import core.service.Service;
import core.support.QueryResult;

public interface AttdDraftService extends Service<Aqjxx> {

	List<Aqjxx> getTotalFromCgx(Long xsjbxxId);

	List<Aqjxx> getInfoByCondition(Long xsjbxxId, String kssj, String jssj);
}
