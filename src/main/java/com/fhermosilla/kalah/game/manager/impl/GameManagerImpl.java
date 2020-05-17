package com.fhermosilla.kalah.game.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fhermosilla.kalah.game.constants.GameConstant;
import com.fhermosilla.kalah.game.enums.GameStatusEnum;
import com.fhermosilla.kalah.game.enums.TurnValueEnum;
import com.fhermosilla.kalah.game.exceptions.ForbiddenException;
import com.fhermosilla.kalah.game.exceptions.GameException;
import com.fhermosilla.kalah.game.exceptions.InvalidParameterException;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;
import com.fhermosilla.kalah.game.manager.GameManager;
import com.fhermosilla.kalah.game.model.Bucket;
import com.fhermosilla.kalah.game.model.Game;
import com.fhermosilla.kalah.game.service.BucketService;
import com.fhermosilla.kalah.game.service.GameService;

/**
 * 
 * @author felipehermosilla
 * This class orchestrate different actions over a game logic
 */
@Component
public class GameManagerImpl implements GameManager {

	Logger logger = LoggerFactory.getLogger(GameManagerImpl.class);

	@Autowired
	GameService gameService;

	@Autowired
	BucketService bucketService;

	@Override
	public Game initializeGame() throws ForbiddenException, ResourceNotFoundException {

		logger.trace("Initializaing game");
		Game newGame = gameService.createGame();

		logger.trace("Initializaing buckets");
		bucketService.initializeAllBuckets();
		List<Bucket> buckets = bucketService.getAllBuckets();
		newGame.setBuckets(buckets);

		return newGame;
	}

	@Override
	public Game allocatePiece(Long gameId, Integer bucketId)
			throws ResourceNotFoundException, InvalidParameterException {
		validateBucketId(bucketId);
		Game game = getGame(gameId);

		// Checking valid combination between turn and bucketId
		if ((game.getTurn() == TurnValueEnum.FIRST_PLAYER && bucketId > GameConstant.basketPlayerAPrincipalId)
				|| (game.getTurn() == TurnValueEnum.SECOND_PLAYER
						&& bucketId < GameConstant.basketPlayerAPrincipalId)) {

			logger.info("The selected bucketId is invalid for the current player, no allocate action performed");
			game.setBuckets(bucketService.getAllBuckets());
			return game;
		}

		Bucket selectedBucket = bucketService.getBucket(bucketId);
		Integer piecesQuantity = selectedBucket.getPieces();
		// Checking the bucket has pieces
		if (selectedBucket.getPieces() == GameConstant.emptyBascketCount) {

			logger.info("The selected bucket has not pieces, no allocate action performed");
			game.setBuckets(bucketService.getAllBuckets());
			return game;
		}

		logger.trace("Emptying the basket: {}", bucketId);
		selectedBucket.setPieces(GameConstant.emptyBascketCount);

		// Updating current index
		game.setCurrentIndex(bucketId);

		for (int i = 0; i < piecesQuantity - 1; i++) {
			movePiece(game, false);
		}

		movePiece(game, true);

		checkGameStatus(game);
		int currentBucketIndex = game.getCurrentIndex();

		// Update player turn based on the game status and the currentBucketIndex
		if (game.getStatus() != GameStatusEnum.FINISHED && currentBucketIndex != GameConstant.basketPlayerAPrincipalId
				&& currentBucketIndex != GameConstant.basketPlayerBPrincipalId) {

			logger.trace("Updating player turn");
			updatePlayerTurn(game);
		}

		game.setBuckets(bucketService.getAllBuckets());

		return game;
	}

	private void checkGameStatus(Game game) throws ResourceNotFoundException {

		logger.trace("Checking the game status");
		TurnValueEnum currentTurn = game.getTurn();
		if (!bucketService.playerHasStillLeftPieces(currentTurn)) {

			logger.info("The player: {} has not left pieces. Finishing the game", currentTurn);
			String winner = bucketService.determineWinner(currentTurn);

			logger.info("Winner of the game: {}", winner);
			game = gameService.finishGame(game.getId(), winner);
			logger.info("The gameId: {} was finished", game.getId());
		}

	}

