package br.edu.ifsuldeminas.mch.apptransportecargas;

import br.edu.ifsuldeminas.mch.apptransportecargas.model.CepResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {
    @GET("{cep}/json/")
    Call<CepResponse> getCepDetails(@Path("cep") String cep);
}
