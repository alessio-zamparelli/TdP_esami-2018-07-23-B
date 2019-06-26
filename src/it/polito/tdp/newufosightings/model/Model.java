package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO.Border;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	private NewUfoSightingsDAO dao;
	private SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	private Map<String, State> stateIdMap;
	private Simulatore sim;

	public Model() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao = new NewUfoSightingsDAO();
	}

	public void creaGrafo(int anno, int maxDays) {
		this.stateIdMap = dao.loadAllStates().stream().collect(Collectors.toMap(State::getId, a -> a));
		Graphs.addAllVertices(this.grafo, this.stateIdMap.values());
		List<Border> borders = dao.loadAllBorders(stateIdMap);
		for (Border b : borders) {
			this.grafo.addEdge(b.getS1(), b.getS2());
//			System.out.println("Calcolo il peso per la coppia: " + b.getS1().getId() + " - " + b.getS2().getId());
			this.grafo.setEdgeWeight(this.grafo.getEdge(b.getS1(), b.getS2()),
					dao.getWeight(anno, maxDays, b.getS1(), b.getS2()));
		}
	}

	public Map<State, Double> getListaPesiPerStato() {
		Map<State, Double> res = new HashMap<>();
		for (State state : this.stateIdMap.values()) {
			double peso = this.grafo.outgoingEdgesOf(state).stream().mapToDouble(a -> this.grafo.getEdgeWeight(a))
					.sum();
			res.put(state, peso);
		}
		return res;
	}

	public void simula(int T1, int T2, int anno) {
		sim = new Simulatore();
		sim.init(T1, T2, dao.loadAllSightings(anno, stateIdMap), new ArrayList<>(stateIdMap.values()));
		sim.run();
	}
	public Map<State, Integer> getRisultatiSimulazione() {
		return sim.getCountEmergenze();
	}
	
}
