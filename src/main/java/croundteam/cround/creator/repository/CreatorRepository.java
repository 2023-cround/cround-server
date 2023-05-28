package croundteam.cround.creator.repository;

import croundteam.cround.creator.domain.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long>, CreatorOffsetQueryRepository {

    boolean existsByPlatformPlatformActivityNameName(String platformActivityName);

    @Query("SELECT c FROM Creator c WHERE c.member.email = :email")
    Optional<Creator> findCreatorByEmail(@Param("email") String email);
}
