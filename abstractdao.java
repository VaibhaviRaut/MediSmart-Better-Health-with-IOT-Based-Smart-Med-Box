package mediSmart.Dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public class abstractdao implements DataAccess {
  
	 private static SessionFactory factory;
	 
	 public abstractdao() {
		  setFactory(getFactory());
	 }

	 
	 public static SessionFactory getFactory()
	 {
		 if(abstractdao.factory!=null)
		 {
			 return factory;
		 }
		 else
		 {
			 return SessionFactorys.getSessionFactory();
		 }
	 }
	 
	 public static void setFactory(SessionFactory factory){
		 abstractdao.factory = factory ;
	 }
	 
	@Override
	public String add(DataInterface dataInterface) {
		Session session = factory.openSession();
		Transaction tx =null;
		String result = "error";
		try{
			tx= session.beginTransaction();
			
			//session.persist(dataInterface);
			session.save(dataInterface);
			tx.commit();
			result = "Added";
		}catch (HibernateException ex){
			if(tx!=null)
				tx.rollback();
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return result;
			}

	@Override
	public String delete(DataInterface dataInterface) {
		Session session = factory.openSession();
		Transaction tx= null;
		String result = "error";
        
		try{
			 tx = session.beginTransaction();
			  session.delete(dataInterface);
			  tx.commit();
			  result = "Deleted";
		}catch (HibernateException ex){
			if(tx!=null) tx.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		return result;
	}

	@Override
	public String update(DataInterface dataInterface) {
		Session session = factory.openSession();
		Transaction tx = null;
		String result = "error";
		
		 try{
			  tx = session.beginTransaction();
			  session.update(dataInterface);
			  tx.commit();
			  result = "Updated";
		 }catch(HibernateException ex){
			 if(tx!=null) tx.rollback();
			 ex.printStackTrace();
		 }finally{
			 session.close();
		 }
		
		return result;
	}
	
	
	public String update(String hSql) {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		Transaction tx = null;
		String result = "error";
		try {
			tx = session.beginTransaction();
			Query q=session.createQuery(hSql);
			q.executeUpdate();
			tx.commit();
			result = "updated";
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public Object getById(String colName, String value, Class<?> da) {
		Session session = factory.openSession();
		Transaction tx = null;
		
		
		try{
			tx = session.beginTransaction();
			Criteria criteriaclasses = session.createCriteria(da).
					add(Restrictions.eq(colName, value));
			return criteriaclasses.uniqueResult();		
		}catch(HibernateException ex){
			if(tx!=null) tx.rollback();
			ex.printStackTrace();
		}finally{
			tx.commit();
			session.close();
		}
		
		return null;
	}

	@Override
	public Object getById(String colName, long value, Class<?> da) {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
		    Criteria criteriaclasses = session.createCriteria(da).
		    		add(Restrictions.eq(colName, value));
		    return criteriaclasses.uniqueResult();
		    
		}catch(HibernateException ex){
			if(tx!=null) tx.rollback();
			ex.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}

	@Override
	public Object getById(String colName,int value, Class<?> da) {
		Session session = factory.openSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
		    Criteria criteriaclasses = session.createCriteria(da).
		    		add(Restrictions.eq(colName, value));
		    return criteriaclasses.uniqueResult();
		    
		}catch(HibernateException ex){
			if(tx!=null) tx.rollback();
			ex.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataInterface> listByQuery(String hSql) {
		
		Session session = factory.openSession();
	    Transaction tx = null;
	    List<?> dataInterfaceList=null;
	    try{
	       tx = session.beginTransaction();
		   dataInterfaceList = session.createQuery(hSql).list(); 
	       tx.commit();
	       
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	       
	     }finally {
	        session.close();
	     }
		return (List<DataInterface>)dataInterfaceList;
	}
	public String addByCriteria(HashMap<String , String> map,DataInterface dataInterface, Class<?> da) {
		Session session = factory.openSession();
		Transaction tx = null;
		String result = "error";
		try{
			tx = session.beginTransaction();
			int flag = 0;
			if(map.size()>0)
			{
				Criteria criteriaclasses = createCriteriaQuery(session, map, da);
				if(criteriaclasses.uniqueResult() != null)
					flag = 1;
			}if(flag==0){
				session.save(dataInterface);
				tx.commit();
				result = "Added";
			}else
			{
				result ="Exist";
			}
		}catch(HibernateException ex){
			if(tx!=null) tx.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		return result;
	}
	
	public String addByCriteria1(HashMap map,DataInterface dataInterface, Class<?> da) {
		Session session = factory.openSession();
		Transaction tx = null;
		String result = "error";
		try{
			tx = session.beginTransaction();
			int flag = 0;
			if(map.size()>0)
			{
				Criteria criteriaclasses = createCriteriaQuery(session, map, da);
				if(criteriaclasses.uniqueResult() != null)
					flag = 1;
			}if(flag==0){
				session.save(dataInterface);
				tx.commit();
				result = "Added";
			}else
			{
				result ="Exist";
			}
		}catch(HibernateException ex){
			if(tx!=null) tx.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		return result;
	}
	
	
	
	public Object authenticate(HashMap<String , String> map,Class<?> da){
		Session session =factory.openSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			if(map.size()>0){
				Criteria criteriaclasses = createCriteriaQuery(session,map, da);
				return criteriaclasses.uniqueResult();
			}
		}catch(HibernateException ex){
			if(tx!=null) tx.rollback();
		}finally{
			session.close();
			
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Criteria createCriteriaQuery(Session session, HashMap<String , String>mp,Class<?> da){
		
		 Iterator<Entry<String, String>> it = mp.entrySet().iterator();
		 Criteria criteriaclasses = session.createCriteria(da);
		 while (it.hasNext()){
			 Map.Entry pair = (Map.Entry)it.next();
			 System.out.println(pair.getKey() + " = " + pair.getValue());
			 it.remove();
			 criteriaclasses.add(Restrictions.eq(pair.getKey().toString(), pair.getValue()));
		 }
		
		return criteriaclasses;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static void main(String args[]){
		abstractdao abd=new abstractdao();
		List<DataInterface> datalist;
	
		
		 String query = " FROM User";
		 datalist = abd.listByQuery(query);
		 Iterator it= datalist.iterator();
		 while(it.hasNext()){
			 System.out.println("hdasjh");
 		 }
	}
}
