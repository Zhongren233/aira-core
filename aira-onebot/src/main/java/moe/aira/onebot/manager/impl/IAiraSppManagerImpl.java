package moe.aira.onebot.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.entity.aira.SearchDict;
import moe.aira.onebot.entity.AiraCardSppDto;
import moe.aira.onebot.manager.IAiraSppManager;
import moe.aira.onebot.mapper.AiraCoreSearchDictMapper;
import moe.aira.onebot.mapper.CardMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@EnableCaching
public class IAiraSppManagerImpl implements IAiraSppManager {
    private final CardMapper cardMapper;
    private final AiraCoreSearchDictMapper airaCoreSearchDictMapper;

    public IAiraSppManagerImpl(CardMapper cardMapper, AiraCoreSearchDictMapper airaCoreSearchDictMapper) {
        this.cardMapper = cardMapper;
        this.airaCoreSearchDictMapper = airaCoreSearchDictMapper;
    }

    @Override
//    @Cacheable(value = "card", key = "#params.toString()", unless = "#params.size()!=0")
    public List<AiraCardSppDto> searchCardsSpp(List<String> params) {
        QueryWrapper<SearchDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("search_key", params);
        List<SearchDict> selectList = airaCoreSearchDictMapper.selectList(queryWrapper);
        QueryWrapper<AiraCardSppDto> cardQuery = new QueryWrapper<>();
        for (SearchDict searchDict : selectList) {
            params.removeIf(param -> param.toLowerCase(Locale.ROOT).equals(searchDict.getSearchKey().toLowerCase(Locale.ROOT)));
            // aira_core_search_dict.search_column 指向 aira_spp_view 的不同类型列(rarity/card_id 为数值列),
            // PostgreSQL 下拿字符串入参直接比较会报 operator does not exist, 统一把列转成 text 再比较.
            String column = "cast(" + searchDict.getSearchColumn() + " as text)";
            if (searchDict.getSearchValue().contains(",")) {
                String[] values = searchDict.getSearchValue().split(",");
                cardQuery.and(w -> {
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            w.or();
                        }
                        w.apply(column + " = {0}", values[i].trim());
                    }
                });
            } else {
                cardQuery.apply(column + " = {0}", searchDict.getSearchValue().trim());
            }
        }
        cardQuery.last("LIMIT 6 OFFSET 0");
        return cardMapper.selectList(cardQuery);
    }
}
