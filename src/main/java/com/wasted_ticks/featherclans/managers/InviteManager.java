package com.wasted_ticks.featherclans.managers;

import com.wasted_ticks.featherclans.FeatherClans;
import com.wasted_ticks.featherclans.config.FeatherClansConfig;
import com.wasted_ticks.featherclans.data.Clan;
import com.wasted_ticks.featherclans.util.RequestUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class InviteManager {

    private FeatherClans plugin;
    private HashMap<String, RequestUtil> requests = new HashMap<>();
    private FeatherClansConfig config;

    public InviteManager(FeatherClans plugin) {
        this.plugin = plugin;
        this.config = plugin.getFeatherClansConfig();
    }

    public RequestUtil getRequest(Player player) {
        return this.requests.get(player.getName());
    }

    public void clearRequest(Player player) {
        this.requests.remove(player.getName());
    }

    public boolean invite(Player invitee, String tag, Player originator) {
        if(requests.containsKey(invitee.getName())) {
            return false;
        }

        requests.put(invitee.getName(), new RequestUtil(tag, originator));

        invitee.sendMessage(originator.getName() + " has requested you join their clan '" + tag + "'");
        invitee.sendMessage("Reply with '/accept' or '/decline'.");
        originator.sendMessage("Invite sent.");

        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
            @Override
            public void run() {
                RequestUtil request = requests.remove(invitee.getName());
                if(request != null) {
                    invitee.sendMessage("Clan invitation request from " + originator.getName() + " has expired.");
                }
            }
        }, config.getClanInviteTimeout() * 20);

        return true;
    }

}
