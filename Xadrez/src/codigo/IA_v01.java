package codigo;
import java.util.*;

/**
 * @author Humberto Mattiello
 * @date June 2022
 * 
 * IA v0.1 - A Básica
 *
 *  TODO: Implementar finais específicos
 *  TODO: Melhorar heuristica dos finais
 *  TODO: Atualmente só coroa para dama
 *  TODO: Melhorar calculo da defesa para incluir suporte (por exemplo, um canhao de duas torres uma na frente da outra conta como uma defesa só)
 *  TODO: Esta identificando alguns xeque-mates incorretamente(por exemplo, no caso em que poderia comer com um peão a peça que está atacando)
 */
public class IA_v01 extends Jogador {
	private int contagemInternaDeLances;
	private int prioridadeMaximaEncontrada;
	private int vantagemMaterial;
	private int tipoDeFinal; // 0 = Genérico;
							 // 1 = Dama e Rei x Rei;
						     // 2 = Torre e Rei x Rei;
						     // 3 = Par de Bispos x Rei;
						     // 4 = Bispo e Cavalo x Rei;
							 // 5 = Rei e Peão x Rei;
	
	//
	//	Construtores
	//
	
	public IA_v01() {
		super();
		this.prioridadeMaximaEncontrada = 0;
		this.contagemInternaDeLances = 0;
		this.vantagemMaterial = 0;
		this.tipoDeFinal = 0;
	}

	public IA_v01(boolean isHumano, boolean isBrancas) {
		super(isHumano, isBrancas);
		this.prioridadeMaximaEncontrada = 0;
		this.contagemInternaDeLances = 0;
		this.vantagemMaterial = 0;
		this.tipoDeFinal = 0;
	}

	public IA_v01(boolean isHumano, boolean isBrancas, String nome) {
		super(isHumano, isBrancas, nome);
		this.prioridadeMaximaEncontrada = 0;
		this.contagemInternaDeLances = 0;
		this.vantagemMaterial = 0;
		this.tipoDeFinal = 0;
	}

	
	//
	// Lógica
	//	
	
	@Override
	public int coletarJogada(Tabuleiro tabuleiro, Jogada jogada, Jogada ultimaJogada) {
		// Verifica pedido de empate
		if(tabuleiro.casas[1][1] == 'E') {
			return this.responderPropostaDeEmpate() ? 4 : 5;
		}
		else { // Jogada normal
			Jogada jogadaAux = new Jogada();
			jogadaAux = this.pensar(tabuleiro, ultimaJogada);
			jogada.setCasaOrigem(jogadaAux.getCasaOrigem());
			jogada.setCasaDestino(jogadaAux.getCasaDestino());
			jogada.setDeslocamentoX(jogadaAux.getDeslocamentoX());
			jogada.setDeslocamentoY(jogadaAux.getDeslocamentoY());
			jogada.setValida(true); // Validade verificada durante formação da jogada 
			return 1;
		}
	}

	/**
	 * Base do algoritmo v1:
	 * 	Primeiro verifica se pode dar xeque mate.
	 *	Se não puder, estabelece as prioridades material e posicional.
	 *	Atribui pesos para cada critério, e monta a prioridade final de cada lance possível.
	 *  Escolhe o lance com maior prioridade, se houver empate, escolhe um dos empatados aleatoriamente.
	 */
	@Override
	protected Jogada pensar(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		ArrayList<Prioridade> listaDeLances = lancesPotenciais(tabuleiro, ultimaJogada);
		int indiceDaJogada = temXequeMate(tabuleiro, ultimaJogada, listaDeLances);
	
		// Verifica se tem xeque mate
		if (indiceDaJogada > 0) {
			return listaDeLances.get(indiceDaJogada);
		}
		
		this.contagemInternaDeLances++;
		
		return lanceComAMaiorPrioridade(listaDeLances, this.prioridadeMaximaEncontrada);		
	}
	
