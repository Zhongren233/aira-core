package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.core.dao.AiraLogPointMapper;
import moe.aira.core.service.IAiraLogPointService;
import moe.aira.entity.aira.AiraLogPoint;
import org.springframework.stereotype.Service;

@Service
public class IAiraLogPointServiceImpl extends ServiceImpl<AiraLogPointMapper, AiraLogPoint> implements IAiraLogPointService {
}
