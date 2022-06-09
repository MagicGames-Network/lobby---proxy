package MagicGames.priority.normal;

import MagicGames.priority.Priority;
import MagicGames.priority.PriorityHandler;
import MagicGames.server.BungeeServer;
import MagicGames.server.ServerGroupStorage;
import dev.waterdog.waterdogpe.network.ServerInfo;

public class NormalPriority implements Priority {

    @Override
    public ServerInfo requestServer(ServerInfo targetServer, ServerGroupStorage group) {
        if (group == null) {
            return null;
        }

        for (BungeeServer server : group.getServers().values()) {
            if (targetServer != null && server.getName().equals(targetServer.getServerName())) continue;

            if (server.getServerInfo().getPlayers().size() < server.getStatus().getMaxPlayers()) {
                return server.getServerInfo();
            }
        }

        return PriorityHandler.getInstance().requestServer(group.getParent());
    }
}