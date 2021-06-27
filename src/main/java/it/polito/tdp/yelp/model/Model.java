package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Map<String, Business> idMap;
	private Graph<Business,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<>();
		dao.getAllBusiness(idMap);
	}
	
	public List<Integer> getAnni(){
		return dao.getAnni();
	}
	public List<String> getCittà(){
		return dao.getCities();
	}
	
	public void creaGrafo(int anno, String città) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		
	}
	
}
