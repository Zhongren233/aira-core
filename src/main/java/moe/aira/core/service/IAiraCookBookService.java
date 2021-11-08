package moe.aira.core.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.AiraCookBook;

public interface IAiraCookBookService {
    int insertCookBook(AiraCookBook airaCookBook);

    AiraCookBook fetchCookBook();
}
