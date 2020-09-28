package General;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Cooldown {
    HashMap<String, Long> cooldowns = new HashMap<>();

    public float getTimeLeft(Player p, String cooldown) {
        String format = p.getName() + "." + cooldown;
        try {
            long l = cooldowns.get(format);
            long diff = l-System.currentTimeMillis();
            if(diff < 0)
                diff = 0;
            float left = diff/1000f;
            return left;
        } catch (NullPointerException e) {
            return 0f;
        }
    }

    public boolean isOnCooldown(Player p, String cooldown) {
        return getTimeLeft(p, cooldown) != 0;
    }

    public void setCooldown(Player p, String cooldown, float time) {
        String format = p.getName() + "." + cooldown;
        long l = System.currentTimeMillis() + (long) (time*1000);
        cooldowns.put(format, l);
    }
}
