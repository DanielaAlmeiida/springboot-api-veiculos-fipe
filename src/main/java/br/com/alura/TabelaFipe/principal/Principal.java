package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosVeiculo;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoAPI;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    ConsumoAPI consumoAPI = new ConsumoAPI();
    ConverteDados conversor = new ConverteDados();

    private String definirEndpointMarcasVeiculo() {
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
        return endereco;
    }

    private String getEnderecoMarca(String endereco) {
        System.out.println("Informe o código da marca para consulta:");
        var codigoMarca = leitura.nextLine();

        endereco =  endereco + "/" + codigoMarca + "/modelos";
        return endereco;
    }

    private List<DadosVeiculo> exibirModelosEFiltrar(Modelos listaModelo) {
        System.out.println("\nModelos dessa marca: ");
        listaModelo.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);



        System.out.println("\nDigite um trecho do nome do veículo a ser buscado: ");
        var nomeModelo = leitura.nextLine().toLowerCase();

        List<DadosVeiculo> modelosFiltrados = listaModelo.modelos()
                .stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeModelo))
                .collect(Collectors.toList());

        return modelosFiltrados;
    }

    private String getEnderecoModelo(String endereco) {
        System.out.println("\nDigite o código do modelo para buscar os valores de avaliação. ");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" +  codigoModelo + "/anos";

        return endereco;
    }

    private List<Veiculo> obterAvalicoesVeiculos(String endereco, List<DadosVeiculo> anos) {
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            var jsonAnos = consumoAPI.obterDados(enderecoAnos);

            Veiculo veiculo = conversor.obterDados(jsonAnos, Veiculo.class);
            veiculos.add(veiculo);
        }

        return veiculos;
    }

    public void exibeMenu() {

        String endereco;


        endereco = definirEndpointMarcasVeiculo();
        var json = consumoAPI.obterDados(endereco);

        var listaVeiculos = conversor.obterLista(json, DadosVeiculo.class);
        listaVeiculos.forEach(System.out::println);


        endereco = getEnderecoMarca(endereco);
        var jsonModelo = consumoAPI.obterDados(endereco);

        var listaModelo = conversor.obterDados(jsonModelo, Modelos.class);
        List<DadosVeiculo> listaModeloFiltrado = exibirModelosEFiltrar(listaModelo);
        System.out.println("\nModelos filtrados: ");
        listaModeloFiltrado.forEach(System.out::println);


        endereco = getEnderecoModelo(endereco);
        var jsonMarcas = consumoAPI.obterDados(endereco);

        List<DadosVeiculo> anos = conversor.obterLista(jsonMarcas, DadosVeiculo.class);
        List<Veiculo> veiculos = obterAvalicoesVeiculos(endereco, anos);

        System.out.println("\nTodos os veículos filtrados com avaliação por ano: ");
        veiculos.forEach(System.out::println);

    }

}
