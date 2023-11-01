package cc.freedyfazbear.auctions.auction;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Auction
{
    private final UUID owner;
    private final ItemStack itemStack;
    private final double price;

    public Auction(UUID owner, ItemStack itemStack, double price) {
        this.owner = owner;
        this.itemStack = itemStack;
        this.price = price;
    }

    public UUID getOwner() {
        return owner;
    }

    public double getPrice() {
        return price;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
