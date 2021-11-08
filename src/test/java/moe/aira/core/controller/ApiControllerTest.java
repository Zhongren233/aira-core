package moe.aira.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.entity.api.ApiResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Test
    void myPage() {

    }

    @Test
    void present() {
    }

    @Test
    void revivePresent() {
    }
}