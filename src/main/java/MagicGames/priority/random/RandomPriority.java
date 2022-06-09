package MagicGames.priority.random;

import MagicGames.priority.Priority;
import MagicGames.priority.PriorityHandler;
import MagicGames.server.ServerGroupStorage;
import dev.waterdog.waterdogpe.network.ServerInfo;

import static MagicGames.priority.PriorityHandler.random;

public class RandomPriority implements Priority {

    @Override
    public ServerInfo requestServer(ServerInfo targetServer, ServerGroupStorage group) {
        if (group == null) {
            return null;
        }

        if (group.getServersInfo().isEmpty()) {
            return PriorityHandler.getInstance().requestServer(group.getParent());
        }

        return random(group.getServersInfo());
    }
}