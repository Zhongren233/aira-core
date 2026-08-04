package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.core.dao.AiraTricolorLogMapper;
import moe.aira.core.service.IAiraTricolorLogService;
import moe.aira.entity.aira.AiraTricolorLog;
import org.springframework.stereotype.Service;

@Service
public class IAiraTricolorLogServiceImpl extends ServiceImpl<AiraTricolorLogMapper, AiraTricolorLog> implements IAiraTricolorLogService {
}