	/**
	 * 
	 * @param tabuleiro
	 * @param ultimaJogada
	 * @param listaDeLances Esse parametro é alterado (reduzido) pelo método!
	 * @return -1 se não tiver xeque mate, índice do lance se tiver.
	 */
	private int temXequeMate(Tabuleiro tabuleiro, Jogada ultimaJogada, ArrayList<Prioridade> listaDeLances) {
		int i = 0;
		PecaRei reiOponente = new PecaRei(this.isBrancas() ? 'r' : 'R');
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		
		// Entre todos os lances, procure algum que de xeque mate.
		for (Jogada jogadaAux : listaDeLances) {			
			// Simula a jogada em um tabuleiro auxiliar
			tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
			
			// Se o lance deixa o rei oponente em xeque
			if (reiOponente.isXequeMate(ultimaJogada, tabuleiroAuxiliar, false)) {
				return i;
			}			

			// Retorna o tabuleiro auxiliar para a posição para a próxima simulação
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();	
			i++;
		}
		
		return -1;
	}

	@Override
	protected ArrayList<Prioridade> lancesPotenciais(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		PecaRei rei = new PecaRei (this.isBrancas() ? 'R' : 'r');
		Casa casaAux = new Casa();
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		Stack<Casa> pecasDisponiveis = new Stack<Casa> ();
		Stack<Casa> destinos = new Stack<Casa> ();
		Heuristica h = new Heuristica(this.isBrancas());
		ArrayList<Prioridade> lancesPotenciais = new ArrayList<Prioridade> ();
		ArrayList<Prioridade> lancesPossiveis = new ArrayList<Prioridade> ();
		
		// Determina a heurística inicial para comparar com as jogadas
		h.atualizaMapas(tabuleiro);
		this.prioridadeMaximaEncontrada = 0;
		this.vantagemMaterial = h.getMaterialTotal() - h.getMaterialOponenteTotal();
		
		// Criar uma pilha com todas as casas que tem pecas da cor do jogador
		pecasDisponiveis = tabuleiro.casasComPecasDestaCor(this.isBrancas());
		
		// Cria uma pilha com todos os movimentos possiveis
		while (!pecasDisponiveis.isEmpty()) {
			casaAux = pecasDisponiveis.pop();
			if(casaAux.getPeca().isRei()) {
				destinos = casaAux.getPeca().movimentosValidos(tabuleiro, casaAux, ultimaJogada, this.isBrancas(), true);
			}
			else {
				destinos = casaAux.getPeca().movimentosValidos(tabuleiro, casaAux, ultimaJogada, this.isBrancas());
			}				
			while (!destinos.isEmpty()) {
				Casa c = destinos.pop();
				//int marca = c.getDefesas();
				Prioridade p = new Prioridade (casaAux, c);
				//marcarLancesEspeciais(p, marca);
				lancesPotenciais.add(p);
			}
		}
		
		// Percorre a lista de movimentos possíveis e remove os que deixariam o rei em xeque
		for (Jogada jogadaAux : lancesPotenciais) {
			// Simula a jogada em um tabuleiro auxiliar
			jogadaAux.setBrancas(this.isBrancas());
			tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
			
			// Verifica se a jogada nao deixa em xeque
			if (!rei.isEmXeque(tabuleiroAuxiliar, false)) {
				int gmi, gmp, gde, mpr = 0; 
				Prioridade p = new Prioridade(jogadaAux.getCasaOrigem(), jogadaAux.getCasaDestino(), tabuleiroAuxiliar);
				
				// Ajusta flags
				p.setBrancas(jogadaAux.isBrancas());
				p.setCoroacao(jogadaAux.isCoroacao());
				p.setCoroada(jogadaAux.getCoroada());
				p.setEnPassant(jogadaAux.isEnPassant());
				p.setRoque(jogadaAux.isRoque());
				p.setRoqueGrande(jogadaAux.isRoqueGrande());
				
				// Ajusta prioridades		
				gmi = h.getMaterialOponenteTotal() - p.heuristicaAposJogada.getMaterialOponenteTotal();
				mpr = h.getMaterialAmeacadoTotal() - p.heuristicaAposJogada.getMaterialAmeacadoTotal();
				mpr += p.heuristicaAposJogada.getMaterialTotal() - h.getMaterialTotal();
				gmp = mpr + gmi;				
				gde = p.heuristicaAposJogada.getDefesaTotal() - h.getDefesaTotal();
				p.setGanhoMaterialImediato(gmi);
				p.setGanhoMaterialPotencial(gmp);
				p.setGanhoEmDesenvolvimento(gde);
				p.setMaterialPreservado(mpr);
				ajustaPrioridadePosicional(tabuleiroAuxiliar, h, p);
				p.calculaPrioridade(100, this.vantagemMaterial, 1);
				if (prioridadeMaximaEncontrada < p.getPrioridade()) {
					prioridadeMaximaEncontrada = p.getPrioridade();
				}
				
				// Adiciona lance na lista de lances
				lancesPossiveis.add(p);
			}
			
			// Retorna o tabuleiro auxiliar para a posição para a próxima simulação
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();			
		}
		
		return lancesPossiveis;		
	}
	
