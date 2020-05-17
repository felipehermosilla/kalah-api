package com.fhermosilla.kalah.game.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhermosilla.kalah.game.constants.GameConstant;
import com.fhermosilla.kalah.game.enums.TurnValueEnum;
import com.fhermosilla.kalah.game.exceptions.GameException;
import com.fhermosilla.kalah.game.exceptions.ResourceNotFoundException;
import com.fhermosilla.kalah.game.model.Bucket;
import com.fhermosilla.kalah.game.repository.BucketRepository;
import com.fhermosilla.kalah.game.service.BucketService;

/**
 *
 * @author felipehermosilla
 * This class performs actions over a bucket
 */
@Service
@Transactional
public class BucketServiceImpl implements BucketService {

	Logger logger = LoggerFactory.getLogger(BucketServiceImpl.class);

	@Autowired
	BucketRepository repository;

	@Override
	public Bucket createBucket(Integer bucketId) {

		Integer defaultBasketQuantity = getDefaultBasketQuantity(bucketId);
		Bucket bucket = getBucket(bucketId);
		if (bucket == null) {
			logger.trace("creating the bucketId: {}", bucketId);
			return repository.save(new Bucket(bucketId, defaultBasketQuantity));
		}
		logger.trace("The bucketId: {} already exist, updating the its default piece quantity", bucketId);
		bucket.setPieces(defaultBasketQuantity);
		return repository.save(bucket);
	}

	private Integer getDefaultBasketQuantity(Integer bucketId) {
		Integer defaultBasketQuantity = null;
		if(bucketId == GameConstant.basketPlayerAPrincipalId ||
				bucketId == GameConstant.basketPlayerBPrincipalId) {
			defaultBasketQuantity = 0;
		}else {
			defaultBasketQuantity = GameConstant.defaultBasketQuantity;
		}
		return defaultBasketQuantity;
	}

	@Override
	public Bucket getBucket(Integer bucketId) {

		logger.trace("Getting bucketId: {} data", bucketId);
		return repository.findByBucketId(bucketId);
	}

	@Override
	public Bucket addPiecesBucket(Integer bucketId, Integer pieces) throws ResourceNotFoundException {
		logger.trace("Adding {} pieces for bucketId: {}", pieces, bucketId);
		Bucket bucket = getBucket(bucketId);

		if (bucket == null) {
			throw new ResourceNotFoundException("The bucketId: " + bucketId + " doesn't exist");
		}

		bucket.addPieces(pieces);
		logger.info("The bucketId: {} pieces was updated to: {} pieces", bucketId, bucket.getPieces());
		return repository.save(bucket);
	}

	@Override
	public void initializeAllBuckets() {
		logger.trace("Initializing first player buckets");
		initializeFirstPlayerBuckets();

		logger.trace("Initializing second player buckets");
		initializeSecondPlayerBuckets();

	}

	private void initializeFirstPlayerBuckets() {
		for (int i = 0; i < GameConstant.playerABucketsId.length; i++) {
			createBucket(GameConstant.playerABucketsId[i]);
		}
	}

	private void initializeSecondPlayerBuckets() {
		for (int i = 0; i < GameConstant.playerBBucketsId.length; i++) {
			createBucket(GameConstant.playerBBucketsId[i]);
		}
	}

	@Override
	public List<Bucket> getAllBuckets() {
		logger.trace("Getting all buckets");
		return repository.findAll();
	}

	@Override
	public Bucket updateBucket(Bucket bucket) {

		logger.info("Updating bucketId: {}", bucket.getBucketId());
		return repository.save(bucket);
	}

	@Override
	public Boolean playerHasStillLeftPieces(TurnValueEnum playerTurn) {

		logger.info("Verifying if player: {} has still pieces on his buckets", playerTurn);
		return playerTurn == TurnValueEnum.FIRST_PLAYER ? checkFirstPlayerHasNotLeftPieces() : checkSecondPlayerHasNotLeftPieces();
		
	}

	private Boolean checkFirstPlayerHasNotLeftPieces() {
		Boolean hasLeftPieces = false;
		for (int i = 0; i < GameConstant.playerABucketsId.length - 1; i++) {
			Bucket bucket = getBucket(GameConstant.playerABucketsId[i]);
			if (bucket.getPieces() > GameConstant.emptyBascketCount) {
				logger.info("The first player still have pieces on his buckets");
				hasLeftPieces = true;
				break;
			}
		}

		return hasLeftPieces;
	}

	private Boolean checkSecondPlayerHasNotLeftPieces() {
		Boolean hasLeftPieces = false;
		for (int i = 0; i < GameConstant.playerBBucketsId.length - 1; i++) {
			Bucket bucket = getBucket(GameConstant.playerBBucketsId[i]);
			if (bucket.getPieces() > GameConstant.emptyBascketCount) {
				logger.info("The second player still have pieces on his buckets");
				hasLeftPieces = true;
				break;
			}
		}
		return hasLeftPieces;
	}

	@Override
	public String determineWinner(TurnValueEnum currentTurn) {
		logger.trace("Determining the game's winner");
		Integer firstPlayerTotal = 0;
		Integer secondPlayerTotal = 0;
		if (currentTurn == TurnValueEnum.FIRST_PLAYER) {

			firstPlayerTotal = getBucket(GameConstant.basketPlayerAPrincipalId).getPieces();
			secondPlayerTotal = getTotalLeftCountPieces(GameConstant.playerBBucketsId)
					+ getBucket(GameConstant.basketPlayerBPrincipalId).getPieces();

		} else if (currentTurn == TurnValueEnum.SECOND_PLAYER) {

			secondPlayerTotal = getBucket(GameConstant.basketPlayerBPrincipalId).getPieces();
			firstPlayerTotal = getTotalLeftCountPieces(GameConstant.playerABucketsId)
					+ getBucket(GameConstant.basketPlayerAPrincipalId).getPieces();

		} else {

			logger.error("Invalid turn parameter");
			throw new GameException("Invalid turn parameter");
		}

		logger.info("Resume: firstPlayerTotal: {} , secondPlayerTotal: {}", firstPlayerTotal, secondPlayerTotal);
		return firstPlayerTotal > secondPlayerTotal ? TurnValueEnum.FIRST_PLAYER.name()
				: TurnValueEnum.SECOND_PLAYER.name();
	}

	private Integer getTotalLeftCountPieces(int[] interval) {
		Integer totalLeftPiece = 0;
		for (int i = 0; i < interval.length - 1; i++) {
			totalLeftPiece += getBucket(interval[i]).getPieces();
		}
		return totalLeftPiece;
	}
}
