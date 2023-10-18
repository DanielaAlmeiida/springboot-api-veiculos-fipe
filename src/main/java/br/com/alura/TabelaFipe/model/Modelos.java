package br.com.alura.TabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

//@JsonIgnore Properties (ignore Unknown = true)
public record Modelos (List<DadosVeiculo> modelos) {

}
