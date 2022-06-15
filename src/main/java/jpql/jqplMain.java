package jpql;

import javax.persistence.*;
import java.util.List;

public class jqplMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("Member1");
            member.setAge(19);
            member.setTeam(team);
            em.persist(member);

            tx.commit();
        }
        catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
            emf.close();
        }
    }

    private static void 스칼라타입쿼리결과조회하기_dto(EntityManager em) {
        List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                .getResultList();


        for (MemberDTO memberDTO : result) {
            System.out.println("memberDTO = " + memberDTO);
        }
    }

    private static void 스칼라타입프로젝션(EntityManager em) {
        List result1 = em.createQuery("select m.username, m.age from Member m").getResultList();
        List result2 = em.createQuery("select 1, 'hello', m.username from Member m").getResultList();
    }

    private static void 임베디드타입프로젝션(EntityManager em) {
        Order order = new Order();
        order.setAddress(new Address("seoul", "gang-nam", "1234"));
        em.persist(order);

        List<Address> result = em.createQuery("select o.address from Order o", Address.class).getResultList();

        for (Address address : result) {
            System.out.println("address = " + address);
        }
    }

    private static void 연관관계엔티티프로젝션(EntityManager em) {
        List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
                .getResultList();
    }

    private static void 엔티티프로젝션(EntityManager em) {
        List<Member> findMembers = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        Member firstMember = findMembers.get(0);
        firstMember.setAge(10);
    }

    private static void 파라미터바인딩위치기준(EntityManager em) {
        TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = ?1 and m.age = ?2", Member.class);
        query.setParameter(1, "Member1");
        query.setParameter(2, 19);

        Member singleResult = query.getSingleResult();
        System.out.println("singleResult.getUsername() = " + singleResult.getUsername());
    }

    private static void 파라미터바인딩이름기준(EntityManager em, Member member) {
        String usernameParam = member.getUsername();
        TypedQuery<Member> query = em.createQuery("select m from  Member m where m.username = :username", Member.class);
        query.setParameter("username", usernameParam);

        List<Member> resultList = query.getResultList();

        for (Member member1 : resultList) {
            System.out.println("member1.getUsername() = " + member1.getUsername());
        }
    }

    private static void 쿼리결과조회메서드(EntityManager em) {
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> resultList = query.getResultList();

        for (Member member1 : resultList) {
            System.out.println("member1 = " + member1.getUsername() + ", " + member1.getAge());
        }
    }
}
