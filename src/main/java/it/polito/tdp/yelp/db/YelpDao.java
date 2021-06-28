package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public void getAllBusiness(Map<String,Business> idMap){
		String sql = "SELECT * FROM Business";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getString("business_id"))) {
					Business business = new Business(res.getString("business_id"), 
							res.getString("full_address"),
							res.getString("active"),
							res.getString("categories"),
							res.getString("city"),
							res.getInt("review_count"),
							res.getString("business_name"),
							res.getString("neighborhoods"),
							res.getDouble("latitude"),
							res.getDouble("longitude"),
							res.getString("state"),
							res.getDouble("stars"));
					idMap.put(res.getString("business_id"), business);
				}
			}
			res.close();
			st.close();
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
		
		}
	}

	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));

				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Integer> getAnni(){
		String sql ="SELECT distinct YEAR(r.review_date) as anno "
				+ "FROM reviews r "
				+ "GROUP BY YEAR(r.review_date)";

		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();
		try {

			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getInt("anno"));
			}
			conn.close();

		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}

		return result;

	}
	
	public List<String> getCities(){
		String sql ="SELECT DISTINCT city FROM business ORDER BY city";
		List<String> result = new LinkedList<String>();
		Connection conn = DBConnect.getConnection();
		try {

			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("city"));
			}
			conn.close();

		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}

		return result;

	}
	
	public List<Business> getVertici(Map<String,Business> idMap, int anno, String città){
		String sql ="SELECT b.business_id AS businessId "
				+ "FROM business b, reviews r "
				+ "WHERE b.business_id=r.business_id "
				+ "AND YEAR(r.review_date)=? "
				+ "AND b.city = ? "
				+ "GROUP BY b.business_id "
				+ "HAVING COUNT(user_id)>0";
		List<Business> result = new LinkedList<>();
		Connection conn = DBConnect.getConnection();
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, città);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
			if(idMap.get(res.getString("businessId"))!=null)
			result.add(idMap.get(res.getString("businessId")));
			}
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public List<Adiacenza> getArchi(Map<String, Business> idMap, int anno, String città){
		String sql ="SELECT r1.business_id as id1, r2.business_id as id2, (AVG (r1.stars)-AVG (r2.stars)) AS peso "
				+ "FROM reviews r1, reviews r2, business b1, business b2 "
				+ "WHERE YEAR(r1.review_date)=? AND YEAR(r2.review_date)=YEAR(r1.review_date) "
				+ "AND b1.business_id = r1.business_id AND b2.business_id = r2.business_id "
				+ "AND b1.city=b2.city AND b1.city=? " 
				+ "AND r1.business_id>r2.business_id "
				+ "GROUP BY r1.business_id,r2.business_id "
				+ "HAVING peso<>0";
		
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2,città);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.get(res.getString("id1"))!=null && idMap.get(res.getString("id2"))!=null)
				result.add(new Adiacenza( idMap.get(res.getString("id1")), idMap.get(res.getString("id2")), res.getDouble("peso")));
			}
				
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

}
