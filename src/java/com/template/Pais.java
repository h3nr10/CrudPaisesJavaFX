package com.template;

public class Pais {

    private Long id;
    private String nome;
    private String sigla;
    private String capital;
    private Double area;
    private Double pib;
    private Integer populacao;
    private Double militar;

    public Pais() {
    }

    public Pais(Long id, String nome, String sigla,
                String capital, Double area,
                Double pib, Integer populacao,
                Double militar) {

        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.capital = capital;
        this.area = area;
        this.pib = pib;
        this.populacao = populacao;
        this.militar = militar;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public Double getPib() { return pib; }
    public void setPib(Double pib) { this.pib = pib; }

    public Integer getPopulacao() { return populacao; }
    public void setPopulacao(Integer populacao) {
        this.populacao = populacao;
    }

    public Double getMilitar() { return militar; }
    public void setMilitar(Double militar) {
        this.militar = militar;
    }
}