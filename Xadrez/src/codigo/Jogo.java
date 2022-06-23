package codigo;

import java.util.*;

/**
 * @author Humberto Mattiello
 * @date July 2021
 *
 */
public class Jogo {
	private boolean vezDasBrancas; // TRUE = Vez das brancas; FALSE = Vez das pretas
	private boolean estado; // TRUE = em andamento; FALSE = nao iniciado ou ja concluido
	private boolean vencedor; // TRUE = brancas venceram; FALSE = pretas venceram (apenas faz sentido se a partida ja terminou e possui um vencedor. Avaliar em conjunto com boolean estado e boolean empate)
	private boolean empate; // TRUE = partida terminou empatada; FALSE = partida terminou com um vencedor (apenas faz sentido se a partida ja terminou. Avaliar em conjunto com boolean estado)
	private boolean propostaEmpate;
	private boolean primeiraJogada; // TRUE = Ainda nao foi efetuada uma jogada (utilizada para nao travar os algoritmos que comparam jogada atual com jogada anterior, como a verificacao de en passant)
	private boolean desistiu;
	private int lanceAtual; // Primeiro lance das brancas é o lance 1, o primeiro das pretas é o 2, etc
	private Tabuleiro tabuleiro; // Objeto que guarda o posicionamento das pecas nas casas
	private Jogador jogadorBrancas;
	private Jogador jogadorPretas;
	private Jogada jogadaAtual; // Objeto que guarda as informacoes da jogada atual proposta pelo jogador
	private Jogada ultimaJogadaValida; // Objeto que guarda as informacoes da jogada anterior a atual (utilizado por alguns algoritmos como en-passant)
	private PecaPeao peao; // Controla En Passants
	private PecaRei reiBranco; // Controla Xeques, Xeque-mates e Roques e se ja moveu+
	private PecaRei reiPreto; // Controla se ja moveu
	private ArrayList<Jogada> listaDeJogadas; // lanceAtual 1 equivale ao índice 0 da lista, etc
	private ArrayList<Tabuleiro> ultimasPosicoes;
	
	// Construtores
	public Jogo() {
		this.vezDasBrancas = true;
		this.estado = false;
		this.vencedor = false;
		this.empate = false;
		this.propostaEmpate = false;
		this.primeiraJogada = true;
		this.desistiu = false;
		this.lanceAtual = 0;
		tabuleiro = new Tabuleiro();
		jogadaAtual = new Jogada();	
		ultimaJogadaValida = new Jogada();
		jogadorBrancas = new Jogador(true, true);
		jogadorPretas = new Jogador(true, false);
		peao = new PecaPeao('P');
		peao.setEnPassant(false);
		reiBranco = new PecaRei('R');
		reiPreto = new PecaRei('r');
		listaDeJogadas = new ArrayList<>();
		ultimasPosicoes = new ArrayList<>();
		ultimasPosicoes.add(tabuleiro);
	}
	
	public Jogo(Jogador brancas, Jogador pretas) {
		this.vezDasBrancas = true;
		this.estado = false;
		this.vencedor = false;
		this.empate = false;
		this.propostaEmpate = false;
		this.primeiraJogada = true;
		this.desistiu = false;
		this.lanceAtual = 0;
		tabuleiro = new Tabuleiro();
		jogadaAtual = new Jogada();	
		ultimaJogadaValida = new Jogada();
		jogadorBrancas = brancas;
		jogadorPretas = pretas;
		peao = new PecaPeao('P');
		peao.setEnPassant(false);
		reiBranco = new PecaRei('R');
		reiPreto = new PecaRei('r');
		listaDeJogadas = new ArrayList<>();
		ultimasPosicoes = new ArrayList<>();
	}
	
