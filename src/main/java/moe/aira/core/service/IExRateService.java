package moe.aira.core.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface IExRateService {
    JsonNode fetchCNYRate() throws IOException;
}
