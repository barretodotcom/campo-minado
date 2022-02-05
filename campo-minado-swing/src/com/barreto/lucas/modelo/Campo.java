package com.barreto.lucas.modelo;

import java.util.ArrayList;
import java.util.List;

import com.barreto.lucas.excecao.ExplosaoException;

public class Campo {
	
			 private final int linha;
			 private final int coluna;
			 
			 private boolean aberto = false;
			 private boolean minado = false;
			 private boolean marcado = false;
			 
			 private List<Campo> vizinhos = new ArrayList<>();
			 private List<CampoObservador> observadores = new ArrayList<>();
			 
			 public Campo(int linha, int coluna) {
					
					this.linha = linha;
					this.coluna = coluna;
			}
			 
			 public void registrarObservador(CampoObservador observador) {
				 observadores.add(observador);
			 }
			 
			 private void notificarObservadores(CampoEvento evento) {
				 observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
				 
			 }
			 
			 public boolean isMarcado() {
				 return marcado;
			 }
			 
			 public boolean isMinado() {
				 return minado;
			 }
			 
			 boolean adicionarVizinho(Campo vizinho) {
				 boolean linhaDiferente = this.linha != vizinho.linha;
				 boolean colunaDiferente = this.coluna != vizinho.coluna;
				 boolean diagonal = linhaDiferente && colunaDiferente;
				 
				 int deltaLinha = Math.abs(this.linha - vizinho.linha);
				 int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
				 int deltaGeral = deltaLinha + deltaColuna;
				 
				 if(deltaGeral == 1 && !diagonal) {
					 vizinhos.add(vizinho);
					 return true;
				 } else if(deltaGeral == 2 & diagonal) {
					 vizinhos.add(vizinho);
					 return true;
				 } else {
					 return false;
				 }
				 
			 }
			 
			 public void alternarMarcacao() {
				 if(!aberto) {
					marcado = !marcado;
					
					if(marcado) {
						notificarObservadores(CampoEvento.MARCAR);
						
					} else {
						notificarObservadores(CampoEvento.DESMARCAR);
					}
				 }
			 }
			 
			 public boolean abrir() {
				 if(!aberto && !marcado) {
					 
					 if(minado) {
						notificarObservadores(CampoEvento.EXPLODIR);
						return true;
					 }
					 
					 setAberto(true);
					 
					 if(vizinhancaSegura()) {
						 vizinhos.forEach(v -> v.abrir());
					 }
					 
					 return true;
					 
				 }
				 return false;
			 }
			 
			 public boolean vizinhancaSegura()  {
				 return vizinhos.stream().noneMatch(c -> c.minado);
				
			 }
			 
			 void minar() {
				 minado = true;
			 }
			 
			 public boolean isAberto() {
				 return aberto;
			 }
			 
			 public int minasNaVizinhanca() {
				 return (int) vizinhos.stream().filter(v -> v.minado).count();
			 }
			 
			 boolean objetivoAlcancado() {
				 boolean desvendado = !minado && aberto;
				 boolean protegido = minado && marcado;
				 return desvendado || protegido;
			 }
			 
			 void reiniciar() {
				 this.aberto = false;
				 this.minado = false;
				 this.marcado = false;
				 notificarObservadores(CampoEvento.REINICIAR);
			 }
			 
			 
			 
			 
			 void setAberto(boolean aberto) {
				this.aberto = aberto;
				if(aberto) {
					notificarObservadores(CampoEvento.ABRIR);
				}
			}

			public int getLinha() {
				 return linha;
			 }
			 
			 public int getColuna() {
				 return coluna;
			 }
}
