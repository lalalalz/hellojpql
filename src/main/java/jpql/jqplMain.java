package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class jqplMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Member member = new Member();
            member.setUsername("kim");
            member.setAge(19);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String jpql = "select t from Member m join m.team t";
            List<Team> resultList = em.createQuery(jpql, Team.class).getResultList();

            for (Team t : resultList) {
                System.out.println("t = " + t);
            }

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

    private static void 묵시적조인(EntityManager em) {
        Team team = new Team();
        team.setName("team1");
        em.persist(team);

        Member member = new Member();
        member.setUsername("kim");
        member.setAge(19);
        member.setTeam(team);
        em.persist(member);

        em.flush();
        em.clear();

        String jpql = "select m.team from Member m";

        List<Team> resultList = em.createQuery(jpql, Team.class).getResultList();

        for (Team t : resultList) {
            System.out.println("t = " + t);
        }
    }

    private static void 연관필드컬렉션값경로표현(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member memberA = new Member();
        memberA.setUsername("memberA");
        memberA.setTeam(team);
        em.persist(memberA);

        Member memberB = new Member();
        memberB.setUsername("memberB");
        memberB.setTeam(team);
        em.persist(memberB);

        em.flush();
        em.clear();

        String jpql = "select t.members from Team t";

        Collection resultList = em.createQuery(jpql, Collection.class).getResultList();

        //for (Object o : resultList) {
        //    System.out.println("(Member) o = " + (Member) o);
        //}
    }

    private static void 연관필드단일값(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member member = new Member();
        member.setUsername("memberA");
        member.setAge(19);
        member.setTeam(team);
        em.persist(member);

        em.flush();
        em.clear();

        String jpql = "select m.team from Member m";

        List<Team> resultList = em.createQuery(jpql, Team.class).getResultList();

        for (Team t : resultList) {
            System.out.println("t = " + t);
        }
    }

    private static void 상태필드경로탐색(EntityManager em) {
        Member member = new Member();
        member.setUsername("memberA");
        member.setAge(19);
        em.persist(member);

        em.flush();
        em.clear();

        String jpql = "select new jpql.MemberDTO(m.username, m.age) from Member m";

        List<MemberDTO> resultList = em.createQuery(jpql, MemberDTO.class).getResultList();

        for (MemberDTO m : resultList) {
            System.out.println("m = " + m);
        }
    }

    private static void nullif테스트(EntityManager em) {
        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(20);
        em.persist(member2);

        Member member3 = new Member();
        member3.setUsername("관리자");
        member3.setAge(10);
        em.persist(member3);

        String jpql = "select NULLIF(m.username, '관리자') from Member m";

        List<String> resultList = em.createQuery(jpql, String.class).getResultList();

        for (String s : resultList) {
            System.out.println("s = " + s);
        }
    }

    private static void colasce테스트(EntityManager em) {
        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(20);
        em.persist(member2);

        Member member3 = new Member();
//            member3.setUsername("member3");
        member3.setAge(10);
        em.persist(member3);

        String jpql = "select coalesce(m.username, '이름 없는 회원') from Member m";

        List<String> resultList = em.createQuery(jpql, String.class).getResultList();

        for (String s : resultList) {
            System.out.println("s = " + s);
        }
    }

    private static void case조건식(EntityManager em) {
        Order order1 = new Order();
        order1.setOrderAmount(100);
        em.persist(order1);

        Order order2 = new Order();
        order2.setOrderAmount(100);
        em.persist(order2);

        Order order3 = new Order();
        order3.setOrderAmount(21);
        em.persist(order3);

        String jpql = "select " +
                "   case" +
                "       when o.orderAmount > 30 then '주문량 초과'" +
                "       else '주문량 정상'" +
                "   end " +
                "from Order o";

        List<String> resultList = em.createQuery(jpql, String.class).getResultList();

        for (String str : resultList) {
            System.out.println("str = " + str);
        }
    }

    private static void in테스트코드(EntityManager em) {
        Order order1 = new Order();
        order1.setOrderAmount(100);
        em.persist(order1);

        Order order2 = new Order();
        order2.setOrderAmount(21);
        em.persist(order2);

        Product product1 = new Product();
        product1.setAmount(21);
        em.persist(product1);

        Product product2 = new Product();
        product2.setAmount(20);
        em.persist(product2);

        String jpql = "select o from Order o where o.orderAmount in (select p.amount from Product p)";

        List<Order> resultList = em.createQuery(jpql, Order.class).getResultList();

        for (Order order : resultList) {
            System.out.println("order = " + order);
        }
    }

    private static void any테스트코드(EntityManager em) {
        Order order1 = new Order();
        order1.setOrderAmount(100);
        em.persist(order1);

        Order order2 = new Order();
        order2.setOrderAmount(20);
        em.persist(order2);

        Product product1 = new Product();
        product1.setAmount(21);
        em.persist(product1);

        Product product2 = new Product();
        product2.setAmount(10);
        em.persist(product2);

        String jpql = "select o from Order o where o.orderAmount > any(select p.amount from Product p)";

        List<Order> resultList = em.createQuery(jpql, Order.class).getResultList();

        for (Order order : resultList) {
            System.out.println("order = " + order);
        }
    }

    private static void all지원함수(EntityManager em) {
        Order order1 = new Order();
        order1.setOrderAmount(20);
        em.persist(order1);

        Order order2 = new Order();
        order2.setOrderAmount(20);
        em.persist(order2);

        Product product1 = new Product();
        product1.setAmount(10);
        em.persist(product1);

        Product product2 = new Product();
        product2.setAmount(10);
        em.persist(product2);

        String jpql = "select o from Order o where o.orderAmount > all(select p.amount from Product p)";

        List<Order> resultList = em.createQuery(jpql, Order.class).getResultList();

        for (Order order : resultList) {
            System.out.println("order = " + order);
        }
    }

    private static void 서브쿼리테스트(EntityManager em) {
        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(20);
        em.persist(member2);

        Member member3 = new Member();
        member3.setUsername("member3");
        member3.setAge(10);
        em.persist(member3);

        String jpql = "select m from Member m where m.age > (select avg(m.age) from m)";

        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    private static void exists테스트코드(EntityManager em) {
        Team teamA = new Team();
        teamA.setName("teamA");
        em.persist(teamA);

        Team teamB = new Team();
        teamB.setName("teamB");
        em.persist(teamB);

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setTeam(teamA);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setTeam(teamA);
        em.persist(member2);

        Member member3 = new Member();
        member3.setUsername("member3");
        member3.setTeam(teamB);
        em.persist(member3);

        String jpql = "select m from Member m where exists (select t from Team t where t.name = 'teasdfmA')";

        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    private static void 연관관계없는조인조건(EntityManager em) {
        Team teamA = new Team();
        teamA.setName("1");
        em.persist(teamA);

        Team teamB = new Team();
        teamB.setName("2");
        em.persist(teamB);

        Member member1 = new Member();
        member1.setUsername("1");
//            member1.setTeam(teamA);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("1");
//            member2.setTeam(teamA);
        em.persist(member2);

        Member member3 = new Member();
        member3.setUsername("2");
//            member3.setTeam(teamB);
        em.persist(member3);

        em.flush();
        em.clear();

        String jpql = "select m from Member m left join Team t on m.username = t.name";

        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        System.out.println("resultList.size() = " + resultList.size());
        for (Member member : resultList) {
            System.out.println("member = " + member.getUsername());
        }
    }

    private static void ON절대상필터링(EntityManager em) {
//        Team teamA = new Team();
//        teamA.setName("teamA");
//        em.persist(teamA);
//
//        Team teamB = new Team();
//        teamB.setName("teamB");
//        em.persist(teamB);
//
//        Member member1 = new Member();
//        member1.setUsername("Member1");
//        member1.setTeam(teamA);
//        em.persist(member1);
//
//        Member member2 = new Member();
//        member2.setUsername("Member2");
//        member2.setTeam(teamA);
//        em.persist(member2);
//
//        Member member3 = new Member();
//        member3.setUsername("Member3");
//        member3.setTeam(teamB);
//        em.persist(member3);
//
//        em.flush();
//        em.clear();
//
//        String jpql = "select m from Member m join m.team t on t.name = 'teamA'";
//
//        List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();
//
//        System.out.println("resultList.size() = " + resultList.size());
//        for (Member member : resultList) {
//            System.out.println("member = " + member.getTeam().getName());
//        }
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
//        List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
//                .getResultList();
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
