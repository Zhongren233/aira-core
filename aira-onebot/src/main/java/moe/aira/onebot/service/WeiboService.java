package moe.aira.onebot.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface WeiboService {
    void sendWeibo(String status, File bufferedImage) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException;

    void updateToken(String code) throws IOException, InterruptedException;

    JsonNode getTokenInfo();
}
