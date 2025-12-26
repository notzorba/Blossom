package com.xyrisdev.blossom.command.subcommand;

import com.xyrisdev.blossom.exception.DuplicateRegionException;
import com.xyrisdev.blossom.menu.RegionManageMenu;
import com.xyrisdev.blossom.region.RegionManager;
import com.xyrisdev.blossom.region.model.Region;
import com.xyrisdev.blossom.util.command.RegisterableSubCommand;
import com.xyrisdev.blossom.util.WorldEditSelectionUtil;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.xyrisdev.blossom.util.AsyncWorldEditUtil;
import com.xyrisdev.library.command.Commands;
import com.xyrisdev.library.command.arguments.Arguments;
import com.xyrisdev.library.command.model.CommandSuggestions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class CreateSubCommand implements RegisterableSubCommand {

	@Override
	public @NotNull Consumer<Commands.Builder> build(@NotNull JavaPlugin plugin) {
		return builder -> builder
				.argument(Arguments.string().string("name", CommandSuggestions.of("<name>")))
				.executes(context -> {
					if (!context.instanceOfPlayer()) {
						context.sender().sendRichMessage("<red>Players are only allowed to use this command!");
						return;
					}

					final Player player = context.senderAsPlayer();
					final String rawName = context.resolve("name");
					final String name = (rawName == null || rawName.isBlank())
							? "blossom16_" + (1000 + new SecureRandom().nextInt(9000))
							: rawName;

					if (!name.matches("[A-Za-z0-9_\\-]{1,48}")) {
						player.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>→ Invalid name. Use letters/numbers/_/- only.</gray>");
						return;
					}

					try {
						RegionManager.instance().create(name, name);
					} catch (DuplicateRegionException e) {
						// Extremely unlikely case since we generate random names,
						// but we handle it gracefully if it occurs
						player.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>→ Region <gradient:#8c75a5:#f46c90>" + name + "</gradient> already exists.</gray>");
						return;
					}

					final Region region = RegionManager.instance().region(name);
					final WorldEditSelectionUtil.Selection selection = WorldEditSelectionUtil.selection(player);
					if (region != null && selection != null) {
						RegionManager.instance().point(name, com.xyrisdev.blossom.region.model.RegionPointType.MIN, selection.min());
						RegionManager.instance().point(name, com.xyrisdev.blossom.region.model.RegionPointType.MAX, selection.max());
						RegionManager.instance().point(name, com.xyrisdev.blossom.region.model.RegionPointType.CENTER, selection.center());

						// Save schematic immediately from the newly set region bounds.
						AsyncWorldEditUtil.schematic(name, BuiltInClipboardFormat.FAST_V3);
					} else {
						player.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>→ No WorldEdit selection found. Make a selection (//wand) then run /blossom create <name> again, or set points in the menu.</gray>");
					}

					RegionManageMenu.region(RegionManager.instance().region(name)).open(player);

					player.sendRichMessage(" ");
					player.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>(v" + plugin.getPluginMeta().getVersion() + ")</gray>");
					player.sendRichMessage(" ");
					player.sendRichMessage("<gray>→ Region <gradient:#8c75a5:#f46c90>" + name + "</gradient> has been created.</gray>");
					if (selection != null) {
						player.sendRichMessage("<gray>→ WorldEdit selection captured and schematic saved.</gray>");
					}
					player.sendRichMessage(" ");
				})
				.error(context -> context.action(sender -> sender.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>→ Failed to create a region );.</gray>")));
	}
}
