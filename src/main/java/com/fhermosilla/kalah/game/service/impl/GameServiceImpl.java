package com.fhermosilla.kalah.game.service.impl;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhermosilla.kalah.game.enums.GameStatusEnum;
import com.fhermosilla.kalah.game.exceptions.ForbiddenException;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;
import com.fhermosilla.kalah.game.model.Game;
import com.fhermosilla.kalah.game.repository.GameRepository;
import com.fhermosilla.kalah.game.service.BucketService;
import com.fhermosilla.kalah.game.service.GameService;

/**
 *
 * @author felipehermosilla
 * Perform operations over a game
 */
@Service
public class GameServiceImpl implements GameService {

	Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	GameRepository repository;

	@Autowired
	BucketService bucketService;

	@Override
	public Game createGame() throws ForbiddenException, ResourceNotFoundException {
		Game currentGame = repository.findByStatus(GameStatusEnum.IN_PROGRESS);
		if (currentGame != null) {
			logger.error("There is already an active game");
			throw new ForbiddenException("There is already an active game");
		}

		Game game = new Game();
		logger.info("Creagint a new game");
		return repository.save(game);
	}

	@Override
	public Game updateGame(Game game) {

		logger.info("Updagint gameId: {}", game.getId());
		return repository.save(game);
	}

	@Override
	public Game finishGame(Long gameId, String winner) throws ResourceNotFoundException {
		Game game = getCurrentGame();

		game.setStatus(GameStatusEnum.FINISHED);
		game.setWinner(winner);
		logger.error("Finishing GamaeId: {}, Winner: {}", gameId, winner);
		return repository.save(game);
	}

	@Override
	public Game getCurrentGame() throws ResourceNotFoundException {

		logger.trace("Getting current game");
		Game game = repository.findByStatus(GameStatusEnum.IN_PROGRESS);

		if (game == null) {
			logger.error("The current Game doesn't exist");
			throw new ResourceNotFoundException("There isn't a current game");
		}

		return game;
	}

	@Override
	public Game getGameById(Long id) throws ResourceNotFoundException {

		logger.trace("Getting gameId: {}", id);
		Optional<Game> gameOptional = repository.findById(id);

		if (!gameOptional.isPresent()) {

			throw new ResourceNotFoundException("Game with id: " + id + " desn't exist");
		}

		return gameOptional.get();
	}

}
