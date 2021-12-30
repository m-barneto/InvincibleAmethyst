package me.mattdokn.invincibleamethyst.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class InvincibleAmethystClient implements ClientModInitializer {
    //region Keybinds
    private final KeyBinding kbProtecting = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.invincibleamethyst.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UP,
            "category.invincibleamethyst"
    ));

    private boolean toggled;

    @Override
    public void onInitializeClient() {
        toggled = true;
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (kbProtecting.wasPressed()) {
                toggled = !toggled;
                mc.player.sendMessage(Text.of("Budding Amethyst protection is now " + (toggled ? "enabled" : "disabled") + "."), false);
            }
        });
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (!toggled) return ActionResult.PASS;
            if (world.getBlockState(pos).isOf(Blocks.BUDDING_AMETHYST)) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}
