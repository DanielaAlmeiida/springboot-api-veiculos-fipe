package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.service.ConsumoAPI;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.List;
import java.util.Scanner;

public class Principal {
    Scanner leitura = new Scanner(System.in);

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        System.out.println("""
                ---------------OPÇÕES-----------------
                Carro
                Moto 
                Caminhão
                --------------------------------------
                Digite o veículo que queira consultar:
                """);
        var opcaoVeiculo = leitura.nextLine().toLowerCase();


        String endereco;
        if (opcaoVeiculo.contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcaoVeiculo.contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumoAPI.obterDados(endereco);
        System.out.println(json);

        List<DadosVeiculo> listaVeiculos = conversor.obterLista(json, DadosVeiculo.class);
        listaVeiculos.forEach(System.out::println);






    }
}
