package MagicGames.priority.normal;

import MagicGames.priority.Priority;
import MagicGames.priority.PriorityHandler;
import MagicGames.server.ServerGroupStorage;
import dev.waterdog.waterdogpe.network.ServerInfo;

public class LowestPriority implements Priority {

    @Override
    public ServerInfo requestServer(ServerInfo targetServer, ServerGroupStorage group) {
        ServerInfo betterServer = null;

        if (group == null) {
            return null;
        }

        for (ServerInfo serverInfo : group.getServersInfo()) {
            if (betterServer == null) {
                betterServer = serverInfo;

                continue;
            }

            if (serverInfo.getPlayers().size() > betterServer.getPlayers().size()) continue;

            betterServer = serverInfo;
        }

        if (betterServer == null) {
            betterServer = PriorityHandler.getInstance().requestServer(group.getParent());
        }

        return betterServer;
    }
}