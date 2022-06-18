package codigo;
import java.util.*;

public class IA_v0 extends Jogador {

	public IA_v0() {
		super();
	}

	public IA_v0(boolean isHumano, boolean isBrancas) {
		super(isHumano, isBrancas);
	}

	public IA_v0(boolean isHumano, boolean isBrancas, String nome) {
		super(isHumano, isBrancas, nome);
	}

	/**
	 * Base do algoritmo v0:
	 * 	Primeiro verifica se pode dar xeque mate.
	 *	Se não puder, verifica qual lance deixa o oponente com menos material.
	 *	Se houver empate no criterio anterior, escolhe o lance que mais preserva o próprio material.
	 *  
	 *  Sim, este algoritmo é materialista e imediatista;)
	 */
	@Override
	protected Jogada pensar(Tabuleiro tabuleiro, Jogada ultimaJogada) {
		ArrayList<Jogada> listaDeLances = lancesPotenciais(tabuleiro, ultimaJogada);
		int indiceDaJogada = temXequeMate(tabuleiro, ultimaJogada, listaDeLances);
		int qtdDeLances = 0;
		int lanceAleatorio = 0;
		
		// Verifica se tem xeque mate
		if (indiceDaJogada > 0) {
			return listaDeLances.get(indiceDaJogada);
		}
		
		// Escolhe os lances que dão maior ganho imediato de material.
		// A lista de lances potenciais é reduzida.		
		qtdDeLances = listaDeLances.size();
		comerPeca(tabuleiro, ultimaJogada, listaDeLances);
		// Se só sobrou um lance, já retorna ele
		qtdDeLances = listaDeLances.size();
		if (qtdDeLances <= 1) {
			return listaDeLances.get(0);
		}
		
		// Agora entre os lances restantes, escolhe o que mais preserva material no imediato.
		preservarPecas(tabuleiro, ultimaJogada, listaDeLances);
		
		// Se sobrou só um lance, retorna ele. 
		// Caso contrário, retorne um lance aleatório.
		qtdDeLances = listaDeLances.size();
		if (qtdDeLances <= 1) {
			return listaDeLances.get(0);
		}
		else {
			double rdm = Math.nextDown((Math.random() * (double)qtdDeLances));
			lanceAleatorio = (int)rdm;		
		}
		
		return listaDeLances.get(lanceAleatorio);
	}	
	
	private void comerPeca(Tabuleiro tabuleiro, Jogada ultimaJogada, ArrayList<Jogada> listaDeLances) {
		ArrayList<Integer> prioridadesDosLances = new ArrayList<Integer> ();
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		int materialOponente = tabuleiro.material(!this.isBrancas());;
		int prioridade = 0;
		int prioridadeMaxima = 0;
		int indiceDoLance = 0;
		
		// Entre todos os lances, escolhe os que deixam o inimigo com menos material
		for (Jogada jogadaAux : listaDeLances) {			
			// Simula a jogada em um tabuleiro auxiliar
			tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
			
			// Verifica como fica o material do oponente após essa possível jogada
			prioridade = materialOponente - tabuleiroAuxiliar.material(!this.isBrancas());
			prioridadesDosLances.add(prioridade);
			if (prioridade > prioridadeMaxima)
				prioridadeMaxima = prioridade;
			
			// Retorna o tabuleiro auxiliar para a posição para a próxima simulação
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();	
		}
		
		// Remove todos os lances que forem menores que a prioridade maxima
		for (int i : prioridadesDosLances) {
			if (i < prioridadeMaxima) {
				listaDeLances.remove(indiceDoLance);
			}
			else {
				indiceDoLance++;
			}
		}
	}
	
	/**
	 * 
	 * @param tabuleiro
	 * @param ultimaJogada
	 * @param listaDeLances Esse parametro é alterado (reduzido) pelo método!
	 * @return -1 se não tiver xeque mate, índice do lance se tiver.
	 */
	private int temXequeMate(Tabuleiro tabuleiro, Jogada ultimaJogada, ArrayList<Jogada> listaDeLances) {
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		PecaRei reiOponente = new PecaRei(this.isBrancas() ? 'r' : 'R');
		int i = 0;
		
		// Entre todos os lances, procure algum que de xeque mate.
		for (Jogada jogadaAux : listaDeLances) {			
			// Simula a jogada em um tabuleiro auxiliar
			tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
			
			// Se o lance deixa o rei oponente em xeque
			if (reiOponente.isEmXeque(tabuleiroAuxiliar)) {
				// Verifica se o rei oponente teria escapatória
				if (reiOponente.isXequeMate(ultimaJogada, tabuleiroAuxiliar)) {
					return i;
				}
			}

			// Retorna o tabuleiro auxiliar para a posição para a próxima simulação
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();	
			i++;
		}
		
		return -1;
	}
	
	/**
	 * @param tabuleiro
	 * @param ultimaJogada
	 * @param listaDeLances Esse parâmetro é alterado (reduzido) pelo método!
	 */
	private void preservarPecas(Tabuleiro tabuleiro, Jogada ultimaJogada, ArrayList<Jogada> listaDeLances) {
		ArrayList<Integer> prioridadesDosLances = new ArrayList<Integer> ();
		Tabuleiro tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		Casa casaAux = new Casa();
		Peca pecaAux;
		int prioridade = 0;
		int prioridadeMaxima = 0;
		int i = 0;
		int aux = 0;
		
		// Percorre a lista de lances.
		// Para cada lance, verifica qual é a peca de origem.
		// Calcula se esta sob ameaca. 
		// Se estiver, simule o lance.
		// Calcula novamente se esta sob ameaca.
		// Se ainda estiver sob ameaca, prioridade 0.
		// Caso contrario, prioridade recebe valorHeuristicoPadrao da peca.
		for (Jogada jogadaAux : listaDeLances) {
			casaAux = jogadaAux.origem;
			pecaAux = casaAux.getPeca();
			aux = pecaAux.sobAmeaca(tabuleiroAuxiliar, casaAux);
			if (0 < aux) {
				tabuleiroAuxiliar.atualizarTabuleiro(jogadaAux);
				prioridade = pecaAux.getValorHeuristicoPadrao();
				prioridadesDosLances.add(pecaAux.getValorHeuristicoPadrao());
				if (prioridade > prioridadeMaxima) {
					prioridadeMaxima = prioridade;					
				}
			}
			else {
				prioridadesDosLances.add(0);
			}			
			tabuleiroAuxiliar = tabuleiro.copiaTabuleiro();
		}
		
		// Agora temos um array cujos elementos equivalem à prioridade dos lances,
		// nos mesmos índices em relação à lista de lances.
		// Também temos o valor máximo de prioridade encontrada.
		// Vamos então excluir todos os elementos que forem menores que a prioridade maxima.
		for (int p : prioridadesDosLances) {
			if (p < prioridadeMaxima) {
				listaDeLances.remove(i);
			}
			else {
				i++;
			}
		}
	}
}
