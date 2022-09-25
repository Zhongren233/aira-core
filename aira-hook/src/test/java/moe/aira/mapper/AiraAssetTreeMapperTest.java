package moe.aira.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import moe.aira.entity.AiraAssetTree;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

//@SpringBootTest
class AiraAssetTreeMapperTest {


    static final HashMap<String, Integer> STRING_INTEGER_HASH_MAP = new HashMap<>();
    static final HashMap<Integer, AtomicInteger> ID_MAP = new HashMap<>();
    static ArrayList<AiraAssetTree> airaAssetTrees = new ArrayList<>();

    @Test
    void test() throws IOException {
        HashMap<String, Integer> pathMap = new HashMap<>();
        pathMap.put("", 0);
        File file = new File("D:/rider/ConsoleApp1/ConsoleApp1/bin/Debug/net6.0/out/resources_production.iOS.catalog.c08575b5acafc6378e49889b853120dc.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(file);
        ArrayNode jsonNodes = (ArrayNode) jsonNode.get(6);
        int size = jsonNodes.size();
        for (JsonNode node : jsonNodes) {
//            System.out.println("剩余" + size-- + "条数据");
            for (JsonNode jsonNode1 : node) {
                if (jsonNode1.isArray()) {
                    for (JsonNode jsonNode2 : jsonNode1) {
                        if (jsonNode2.isTextual()) {
                            String base = jsonNode2.textValue();
                            byte[] decode = Base64.getDecoder().decode(base);
                            byte length = decode[3];
                            byte[] nameBytes = new byte[length];
                            byte[] md5Bytes = new byte[16];
                            System.arraycopy(decode, 4, nameBytes, 0, length);
                            System.arraycopy(decode, 4 + length, md5Bytes, 0, 16);
                            String fullPath = new String(nameBytes, StandardCharsets.UTF_8);
                            create(fullPath, md5Bytes, true);
                            System.out.println("https://assets.boysm.hekk.org/asset_bundles/iOS/" + fullPath + ".bundle." + DatatypeConverter.printHexBinary(md5Bytes).toLowerCase());

                        }
                    }
                }


            }
        }

        try (FileWriter fileWriter = new FileWriter("./out.csv")) {
            fileWriter.write("id,parentId,fullPath,name,md5,dictionary\n");
            for (AiraAssetTree airaAssetTree : airaAssetTrees) {
                fileWriter.write(airaAssetTree.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Integer create(String fullPath, byte[] md5Bytes, boolean file) {
        Integer parentId;
        String md5 = null;
        if (md5Bytes != null) {
            md5 = DatatypeConverter.printHexBinary(md5Bytes).toLowerCase();

        }


        int endIndex = fullPath.lastIndexOf("/");
        if (endIndex != -1) {
            String substring = fullPath.substring(0, endIndex);
            parentId = STRING_INTEGER_HASH_MAP.get(substring);
            if (parentId == null) {
                parentId = create(substring, null, false);
            }
        } else {
            parentId = 0;
        }

        AiraAssetTree airaAssetTree = new AiraAssetTree();
        airaAssetTree.setMd5(md5);
        airaAssetTree.setDictionary(md5 == null);
        airaAssetTree.setFullPath(fullPath);
        int dept = (int) fullPath.chars().filter(ch -> ch == '/').count();
        AtomicInteger atomicInteger = ID_MAP.get(dept);
        if (atomicInteger == null) {
            AtomicInteger value = new AtomicInteger(dept * 10000000);
            ID_MAP.put(dept, value);
            atomicInteger = value;
        }
        int id = atomicInteger.addAndGet(1);
        airaAssetTree.setId(id);
        String substring;
        if (endIndex == -1) {
            substring = fullPath;
        } else {
            substring = fullPath.substring(endIndex + 1);
        }
        airaAssetTree.setName(substring);
        airaAssetTree.setFullPath(fullPath);
        airaAssetTree.setParentId(parentId);

        airaAssetTrees.add(airaAssetTree);
        STRING_INTEGER_HASH_MAP.put(fullPath, id);
        return id;
    }
}