package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.core.dao.StoryLineMapper;
import moe.aira.core.service.IStoryLineService;
import moe.aira.entity.aira.StoryLine;
import org.springframework.stereotype.Service;

@Service
public class IStoryLineServiceImpl extends ServiceImpl<StoryLineMapper, StoryLine> implements IStoryLineService {
}
