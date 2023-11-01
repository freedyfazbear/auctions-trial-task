package cc.freedyfazbear.auctions.inventory;

import cc.freedyfazbear.auctions.AuctionPlugin;
import cc.freedyfazbear.auctions.auction.Auction;
import cc.freedyfazbear.auctions.helper.ChatHelper;
import cc.freedyfazbear.auctions.helper.ItemHelper;
import cc.freedyfazbear.auctions.helper.VaultHelper;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class AuctionInventory
{
    private final AuctionPlugin auctionPlugin;

    public AuctionInventory(AuctionPlugin auctionPlugin) {
        this.auctionPlugin = auctionPlugin;
    }

    public void openGui(Player player) {
        Gui gui = Gui.gui() //could be PaginatedGui if auctions contains more items than the inventory size is
                .title(ChatHelper.parseComponent("&bAuctions"))
                .disableAllInteractions()
                .rows(6)
                .create();
        for (Auction auction : auctionPlugin.getAuctionManager().getAuctionMap().values()) {
            gui.addItem(ItemBuilder.from(auction.getItemStack().getType())
                            .setName("&d" + auction.getItemStack().getType().name())
                            .setLore(ChatHelper.parse(List.of("&8>> &7Price: &d" + auction.getPrice(), "&8>> &7Listed by: &d" + Bukkit.getOfflinePlayer(auction.getOwner()).getName())))
                    .asGuiItem(event -> {
                        if (VaultHelper.purchase(player, auction.getPrice())) {
                            if (!auctionPlugin.getAuctionManager().getAuctionMap().containsValue(auction)) {
                                ChatHelper.sendMessage(player, "&cSomeone bought this item earlier than you!");
                                player.closeInventory();
                                return;
                            }

                            VaultHelper.sell(Bukkit.getOfflinePlayer(auction.getOwner()), auction.getPrice());

                            ChatHelper.sendMessage(player, "&aYou bought this item for " + auction.getPrice() + "$");

                            ItemHelper.addItemToPlayer(player, auction.getItemStack());

                            auctionPlugin.getAuctionManager().removeAuction(auction);
                            player.closeInventory();
                        } else {
                            ChatHelper.sendMessage(player, "&cYou cant afford for this!");
                        }
                    }));
        }

        gui.open(player);
    }
}
