package br.leg.alrr.common.business;

/**
 *
 * @author Heliton
 */
public class Endereco {
    
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;

    public Endereco() {       
    }
    
    public Endereco(String cep, String bairro, String logradouro, String numero) {
        this.cep = cep;
        this.bairro = bairro;
        this.logradouro = logradouro;
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    
}
