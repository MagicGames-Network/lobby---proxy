package MagicGames.priority;

import MagicGames.server.ServerGroupStorage;
import dev.waterdog.waterdogpe.network.ServerInfo;

public interface Priority {

    ServerInfo requestServer(ServerInfo targetServer, ServerGroupStorage group);
}