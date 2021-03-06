package code.methods.commands;

import code.PluginService;
import code.data.UserData;
import code.methods.MethodService;

import java.util.Map;
import java.util.UUID;

public class HelpOpMethod implements MethodService {

    private final PluginService pluginService;
    private final Map<UUID, UserData> cache;

    private String status;

    public HelpOpMethod(PluginService pluginService) {
        this.pluginService = pluginService;
        this.cache = pluginService.getCache().getPlayerUUID();
    }
    public String getStatus(){
        return status;
    }

    public void toggleOption(UUID uuid){
        UserData usercache = cache.get(uuid);

        if (usercache.isPlayerHelpOp()) {
            usercache.toggleHelpOp(false);
            status = pluginService.getFiles().getCommand().getString("commands.helpop.player.variable-off");
            return;
        }

        usercache.toggleHelpOp(true);
        status = pluginService.getFiles().getCommand().getString("commands.helpop.player.variable-on");
    }

    public void enableOption(UUID uuid){
        cache.get(uuid).toggleHelpOp(true);
    }

    public void disableOption(UUID uuid){
        cache.get(uuid).toggleHelpOp(false);
    }

}
