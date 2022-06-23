package codigo;

public class Prioridade extends Jogada {
	private int ganhoMaterialImediato;
	private int ganhoMaterialPotencial;
	private int materialPreservado;
	private int ganhoEmDesenvolvimento;
	private int prioridade;
	Heuristica heuristicaAposJogada;
	
	//
	// Construtores
	//	

	public Prioridade() {
		super();
		this.ganhoMaterialImediato = 0;
		this.ganhoMaterialPotencial = 0;
		this.ganhoEmDesenvolvimento = 0;
		this.heuristicaAposJogada = new Heuristica(this.getCasaOrigem().getPeca().isBranca());
	}

	public Prioridade(Casa origem, Casa destino) {
		super(origem, destino);
		this.ganhoMaterialImediato = 0;
		this.ganhoMaterialPotencial = 0;
		this.ganhoEmDesenvolvimento = 0;
		this.heuristicaAposJogada = new Heuristica(this.getCasaOrigem().getPeca().isBranca());
	}

	public Prioridade(Casa origem, Casa destino, char pecaOrigem) {
		super(origem, destino, pecaOrigem);
		this.ganhoMaterialImediato = 0;
		this.ganhoMaterialPotencial = 0;
		this.ganhoEmDesenvolvimento = 0;
		this.heuristicaAposJogada = new Heuristica(this.getCasaOrigem().getPeca().isBranca());
	}

	public Prioridade(Casa origem, Casa destino, Tabuleiro tabuleiro) {
		super(origem, destino, origem.getTipoPeca());
		this.ganhoMaterialImediato = 0;
		this.ganhoMaterialPotencial = 0;
		this.ganhoEmDesenvolvimento = 0;
		this.heuristicaAposJogada = new Heuristica(this.getCasaOrigem().getPeca().isBranca());
		this.heuristicaAposJogada.atualizaMapas(tabuleiro);
	}
	
	//
	// Setters e getters
	//
	
	public int getGanhoMaterialImediato() {
		return ganhoMaterialImediato;
	}

	public void setGanhoMaterialImediato(int ganhoMaterialImediato) {
		this.ganhoMaterialImediato = ganhoMaterialImediato;
	}

	public int getGanhoMaterialPotencial() {
		return ganhoMaterialPotencial;
	}

	public void setGanhoMaterialPotencial(int ganhoMaterialPotencial) {
		this.ganhoMaterialPotencial = ganhoMaterialPotencial;
	}

	public int getGanhoEmDesenvolvimento() {
		return ganhoEmDesenvolvimento;
	}

	public void setGanhoEmDesenvolvimento(int ganhoEmDesenvolvimento) {
		this.ganhoEmDesenvolvimento = ganhoEmDesenvolvimento;
	}

	public int getMaterialPreservado() {
		return materialPreservado;
	}

	public void setMaterialPreservado(int materialPreservado) {
		this.materialPreservado = materialPreservado;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
	
	//
	// Overrides Úteis
	//

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Prioridade tmp = new Prioridade(this.getCasaOrigem(), this.getCasaDestino(), this.getTipoPecaOrigem());
		
		tmp.setPrioridade(this.prioridade);
		tmp.setGanhoEmDesenvolvimento(this.ganhoEmDesenvolvimento);
		tmp.setGanhoMaterialPotencial(this.ganhoMaterialPotencial);
		tmp.setGanhoMaterialImediato(this.ganhoMaterialImediato);
		
		return tmp;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Prioridade))
			return false;
		
		Prioridade tmp = (Prioridade) obj;
		
		return this.getPrioridade() == tmp.getPrioridade();
	}

	@Override
	public String toString() {
		String str = new String("");
		
		str += super.toString();
		str += "\r\n GMI: " + Integer.toString(ganhoMaterialImediato);
		str += "\r\n GMP: " + Integer.toString(ganhoMaterialPotencial);
		str += "\r\n MPr: " + Integer.toString(materialPreservado);
		str += "\r\n GDe: " + Integer.toString(ganhoEmDesenvolvimento);
		str += "\r\n PTC: " + Integer.toString(prioridade);
		str += "\r\n\r\n";
		
		return str;
	}

	
	//
	// Lógica
	//

	public int calculaPrioridade(int pesoMaterial, int pesoSimplificar, int pesoPosicional) {
		this.prioridade = pesoMaterial * ganhoMaterialPotencial;
		this.prioridade +=  pesoSimplificar * this.ganhoMaterialImediato;
		this.prioridade +=  pesoPosicional * ganhoEmDesenvolvimento;
		return prioridade;
	}
	
	public boolean prioridadeMaiorQue(int prioridade) {
		if (this.getPrioridade() > prioridade) {
			return true;
		}
		return false;
	}
}