	// Logica
	/**
	 * Metodo principal da logica do jogo.
	 * Invoca metodos secundarios e acessorios para verificar a validade das jogadas,
	 * manter controle sobre de quem eh a vez de jogar, verificar se a partida terminou,
	 * coletar os movimentos dos jogadores e fazer o reposicionamento das pecas de acordo.
	 */
	public void jogar() {
		int lance = 0;		
		this.estado = true;
		ultimasPosicoes.add(tabuleiro);
		
		while (this.estado) {
			this.tabuleiro.imprimeTabuleiro();
			
			if (this.lanceAtual > 2) {
				if (reiBranco.isXequeMate(jogadaAtual, tabuleiro, true)) {
					System.out.println("Xeque-mate!");
					System.out.println("Fim de jogo. Vitoria das Pretas!");
					this.vencedor = false;
					break;			
				}
				if (reiPreto.isXequeMate(jogadaAtual, tabuleiro, true)) {
					System.out.println("Xeque-mate!");
					System.out.println("Fim de jogo. Vitoria das Brancas!");
					this.vencedor = true;
					break;	
				}
			}
			
			if (this.desistiu) {	
				System.out.println((vezDasBrancas ? "Brancas" : "Pretas") + " abandonam.");
				System.out.println("Fim de jogo. Vitoria das " + (this.vencedor ? "Brancas!" : "Pretas!"));
				break;
			}
			
			if (this.empate) {	
				System.out.println("Fim de jogo. Partida empatada!");
				break;
			}
			
			if (this.propostaEmpate) {
				System.out.println((vezDasBrancas ? "Brancas" : "Pretas") + " propoem empate.");
				System.out.println("Digite \"aceitar\" ou \"rejeitar\".");
				
				// Caso seja uma IA, faz uma marcacao em um tabuleiro ficticio para indicar proposta de empate.
				boolean isIA = false;				
				if(!vezDasBrancas) {
					if(!jogadorBrancas.isHumano()) {
						isIA = true;
					}
				}
				else {
					if(!jogadorPretas.isHumano()) {
						isIA = true;
					}
				}
				
				if(isIA) {
					Tabuleiro t = new Tabuleiro();
					t.casas[1][1] = 'E';
					lance = (!vezDasBrancas ? jogadorBrancas.coletarJogada(t, jogadaAtual, ultimaJogadaValida) : jogadorPretas.coletarJogada(t, jogadaAtual, ultimaJogadaValida));					
				}
				else {
					lance = jogadaAtual.coletarJogada(tabuleiro);
				}			
			}
			else {			
				System.out.println("Jogam as " + (vezDasBrancas ? "brancas" : "pretas"));
				System.out.println("Opcoes: ");
				System.out.println("1) Digite alguma jogada no formato \"e2-e4\", por exemplo.");
				System.out.println("2) Digite \"empate\" para propor empate.");
				System.out.println("3) Digite \"desistir\" para desistir do jogo.");				
				if (reiBranco.emXeque() || reiPreto.emXeque()) {
					System.out.println("Xeque!");
				}
				lance = (vezDasBrancas ? jogadorBrancas.coletarJogada(tabuleiro, jogadaAtual, ultimaJogadaValida) : jogadorPretas.coletarJogada(tabuleiro, jogadaAtual, ultimaJogadaValida));
			}
			
			System.out.print("\r\nLance " + (1 + this.lanceAtual / 2) + " (" + (this.vezDasBrancas ? "Brancas)" : "Pretas)") + ": ");
			System.out.println(jogadaAtual);
			
			switch (lance) {
				case 1: {
					if (validarJogada()) {
						// Se chegou ate aqui, eh porque a jogada eh valida
						atualizarHistorico();
						tabuleiro.atualizarTabuleiro(jogadaAtual);
						
						// Caso tenha sido movimento do rei, atualiza flags do rei
						if (jogadaAtual.getCasaDestino().getPeca().isRei()) {
							if (vezDasBrancas) {
								reiBranco.setJaMoveu(true);
							}
							else {
								reiPreto.setJaMoveu(true);
							}
						}	
						
						// Verificar condições de empate
						if (verificarEmpate()) {
							this.empate = true;
						}

						// Abaixar flag de primeira jogada
						this.primeiraJogada = false;
						
						// Inverter o marcador de quem eh a vez
						this.vezDasBrancas = !this.vezDasBrancas;
						
						// Atualizar ultimaJogadaValida
						this.ultimaJogadaValida = jogadaAtual.copiaJogada();
					}
					else {
						System.out.println("Jogada invalida.");
					}
					break;
				}
				case 2: {
					this.desistiu = true;
					this.vencedor = this.vezDasBrancas ? false : true;
					break;
				}
				case 3: {
					this.propostaEmpate = true;
					break;
				}
				case 4: {
					if (this.propostaEmpate)
						this.empate = true;
					break;
				}
				case 5: {
					this.propostaEmpate = false;
					break;
				}		
			}
		}
		this.estado = false;
	}
	
