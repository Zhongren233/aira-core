package moe.aira.onebot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.entity.aira.StoryLine;
import moe.aira.onebot.mapper.StoryLineMapper;
import moe.aira.onebot.service.IStoryLineService;
import org.springframework.stereotype.Service;

@Service
public class IStoryLineServiceImpl extends ServiceImpl<StoryLineMapper, StoryLine> implements IStoryLineService {
}
