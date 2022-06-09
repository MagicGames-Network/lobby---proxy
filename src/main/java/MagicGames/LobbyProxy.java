package MagicGames;

import MagicGames.config.Configuration;
import MagicGames.ping.StatusStorage;
import MagicGames.server.ServerException;
import MagicGames.server.ServerStorage;
import dev.waterdog.waterdogpe.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.*;

public class LobbyProxy extends Plugin {

    private static LobbyProxy instance;

    private Configuration configuration;

    public static LobbyProxy getInstance() {
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveResource("config.yml");

        try (InputStream in = new FileInputStream(this.getDataFolder().toString() + "/config.yml")) {
            this.configuration = (new Yaml(new CustomClassLoaderConstructor(Configuration.class.getClassLoader()))).loadAs(in, Configuration.class);

            ServerStorage.getInstance().init();

            StatusStorage.getInstance().init();

            getLogger().info("Lobby - proxy activated.");

            //new PreTransferListener();
        } catch (IOException | ServerException e) {
            e.printStackTrace();
        }
    }
}
