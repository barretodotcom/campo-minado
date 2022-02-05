package com.barreto.lucas.visao;

import javax.swing.JFrame;

import com.barreto.lucas.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends  JFrame{

			 public static void main(String[] args) {
				
				 new TelaPrincipal();
				 
				 
			}
			 
			 public TelaPrincipal(){
				 
				 Tabuleiro tabuleiro = new Tabuleiro(16,30,50);
				 add(new PainelTabuleiro(tabuleiro));
				 setVisible(true);
				 setSize(690,438);
				 setTitle("Campo Minado Lane");
				 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				 setLocationRelativeTo(null);;
				 
			 }
			
}
