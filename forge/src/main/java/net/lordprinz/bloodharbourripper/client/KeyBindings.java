package net.lordprinz.bloodharbourripper.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY = "key.categories.bloodharbourripper";

    public static final KeyMapping EXECUTE_KEY = new KeyMapping(
            "key.bloodharbourripper.execute",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KEY_CATEGORY
    );

    public static final KeyMapping DASH_KEY = new KeyMapping(
            "key.bloodharbourripper.dash",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            KEY_CATEGORY
    );
}