	/**
	 * Este método faz duas coisas: remove todos os elementos menores que a prioridade
	 * indicada da lista de lances, e se sobrar mais de uma, retorna uma delas aleatoriamente.
	 * @param listaDeLances
	 * @return
	 */
	private Prioridade lanceComAMaiorPrioridade(ArrayList<Prioridade> listaDeLances, int prioridade) {
		try {
			for(int i = listaDeLances.size() - 1; i >= 0; i--) {
				if(listaDeLances.get(i).getPrioridade() < prioridade) {
					listaDeLances.remove(i);
				}
			}
			int qtdDeLances = listaDeLances.size();
			if (qtdDeLances > 1) {
				double rdm = Math.nextDown((Math.random() * (double)qtdDeLances));
				int lanceAleatorio = (int)rdm;				
				return listaDeLances.get(lanceAleatorio);
			}
			else {
				return listaDeLances.get(0);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Mecanismo de ganho em desenvolvimento
	 * No começo do jogo, ele prioriza o dominio do cento e o desenvolvimento das pecas menores.
	 * No meio do jogo, ele prioriza desenvolvimento em direção ao rei oponente.
	 * No fim do jogo, ele prioriza avanço de peões distantes do rei oponente.
	 * @param lanceDepois terá seu campo mpr alterado
	 */
	private void ajustaPrioridadePosicional(Tabuleiro tabuleiro, Heuristica antes, Prioridade lanceDepois) {
		int faseDoJogo = 0;
		int tmp = 0; 
		int ganho = lanceDepois.getGanhoEmDesenvolvimento();
		Casa casaAux;
		Casa casaDoRei = encontraReiDoOponente(tabuleiro);
		Stack<Casa> casasAoRedor = casasAoRedorDoReiOponente(casaDoRei);
		
		if (this.contagemInternaDeLances > 8) {
			if (antes.getMaterialOponenteTotal() < 113) {
				faseDoJogo = 2; 
			}
			else {
				faseDoJogo = 1; 
			}
		}
		
		switch (faseDoJogo) {
			case 0: { // Abertura
				ganho = lanceDepois.getGanhoEmDesenvolvimento();
				
				// Nucleo do centro - peso 4
				tmp = 0;
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('d', 4));
				tmp -= antes.getDefesaEm(new Casa('d', 4));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('d', 5));
				tmp -= antes.getDefesaEm(new Casa('d', 5));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('e', 4));
				tmp -= antes.getDefesaEm(new Casa('e', 4));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('e', 5));
				tmp -= antes.getDefesaEm(new Casa('e', 5));
				ganho += tmp * 4;
				
				// Bordas do centro - peso 2
				tmp = 0;
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('c', 3));
				tmp -= antes.getDefesaEm(new Casa('c', 3));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('c', 4));
				tmp -= antes.getDefesaEm(new Casa('c', 4));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('c', 5));
				tmp -= antes.getDefesaEm(new Casa('c', 5));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('c', 6));
				tmp -= antes.getDefesaEm(new Casa('c', 6));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('d', 3));
				tmp -= antes.getDefesaEm(new Casa('d', 3));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('d', 6));
				tmp -= antes.getDefesaEm(new Casa('d', 6));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('e', 3));
				tmp -= antes.getDefesaEm(new Casa('e', 3));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('e', 6));
				tmp -= antes.getDefesaEm(new Casa('e', 6));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('f', 3));
				tmp -= antes.getDefesaEm(new Casa('f', 3));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('f', 4));
				tmp -= antes.getDefesaEm(new Casa('f', 4));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('f', 5));
				tmp -= antes.getDefesaEm(new Casa('f', 5));
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(new Casa('f', 6));
				tmp -= antes.getDefesaEm(new Casa('f', 6));
				ganho += tmp * 2;
				
				// Peca pequena - Adiciona um valor constante
				if (lanceDepois.getCasaOrigem().getPeca().isCavalo() ||
					lanceDepois.getCasaOrigem().getPeca().isBispo() ||	
					lanceDepois.getCasaOrigem().getPeca().isPeao()) {
					ganho += 10;
				}
				else {
					ganho -= 10;
				}
				
				// Roque e roque grande - adiciona prioridade
				if (lanceDepois.isRoque()) {
					ganho += 30;
				}
				if (lanceDepois.isRoqueGrande()) {
					ganho += 5;
				}				
				
				// Randomiza os lances bons acima de um certo valor para ter alguma variabilidade nas escolhas
				if (ganho > 19) {
					ganho = 19;
				}
						
				lanceDepois.setGanhoEmDesenvolvimento(ganho);
				break;
			}
			case 1: { // Meio jogo
				ganho = lanceDepois.getGanhoEmDesenvolvimento();
								
				// Roque e roque grande - adiciona prioridade
				if (lanceDepois.isRoque() || lanceDepois.isRoqueGrande()) {
					ganho += 10;
				}
				
				// Casa do rei
				tmp = 0;
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(casaDoRei);
				tmp -= antes.getDefesaEm(casaDoRei);
				ganho += tmp * 20;
				
				// Casas ao redor do rei
				tmp = 0;
				while (!casasAoRedor.isEmpty()) {
					casaAux = casasAoRedor.pop();
					tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(casaAux);
					tmp -= antes.getDefesaEm(casaAux);
				}
				ganho += tmp * 10;
				
				if (faseDoJogo > 1 && ganho > 30) {
					ganho = 30;
				}
				
				lanceDepois.setGanhoEmDesenvolvimento(ganho);
				break;
			}
			case 2: { // Final
				ganho = lanceDepois.getGanhoEmDesenvolvimento();
				Casa origemDoLance = lanceDepois.getCasaOrigem();
				int distancia = Math.abs(casaDoRei.getCoordenadaX() - origemDoLance.getCoordenadaX());
				
				// Peao - quanto mais distante do rei, melhor
				// e quanto mais próximo de coroar, melhor
				if (lanceDepois.getCasaOrigem().getPeca().isPeao()) {
					ganho += distancia;
					if(this.isBrancas()) {
						ganho += (origemDoLance.getCoordenadaY() - 1);
					}
					else {
						ganho += (8 - origemDoLance.getCoordenadaY());
					}
				}
				
				// Casa do rei
				tmp = 0;
				tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(casaDoRei);
				tmp -= antes.getDefesaEm(casaDoRei);
				ganho += tmp * 2;
				
				// Casas ao redor do rei
				tmp = 0;
				while (!casasAoRedor.isEmpty()) {
					casaAux = casasAoRedor.pop();
					tmp += lanceDepois.heuristicaAposJogada.getDefesaEm(casaAux);
					tmp -= antes.getDefesaEm(casaAux);
				}
				ganho += tmp;

				// Quanto menos peças oponentes houver, melhor mexer o rei
				if (lanceDepois.getCasaOrigem().getPeca().isRei()) {
					if (lanceDepois.heuristicaAposJogada.getMaterialOponenteTotal() - 100 < 5) {
						ganho += 4;
					}
					else if (lanceDepois.heuristicaAposJogada.getMaterialOponenteTotal() - 100 < 10) {
						ganho += 2;
					}
				}			
				
				// Mantem todos os lances bons com a mesma prioridade para aleatoriza-los.
				// Isso permite que a IA nao fique em loop e tente fazer jogadas diferentes.
				// Assim ela pode eventualmente conseguir dar xeque-mate no final.
				if (ganho > 10) {
					ganho = 10;
				}
				
				lanceDepois.setGanhoEmDesenvolvimento(ganho);			
				break;
			}
		}
	}
	
	private Casa encontraReiDoOponente (Tabuleiro t) {
		char x = 'a';
		int y = 1;
		Casa casaDoReiOponente = new Casa();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				x = t.indexParaCoordenadaX(j);
				y = t.indexParaCoordenadaY(i);
				casaDoReiOponente = new Casa(x, y, t.getPecaEm(x, y));
				if (this.isBrancas()) {
					if (casaDoReiOponente.getTipoPeca() == 'r') {
						return casaDoReiOponente;
					}
				}
				else {
					if (casaDoReiOponente.getTipoPeca() == 'R') {
						return casaDoReiOponente;
					}
				}
			}
		}
		
		return casaDoReiOponente;
	}
	
	private Stack<Casa> casasAoRedorDoReiOponente(Casa casaDoRei){
		char x = 'a';
		int y = 1;
		Stack<Casa> pilhaDeCasas = new Stack<Casa> ();
	
		y = casaDoRei.getCoordenadaY();
		x = casaDoRei.getCoordenadaX();
		if (y < 7) {
			// Norte
			pilhaDeCasas.push(new Casa (x, y + 1));
			
			// Nordeste
			if (x < 'h') {
				pilhaDeCasas.push(new Casa ((char)(x + 1), y + 1));
			}
				
			// Noroeste
			if (x > 'a') {
				pilhaDeCasas.push(new Casa ((char)(x - 1), y + 1));
			}
		}
		if (y > 1) {
			// Sul
			pilhaDeCasas.push(new Casa (x, y - 1));
				
			// Sudeste
			if (x < 'h') {
				pilhaDeCasas.push(new Casa ((char)(x + 1), y - 1));
			}
				
			// Sudoeste
			if (x > 'a') {
				pilhaDeCasas.push(new Casa ((char)(x - 1), y - 1));
			}
		}
		// Leste
		if (x < 'h') {
			pilhaDeCasas.push(new Casa ((char)(x + 1), y));
		}		
		// Oeste
		if (x > 'a') {
			pilhaDeCasas.push(new Casa ((char)(x - 1), y));
		}
	
		return pilhaDeCasas;
	}
	
	private int identificaTipoDeFinal(Tabuleiro tabuleiro) {
		int qtdMaterial = tabuleiro.material(this.isBrancas());
		
		// Final genérico
		// Tem mais material do que uma dama,
		// e o oponente ainda tem peça além do rei.
		if(110 < qtdMaterial){
			return 0;
		}		
		int qtdMaterialOponente = tabuleiro.material(!this.isBrancas());
		if (100 < qtdMaterialOponente) {
			return 0;
		}
		
		Stack<Casa> casasComPecas = tabuleiro.casasComPecasDestaCor(this.isBrancas());
		boolean contemBispo = false;
		boolean contemCavalo = false;
		while(!casasComPecas.isEmpty()) {
			char pecaAux = casasComPecas.pop().getTipoPeca();
			switch(pecaAux) {
				case 'D':{
					// Final de dama!
					return 1;
				}
				case 'd': {
					return 1;
				}
				case 'T': {
					if (qtdMaterial == 105) {
						return 2;
					}
					break;
				}
				case 't': {
					if (qtdMaterial == 105) {
						return 2;
					}
					break;
				}
				case 'B':{
					if (contemBispo) {
						if (qtdMaterial == 106) {
							return 3;
						}
					}
					if (contemCavalo) {
						if (qtdMaterial == 106) {
							return 4;
						}
					}
					contemBispo = true;
					break;
				}
				case 'b':{
					if (contemBispo) {
						if (qtdMaterial == 106) {
							return 3;
						}
					}
					if (contemCavalo) {
						if (qtdMaterial == 106) {
							return 4;
						}
					}
					contemBispo = true;
					break;
				}
				case 'C':{
					if (contemCavalo) {
						return 0;
					}
					if (contemBispo) {
						if (qtdMaterial == 106) {
							return 4;
						}
					}
					contemCavalo = true;					
					break;
				}
				case 'c':{
					if (contemCavalo) {
						return 0;
					}
					if (contemBispo) {
						if (qtdMaterial == 106) {
							return 4;
						}
					}
					contemCavalo = true;					
					break;
				}
			}
		}
		return 0;
	}
	
	public boolean responderPropostaDeEmpate() {
		// Aceita se estiver em desvantagem material
		return this.vantagemMaterial < 0;
	}
}