	/**
	 * Metodo que faz todas as validacoes necessarias para concluir se um movimento proposto pelo jogador
	 * e ou nao valido. 
	 * @return true se a jogada for valida, false caso contrario
	 */
	private boolean validarJogada() {
		jogadaAtual.setBrancas(vezDasBrancas);
		
		// Verifica se coordenadas de origem e destino sao validas
		if (!this.jogadaAtual.isCoordenadasValidas())
			return false;
		
		// Verifica se tem uma peca do jogador atual na casa de origem
		if (!verificarPecaValidaOrigem())
			return false;
		
		// Verifica se nao tem uma peca do jogador atual na casa de destino
		if (!verificarCasaDestinoValida()) 
			return false;		
		
		// Verifica se a jogada atual e um En Passant
		if (!this.primeiraJogada) {
			peao.setTipoPeca(vezDasBrancas ? 'P' : 'p');
			peao.verificarEnPassant(tabuleiro, jogadaAtual, ultimaJogadaValida);
		}
			
		// Verifica se a jogada atual eh um Roque
		if (vezDasBrancas) {
			if (!reiBranco.verificarRoque(tabuleiro, jogadaAtual, true))
				reiBranco.verificarRoqueGrande(tabuleiro, jogadaAtual, true);
		}
		else {
			if (!reiPreto.verificarRoque(tabuleiro, jogadaAtual, true))
				reiPreto.verificarRoqueGrande(tabuleiro, jogadaAtual, true);
		}

		// No caso de movimentações especiais, como roque por exemplo, não é necessário fazer a validação do movimento.
		// Pois esses casos especiais ja foram tratados individualmente.
		// Apenas no caso de movimentações simples é feita a validação.	
		if (!(peao.isEnPassant() || 
			  reiBranco.isRoque() || reiBranco.isRoqueGrande() || 
			  reiPreto.isRoque() || reiPreto.isRoqueGrande())){ 
			if (!isMovimentoValido()) {
				return false;	
			}
		}
		
		if(peao.verificarCoroacao(jogadaAtual, tabuleiro)) {
			promoverPeao();
		}
		
		// Essa verificacao é para ver se o jogador deixaria o seu rei em xeque com este movimento
		// Primeiro simula a jogada em um tabuleiro auxiliar.
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		tabuleiroAuxiliar.atualizarTabuleiro(jogadaAtual);
		if(vezDasBrancas) {
			if (reiBranco.isEmXeque(tabuleiroAuxiliar, false)) {
				return false;
			}
		}
		else {
			if (reiPreto.isEmXeque(tabuleiroAuxiliar, false)) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Verifica se na casa de origem tem uma peca da cor que eh a vez
	 * @return true se a peca eh valida, false caso contrario
	 */
	private boolean verificarPecaValidaOrigem() {
		if (this.vezDasBrancas) {
			if (this.jogadaAtual.getCasaOrigem().getPeca().isPecaBranca()) {
				return true;
			}				
		}
		else {
			if (this.jogadaAtual.getCasaOrigem().getPeca().isPecaPreta()) {
				return true;	
			}				
		}
		return false;
	}	
	
	/**
	 * Verifica se a peca na casa de destino nao eh da mesma cor
	 * @return true se destino for valido conforme criterio acima, false caso contrario
	 */
	private boolean verificarCasaDestinoValida() {
		if (this.vezDasBrancas) {
			if (!this.jogadaAtual.getCasaDestino().getPeca().isPecaBranca()) {
				return true;
			}				
		}
		else {
			if (!this.jogadaAtual.getCasaDestino().getPeca().isPecaPreta()) {
				return true;	
			}				
		}
		return false;
	}	
	
	/**
	 * Este metodo utiliza polimorfismo para invocar a validacao de movimento de acordo com a peca que foi jogada.
	 * @return true se o movimento da peca e valido
	 */
	private boolean isMovimentoValido () {
		return this.jogadaAtual.getCasaOrigem().getPeca().isMovimentoValido(jogadaAtual, tabuleiro, vezDasBrancas);
	}
	
			
	private boolean verificarEmpate() {
		if (empatePorFaltaDePecas(tabuleiro))
			return true;
		if (empatePorAfogamento(tabuleiro, jogadaAtual, vezDasBrancas))
			return true;
		if (empatePorRepeticao())
			return true;
		return false;
	}
	
	private boolean empatePorAfogamento(Tabuleiro tabuleiro, Jogada ultimaJogada, boolean vezDasBrancas) {
		// Tem que juntar todos os movimento possíveis
		// de todas as peças do jogador atual.
		// Se a pilha for vazia, é empate. Caso contrário, não é empate por afogamento.
		PecaRei rei = !vezDasBrancas ? reiBranco : reiPreto;
		Casa casaAux = new Casa();
		Casa casaAux2 = new Casa();
		Tabuleiro tabuleiroPrevisto = tabuleiro.copiaTabuleiro();
		Jogada jogadaTeste = new Jogada();
		Stack<Casa> possiveisMovimentos = new Stack<Casa>();
		Stack<Casa> casasComPeca = new Stack<Casa>();
		
		// Se estiver em xeque, não pode ser afogamento
		if (vezDasBrancas) {
			if (reiPreto.emXeque()) {
				return false;
			}
		}
		else {
			if (reiBranco.emXeque()) {
				return false;
			}
		}	
		
		// Gera uma pilha com todas as pecas do jogador atual
		casasComPeca = tabuleiroPrevisto.casasComPecasDestaCor(!vezDasBrancas);
		while (!casasComPeca.isEmpty()) {
			// E de cada uma dessas pecas, extrai sua pilha de movimentos validos
			casaAux = casasComPeca.pop();			
			possiveisMovimentos = casaAux.getPeca().movimentosValidos(tabuleiroPrevisto, casaAux, ultimaJogada, !vezDasBrancas);
			
			// Percorre pilha de casas extraida anteriormente, a cada passo simulando uma possivel jogada, vendo se o rei continua em xeque
			while (!possiveisMovimentos.isEmpty()) {
				casaAux2 = possiveisMovimentos.pop();
				jogadaTeste.setCasaOrigem(casaAux);
				jogadaTeste.setCasaDestino(casaAux2);
				tabuleiroPrevisto.atualizarTabuleiro(jogadaTeste);
				
				// Assim que encontrar um movimento valido para o processamento e retorna false para indicar que nao é afogamento
				if (!rei.isEmXeque(tabuleiroPrevisto, false))
					return false;
				
				// Essa tentativa nao deu, entao retorna o tabuleiro para a posicao inicial para a verificacao seguinte...
				// Para isso, basta inverter origem e destino, e atualizar o tabuleiro novamente
				jogadaTeste.setCasaOrigem(casaAux2);
				jogadaTeste.setCasaDestino(casaAux);
				tabuleiroPrevisto = tabuleiro.copiaTabuleiro();					
			}				
		}					
			
		return true;	
	}
	
	private boolean empatePorRepeticao() {
		int contagemRepetidas = 0;
		
		if (lanceAtual < 8)
			return false;
		
		for (Tabuleiro tabuleiroAux : ultimasPosicoes) {
			if (tabuleiroAux.equals(tabuleiro)){
				contagemRepetidas++;
			}
			if (contagemRepetidas >= 3)
				return true;
		}
		return false;
	}
	
	private boolean empatePorFaltaDePecas(Tabuleiro tabuleiro) {
		Stack<Casa> casasComPeca = new Stack<Casa>();
		Casa casaAux = new Casa ();
		boolean brancasTemPeca = false;
		boolean pretasTemPeca = false;
		boolean boolAux = false;

		// Monta pilha de peças brancas
		casasComPeca = tabuleiro.casasComPecasDestaCor(true);
		while (!casasComPeca.isEmpty()) {
			casaAux = casasComPeca.pop();
			if (casaAux.getPeca().isPeao()) {
				brancasTemPeca = true;
				break;
			}
			if (casaAux.getPeca().isTorre()) {
				brancasTemPeca = true;
				break;
			}
			if (casaAux.getPeca().isDama()) {
				brancasTemPeca = true;
				break;
			}
			if (casaAux.getPeca().isBispo()) {
				if (boolAux) {
					brancasTemPeca = true;
					break;
				}
				boolAux = true;
				continue;
			}
			if (casaAux.getPeca().isCavalo()) {
				if (boolAux) {
					brancasTemPeca = true;					
					break;
				}
				boolAux = true;
				continue;
			}
		}
		
		if (brancasTemPeca)
			return false;
	
		// Monta pilha de peças pretas
		boolAux = false;
		casasComPeca.clear();
		casasComPeca = tabuleiro.casasComPecasDestaCor(false);
		while (!casasComPeca.isEmpty()) {
			casaAux = casasComPeca.pop();
			if (casaAux.getPeca().isPeao()) {
				pretasTemPeca = true;
				break;
			}
			if (casaAux.getPeca().isTorre()) {
				pretasTemPeca = true;
				break;
			}
			if (casaAux.getPeca().isDama()) {
				pretasTemPeca = true;
				break;
			}
			if (casaAux.getPeca().isBispo()) {
				if (boolAux) {
					pretasTemPeca = true;
					break;
				}
				boolAux = true;
				continue;
			}
			if (casaAux.getPeca().isCavalo()) {
				if (boolAux) {
					pretasTemPeca = true;
					break;
				}
				boolAux = true;
				continue;
			}
		}
		
		if (pretasTemPeca)
			return false;
		
		return true;
	}
	
	private char promoverPeao() {
		String auxStr = new String("");
		
		jogadaAtual.setCoroada(' ');
		
		if(vezDasBrancas) {
			System.out.println("Peão coroado! Digite a peça (por exemplo D, C, etc): ");
			if (!jogadorBrancas.isHumano()) {
				jogadaAtual.setCoroada('D');
				return 'D';
			}
			Scanner input = new Scanner(System.in);
			while(!auxStr.equals("D") && !auxStr.equals("T") &&
				  !auxStr.equals("B") && !auxStr.equals("C"))
				auxStr = input.nextLine();
			jogadaAtual.setCoroada(auxStr.charAt(0));
		}
		else {
			System.out.println("Peão coroado! Digite a peça (por exemplo d, c, etc): ");
			if (!jogadorPretas.isHumano()) {
				jogadaAtual.setCoroada('d');
				return 'd';
			}
			Scanner input = new Scanner(System.in);
			while(!auxStr.equals("d") && !auxStr.equals("t") &&
				  !auxStr.equals("b") && !auxStr.equals("c"))
				auxStr = input.nextLine();
			jogadaAtual.setCoroada(auxStr.charAt(0));
		}
		
		return jogadaAtual.getCoroada();
	}

	/**
	 * Método que incrementa o marcador de jogada atual e as listas com os históricos
	 * de lances e posicionamentos. Essas listas devem ser mantidas para serem usadas por
	 * outros métodos.
	 */
	private void atualizarHistorico() {
		lanceAtual++;
		listaDeJogadas.add(jogadaAtual.copiaJogada());
		ultimasPosicoes.add(tabuleiro.copiaTabuleiro());
	}	
}
