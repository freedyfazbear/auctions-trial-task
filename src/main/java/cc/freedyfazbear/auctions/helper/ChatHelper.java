package cc.freedyfazbear.auctions.helper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatHelper {
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand()
            .toBuilder()
            .hexColors()
            .character('&')
            .hexCharacter('#')
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
    private static final List<String> colorList = new ArrayList<>(Arrays.asList("&b", "&6", "&a", "&b", "&d"));

    public static String parse(String message) {
        return ChatColor.translateAlternateColorCodes('&',legacyComponentSerializer.serialize(legacyComponentSerializer.deserialize(message).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)).replace(">>", "»"));
    }

    public static Component parseComponent(String message) {
        return legacyComponentSerializer.deserialize(message);
    }

    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public static List<String> fixColor(final List<String> message) {
        return message.stream().map(ChatHelper::parse).collect(Collectors.toList());
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(parse(message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(parse(message));
    }

    public static List<String> parse(List<String> message) {
        return message.stream()
                .map(ChatHelper::parse)
                .toList();
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatHelper.parse(message)));
    }

    public static Material getMaterial(final String materialName) {
        Material returnMaterial = null;
        if (isInteger(materialName)) {
            final int id = Integer.parseInt(materialName);
            returnMaterial = Material.getMaterial(String.valueOf(id));
        } else {
            returnMaterial = Material.matchMaterial(materialName);
        }
        return returnMaterial;
    }

    public static void giveItems(final Player p, final ItemStack... items) {
        final Inventory i = p.getInventory();
        final HashMap<Integer, ItemStack> notStored = i.addItem(items);
        for (final Map.Entry<Integer, ItemStack> e : notStored.entrySet()) {
            p.getWorld().dropItemNaturally(p.getLocation(), e.getValue());
        }
    }

    public static String getDurationBreakdownMinutes(long second) {
        long millis = second * 1000;
        if (millis == 0) {
            return "0";
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days > 0) {
            millis -= TimeUnit.DAYS.toMillis(days);
        }

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0) {
            millis -= TimeUnit.HOURS.toMillis(hours);
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0) {
            millis -= TimeUnit.MINUTES.toMillis(minutes);
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds > 0) {
            millis -= TimeUnit.SECONDS.toMillis(seconds);
        }
        long miliseconds = TimeUnit.MILLISECONDS.toMillis(millis);
        if (miliseconds > 0) {
            millis -= TimeUnit.MILLISECONDS.toMillis(millis);
        }

        StringBuilder sb = new StringBuilder();


        if (hours > 0) {
            sb.append(hours).append("h ");
        } else {
            sb.append(0).append("h ");
        }

        if (minutes > 0) {
            sb.append(minutes).append("min");
        } else {
            sb.append(0).append("min ");
        }
        return sb.toString();
    }

    public static long parseTime(String string) {

        Stack<Character> type = new Stack<>();
        StringBuilder value = new StringBuilder();

        boolean calc = false;
        long time = 0;

        for (char c : string.toCharArray()) {
            switch (c) {
                case 'd':
                case 'h':
                case 'm':
                case 's':
                    if (!calc) {
                        type.push(c);
                    }

                    try {
                        long i = Integer.parseInt(value.toString());
                        switch (type.pop()) {
                            case 'd':
                                time += i * 86400000L;
                                break;
                            case 'h':
                                time += i * 3600000L;
                                break;
                            case 'm':
                                time += i * 60000L;
                                break;
                            case 's':
                                time += i * 1000L;
                                break;
                        }
                    } catch (NumberFormatException e) {
                        return time;
                    }

                    type.push(c);
                    calc = true;

                    break;
                default:
                    value.append(c);
                    break;
            }
        }

        return time;
    }

    public static void sendTitle(final Player p, final String title, final String subtitle) {
        p.sendTitle(ChatHelper.parse(title), ChatHelper.parse(subtitle));
    }

    public static boolean isInteger(String string) {
        return Pattern.matches("-?[0-9]+", string.subSequence(0, string.length()));
    }

    public static int intValue(final long time) {
        return (int)(time - System.currentTimeMillis()) / 1000;
    }

    public static int getIntegerFromString(String text) {
        return Integer.parseInt(text.replaceAll("\\D", ""));
    }

    public static String rainbowColor(final String string) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); ++i) {
            stringBuilder.append(colorList.get(new Random().nextInt(colorList.size()))).append(string.charAt(i));
        }
        return ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());
    }
}
