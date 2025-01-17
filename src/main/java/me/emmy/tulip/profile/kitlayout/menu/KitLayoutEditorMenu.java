package me.emmy.tulip.profile.kitlayout.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.hotbar.HotbarUtility;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.ItemBuilder;
import me.emmy.tulip.utils.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 10/08/2024 - 20:21
 */
@AllArgsConstructor
public class KitLayoutEditorMenu extends Menu {

    private Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eEditing Kit: &d" + kit.getName());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new LoadCurrentLayoutButton(kit));
        buttons.put(12, new SaveButton(kit));
        buttons.put(14, new ResetButton(kit));
        buttons.put(16, new CancelEditingProcessButton());

        addBorder(buttons, (byte) 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    public static class SaveButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD_BLOCK)
                    .name(CC.translate("&aSave Kit Layout"))
                    .lore(Collections.singletonList(CC.translate("&7Click to save the kit layout.")))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            profile.getKitLayout().setLayout(kit.getName(), player.getInventory().getContents());
            profile.saveProfile();
            profile.getKitLayout().setEditing(false);

            PlayerUtil.reset(player);
            player.closeInventory();
            player.sendMessage(CC.translate("&aKit layout saved."));
            HotbarUtility.applyHotbarItems(player);
        }
    }

    @AllArgsConstructor
    public static class ResetButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE_BLOCK)
                    .name(CC.translate("&cReset Kit Layout"))
                    .lore(Collections.singletonList(CC.translate("&7Click to reset the kit layout.")))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            player.getInventory().setContents(kit.getItems());
            player.sendMessage(CC.translate("&cGiving you the default kit layout."));
        }
    }

    @AllArgsConstructor
    public static class LoadCurrentLayoutButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BOOK)
                    .name(CC.translate("&eLoad Current Layout"))
                    .lore(Collections.singletonList(CC.translate("&7Click to load the current layout.")))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            ItemStack[] items = profile.getKitLayout().getLayout(kit.getName());

            if (items == null) {
                items = kit.getItems();
            }

            player.getInventory().setContents(items);
            player.sendMessage(CC.translate("&aLoaded your currently saved kit layout."));
        }
    }

    @AllArgsConstructor
    public static class CancelEditingProcessButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BARRIER)
                    .name(CC.translate("&cCancel Editing Process"))
                    .lore(Collections.singletonList(CC.translate("&7Click to cancel the editing process.")))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            profile.getKitLayout().setEditing(false);

            PlayerUtil.reset(player);
            HotbarUtility.applyHotbarItems(player);
            player.closeInventory();
            player.sendMessage(CC.translate("&cEditing process cancelled."));
        }
    }

    @Override
    public boolean isUpdateAfterClick() {
        return false;
    }

    @Override
    public void onClose(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        HotbarUtility.applyHotbarItems(player);
    }
}
