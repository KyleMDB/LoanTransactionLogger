import com.mongodb.client.*;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

public class MongoCrudTest {
    public static void main(String[] args) {
        // MongoDB 연결 (기본 포트 사용, 로컬 서버)
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://it_admin:manager@prod01.npgjj.mongodb.net")) {

 //           try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("testcollection");

            // 1. INSERT
            Document doc = new Document("name", "Bob").append("age", 25);
            collection.insertOne(doc);
            System.out.println("Inserted: " + doc.toJson());

            // 2. FIND
/*            Document found = collection.find(eq("name", "Bob")).first();
            System.out.println("Found: " + (found != null ? found.toJson() : "Not found"));

            // 3. UPDATE
            collection.updateOne(eq("name", "Bob"), new Document("$set", new Document("age", 26)));
            Document updated = collection.find(eq("name", "Bob")).first();
            System.out.println("Updated: " + updated.toJson());

            // 4. DELETE
            collection.deleteOne(eq("name", "Bob"));
            System.out.println("Deleted.");

 */
        }
    }
}
