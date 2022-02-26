package moe.aira.core.service;

import moe.aira.core.entity.aira.Card;

import java.util.List;

public interface ICardService {
    List<Card> searchCard(List<String> searchKeys);
}
