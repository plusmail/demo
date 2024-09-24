package kroryi.demo.repository;

import kroryi.demo.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid= :mid")
    Optional<Member> getWithRoles(String mid);

}
