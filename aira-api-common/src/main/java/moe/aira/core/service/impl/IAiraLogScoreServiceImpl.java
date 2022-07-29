package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.core.dao.AiraLogScoreMapper;
import moe.aira.core.service.IAiraLogScoreService;
import moe.aira.entity.aira.AiraLogScore;
import org.springframework.stereotype.Service;

@Service
public class IAiraLogScoreServiceImpl extends ServiceImpl<AiraLogScoreMapper, AiraLogScore> implements IAiraLogScoreService {
}
