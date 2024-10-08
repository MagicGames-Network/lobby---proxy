package MagicGames.priority;

import MagicGames.priority.normal.FillerPriority;
import MagicGames.priority.normal.LowestPriority;
import MagicGames.priority.normal.NormalPriority;
import MagicGames.priority.random.RandomFillerPriority;
import MagicGames.priority.random.RandomLowestPriority;
import MagicGames.priority.random.RandomPriority;
import MagicGames.server.ServerGroupStorage;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.ServerInfo;

import java.security.SecureRandom;
import java.util.List;

public class PriorityHandler {

    private static final PriorityHandler instance = new PriorityHandler();
    private static final SecureRandom secureRandom = new SecureRandom();

    private static final String NORMAL = "NORMAL";
    private final static String LOWEST = "LOWEST";
    private final static String FILLER = "FILLER";
    private final static String RANDOM = "RANDOM";
    private static final String RANDOM_LOWEST = "RANDOM_LOWEST";
    private static final String RANDOM_FILLER = "RANDOM_FILLER";

    public static <T> T random(List<T> list) {
        return list.get(secureRandom.nextInt(list.size()));
    }

    public static PriorityHandler getInstance() {
        return instance;
    }

    public Priority getGroupPriority(ServerGroupStorage group) {
        for (PriorityEnum priority : PriorityEnum.values()) {
            if (group.getPriority().equals(priority.getValue())) {
                return priority.getPriority();
            }
        }

        ProxyServer.getInstance().getLogger().info("Priority not found... Using default priority");

        return PriorityEnum.NORMAL.getPriority();
    }

    public ServerInfo requestServer(ServerGroupStorage group) {
        return group != null ? requestServer(null, group) : null;
    }

    public ServerInfo requestServer(ServerInfo targetServer, ServerGroupStorage group) {
        return this.getGroupPriority(group).requestServer(targetServer, group);
    }

    public enum PriorityEnum {
        NORMAL(PriorityHandler.NORMAL, new NormalPriority()),
        LOWEST(PriorityHandler.LOWEST, new LowestPriority()),
        FILLER(PriorityHandler.FILLER, new FillerPriority()),
        RANDOM(PriorityHandler.RANDOM, new RandomPriority()),
        RANDOM_LOWEST(PriorityHandler.RANDOM_LOWEST, new RandomLowestPriority()),
        RANDOM_FILLER(PriorityHandler.RANDOM_FILLER, new RandomFillerPriority());

        private final String value;

        private final Priority priority;

        PriorityEnum(String value, Priority priority) {
            this.value = value;

            this.priority = priority;
        }

        public String getValue() {
            return this.value;
        }

        public Priority getPriority() {
            return this.priority;
        }
    }
}