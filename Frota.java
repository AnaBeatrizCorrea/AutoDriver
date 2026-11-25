import java.util.ArrayList;
import java.util.List;

public class Frota {
	private String nome;
	private ArrayList<Veiculo> veiculos;
	private int frotaID;
	private static int contadorFrotas;

	public Frota(String nome) {
		setNome(nome);
		this.frotaID = ++contadorFrotas;
		this.veiculos = new ArrayList<>();
	}

	public int getFrotaID() {
		return frotaID;
	}

	public void adicionarVeiculo(Veiculo veiculo) {
		if (veiculo == null) {
			throw new IllegalArgumentException("O veículo não pode ser nulo.");
		}
		this.veiculos.add(veiculo);
		System.out.println("Veiculo adicionado na frota. Total de veiculos: " + getQuantidadeVeiculos());
	}

	public Veiculo buscarPorPlaca(String placa) {
		for (Veiculo v : veiculos) {
			if (v.getPlaca().equalsIgnoreCase(placa)) {
				return v;
			}
		}
		return null;
	}

	public int getQuantidadeVeiculos() {
		return this.veiculos.size();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("O nome da frota não pode ser nulo ou vazio.");
		}
		this.nome = nome.trim();
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

}
