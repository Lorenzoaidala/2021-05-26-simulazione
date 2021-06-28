package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {

	private YelpDao dao;
	private Map<String, Business> idMap;
	private Graph<Business,DefaultWeightedEdge> grafo;
	
	private List<Business> bestPercorso;

	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<>();
		dao.getAllBusiness(idMap);
	}

	public List<Integer> getAnni(){
		return dao.getAnni();
	}
	public List<String> getCittà(){
		Collections.sort(dao.getCities());
		return dao.getCities();
	}

	public void creaGrafo(int anno, String città) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap, anno, città));

		for(Adiacenza a : dao.getArchi(idMap, anno, città)) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, a.getBusiness2(), a.getBusiness1(), a.getPeso());
			} else {
				Graphs.addEdge(this.grafo, a.getBusiness1(), a.getBusiness2(), -1*a.getPeso());
			}
		}


	}
	
	public Business getLocaleMigliore() {
		double result = 0;
		Business migliore = null;
		if(this.grafo!=null) {
			for(Business b:this.grafo.vertexSet()) {
				double sommaEntrante=0;
				for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(b)) {
					sommaEntrante+=this.grafo.getEdgeWeight(e);
				}
				double sommaUscente=0;
				for(DefaultWeightedEdge e:this.grafo.outgoingEdgesOf(b)) {
					sommaUscente+=this.grafo.getEdgeWeight(e);
				}
				if((sommaEntrante-sommaUscente)>result) {
					result = (sommaEntrante-sommaUscente);
					migliore = idMap.get(b.getBusinessId());
				}
			}
		}
		return migliore;
	}
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Business> trovaPercorso(Business partenza, Business arrivo, double soglia){
		this.bestPercorso=null;
		List<Business> parziale = new LinkedList<Business>();
		parziale.add(partenza);
		cerca(parziale, arrivo, 1, soglia);
		return this.bestPercorso;
	}
	
	public void cerca(List<Business> parziale, Business arrivo, int livello, double soglia) {
		//caso terminale
		Business ultimo = parziale.get(parziale.size()-1);
		if(ultimo.equals(arrivo)) {
			if(this.bestPercorso==null) {
				this.bestPercorso = new ArrayList<Business>(parziale);
				return;
			} else if(parziale.size()<this.bestPercorso.size()) {
				this.bestPercorso = new ArrayList<Business>(parziale);
				return;
			}
		} //trovare i percorsi
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(ultimo)) {
			if(this.grafo.getEdgeWeight(e)>soglia) {
				
				Business prossimo = Graphs.getOppositeVertex(this.grafo, e, ultimo);
				
				if(!parziale.contains(prossimo)) {
					parziale.add(prossimo);
					cerca(parziale,arrivo,livello+1,soglia);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}
	
	public List<Business> getLocali(){
		Set<Business> provvisorio= this.grafo.vertexSet();
		List<Business> result = new ArrayList<Business>(provvisorio);
		Collections.sort(result);
		return result;
	}
	

}
