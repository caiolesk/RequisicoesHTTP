package com.whatsapp.caiocavallari.requisicoeshttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whatsapp.caiocavallari.requisicoeshttp.api.CEPService;
import com.whatsapp.caiocavallari.requisicoeshttp.api.DataService;
import com.whatsapp.caiocavallari.requisicoeshttp.model.CEP;
import com.whatsapp.caiocavallari.requisicoeshttp.model.Foto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button btnRecuperar;
    private TextView textViewResultado;
    private Retrofit retrofit;
    private List<Foto> listaFotos = new ArrayList<>();
    private EditText editTextCep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                //.baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnRecuperar = findViewById(R.id.btnRecuperar);
        textViewResultado = findViewById(R.id.textViewResultado);
        editTextCep = findViewById(R.id.editTCep);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarCepRetrofit();
                //recuperarListaRetrofit();
                /*
                MyTask task = new MyTask();
                String urlApi = "https://blockchain.info/ticker";
                String urlCep = "https://viacep.com.br/ws/14160810/json/";
                task.execute(urlApi);
                */

            }
        });
    }

    private void recuperarListaRetrofit(){
        DataService service = retrofit.create(DataService.class);
        Call<List<Foto>> call = service.recuperarFotos();

        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if(response.isSuccessful()){
                    listaFotos = response.body();

                    for (int i=0; i<listaFotos.size();i++){
                        Foto foto = listaFotos.get(i);
                        Log.d("resultado", "resultado: " + foto.getId() + "/" + foto.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {

            }
        });
    }



    private void recuperarCepRetrofit(){
        CEPService cepService = retrofit.create(CEPService.class);
        Call<CEP> call = cepService.recuperarCEP(editTextCep.getText().toString());
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()){
                    CEP cep = response.body();
                    textViewResultado.setText(cep.getLogradouro()+"/"+cep.getBairro());
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });
    }






    class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                 inputStream = conexao.getInputStream();

                 inputStreamReader = new InputStreamReader(inputStream);

                 BufferedReader reader = new BufferedReader(inputStreamReader);
                 buffer = new StringBuffer();
                 String linha = "";
                 while ((linha = reader.readLine()) != null){
                     buffer.append(linha);
                 }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String logradouro= null;
            String cep= null;
            String complemento= null;
            String uf= null;

            String objtoReal = null;
            String valorMoeda = null;
            String simbolo = null;

            try {
                JSONObject jsonObject = new JSONObject(s);
                objtoReal = jsonObject.getString("BRL");

                JSONObject jsonObjectReal = new JSONObject(objtoReal);
                valorMoeda = jsonObjectReal.getString("last");
                simbolo = jsonObjectReal.getString("symbol");
                /*
                JSONObject jsonObject = new JSONObject(s);
                logradouro = jsonObject.getString("logradouro");
                cep = jsonObject.getString("cep");
                complemento = jsonObject.getString("complemento");
                uf = jsonObject.getString("uf");
                */
            } catch (JSONException e) {
                e.printStackTrace();
            }

            textViewResultado.setText(simbolo +" "+ valorMoeda);
            //textViewResultado.setText(s);
           // textViewResultado.setText(logradouro +"/"+cep+"/"+complemento+"/"+uf);
        }


    }
}
