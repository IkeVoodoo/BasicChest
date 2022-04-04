package me.ikevoodoo.basicchest.database;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;

public class MongoHandler {

    private static final String URL_WITH_CREDENTIALS = "mongodb://%s:%s@%s:%d";
    private static final String URL_WITHOUT_CREDENTIALS = "mongodb://%s:%d";

    private MongoDatabase database;

    /**
     * Create a new MongoHandler, this will connect to the database automatically
     * The connection will be closed when, and only when the program ends
     *
     * @param host The hostname of the database
     * @param port The port of the database
     * @param password The password of the database
     *                 If the database does not require a password, pass null or an empty string
     * */
    public MongoHandler(String host, int port, String db, String username, String password) {
        ConnectionString connectionString = new ConnectionString(getUrl(host, port, username, password));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        // Not wrapping in a try-with-resources because the client will be closed automatically at the end of the program
        MongoClient client = MongoClients.create(settings);

        database = client.getDatabase(db);
    }

    /**
     * Set a document in the database
     * Delete the document if it already exists (based on uuid key)
     *
     * @param value The value to set
     * */
    public void set(Document value) {
        // remove document if it already exists (only if value has uuid)
        if(!value.containsKey("_id")) {
            throw new IllegalStateException("Document must have an id");
        }

        Document filter = new Document("_id", value.get("_id"));
        MongoCollection<Document> collection = database.getCollection("basicchest");
        collection.countDocuments(filter).subscribe(new SubscriberImplemenation<>(amount -> {
            if(amount > 0) {
                collection.deleteOne(filter).subscribe(new SubscriberImplemenation<>(t -> add(collection, value)));
            } else {
                add(collection, value);
            }
        }));
    }
    
    /**
     * Get a value from the database
     * @param query The query to use
     * */
    public void get(Document query, OperationCallback<Document> callback) {
        database.getCollection("basicchest")
                .find(query)
                .first()
                .subscribe(new SubscriberImplemenation<>(callback::accept));
    }

    private String getUrl(String host, int port, String username, String password) {
        if((password == null || password.isBlank()) || (username == null || username.isBlank())) {
            return String.format(URL_WITHOUT_CREDENTIALS, host, port);
        }

        return String.format(URL_WITH_CREDENTIALS, username, password, host, port);
    }

    private void add(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document).subscribe(new SubscriberImplemenation<>());
    }
}
