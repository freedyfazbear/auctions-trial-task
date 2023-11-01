package cc.freedyfazbear.auctions;

import cc.freedyfazbear.auctions.auction.AuctionManager;
import cc.freedyfazbear.auctions.commands.AuctionCommand;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class AuctionPlugin extends JavaPlugin
{
    private AuctionManager auctionManager;

    @Override
    public void onEnable() {
        auctionManager = new AuctionManager();

        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.registerBrigadier();

        handler.register(new AuctionCommand(this));

        if (handler.getBrigadier().isPresent()) {
            handler.getBrigadier().get().register();
        }
    }

    public AuctionManager getAuctionManager() {
        return auctionManager;
    }
}
