package com.fhermosilla.kalah.game.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fhermosilla.kalah.game.manager.GameManager;
import com.fhermosilla.kalah.game.model.Game;
import com.fhermosilla.kalah.game.service.GameService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *
 * @author felipehermosilla
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/games")
@Api(value = "Kalah Game API")
public class GameController {

	Logger logger = LoggerFactory.getLogger(GameController.class);

	@Autowired
	GameManager gameManager;

	@Autowired
	GameService gameService;

	@PostMapping
	@ApiOperation(value = "Crate a new game instance. It returns a Game object with their default properties", produces = "Application/JSON", response = Game.class, httpMethod = "POST")
	public ResponseEntity<Game> createGame() throws Exception {

		logger.trace("Initializaing a new game ");

		Game game = gameManager.initializeGame();

		logger.info("The new game was succefully created");

		return ResponseEntity.ok(game);
	}

	@GetMapping(value = "{gameId}")
	@ApiOperation(value = "Endpoint that return a specific game data", produces = "Application/JSON", response = Game.class, httpMethod = "GET")
	public ResponseEntity<Game> getGameData(
			@ApiParam(value = "The id of game. It can't be empty or null", required = true, example="1") @PathVariable(value = "gameId") Long gameId) throws Exception {
		
		logger.trace("Getting game data from id: {}", gameId);

		return ResponseEntity.ok(gameManager.getGameData(gameId));
	}

	@GetMapping(value = "/current")
	@ApiOperation(value = "Endpoint that return the current game data", produces = "Application/JSON", response = Game.class, httpMethod = "GET")
	public ResponseEntity<Game> getCurrentData() throws Exception {
		
		logger.trace("Getting current game data ");

		return ResponseEntity.ok(gameManager.GetCurrentGameData());
	}

	@PutMapping(value = "{gameId}/buckets/{bucketId}")
	@ApiOperation(value = "Endpoint for move a piece of the game.", produces = "Application/JSON", response = Game.class, httpMethod = "PUT")
	public ResponseEntity<Game> movePiece(
			@ApiParam(value = "The id of game created by calling createGame() method. It can't be empty or null", required = true, example="1") @PathVariable(value = "gameId") Long gameId,
			@PathVariable(value = "bucketId") Integer bucketId) throws Exception {

		logger.trace("Moving a piece of the gameId: {}, bucketId: {} ", gameId, bucketId);

		return ResponseEntity.ok(gameManager.allocatePiece(gameId, bucketId));
	}

	@DeleteMapping(value = "{gameId}")
	@ApiOperation(value = "Endpoint that force the end of a current game", produces = "Application/JSON", response = Game.class, httpMethod = "DELETE")
	public ResponseEntity<Game> finishGame(
			@ApiParam(value = "The id of game. It can't be empty or null", required = true, example="1") @PathVariable(value = "gameId") Long gameId,
			@PathVariable(value = "bucketId", required = true) Integer bucketId) throws Exception {

		logger.trace("Finishing game from id: {}" , gameId);

		return ResponseEntity.ok(gameService.finishGame(gameId, "NO_WINNER"));
	}

}
