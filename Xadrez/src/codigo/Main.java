package codigo;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		String nomeJogadorBrancas = "";
		String nomeJogadorPretas = "";
		Scanner input = new Scanner(System.in);
		Jogador jogadorBrancas;
		Jogador jogadorPretas;
		Jogo jogo;
		
		System.out.println("Bem vindo ao jogo de Xadrez!");
		System.out.println("Digite o nome do jogador das Brancas."); 
		System.out.println("\"IA\" para Inteligencia Artificial. Qualquer nome não-nulo para humano");
		while (nomeJogadorBrancas.equals(""))
			nomeJogadorBrancas = input.nextLine();
		if(nomeJogadorBrancas.equals("IA")) 
			jogadorBrancas = new IA_v0(false, true, nomeJogadorBrancas);
		else
			jogadorBrancas = new Jogador(true, true, nomeJogadorBrancas);
		
		System.out.println("Digite o nome do jogador das Pretas."); 
		System.out.println("\"IA\" para Inteligencia Artificial. Qualquer nome não-nulo para humano");
		while (nomeJogadorPretas.equals(""))
			nomeJogadorPretas = input.nextLine();
		if(nomeJogadorPretas.equals("IA")) 
			jogadorPretas = new IA_v0(false, false, nomeJogadorPretas);
		else
			jogadorPretas = new Jogador(true, false, nomeJogadorPretas);
			
		//Jogo jogo = new Jogo();
		jogo = new Jogo(jogadorBrancas, jogadorPretas);
		jogo.jogar();
	}
}
