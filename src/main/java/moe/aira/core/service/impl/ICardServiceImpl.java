package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.core.entity.aira.Card;
import moe.aira.core.entity.aira.SearchDict;
import moe.aira.core.dao.AiraCoreSearchDictMapper;
import moe.aira.core.dao.CardMapper;
import moe.aira.core.service.ICardService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ICardServiceImpl implements ICardService {
    final
    CardMapper cardMapper;
    final
    AiraCoreSearchDictMapper searchDictMapper;

    public ICardServiceImpl(CardMapper cardMapper, AiraCoreSearchDictMapper searchDictMapper) {
        this.cardMapper = cardMapper;
        this.searchDictMapper = searchDictMapper;
    }

    @Override
    public List<Card> searchCard(List<String> searchKeys) {
        QueryWrapper<Card> cardQueryWrapper = new QueryWrapper<>();
        QueryWrapper<SearchDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("search_key", searchKeys);
        List<SearchDict> searchDicts = searchDictMapper.selectList(queryWrapper);
        if (searchDicts.isEmpty()) {
            throw new NoSuchElementException("Aira没有理解所有的搜索词呢...");
        }
        for (SearchDict searchDict : searchDicts) {
            String column = searchDict.getSearchColumn();
            String searchValue = searchDict.getSearchValue();
            String[] split = searchValue.split(",");
            if (split.length > 1) {
                cardQueryWrapper.in(column, Arrays.asList(split));
            } else {
                cardQueryWrapper.eq(column, searchValue);
            }
        }
        cardQueryWrapper.orderByDesc("rarity");
        List<String> collect = searchDicts.stream().map(SearchDict::getSearchKey).collect(Collectors.toList());
        searchKeys.removeAll(collect);
        return cardMapper.selectList(cardQueryWrapper);
    }
}
