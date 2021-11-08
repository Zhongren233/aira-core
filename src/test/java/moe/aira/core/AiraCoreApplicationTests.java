package moe.aira.core;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AiraCoreApplicationTests {
    private static final Pattern PATTERN = Pattern.compile("'([^']*?)'|(\\S+)");


    @Test
    void contextLoads() {
        String str = "/bind '你好 测试' -name";
        Matcher matcher = PATTERN.matcher(str);
        MatchResult matchResult = matcher.toMatchResult();
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group);
        }
    }

    @Test
    void testHash() {

    }

}
