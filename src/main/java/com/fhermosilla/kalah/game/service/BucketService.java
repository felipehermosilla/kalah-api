package com.fhermosilla.kalah.game.service;

import java.util.List;

import com.fhermosilla.kalah.game.enums.TurnValueEnum;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;
import com.fhermosilla.kalah.game.model.Bucket;

/**
 *
 * @author felipehermosilla
 *
 */
public interface BucketService {

	/**
	 * Create bucket. In case that the bucket already exist it reset the bucket
	 * @param bucketId
	 * @return Created Bucket
	 */
	public Bucket createBucket(Integer bucketId);

	/**
	 * Update a specific bucket
	 * @param bucket
	 * @return Updated Bucket
	 */
	public Bucket updateBucket(Bucket bucket);

	/**
	 * Get a bucket data
	 * @param bucketId
	 * @return Update Bucket Data
	 */
	public Bucket getBucket(Integer bucketId);

	/**
	 * Add pieces to a specific bucket
	 * @param bucketId
	 * @param pieces
	 * @return Bucket Data
	 * @throws ResourceNotFoundException 
	 */
	public Bucket addPiecesBucket(Integer bucketId, Integer pieces) throws ResourceNotFoundException;

	/**
	 * Initialize all buckets
	 */
	public void initializeAllBuckets();

	/**
	 * Get All Buckets
	 * @return List of buckets
	 */
	public List<Bucket> getAllBuckets();

	/**
	 *
	 * @param currentTurn
	 * @return Winner of the game
	 */
	public String determineWinner(TurnValueEnum currentTurn);

	/**
	 * Determine if the player has still left pieces in his buckets
	 * @param playerTurn
	 * @return true if the player has still pieces and false if not 
	 */
	public Boolean playerHasStillLeftPieces(TurnValueEnum playerTurn);
}
