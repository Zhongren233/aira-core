package moe.aira.core.biz;

import moe.aira.entity.aira.AiraTriColorMatsuriInfo;
import moe.aira.entity.aira.AiraTricolorLog;

import java.util.List;

public interface IAiraTricolorBiz {

    AiraTriColorMatsuriInfo fetchInfo();

    boolean saveLog(List<AiraTricolorLog> logs);
}
