package MagicGames.server;

import MagicGames.LobbyProxy;
import MagicGames.PlayerLocker;
import MagicGames.config.Configuration;
import MagicGames.config.GroupList;
import MagicGames.ReconnectHandler;
import MagicGames.priority.PriorityHandler;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.ServerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerStorage {

    private static final ServerStorage instance = new ServerStorage();

    private final List<ServerGroupStorage> groups = new ArrayList<>();

    public static ServerStorage getInstance() {
        return instance;
    }

    public void init() throws ServerException {
        Configuration configuration = LobbyProxy.getInstance().getConfiguration();

        if (configuration.getGroups().isEmpty()) {
            throw new ServerException("Groups section not found");
        }

        for (Map.Entry<String, GroupList> storageEntry : configuration.getGroups().entrySet()) {
            GroupList group = storageEntry.getValue();

            Map<String, BungeeServer> servers = new HashMap<>();

            for (String server : group.getServers()) {
                servers.put(server, new BungeeServer(storageEntry.getKey(), server));
            }

            this.groups.add(new ServerGroupStorage(storageEntry.getKey(), group.getPriority(), group.getParent(), servers));
        }

        ProxyServer.getInstance().setJoinHandler(proxiedPlayer -> {
            PlayerLocker.lock(proxiedPlayer);
            ProxyServer.getInstance().getScheduler().scheduleDelayed(() -> PlayerLocker.unlock(proxiedPlayer), 5 * 20);

            return PriorityHandler.getInstance().requestServer(ServerStorage.getInstance().getDefaultGroup());
        });

        ProxyServer.getInstance().setReconnectHandler(new ReconnectHandler());
    }

    public List<ServerGroupStorage> getGroups() {
        return this.groups;
    }

    public ServerGroupStorage getDefaultGroup() {
        return this.getGroup(LobbyProxy.getInstance().getConfiguration().getDefaultGroup());
    }

    public ServerGroupStorage getGroup(String groupName) {
        for (ServerGroupStorage group : this.groups) {
            if (!group.getName().equalsIgnoreCase(groupName)) continue;

            return group;
        }

        return null;
    }

    public ServerGroupStorage getGroupByServer(ServerInfo serverInfo) {
        for (ServerGroupStorage group : this.groups) {
            if (!group.containsServer(serverInfo.getServerName())) continue;

            return group;
        }

        return null;
    }
}