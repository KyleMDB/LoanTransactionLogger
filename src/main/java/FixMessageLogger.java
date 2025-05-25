import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;

public class FixMessageLogger {

    public static void main(String[] args) {
        String fixMessage = "8=FIX.4.2|9=112|35=D|49=CLIENT12|56=BROKER12|34=178|52=20250525-14:32:10|" +
                "11=13346|21=1|55=AAPL|54=1|60=20250525-14:32:00|38=100|40=2|44=174.50|";

        Map<String, String> parsed = parseFixMessage(fixMessage);

        // MongoDB 저장
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("trading");
            MongoCollection<Document> col = db.getCollection("fix_logs");

            Document doc = new Document(parsed)
                    .append("rawMessage", fixMessage)
                    .append("parsedTime", new Date());

            col.insertOne(doc);
            System.out.println("FIX message logged:\n" + doc.toJson());
        }
    }

    public static Map<String, String> parseFixMessage(String msg) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = msg.split("\\|");

        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                result.put("tag_" + kv[0], kv[1]);
            }
        }
        return result;
    }
}
