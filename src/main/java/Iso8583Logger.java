import com.mongodb.client.*;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Iso8583Logger {

    public static void main(String[] args) {
        // ISO 8583 전문 예시 (간단한 문자열 버전, 실제로는 binary 혹은 ASCII)
        String rawMessage = "02001234567890123456000000001000000525143210";

        // 파싱 로직 (샘플용, 실무에선 라이브러리 사용 권장)
        Map<String, String> parsedFields = parseIso8583(rawMessage);

        // MongoDB 저장
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("banking");
            MongoCollection<Document> col = db.getCollection("iso8583_logs");

            Document doc = new Document(parsedFields).append("rawMessage", rawMessage);
            col.insertOne(doc);
            System.out.println("Inserted: " + doc.toJson());
        }
    }

    // ISO 8583 전문 파싱 예제 (필드 위치 기반 샘플)
    public static Map<String, String> parseIso8583(String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("mti", msg.substring(0, 4));
        map.put("pan", msg.substring(4, 20));
        map.put("processingCode", msg.substring(20, 26));
        map.put("amount", msg.substring(26, 32));
        map.put("transmissionDateTime", msg.substring(32, 42));
        return map;
    }
}
