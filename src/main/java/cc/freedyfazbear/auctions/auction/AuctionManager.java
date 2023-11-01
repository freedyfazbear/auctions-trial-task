package cc.freedyfazbear.auctions.auction;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionManager
{
    private final Map<UUID, Auction> auctionMap = new ConcurrentHashMap<>();

    public void addAuction(Auction auction) {
        auctionMap.putIfAbsent(auction.getOwner(), auction);
    }

    public void removeAuction(Auction auction) {
        auctionMap.remove(auction.getOwner());
    }

    public Auction getAuction(Auction auction) {
        return auctionMap.get(auction.getOwner());
    }

    public Map<UUID, Auction> getAuctionMap() {
        return auctionMap;
    }
}
