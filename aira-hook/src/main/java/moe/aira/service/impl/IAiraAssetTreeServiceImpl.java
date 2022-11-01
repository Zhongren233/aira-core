package moe.aira.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.aira.entity.AiraAssetTree;
import moe.aira.mapper.AiraAssetTreeMapper;
import moe.aira.service.IAiraAssetTreeService;
import org.springframework.stereotype.Service;

@Service
public class IAiraAssetTreeServiceImpl extends ServiceImpl<AiraAssetTreeMapper, AiraAssetTree> implements IAiraAssetTreeService {
}