	private void movePiece(Game game, Boolean isLast) {

		int currentIndex = game.getCurrentIndex() % GameConstant.totalBaskets + 1;

		TurnValueEnum turn = game.getTurn();

		logger.trace("Movint the piece for the index: {} and the turn: {}", currentIndex, turn.toString());
		if ((currentIndex == GameConstant.basketPlayerAPrincipalId && turn == TurnValueEnum.SECOND_PLAYER)
				|| (currentIndex == GameConstant.basketPlayerBPrincipalId && turn == TurnValueEnum.FIRST_PLAYER))
			currentIndex = currentIndex % GameConstant.totalBaskets + 1;

		game.setCurrentIndex(currentIndex);

		Bucket targetBucket = bucketService.getBucket(currentIndex);

		if (!isLast || currentIndex == GameConstant.basketPlayerAPrincipalId
				|| currentIndex == GameConstant.basketPlayerBPrincipalId) {

			logger.trace("Placing the bucketId: {}", targetBucket.getBucketId());
			targetBucket.place();
			bucketService.updateBucket(targetBucket);
			return;
		}

		// Checking the opposite bucket
		logger.trace("Checking the opposite bucket");
		Bucket oppositeBucket = bucketService.getBucket(GameConstant.totalBaskets - currentIndex);

		// Logic that compares if targetBucket is empty and the oppositeBucket isn't
		if (targetBucket.isEmpty() && !oppositeBucket.isEmpty()) {
			logger.trace("Emptying the opposite bucket");
			Integer oppositePieces = oppositeBucket.getPieces();
			oppositeBucket.clearPieces();
			Integer bucketIndex = currentIndex < GameConstant.basketPlayerAPrincipalId
					? GameConstant.basketPlayerAPrincipalId
					: GameConstant.basketPlayerBPrincipalId;
			Bucket bucket = bucketService.getBucket(bucketIndex);
			bucket.addPieces(oppositePieces + 1);
			bucketService.updateBucket(bucket);
			return;
		}

		logger.trace("Placing the bucketId: {}", targetBucket.getBucketId());
		targetBucket.place();
		bucketService.updateBucket(targetBucket);
	}

	private void validateBucketId(Integer bucketId) throws InvalidParameterException {
		logger.trace("Validating bucketId value");
		if (bucketId < 1 || bucketId > 14) {

			logger.error("Invalid bucket value");
			throw new InvalidParameterException("The bucketId must be a number between 1-14");
		}
	}

	private Game getGame(Long gameId) throws ResourceNotFoundException, InvalidParameterException {
		Game game = gameService.getGameById(gameId);
		if (game == null) {

			logger.error("Invalid gameId");
			throw new ResourceNotFoundException("The game with id:" + gameId + " doesn't exist");
		} else if (game.getStatus() == null) {

			logger.error("Invalid status game");
			throw new GameException("Something got wrong: the game has an invalid state");
		} else if (game.getStatus() != GameStatusEnum.IN_PROGRESS) {

			logger.error("Invalid status game for moving a piece");
			throw new InvalidParameterException("You cannot play a game with state: " + game.getStatus());
		} else if (game.getTurn() == null) {

			logger.error("The game has not a player turn reference");
			throw new GameException("Something got wrong: the game has not a turn reference");
		}

		return game;
	}

	private void updatePlayerTurn(Game game) {

		game.setTurn(game.getTurn() == TurnValueEnum.FIRST_PLAYER ? TurnValueEnum.SECOND_PLAYER
				: TurnValueEnum.FIRST_PLAYER);
		logger.trace("Next turn: {}", game.getTurn());
		gameService.updateGame(game);
	}

	@Override
	public Game getGameData(Long gameId) throws ResourceNotFoundException {

		logger.trace("Loaging game data for id: {}", gameId);
		Game game = gameService.getGameById(gameId);
		if (game.getStatus() == GameStatusEnum.IN_PROGRESS)
			game.setBuckets(bucketService.getAllBuckets());
		return game;
	}

	@Override
	public Game GetCurrentGameData() throws ResourceNotFoundException {

		logger.trace("Loaging the current game data");
		Game game = gameService.getCurrentGame();
		game.setBuckets(bucketService.getAllBuckets());
		return game;
	}
}
