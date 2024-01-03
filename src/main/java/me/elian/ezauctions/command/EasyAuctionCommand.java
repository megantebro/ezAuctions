package me.elian.ezauctions.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.elian.ezauctions.Logger;
import me.elian.ezauctions.controller.*;
import me.elian.ezauctions.data.Database;
import me.elian.ezauctions.model.Auction;
import me.elian.ezauctions.model.AuctionPlayer;
import me.elian.ezauctions.scheduler.TaskScheduler;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ExecutionException;

@Singleton
@CommandAlias("auction|auctions|auc|")
@CommandPermission("ezauctions.easyauction")
@Description("Easy Auction Command")
public class EasyAuctionCommand extends BaseCommand {
	AuctionCommand auctionCommand;

	@Inject
	public EasyAuctionCommand(Plugin plugin, Logger logger, Economy economy, AuctionController auctionController,
	                          AuctionPlayerController playerController, ConfigController config,
	                          MessageController messages, ScoreboardController scoreboard,
	                          UpdateController updateController, Database database, TaskScheduler scheduler) {
		this.auctionCommand = new AuctionCommand(plugin, logger, economy, auctionController, playerController, config,
				messages, scoreboard, updateController, database, scheduler);
	}

	@Default
	@Syntax("[price] [amount]")
	@CommandCompletion("開始価格 個数")
	public void auction(Player player, double price, @Default(value = "0") int amount) {
		// Calculate the increment
		// by taking the smallest power of 10 that is greater than the price
		// and dividing it by 10
		// For example: 100 -> 100, 6500 -> 1000, 999 -> 100, 1000 -> 1000, 12950 -> 10000
		double increment = Math.pow(10, Math.floor(Math.log10(price))) / 10;

		// Execute the auction start command
		auctionCommand.start(player, amount == 0 ? "hand" : String.valueOf(amount), price, increment, 0, 0);
	}
}
