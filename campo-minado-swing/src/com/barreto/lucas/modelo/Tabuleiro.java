package com.barreto.lucas.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Tabuleiro implements CampoObservador {
	
			private final List<Campo> campos = new ArrayList<>();
			private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
			
			private final int linhas;
			private final int colunas;
			private final int minas;
			
			public Tabuleiro(int a, int b, int c){
				this.linhas = a;
				this.colunas = b;
				this.minas = c;
				
				gerarCampos();
				sortearMinas();
				associarVizinho(); 
			}
			
			public void registrarObservador(Consumer<ResultadoEvento> observador) {
				observadores.add(observador);
			}
			
			private void notificarObservadores(boolean resultado) {
				observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
			}
			
			private void sortearMinas() {
				long minasArmadas = 0;
				Predicate<Campo> minado =  c-> c.isMinado();
				
				do {
					minasArmadas = campos.stream().filter(minado).count();
					int aleatorio = (int)(Math.random() * campos.size());
					campos.get(aleatorio).minar();
				} while(minasArmadas < minas);
				
			}
			
			private void gerarCampos() {
				for(int linha = 0; linha < linhas; linha++) {
					for(int coluna = 0; coluna<colunas; coluna++) {
						Campo campo = new Campo(linha,coluna);
						campo.registrarObservador(this);						
						campos.add(campo);
						
					}
				}
			}
			
			private void associarVizinho() {
				for(Campo c1 : campos) {
					for(Campo c2 : campos) {
						c1.adicionarVizinho(c2);
					}
				}
				
			}
			
			public void reiniciar() {
				campos.stream().forEach(c ->c.reiniciar());
				sortearMinas();
			}
			
			public boolean ObjetivoAlcancado() {
				return campos.stream().allMatch(c -> c.objetivoAlcancado());
			}
			
			public void abrirCampo(int linha, int coluna) {
					campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrir());
					
					
				
			}
			
			public void alternarMarcacao(int linha, int coluna){
				campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.alternarMarcacao());
			}

			
			public void eventoOcorreu(Campo campo, CampoEvento evento) {			
				if(evento == CampoEvento.EXPLODIR) {
					mostrarMinas();
					notificarObservadores(false);
				} else if(ObjetivoAlcancado()){
					notificarObservadores(true);
				}
				
			}
			
			private void mostrarMinas() {
				campos.stream().filter(c -> c.isMinado()).filter(c -> !c.isMarcado()).forEach(c -> c.setAberto(true));
			}

			public int getLinhas() {
				return linhas;
			}

			public int getColunas() {
				return colunas;
			}

			public int getMinas() {
				return minas;
			}
			
			public void paraCadaCampo(Consumer<Campo> funcao) {
				campos.forEach(funcao);
			}
			
			 

}
