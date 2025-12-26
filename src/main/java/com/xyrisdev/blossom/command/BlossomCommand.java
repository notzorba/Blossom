package com.xyrisdev.blossom.command;

import com.xyrisdev.blossom.command.subcommand.CreateSubCommand;
import com.xyrisdev.blossom.command.subcommand.ManageSubCommand;
import com.xyrisdev.blossom.command.subcommand.RegenerateSubCommand;
import com.xyrisdev.blossom.command.subcommand.IntervalSubCommand;
import com.xyrisdev.blossom.command.subcommand.ReloadSubCommand;
import com.xyrisdev.library.command.Commands;
import com.xyrisdev.library.command.meta.CommandMeta;
import com.xyrisdev.library.command.model.ErrorType;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class BlossomCommand {

	public static @NotNull Commands blossom(@NotNull JavaPlugin plugin) {
		final String version = plugin.getPluginMeta().getVersion();
		return Commands.of("blossom")
				.meta(CommandMeta.builder().namespace("blossom"))
				.requires(sender -> sender.hasPermission("blossom.admin") || sender.hasPermission("blossom.*") || sender.isOp())
				.executes(context -> {
					final CommandSender sender = context.sender();

					sender.sendRichMessage(" ");
					sender.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>(v" + version + ")</gray>");
					sender.sendRichMessage("<hover:show_text:'<gray>Click to star on GitHub!</gray>'><click:open_url:https://github.com/Darkxx14/Blossom><gray>Star</gray> <gradient:#8c75a5:#f46c90>Blossom</gradient> <gray>on GitHub!</gray></click></hover>");
					sender.sendRichMessage(" ");
					sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom create <name></gradient> <gray>→ Create a new region from your WorldEdit selection.</gray>");
					sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom manage <region></gradient> <gray>→ View/edit a region.</gray>");
					sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom regenerate <region></gradient> <gray>→ Regenerate a specific region.</gray>");
					sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom interval <region> <time> <unit></gradient> <gray>→ Set regeneration interval for a region.</gray>");
					sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom reload</gradient> <gray>→ Reload the plugin's configurations.</gray>");
					sender.sendRichMessage(" ");
				})

				.child("create", new CreateSubCommand().build(plugin))
				.child("manage", new ManageSubCommand().build(plugin))
				.child("regenerate", new RegenerateSubCommand().build(plugin))
				.child("interval", new IntervalSubCommand().build(plugin))
				.child("reload", new ReloadSubCommand().build(plugin))

				.error(ctx -> {
					if (ctx.type() == ErrorType.PERMISSION_DENIED) {
						ctx.action(sender -> sender.sendRichMessage("<gray>Imagine not having permission in 2025 ☠.</gray>"));
					} else {
						ctx.action(sender -> {
							sender.sendRichMessage(" ");
							sender.sendRichMessage("<b><gradient:#8c75a5:#f46c90>Blossom</gradient></b> <gray>(v" + version + ")</gray>");
							sender.sendRichMessage("<hover:show_text:'<gray>Click to star on GitHub!</gray>'><click:open_url:https://github.com/Darkxx14/Blossom><gray>Star</gray> <gradient:#8c75a5:#f46c90>Blossom</gradient> <gray>on GitHub!</gray></click></hover>");
							sender.sendRichMessage(" ");
							sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom create <name></gradient> <gray>→ Create a new region from your WorldEdit selection.</gray>");
							sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom manage <region></gradient> <gray>→ View/edit a region.</gray>");
							sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom regenerate <region></gradient> <gray>→ Regenerate a specific region.</gray>");
							sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom interval <region> <time> <unit></gradient> <gray>→ Set regeneration interval for a region.</gray>");
							sender.sendRichMessage("<gradient:#8c75a5:#f46c90>/blossom reload</gradient> <gray>→ Reload the plugin's configurations.</gray>");
							sender.sendRichMessage(" ");
						});
					}
				})
				.build();
	}
}
