package it.polito.tdp.yelp.model;

public class Adiacenza {

	private Business business1;
	private Business business2;
	private Double peso;
	public Adiacenza(Business business1, Business business2, Double peso) {
		super();
		this.business1 = business1;
		this.business2 = business2;
		this.peso = peso;
	}
	public Business getBusiness1() {
		return business1;
	}
	public void setBusiness1(Business business1) {
		this.business1 = business1;
	}
	public Business getBusiness2() {
		return business2;
	}
	public void setBusiness2(Business business2) {
		this.business2 = business2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [business1=" + business1 + ", business2=" + business2 + ", peso=" + peso + "]";
	}


}
