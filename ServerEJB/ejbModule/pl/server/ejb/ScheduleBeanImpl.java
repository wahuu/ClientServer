package pl.server.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.server.dto.Schedule;
/**
 * 
 * operacje na tabeli schedule 
 */
@Named
@Stateless
public class ScheduleBeanImpl {
	@PersistenceContext
	private EntityManager em;

	public void addSchedule(Schedule schedule) {
		em.persist(schedule);
	}

	public void delete(Schedule schedule) {
		schedule = em.merge(schedule);
		em.remove(schedule);
	}

	public List<Schedule> getAll() {
		List<Schedule> resultList = em.createQuery("select s from Schedule s", Schedule.class).getResultList();
		return resultList;
	}

	public void update(Schedule schedule) {
		em.merge(schedule);
	}

	public List<Schedule> getByTitle(String title) {
		TypedQuery<Schedule> query = em.createQuery("Select s from Schedule s where s.title = :title", Schedule.class);
		query.setParameter("title", title);
		return query.getResultList();
	}

	public List<Schedule> getByConnection(String ip) {
		TypedQuery<Schedule> query = em.createQuery("Select s from Schedule s where s.connection.ip = :ip", Schedule.class);
		query.setParameter("ip", ip);
		return query.getResultList();
	}

}
