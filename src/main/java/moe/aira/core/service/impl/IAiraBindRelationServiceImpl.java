package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.core.dao.AiraBindRelationMapper;
import moe.aira.core.entity.aira.AiraBindRelation;
import moe.aira.core.service.IAiraBindRelationService;
import org.springframework.stereotype.Service;

@Service
public class IAiraBindRelationServiceImpl extends ServiceImpl<AiraBindRelationMapper, AiraBindRelation> implements IAiraBindRelationService {
}
