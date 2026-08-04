package moe.aira.core.config;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import moe.aira.core.entity.dto.QuizResponse;

import java.io.IOException;

public class QuestionEntryDeserializer extends JsonDeserializer<QuizResponse.QuestionEntry> {

    @Override
    public QuizResponse.QuestionEntry deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        if (!node.isArray() || node.size() != 2) {
            throw new JsonMappingException(p, "QuestionEntry must be a 2-element array: [kind, payload]");
        }

        ArrayNode arr = (ArrayNode) node;
        int kind = arr.get(0).asInt();
        JsonNode payloadNode = arr.get(1);

        QuizResponse.QuestionEntry entry = new QuizResponse.QuestionEntry();
        entry.kind = kind;

        // 根据 payload 的字段判断是哪种题
        ObjectMapper mapper = (ObjectMapper) codec;

        if (payloadNode.isObject()) {
            ObjectNode obj = (ObjectNode) payloadNode;

            if (obj.has("choices_list")) {
                entry.payload = mapper.treeToValue(obj, QuizResponse.TextInputQuestion.class);
            } else if (obj.has("choices")) {
                entry.payload = mapper.treeToValue(obj, QuizResponse.ChoiceQuestion.class);
            } else {
                // 兜底：你也可以定义 UnknownQuestionPayload
                entry.payload = mapper.treeToValue(obj, UnknownQuestionPayload.class);
            }
        } else {
            throw new JsonMappingException(p, "payload must be an object");
        }

        return entry;
    }

    // 可选：兜底类型
    public static class UnknownQuestionPayload implements QuizResponse.QuestionPayload {
        public JsonNode raw;

        public UnknownQuestionPayload() {
        }

        @Override
        public String getDescription() {
            return "";
        }
    }
}