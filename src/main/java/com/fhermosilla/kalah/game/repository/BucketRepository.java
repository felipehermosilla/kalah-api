package com.fhermosilla.kalah.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fhermosilla.kalah.game.model.Bucket;

/**
 *
 * @author felipehermosilla
 * Bucket Repository
 */
@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {

	Bucket findByBucketId(Integer bucketId);
}
