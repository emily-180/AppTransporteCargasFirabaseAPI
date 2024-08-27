package br.edu.ifsuldeminas.mch.apptransportecargas.model;

import com.google.gson.annotations.SerializedName;

public class CepResponse {

    @SerializedName("cep")
    private String cep;

    @SerializedName("logradouro")
    private String logradouro;

    @SerializedName("complemento")
    private String complemento;

    @SerializedName("bairro")
    private String bairro;

    @SerializedName("localidade")
    private String localidade;

    @SerializedName("uf")
    private String uf;

    @SerializedName("ibge")
    private String ibge;

    @SerializedName("gia")
    private String gia;

    @SerializedName("ddd")
    private String ddd;

    @SerializedName("siafi")
    private String siafi;

    public String getLogradouro() {
        return logradouro;
    }


    public String getBairro() {
        return bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getUf() {
        return uf;
    }

}
