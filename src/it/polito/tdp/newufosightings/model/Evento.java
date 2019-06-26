package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	public enum TipoEvento {
		INCREMENTA_DEFCON, AVVISTAMENTO, FINE_EMERGENZA
	}

	private LocalDateTime data;
	private State stato;
	private TipoEvento tipoEvento;
	private Sighting sighting;

	public Evento(LocalDateTime data, State stato, TipoEvento tipoEvento, Sighting sighting) {
		this.data = data;
		this.stato = stato;
		this.tipoEvento = tipoEvento;
		this.sighting = sighting;
	}

	public LocalDateTime getData() {
		return data;
	}

	public State getStato() {
		return stato;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	
	public Sighting getSighting() {
		return sighting;
	}

	@Override
	public int compareTo(Evento o) {
		return this.data.compareTo(o.data);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Evento [data=");
		builder.append(data);
		builder.append(", stato=");
		builder.append(stato);
		builder.append(", tipoEvento=");
		builder.append(tipoEvento);
		builder.append(", sighting=");
		builder.append(sighting);
		builder.append("]");
		return builder.toString();
	}

}
