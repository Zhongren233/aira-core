package moe.aira.onebot.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.entity.aira.SearchDict;
import moe.aira.onebot.entity.AiraCardSppDto;
import moe.aira.onebot.manager.IAiraSppManager;
import moe.aira.onebot.mapper.AiraCoreSearchDictMapper;
import moe.aira.onebot.mapper.CardMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class IAiraSppManagerImpl implements IAiraSppManager {
    private final CardMapper cardMapper;
    private final AiraCoreSearchDictMapper airaCoreSearchDictMapper;

    public IAiraSppManagerImpl(CardMapper cardMapper, AiraCoreSearchDictMapper airaCoreSearchDictMapper) {
        this.cardMapper = cardMapper;
        this.airaCoreSearchDictMapper = airaCoreSearchDictMapper;
    }

    @Override
    @Cacheable(value = "card")
    public List<AiraCardSppDto> searchCardsSpp(List<String> params) {
        QueryWrapper<SearchDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("search_key", params);
        List<SearchDict> selectList = airaCoreSearchDictMapper.selectList(queryWrapper);
        QueryWrapper<AiraCardSppDto> cardQuery = new QueryWrapper<>();
        for (SearchDict searchDict : selectList) {
            params.removeIf(param -> param.toLowerCase(Locale.ROOT).equals(searchDict.getSearchKey().toLowerCase(Locale.ROOT)));
            if (searchDict.getSearchValue().contains(",")) {
                cardQuery.in(searchDict.getSearchKey(), Arrays.asList(searchDict.getSearchValue().split(",")));
            } else {
                cardQuery.eq(searchDict.getSearchKey(), searchDict.getSearchValue());
            }
        }
        return cardMapper.selectList(cardQuery);
    }
}
