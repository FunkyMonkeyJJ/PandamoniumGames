package Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class UnbreakableItem {
    private final ItemStack item;

    public UnbreakableItem(Material material) {
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }

    public ItemStack getItemStack() {
        return item;
    }
}
