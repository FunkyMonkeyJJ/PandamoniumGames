package Items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public enum UpgradeItems {
    HELMET(Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET,
            Material.IRON_HELMET, Material.DIAMOND_HELMET,
            Material.GOLDEN_HELMET),
    CHESTPLATE(Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
            Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE),
    LEGGINGS(Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS,
            Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS,
            Material.GOLDEN_LEGGINGS),
    BOOTS(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS,
            Material.IRON_BOOTS, Material.DIAMOND_BOOTS,
            Material.GOLDEN_BOOTS),
    SWORDS(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.DIAMOND_SWORD, Material.GOLDEN_SWORD),
    PICKAXES(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE,
            Material.GOLDEN_PICKAXE),
    AXES(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.DIAMOND_AXE, Material.GOLDEN_AXE),
    BOWS(Material.BOW, Enchantment.ARROW_DAMAGE, Enchantment.ARROW_DAMAGE,
            Enchantment.ARROW_DAMAGE, Enchantment.ARROW_KNOCKBACK,
            Enchantment.ARROW_KNOCKBACK);

    UpgradeItems(Material... materialsInOrderOfUpgrade) {

    }

    UpgradeItems(Material material,
                 Enchantment... enchantmentsInOrderOfUpgrade) {

    }

//    public boolean checkPlayer(Player player) {
//        PlayerInventory inv = player.getInventory();
//        for (int i = 0; i < values().length - 1; i++) {
//            UpgradeItems item = values()[i];
//            if (inv.contains())
//        }
//        return true;
//    }
//
//    public boolean checkPlayerBow(Player player) {
//        PlayerInventory inv = player.getInventory();
//        ItemStack playerBow = inv.getItem(3);
//        assert playerBow != null;
//        ItemMeta meta = playerBow.getItemMeta();
//        assert meta != null;
//        int power = meta.getEnchantLevel(Enchantment.ARROW_DAMAGE);
//        int punch = meta.getEnchantLevel(Enchantment.ARROW_KNOCKBACK);
//
//        ItemStack returnedBow = new ItemStack(Material.BOW);
//        return true;
//    }
}
