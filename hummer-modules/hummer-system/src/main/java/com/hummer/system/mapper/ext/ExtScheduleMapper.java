package com.hummer.system.mapper.ext;

import com.hummer.common.core.dto.ScheduleDao;
import com.hummer.common.core.domain.request.QueryScheduleRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtScheduleMapper {
    List<ScheduleDao> list(@Param("request") QueryScheduleRequest request);
}