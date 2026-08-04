package moe.aira.core.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import moe.aira.core.config.QuestionEntryDeserializer;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizResponse {


    // 关键：questions 是形如 [ [3, {...}], [2, {...}] ... ]
    @JsonProperty("questions")
    public List<QuestionEntry> questions;

    @Data
    @JsonDeserialize(using = QuestionEntryDeserializer.class)
    public static class QuestionEntry {
        // 数组第一个元素：2 / 3 ...
        public int kind;

        // 数组第二个元素：题目对象（不同结构）
        public QuestionPayload payload;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION) // Jackson 2.12+：按字段推断子类
    @JsonSubTypes({
            @JsonSubTypes.Type(TextInputQuestion.class),
            @JsonSubTypes.Type(ChoiceQuestion.class)
    })
    public interface QuestionPayload {
        String getDescription();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextInputQuestion implements QuestionPayload {

        @JsonProperty("choices_list")
        public List<List<String>> choicesList;

        public String description;

        @JsonProperty("answer_indexes")
        public List<Integer> answerIndexes;

        @JsonProperty("answer_text")
        public String answerText;

        @JsonProperty("judge_text")
        public String judgeText;

        @JsonProperty("answer_index")
        public Integer answerIndex;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChoiceQuestion implements QuestionPayload {
        @Data
        public static class Choice {
            @JsonProperty("icon_asset_path")
            public String iconAssetPath; // 可能为 null

            @JsonProperty("choice_text")
            public String choiceText;
        }

        public List<Choice> choices;

        public String description;

        @JsonProperty("answer_index")
        public Integer answerIndex;
    }

}
