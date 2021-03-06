package code.methods.click;

import code.PluginService;
import code.data.UserData;
import code.methods.commands.StaffChatMethod;
import code.methods.player.PlayerMessage;
import code.utils.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;

public class ChatClickMethod implements Listener {

    private final PluginService pluginService;

    public ChatClickMethod(PluginService pluginService){
        this.pluginService = pluginService;

    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){

        UUID uuid = event.getPlayer().getUniqueId();
        
        UserData userData = pluginService.getCache().getPlayerUUID().get(uuid);
        PlayerMessage playersender = pluginService.getPlayerMethods().getSender();

        if (!(userData.isClickMode())){
            return;
        }

        List<String> clickchat = userData.getClickChat();
        ChatMethod chatMethod = pluginService.getPlayerMethods().getChatMethod();

        Configuration command = pluginService.getFiles().getCommand();

        if (event.getMessage().startsWith("-cancel")) {
            chatMethod.unset(uuid);
            return;
        }

        StaffChatMethod staffChatMethod = pluginService.getPlayerMethods().getStaffChatMethod();

        if (staffChatMethod.isUsingStaffSymbol(event)) {
            staffChatMethod.getStaffSymbol(event);
            return;
        }

        event.setCancelled(true);

        if (clickchat.size() < 1) {
            clickchat.add(event.getMessage());
            playersender.sendMessage(event.getPlayer(), command.getString("commands.broadcast.mode.selected.message")
                    .replace("%message%", clickchat.get(0)));
            chatMethod.setAgain(uuid);
            return;
        }

        if (clickchat.size() < 2){
            clickchat.add(event.getMessage());
            playersender.sendMessage(event.getPlayer(), command.getString("commands.broadcast.mode.selected.command")
                    .replace("%command%", clickchat.get(1)));
            chatMethod.setAgain(uuid);
            return;
        }

        if (clickchat.size() < 3){

            if (event.getMessage().startsWith("-now")) {
                clickchat.add("1");
            } else {
                clickchat.add(event.getMessage());
            }

            playersender.sendMessage(event.getPlayer(), command.getString("commands.broadcast.mode.selected.cooldown")
                    .replace("%cooldown%", clickchat.get(2)));


            userData.toggleClickMode(false);
            chatMethod.setAgain(uuid);

        }

    }
}
