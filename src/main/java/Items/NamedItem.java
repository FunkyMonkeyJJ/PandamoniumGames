package Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class NamedItem {
    private final ItemStack item;

    public NamedItem(Material material, String name) {
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public NamedItem(Material material, String name, boolean unbreakable) {
        this(material, name);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
    }

    public ItemStack getItemStack() {
        return item;
    }
}
