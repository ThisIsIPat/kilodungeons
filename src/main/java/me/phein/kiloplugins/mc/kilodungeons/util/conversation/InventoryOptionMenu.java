package me.phein.kiloplugins.mc.kilodungeons.util.conversation;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.phein.kiloplugins.mc.kilodungeons.KiloDungeonsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class InventoryOptionMenu {
    private final static int OPTION_ROW_LIMIT = 16;
    private final static int OPTION_COL_LIMIT = 9;

    private final List<Option> options = new LinkedList<>();
    private final String invId;
    private final String inventoryTitle;

    public InventoryOptionMenu(String invId, String inventoryTitle) {
        this.invId = invId;
        this.inventoryTitle = inventoryTitle;
    }

    private ItemStack createItemStackOption(Material material, String title, String[] description) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(title);
        if (description != null) {
            meta.setLore(Arrays.asList(description));
        }
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public InventoryOptionMenu with(String title, Material material, Consumer<InventoryClickEvent> observer) {
        return with(title, null, material, observer);
    }

    public InventoryOptionMenu with(String title, String[] description, Material material, Consumer<InventoryClickEvent> observer) {
        return with(createItemStackOption(material, title, description), observer);
    }

    public InventoryOptionMenu with(ItemStack item, Consumer<InventoryClickEvent> observer) {
        this.options.add(new Option(item, observer));

        return this;
    }

    public void open(Player player) {
        SmartInventory inventory = SmartInventory.builder()
                .manager(KiloDungeonsPlugin.getInstance().getInventoryManager())
                .id(invId)
                .provider(new Provider(options))
                .size((options.size() - 1) / 9 + 1, OPTION_COL_LIMIT)
                .title(inventoryTitle)
                .build();

        inventory.open(player);
    }

    public static class Provider implements InventoryProvider {
        private final List<Option> options;

        public Provider(List<Option> options) {
            this.options = options;
        }

        @Override
        public void init(Player player, InventoryContents contents) {
            Iterator<Option> optionIt = this.options.iterator();

            for (int row = 0; row < OPTION_ROW_LIMIT; row++) {
                for (int column = 0; column < OPTION_COL_LIMIT; column++) {
                    Option option = optionIt.next();

                    contents.set(row, column, ClickableItem.of(option.getItem(), option.getObserver()));

                    if (!optionIt.hasNext()) return;
                }
            }
        }

        @Override
        public void update(Player player, InventoryContents contents) {}
    }

    private static class Option {
        private final ItemStack item;
        private final Consumer<InventoryClickEvent> observer;

        public Option(ItemStack item, Consumer<InventoryClickEvent> observer) {
            this.item = item;
            this.observer = observer;
        }

        public ItemStack getItem() {
            return item;
        }

        public Consumer<InventoryClickEvent> getObserver() {
            return observer;
        }
    }
}
