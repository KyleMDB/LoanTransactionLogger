/*
        필드명     	설명
        loan_txn_id	여신 거래 고유 ID (UUID)
        customer_id	고객 ID (랜덤 8자리 숫자)
        loan_account_no	여신계좌번호 (랜덤 12자리)
        branch_code	지점 코드 (랜덤 선택: 강남, 종로 등)
        txn_type	거래 유형 (대출, 상환, 연체, 이자납입 등)
        amount	거래 금액
        currency	통화 (KRW)
        timestamp	거래 발생 시간 (UTC ISO 형식)
        status	성공/실패 상태
        remarks	기타 비고
*/
import com.mongodb.client.*;
import org.bson.Document;

import java.time.Instant;
import java.util.*;

public class LoanTransactionLogger {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("bankdb");
            MongoCollection<Document> collection = database.getCollection("loan_transactions");

            List<Document> transactions = new ArrayList<>();
            Random random = new Random();

            String[] txnTypes = {"LOAN_DISBURSE", "REPAYMENT", "DELINQUENCY", "INTEREST_PAYMENT"};
            String[] branchCodes = {"BR001_Gangnam", "BR002_Jongro", "BR003_Yeouido", "BR004_Songdo"};

            for (int i = 0; i < 10; i++) {
                String loanTxnId = UUID.randomUUID().toString();
                String customerId = String.format("%08d", random.nextInt(100_000_000));
                String loanAccountNo = String.format("%012d", random.nextLong(1_000_000_000_000L));
                String txnType = txnTypes[random.nextInt(txnTypes.length)];
                String branchCode = branchCodes[random.nextInt(branchCodes.length)];
                int amount = 500_000 + random.nextInt(10_000_000); // 50만 ~ 1천만
                String status = random.nextDouble() < 0.95 ? "SUCCESS" : "FAILURE";

                Document txn = new Document("loan_txn_id", loanTxnId)
                        .append("customer_id", customerId)
                        .append("loan_account_no", loanAccountNo)
                        .append("branch_code", branchCode)
                        .append("txn_type", txnType)
                        .append("amount", amount)
                        .append("currency", "KRW")
                        .append("timestamp", Instant.now().toString())
                        .append("status", status)
                        .append("remarks", "Auto-generated test transaction");

                transactions.add(txn);
            }

            collection.insertMany(transactions);
            System.out.println("✅ Inserted 10 loan transaction logs.");
        }
    }
}
