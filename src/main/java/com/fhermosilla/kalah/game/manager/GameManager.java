package com.fhermosilla.kalah.game.manager;

import com.fhermosilla.kalah.game.exceptions.ForbiddenException;
import com.fhermosilla.kalah.game.exceptions.InvalidParameterException;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;
import com.fhermosilla.kalah.game.model.Game;

/**
 *
 * @author felipehermosilla
 * Game Manager
 */
public interface GameManager {

	/**
	 * Initialize a game: buckets, default values and default player's turn
	 * @return
	 * @throws ForbiddenException
	 * @throws ResourceNotFoundException 
	 */
	public Game initializeGame() throws ForbiddenException, ResourceNotFoundException;

	/**
	 * Allocate a piece based on the selected bucketId
	 * @param gameId
	 * @param bucketId
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws InvalidParameterException
	 */
	public Game allocatePiece(Long gameId, Integer bucketId) throws ResourceNotFoundException, InvalidParameterException;

	/**
	 * Return a game's data along with its buckets
	 * @param gameId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Game getGameData(Long gameId) throws ResourceNotFoundException;

	/**
	 * Return the current game data if exist
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Game GetCurrentGameData() throws ResourceNotFoundException;
}
