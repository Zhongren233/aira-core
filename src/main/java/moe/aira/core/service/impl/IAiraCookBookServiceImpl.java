package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.core.entity.aira.AiraCookBook;
import moe.aira.core.dao.AiraCookBookMapper;
import moe.aira.core.service.IAiraCookBookService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IAiraCookBookServiceImpl implements IAiraCookBookService {
    final
    AiraCookBookMapper airaCookBookMapper;

    public IAiraCookBookServiceImpl(AiraCookBookMapper airaCookBookMapper) {
        this.airaCookBookMapper = airaCookBookMapper;
    }

    @Override
    public int insertCookBook(AiraCookBook airaCookBook) {
        airaCookBook.setStatus(Boolean.TRUE);
        return airaCookBookMapper.insert(airaCookBook);
    }

    @Override
    public AiraCookBook fetchCookBook() {
        QueryWrapper<AiraCookBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<AiraCookBook> airaCookBooks = airaCookBookMapper.selectList(queryWrapper);
        Collections.shuffle(airaCookBooks);
        return airaCookBooks.get(0);
    }
}
