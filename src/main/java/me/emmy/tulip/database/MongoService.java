package me.emmy.tulip.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Remi
 * @project Tulip
 * @date 27/07/2024 - 15:27
 */
@Getter
public class MongoService {
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;

    public void startMongo() {
        FileConfiguration config = ConfigHandler.getInstance().getSettingsConfig();

        String databaseName = config.getString("mongo.databaseName");
        Bukkit.getConsoleSender().sendMessage(CC.translate("&bConnecting to the MongoDB database..."));

        ConnectionString connectionString = new ConnectionString(Objects.requireNonNull(config.getString("mongo.connectionString")));
        MongoClientSettings.Builder settings = MongoClientSettings.builder();
        settings.applyConnectionString(connectionString);
        settings.applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS));
        settings.retryWrites(true);

        this.mongoClient = MongoClients.create(settings.build());
        this.mongoDatabase = mongoClient.getDatabase(databaseName);

        Bukkit.getConsoleSender().sendMessage(CC.translate("&eSuccessfully connected to the MongoDB database."));
    }
}