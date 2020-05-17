package com.fhermosilla.kalah.game.service;

import com.fhermosilla.kalah.game.exceptions.ForbiddenException;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;
import com.fhermosilla.kalah.game.model.Game;

/**
 *
 * @author felipehermosilla
 *
 */
public interface GameService {

	/**
	 * Create a new game
	 * @return Game data
	 * @throws ForbiddenException
	 * @throws ResourceNotFoundException 
	 */
	public Game createGame() throws ForbiddenException, ResourceNotFoundException;

	/**
	 * Update a game 
	 * @param game
	 * @return Updated Game data
	 */
	public Game updateGame(Game game);

	/**
	 * Finish a game
	 * @param gameId
	 * @param winner
	 * @return Finished game data
	 * @throws ResourceNotFoundException
	 */
	public Game finishGame(Long gameId, String winner) throws ResourceNotFoundException;

	/**
	 * Get the current game
	 * @return current game data
	 * @throws ResourceNotFoundException 
	 */
	public Game getCurrentGame() throws ResourceNotFoundException;

	/**
	 * Get a game data based on the id
	 * @param id
	 * @return Game data
	 * @throws ResourceNotFoundException
	 */
	public Game getGameById(Long id) throws ResourceNotFoundException;
}
