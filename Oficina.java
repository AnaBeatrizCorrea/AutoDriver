import java.util.*;
import java.math.*;
import java.time.*;
import java.time.format.*;

public class Oficina implements Oficinas {

	private String proprietario;
	private int funcionario;
	private List<Frota> frotas = new ArrayList<>();
	private ArrayList<Veiculo> frotaAtiva = new ArrayList<>();

	public Oficina(String proprietario, int funcionario) {
		setProprietario(proprietario);
		setFuncionario(funcionario);
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		if (proprietario == null || proprietario.trim().isEmpty()) {
			throw new IllegalArgumentException("O proprietário não pode ser nulo ou vazio.");
		}
		this.proprietario = proprietario.trim();
	}

	public int getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(int funcionario) {
		if (funcionario < 0) {
			throw new IllegalArgumentException("A quantidade de funcionários não pode ser negativa.");
		}
		this.funcionario = funcionario;
	}

	public List<Frota> getFrotas() {
		return frotas;
	}

	public List<Veiculo> getFrotaAtiva() {
		return frotaAtiva;
	}

	public void adicionarFrota(Frota frota) {
		if (frota == null) {
			throw new IllegalArgumentException("A frota não pode ser nula.");
		}
		if (!frotas.contains(frota)) {
			frotas.add(frota);
		}
	}

	public Veiculo buscarVeiculo(String placa) {
		for (Frota frota : frotas) {
			Veiculo v = frota.buscarPorPlaca(placa);
			if (v != null) {
				return v;
			}
		}
		return null;
	}

	@Override
	public void adicionarVeiculoNaFrota(Frota frota, Veiculo veiculo) {
		if (frota == null || veiculo == null) {
			throw new IllegalArgumentException("A frota e o veículo não podem ser nulos.");
		}
		if (buscarVeiculo(veiculo.getPlaca()) != null) {
			throw new IllegalArgumentException("Já existe um veículo com a placa " + veiculo.getPlaca() + ".");
		}
		adicionarFrota(frota);
		frota.adicionarVeiculo(veiculo);
		System.out.println(veiculo.getNome() + " adicionado à " + frota.getNome());
	}

	// sobrecarga: adiciona vários veículos de uma vez
	@Override
	public void adicionarVeiculoNaFrota(Frota frota, List<Veiculo> veiculos) {
		for (Veiculo v : veiculos) {
			adicionarVeiculoNaFrota(frota, v);
		}
	}

	@Override
	public void agendarRevisao(Veiculo veiculo, String data, String horas) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime agendamento;
		try {
			agendamento = LocalDateTime.parse(data + " " + horas, formatter);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Data ou hora inválida. Use dd/MM/yyyy e HH:mm.");
		}
		marcarManutencao(veiculo, agendamento.format(formatter));
	}

	// sobrecarga: agenda a revisão para agora
	@Override
	public void agendarRevisao(Veiculo veiculo) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		marcarManutencao(veiculo, LocalDateTime.now().format(formatter));
	}

	private void marcarManutencao(Veiculo veiculo, String quando) {
		if (veiculo == null) {
			throw new IllegalArgumentException("O veículo não pode ser nulo.");
		}
		if (veiculo.getStatus().equalsIgnoreCase("Alugado")) {
			throw new IllegalStateException(veiculo.getNome() + " está alugado. Aguarde a devolução.");
		}
		if (veiculo.getStatus().equalsIgnoreCase("Em Manutenção")) {
			throw new IllegalStateException(veiculo.getNome() + " já está em manutenção.");
		}
		// se o km já chegou na revisão (status Pendente) é revisão por km, senão é periódica
		String tipo = "periódica";
		if (veiculo.getStatus().equalsIgnoreCase("Pendente de Manutenção")) {
			tipo = "por quilometragem";
		}
		veiculo.setStatus("Em Manutenção");
		System.out.println("Revisão " + tipo + " agendada para " + veiculo.getNome() + " em " + quando
				+ ". Veículo bloqueado para locação.");
	}

	@Override
	public void concluirRevisao(Veiculo veiculo) {
		if (!veiculo.getStatus().equalsIgnoreCase("Em Manutenção")) {
			throw new IllegalStateException(veiculo.getNome() + " não está em manutenção.");
		}
		veiculo.setStatus("Pronto para Uso");
		System.out.println("Revisão concluída. " + veiculo.getNome() + " liberado para locação.");
	}

	@Override
	public boolean alugarVeiculo(Veiculo veiculo, int dias) {
		if (dias <= 0) {
			throw new IllegalArgumentException("A quantidade de dias deve ser maior que zero.");
		}
		int diasAnteriores = veiculo.getDiaria();
		veiculo.setDiaria(dias);
		if (!veiculo.alugar()) { // recusa se estiver em manutenção, pendente ou já alugado
			veiculo.setDiaria(diasAnteriores);
			return false;
		}
		frotaAtiva.add(veiculo);
		System.out.println(veiculo.getNome() + " registrado na frota ativa.");
		return true;
	}

	@Override
	public void devolverVeiculo(Veiculo veiculo) {
		if (!veiculo.getStatus().equalsIgnoreCase("Alugado")) {
			throw new IllegalStateException(veiculo.getNome() + " não está alugado.");
		}
		veiculo.setStatus("Pronto para Uso");
		veiculo.setDiaria(0);
		veiculo.setTotal(BigDecimal.ZERO);
		frotaAtiva.remove(veiculo);
		System.out.println(veiculo.getNome() + " devolvido e removido da frota ativa.");
	}

	@Override
	public void gerarRelatorioManutencao() {
		ArrayList<Veiculo> todos = new ArrayList<>();
		for (Frota frota : frotas) {
			todos.addAll(frota.getVeiculos());
		}
		imprimirRelatorio(todos);
	}

	// sobrecarga: relatório de uma frota só
	@Override
	public void gerarRelatorioManutencao(Frota frota) {
		if (frota == null) {
			throw new IllegalArgumentException("A frota não pode ser nula.");
		}
		imprimirRelatorio(frota.getVeiculos());
	}

	private void imprimirRelatorio(List<Veiculo> lista) {
		BigDecimal totalCarros = BigDecimal.ZERO;
		BigDecimal totalMotos = BigDecimal.ZERO;
		BigDecimal totalCaminhoes = BigDecimal.ZERO;

		for (Veiculo v : lista) {
			BigDecimal custo = v.calcularCustoManutencao();
			System.out.println(v.getNome() + " (" + v.getPlaca() + ") - Custo Manutenção: R$ "
					+ custo.setScale(2, RoundingMode.HALF_UP));
			if (v instanceof Carro) {
				totalCarros = totalCarros.add(custo);
			} else if (v instanceof Moto) {
				totalMotos = totalMotos.add(custo);
			} else {
				totalCaminhoes = totalCaminhoes.add(custo);
			}
		}

		System.out.println("Total carros: R$ " + totalCarros.setScale(2, RoundingMode.HALF_UP));
		System.out.println("Total motos: R$ " + totalMotos.setScale(2, RoundingMode.HALF_UP));
		System.out.println("Total caminhões: R$ " + totalCaminhoes.setScale(2, RoundingMode.HALF_UP));
		BigDecimal geral = totalCarros.add(totalMotos).add(totalCaminhoes);
		System.out.println("TOTAL: R$ " + geral.setScale(2, RoundingMode.HALF_UP));
	}

}
