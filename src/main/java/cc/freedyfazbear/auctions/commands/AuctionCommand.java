package cc.freedyfazbear.auctions.commands;

import cc.freedyfazbear.auctions.AuctionPlugin;
import cc.freedyfazbear.auctions.auction.Auction;
import cc.freedyfazbear.auctions.helper.ChatHelper;
import cc.freedyfazbear.auctions.helper.ItemHelper;
import cc.freedyfazbear.auctions.inventory.AuctionInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;

@Command({"auction", "auctions", "bazar", "bazaar", "ah"})
public class AuctionCommand
{
    private final AuctionPlugin auctionPlugin;

    public AuctionCommand(AuctionPlugin auctionPlugin) {
        this.auctionPlugin = auctionPlugin;
    }

    @Default
    public void auctionCommand(Player sender) {
        new AuctionInventory(auctionPlugin).openGui(sender);
    }

    @Subcommand("sell")
    public void auctionSellCommand(Player sender, @Named("price") @Range(min = 1.0) double price) {
        ItemStack itemStack = sender.getInventory().getItem(EquipmentSlot.HAND);

        if (itemStack.getType() == Material.AIR) {
            ChatHelper.sendMessage(sender, "&cYou cant sell the air");
            return;
        }


        auctionPlugin.getAuctionManager().addAuction(new Auction(sender.getUniqueId(), itemStack, price));
        ChatHelper.sendMessage(sender, "&aSuccessfully listed item");
        ItemHelper.reduceItemAmount(sender, itemStack, 1);
    }
}
