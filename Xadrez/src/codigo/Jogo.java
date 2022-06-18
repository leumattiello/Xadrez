package codigo;

import java.util.*;

//TODO Implementar Coroação dos peões
//TODO Implementar INTELIGENCIA ARTIFICIAL
//TODO IA esta entrando em loop infinito em determinadas condições de xeque


/**
 * @author Humberto Mattiello
 * @date Julho 2021
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
		peao.enPassant = false;
		reiBranco = new PecaRei('R');
		reiBranco.jaMoveu = false;
		reiBranco.roque = false;
		reiBranco.roqueGrande = false;
		reiBranco.xeque = false;
		reiBranco.xequeMate = false;
		reiPreto = new PecaRei('r');
		reiPreto.jaMoveu = false;
		reiPreto.roque = false;
		reiPreto.roqueGrande = false;
		reiPreto.xeque = false;
		reiPreto.xequeMate = false;
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
		peao.enPassant = false;
		reiBranco = new PecaRei('R');
		reiBranco.jaMoveu = false;
		reiBranco.roque = false;
		reiBranco.roqueGrande = false;
		reiBranco.xeque = false;
		reiBranco.xequeMate = false;
		reiPreto = new PecaRei('r');
		reiPreto.jaMoveu = false;
		reiPreto.roque = false;
		reiPreto.roqueGrande = false;
		reiPreto.xeque = false;
		reiPreto.xequeMate = false;
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
			
			if (reiBranco.xequeMate || reiPreto.xequeMate) {
				System.out.println("Xeque-mate!");
				System.out.println("Fim de jogo. Vitoria das " + (this.vencedor ? "Brancas!" : "Pretas!"));
				break;			
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
				System.out.println((vezDasBrancas ? "Brancas" : "Pretas") + "propoem empate.");
				System.out.println("Digite \"aceitar\" ou \"rejeitar\".");
			}
			else {			
				System.out.println("Jogam as " + (vezDasBrancas ? "brancas" : "pretas"));
				System.out.println("Opcoes: ");
				System.out.println("1) Digite alguma jogada no formato \"e2-e4\", por exemplo.");
				System.out.println("2) Digite \"empate\" para propor empate.");
				System.out.println("3) Digite \"desistir\" para desistir do jogo.");				
				if (reiBranco.xeque || reiPreto.xeque)
					System.out.println("Xeque!");
			}
			
			lance = (vezDasBrancas ? jogadorBrancas.coletarJogada(tabuleiro, jogadaAtual, ultimaJogadaValida) : jogadorPretas.coletarJogada(tabuleiro, jogadaAtual, ultimaJogadaValida));
			
			switch (lance) {
				case 1: {
					if (validarJogada()) {
						// Se chegou ate aqui, eh porque a jogada eh valida
						atualizarHistorico();
						ajustarPosicionamentos(tabuleiro, jogadaAtual);
						
						// Caso tenha sido movimento do rei, atualiza flags do rei
						if (jogadaAtual.destino.getPeca().isRei()) {
							if (vezDasBrancas)
								reiBranco.jaMoveu = true;
							else
								reiPreto.jaMoveu = true;
						}	

						// Verifica se o xeque aplicado nao eh um xeque-mate
						if (reiBranco.xeque) {
							reiBranco.xequeMate = xequeMate(vezDasBrancas, jogadaAtual, tabuleiro);
						}
						if (reiPreto.xeque) {
							reiPreto.xequeMate = xequeMate(vezDasBrancas, jogadaAtual, tabuleiro);
						}

						// Vitoria das brancas por Xeque-Mate
						if (reiBranco.xequeMate) {
							this.vencedor = false;
						}
						
						// Vitoria das pretas por Xeque-Mate
						if (reiPreto.xequeMate) {
							this.vencedor = true;
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
						this.ultimaJogadaValida = copiaJogada(jogadaAtual);
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
			peao.enPassant = peao.verificarEnPassant(tabuleiro, jogadaAtual, ultimaJogadaValida);
		}
			
		// Verifica se a jogada atual eh um Roque
		if (vezDasBrancas) {
			reiBranco.roque = reiBranco.verificarRoque(tabuleiro, jogadaAtual);
			if (!reiBranco.roque)
				reiBranco.roqueGrande = reiBranco.verificarRoqueGrande(tabuleiro, jogadaAtual);
		}
		else {
			reiPreto.roque = reiPreto.verificarRoque(tabuleiro, jogadaAtual);
			if (!reiPreto.roque)
				reiPreto.roqueGrande = reiPreto.verificarRoqueGrande(tabuleiro, jogadaAtual);
		}

		// No caso de movimentações especiais, como roque por exemplo, não é necessário fazer a validação do movimento.
		// Pois esses casos especiais ja foram tratados individualmente.
		// Apenas no caso de movimentações simples é feita a validação.	
		if (!(peao.enPassant || reiBranco.roque || reiBranco.roqueGrande || reiPreto.roque || reiPreto.roqueGrande)){ 
			if (!isMovimentoValido())
				return false;	
		}
		
		verificarCoroacao();
		
		// Essa verificacao eh para ver se o jogador deixaria o seu rei em xeque com este movimento
		if (verificarXeque(vezDasBrancas))
			return false;
		
		// Ja esta eh para ver se ele esta aplicando xeque no rei adversario
		if (verificarXeque(!vezDasBrancas)) {
			if(vezDasBrancas) {
				reiPreto.xeque = true;
				reiBranco.xeque = false;
			}
			else {
				reiBranco.xeque = true;
				reiPreto.xeque = false;
			}
		}
		else {
			reiBranco.xeque = false;
			reiPreto.xeque = false;
		}
		
		return true;
	}
	
	
	/**
	 * Verifica se na casa de origem tem uma peca da cor que eh a vez
	 * @return true se a peca eh valida, false caso contrario
	 */
	private boolean verificarPecaValidaOrigem() {
		if (this.vezDasBrancas) {
			if (this.jogadaAtual.origem.getPeca().isPecaBranca()) {
				return true;
			}				
		}
		else {
			if (this.jogadaAtual.origem.getPeca().isPecaPreta()) {
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
			if (!this.jogadaAtual.destino.getPeca().isPecaBranca()) {
				return true;
			}				
		}
		else {
			if (!this.jogadaAtual.destino.getPeca().isPecaPreta()) {
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
		return this.jogadaAtual.origem.getPeca().isMovimentoValido(jogadaAtual, tabuleiro, vezDasBrancas);
	}
	
	/**
	 * Verifica se o movimento atual proposto e ou nao um xeque.
	 * @param vezDasBrancas
	 * @return true Se a jogada atual e um xeque
	 */
	private boolean verificarXeque(boolean vezDasBrancas) {
		Tabuleiro tabuleiroPrevisto = new Tabuleiro ();
		Jogada copiaDaJogada = copiaJogada(jogadaAtual);
		
		// Copia o tabuleiro manualmente, pois o java copia apenas a referencia na atribuicao normal de objetos usando operador =
		for (int i = 0; i < 8; i++) 
			for (int j = 0; j < 8; j++)
				tabuleiroPrevisto.casas[i][j] = tabuleiro.casas[i][j];
		
		// Agora temos uma copia do tabuleiro e uma copia da jogada, podemos fazer o teste que quisermos
		ajustarPosicionamentos(tabuleiroPrevisto, copiaDaJogada);
		
		if (vezDasBrancas) {
			if (reiBranco.isEmXeque(tabuleiroPrevisto)) {
				return true;
			}
		}
		else {
			if (reiPreto.isEmXeque(tabuleiroPrevisto)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Metodo que avalia, dada a configuracao atual do tabuleiro, se quem esta jogando esta em xeque-mate ou nao.
	 * Este algoritmo de verificacao de xeque mate tem o seguinte fundamento.
	 * Antes de mais nada, caso nao seja xeque, retorna false, pois nao tem como ser xeque mate se nao eh xeque, entao evita gastar processamento a toa.
	 * Isto feito, primeiramente serao avaliadas todas as possibilidades de movimentacao do rei. Se alguma delas for valida, nao eh xeque mate, portanto retorna false.
	 * Em seguida, cria uma pilha de todas as casas que possuem peca do jogador atual (exceto o rei).
	 * Cada casa, ao ser desempilhada, ira gerar uma outra pilha de movimentos possiveis.
	 * Se, em algum momento, for encontrado algum movimento valido, retorna false, pois nao eh xeque-mate.
	 * Se nao foi encontrado nenhum movimento valido, o algoritmo termina retornando true.
	 * @param vezDasBrancas
	 * @param tabuleiro
	 * @return
	 */
	private boolean xequeMate(boolean vezDasBrancas, Jogada ultimaJogada, Tabuleiro tabuleiro) {
		PecaRei rei = !vezDasBrancas ? reiBranco : reiPreto;
		Casa casaDoRei = new Casa();
		Casa casaAux = new Casa();
		Casa casaAux2 = new Casa();
		Tabuleiro tabuleiroPrevisto = new Tabuleiro ();
		Jogada jogadaTeste = new Jogada();
		Stack<Casa> possiveisMovimentos = new Stack<Casa>();
		Stack<Casa> casasComPeca = new Stack<Casa>();
		char x = 'a';
		int y = 1;
		
		// Se nao estiver em xeque, nao tem como ser xeque-mate!
		if (!rei.xeque)
			return false;	
		
		// Copia o tabuleiro manualmente, pois o java copia apenas a referencia na atribuicao normal de objetos usando operador =
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {				
				tabuleiroPrevisto.casas[i][j] = tabuleiro.casas[i][j];

 				// Aproveita e ja confere para identificar em qual casa esta o potencial rei que esta levando mate
				if (tabuleiroPrevisto.casas[i][j] == rei.getTipoPeca()) {
					x = tabuleiro.indexParaCoordenadaX(j);
					y = tabuleiro.indexParaCoordenadaY(i);
					casaDoRei.setPeca(rei.getTipoPeca());
					casaDoRei.setCoordenadaX(x);
					casaDoRei.setCoordenadaY(y);
				}
			}
		}
		
		// Gera uma pilha com todas as pecas do jogador atual
		casasComPeca = tabuleiroPrevisto.casasComPecasDestaCor(!vezDasBrancas);
		while (!casasComPeca.isEmpty()) {
			// E de cada uma dessas pecas, extrai sua pilha de movimentos validos
			casaAux = casasComPeca.pop();			
			possiveisMovimentos = casaAux.peca.movimentosValidos(tabuleiroPrevisto, casaAux, ultimaJogada, !vezDasBrancas);
			
			// Percorre pilha de casas extraida anteriormente, a cada passo simulando uma possivel jogada, vendo se o rei continua em xeque
			while (!possiveisMovimentos.isEmpty()) {
				casaAux2 = possiveisMovimentos.pop();
				jogadaTeste.origem = casaAux;
				jogadaTeste.destino = casaAux2;
				tabuleiroPrevisto.atualizarTabuleiro(jogadaTeste);
				
				// Assim que encontrar um movimento valido para o processamento e retorna false para indicar que nao eh xeque-mate
				if (!rei.isEmXeque(tabuleiroPrevisto))
					return false;
				
				// Essa tentativa nao deu, entao retorna o tabuleiro para a posicao inicial para a verificacao seguinte...
				// Para isso, basta inverter origem e destino, e atualizar o tabuleiro novamente
				jogadaTeste.origem = casaAux2;
				jogadaTeste.destino = casaAux;
				tabuleiroPrevisto = copiaTabuleiro(tabuleiro);					
			}				
		}					
			
		return true;
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
		Tabuleiro tabuleiroPrevisto = new Tabuleiro ();
		Jogada jogadaTeste = new Jogada();
		Stack<Casa> possiveisMovimentos = new Stack<Casa>();
		Stack<Casa> casasComPeca = new Stack<Casa>();
		
		// Se estiver em xeque, não pode ser afogamento
		if (rei.xeque)
			return false;	

		tabuleiroPrevisto = copiaTabuleiro(tabuleiro);
		
		// Gera uma pilha com todas as pecas do jogador atual
		casasComPeca = tabuleiroPrevisto.casasComPecasDestaCor(!vezDasBrancas);
		while (!casasComPeca.isEmpty()) {
			// E de cada uma dessas pecas, extrai sua pilha de movimentos validos
			casaAux = casasComPeca.pop();			
			possiveisMovimentos = casaAux.peca.movimentosValidos(tabuleiroPrevisto, casaAux, ultimaJogada, !vezDasBrancas);
			
			// Percorre pilha de casas extraida anteriormente, a cada passo simulando uma possivel jogada, vendo se o rei continua em xeque
			while (!possiveisMovimentos.isEmpty()) {
				casaAux2 = possiveisMovimentos.pop();
				jogadaTeste.origem = casaAux;
				jogadaTeste.destino = casaAux2;
				tabuleiroPrevisto.atualizarTabuleiro(jogadaTeste);
				
				// Assim que encontrar um movimento valido para o processamento e retorna false para indicar que nao eh xeque-mate
				if (!rei.isEmXeque(tabuleiroPrevisto))
					return false;
				
				// Essa tentativa nao deu, entao retorna o tabuleiro para a posicao inicial para a verificacao seguinte...
				// Para isso, basta inverter origem e destino, e atualizar o tabuleiro novamente
				jogadaTeste.origem = casaAux2;
				jogadaTeste.destino = casaAux;
				tabuleiroPrevisto = copiaTabuleiro(tabuleiro);					
			}				
		}					
			
		return true;	
	}
	
	private boolean empatePorRepeticao() {
		int contagemRepetidas = 0;
		
		if (lanceAtual < 8)
			return false;
		
		for (Tabuleiro tabuleiroAux : ultimasPosicoes) {
			if (tabuleiroAux.equalsTo(tabuleiro)){
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
			if (casaAux.peca.isPeao()) {
				brancasTemPeca = true;
				break;
			}
			if (casaAux.peca.isTorre()) {
				brancasTemPeca = true;
				break;
			}
			if (casaAux.peca.isDama()) {
				brancasTemPeca = true;
				break;
			}
			if (casaAux.peca.isBispo()) {
				if (boolAux) {
					brancasTemPeca = true;
					break;
				}
				boolAux = true;
				continue;
			}
			if (casaAux.peca.isCavalo()) {
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
			if (casaAux.peca.isPeao()) {
				pretasTemPeca = true;
				break;
			}
			if (casaAux.peca.isTorre()) {
				pretasTemPeca = true;
				break;
			}
			if (casaAux.peca.isDama()) {
				pretasTemPeca = true;
				break;
			}
			if (casaAux.peca.isBispo()) {
				if (boolAux) {
					pretasTemPeca = true;
					break;
				}
				boolAux = true;
				continue;
			}
			if (casaAux.peca.isCavalo()) {
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
	
	private void verificarCoroacao() {
		String auxStr = new String("");
		if(vezDasBrancas) {
			if(jogadaAtual.origem.getPeca().isPeao()) {
				if(jogadaAtual.destino.getCoordenadaY() == 8) {
					System.out.println("Peão coroado! Digite a peça (por exemplo D, C, etc): ");
					Scanner input = new Scanner(System.in);
					while(//auxStr.equals("") &&
							!auxStr.equals("D") && !auxStr.equals("T") &&
							!auxStr.equals("B") && !auxStr.equals("C"))
						auxStr = input.nextLine();
					tabuleiro.setPecaEm(jogadaAtual.origem.getCoordenadaX(), jogadaAtual.origem.getCoordenadaY(), auxStr.charAt(0));
				}
			}
		}
		else {
			if(jogadaAtual.origem.getPeca().isPeao()) {
				if(jogadaAtual.destino.getCoordenadaY() == 1) {
					System.out.println("Peão coroado! Digite a peça (por exemplo d, c, etc): ");
					Scanner input = new Scanner(System.in);
					while(//auxStr.equals("") &&
							!auxStr.equals("d") && !auxStr.equals("t") &&
							!auxStr.equals("b") && !auxStr.equals("c"))
						auxStr = input.nextLine();
					tabuleiro.setPecaEm(jogadaAtual.origem.getCoordenadaX(), jogadaAtual.origem.getCoordenadaY(), auxStr.charAt(0));		
				}
			}
		}
	}

	/**
	 * Metodo para, apos todas as validacoes de movimento, efetuar a jogada, alterando o posicionamento das
	 * pecas no tabuleiro.
	 * @param tabuleiro
	 * @param jogada
	 */
	private void ajustarPosicionamentos(Tabuleiro tabuleiro, Jogada jogada) {
		// Ajusta o novo posicionamento das pecas, para depois atualizar o tabuleiro
		jogada.origem.setPeca(tabuleiro.getPecaEm(jogada.origem.getCoordenadaX(), jogada.origem.getCoordenadaY()));
		jogada.destino.setPeca(tabuleiro.getPecaEm(jogada.origem.getCoordenadaX(), jogada.origem.getCoordenadaY()));
		jogada.origem.setPeca(' ');
		
		// O En Passant exige um reposicionamento especial (remocao do peao que foi comido)
		if (peao.enPassant) {
			// Pega coluna para a qual o peao se moveu
			char colDoEnPassant = jogada.destino.getCoordenadaX();
			
			// Pega a fileira para a qual o peao se moveu, menos 1
			int filDoEnPassant = this.vezDasBrancas ? jogada.destino.getCoordenadaY() - 1 : jogada.destino.getCoordenadaY() + 1;
			
			// Remove a peca nessas coordenadas
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(filDoEnPassant)][tabuleiro.coordenadaXParaIndex(colDoEnPassant)] = ' ';			
		}
		
		// O Roque exige um posicionamento especial (ajuste da torre)
		if (reiBranco.roque) {
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(1)][tabuleiro.coordenadaXParaIndex('h')] = ' ';
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(1)][tabuleiro.coordenadaXParaIndex('f')] = 'T';
		}
		if (reiPreto.roque) {
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(8)][tabuleiro.coordenadaXParaIndex('h')] = ' ';
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(8)][tabuleiro.coordenadaXParaIndex('f')] = 't';				
		}		
		if (reiBranco.roqueGrande) {
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(1)][tabuleiro.coordenadaXParaIndex('a')] = ' ';
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(1)][tabuleiro.coordenadaXParaIndex('d')] = 'T';
		}
		if (reiPreto.roqueGrande) {
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(8)][tabuleiro.coordenadaXParaIndex('a')] = ' ';
			tabuleiro.casas[tabuleiro.coordenadaYParaIndex(8)][tabuleiro.coordenadaXParaIndex('d')] = 't';				
		}
		
		// Refletir no tabuleiro as mudancas feitas na variavel jogadaAtual
		tabuleiro.atualizarTabuleiro(jogada);
	}
	
	/**
	 * Método que incrementa o marcador de jogada atual e as listas com os históricos
	 * de lances e posicionamentos. Essas listas devem ser mantidas para serem usadas por
	 * outros métodos.
	 */
	private void atualizarHistorico() {
		lanceAtual++;
		listaDeJogadas.add(copiaJogada(jogadaAtual));
		ultimasPosicoes.add(copiaTabuleiro(tabuleiro));
	}
	
	/**
	 * Metodo acessorio para criar uma copia de um objeto do tipo Jogada.
	 * Necessario pois a atribuicao simples copia apenas a referencia.
	 * @param jogada
	 * @return Um novo objeto com as mesmas propriedades do elemento passado como parametro.
	 */
	private Jogada copiaJogada(Jogada jogada) {
		Jogada jogadaAux = new Jogada ();
		
		// Copiar manualmente todos os dados, caso contrario o Java copia apenas a referencia
		Peca pecaAux = new Peca();
		pecaAux.setTipoPeca(jogada.destino.peca.getTipoPeca());
		
		Casa casaOrigemAux = new Casa();
		casaOrigemAux.setPeca(pecaAux.getTipoPeca());
		casaOrigemAux.setCoordenadaX(jogada.origem.getCoordenadaX());
		casaOrigemAux.setCoordenadaY(jogada.origem.getCoordenadaY());
		
		Casa casaDestinoAux = new Casa();
		casaDestinoAux.setPeca(pecaAux.getTipoPeca());
		casaDestinoAux.setCoordenadaX(jogada.destino.getCoordenadaX());
		casaDestinoAux.setCoordenadaY(jogada.destino.getCoordenadaY());
		
		int deslocamentoX = jogada.getDeslocamentoX();
		int deslocamentoY = jogada.getDeslocamentoY();
		
		jogadaAux.origem = casaOrigemAux;
		jogadaAux.destino = casaDestinoAux;
		jogadaAux.setDeslocamentoX(deslocamentoX);
		jogadaAux.setDeslocamentoY(deslocamentoY);
		
		return jogadaAux;
	}
	
	private Tabuleiro copiaTabuleiro (Tabuleiro tabuleiro) {
		Tabuleiro tabuleiroAux = new Tabuleiro();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {				
				tabuleiroAux.casas[i][j] = tabuleiro.casas[i][j];
			}
		}
		
		return tabuleiroAux;
	}
	
}
