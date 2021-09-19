package br.leg.alrr.common.util;




public class Mensagem {
	
	private static final long serialVersionUID = 1L;

	
    private Long id;
	private String destino;
	private String titulo;
	private String mensagem;
	 
	
	public Mensagem() {
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDestino() {
	 return destino;
	 }
	 
	public void setDestino(String destino) {
	 this.destino = destino;
	 }
	 
	public String getMensagem() {
	 return mensagem;
	 }
	 
	public void setMensagem(String mensagem) {
	 this.mensagem = mensagem;
	 }
	 
	public String getTitulo() {
	 return titulo;
	 }
	 
	public void setTitulo(String titulo) {
	 this.titulo = titulo;
	 }

}
