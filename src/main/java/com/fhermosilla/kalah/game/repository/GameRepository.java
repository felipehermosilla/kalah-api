package com.fhermosilla.kalah.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fhermosilla.kalah.game.enums.GameStatusEnum;
import com.fhermosilla.kalah.game.model.Game;

/**
 *
 * @author felipehermosilla
 * Game Repository
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	Game findByStatus(GameStatusEnum status);

}
