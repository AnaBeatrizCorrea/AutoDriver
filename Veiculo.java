import java.math.*;
import java.time.Year;

public abstract class Veiculo {
	private String modelo;
	private String nome;
	private String placa;
	private String status;
	private int ano;
	private int diaria;
	private BigDecimal km = BigDecimal.ZERO;
	private BigDecimal precoDiaria = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;

	public Veiculo(String modelo, String nome, String placa, int ano, int diaria, BigDecimal km, BigDecimal precoDiaria) {
		setModelo(modelo);
		setNome(nome);
		setAno(ano);
		setPlaca(placa);
		setKm(km);
		setPrecoDiaria(precoDiaria);
		setDiaria(diaria);
		verificarStatus();
	}

	public void calcularTotal() {
		this.total = this.precoDiaria.multiply(BigDecimal.valueOf(this.diaria));
	}

	public void verificarStatus() {
		if (km.compareTo(BigDecimal.ZERO) == 0) {
			status = "Novo";
		} else if (km.remainder(new BigDecimal("10000")).compareTo(BigDecimal.ZERO) == 0) {
			status = "Pendente de Manutenção";
		} else {
			status = "Pronto para Uso";
		}
	}

	public boolean alugar() {
		if (status.equalsIgnoreCase("Alugado")) {
			System.out.println("INDISPONÍVEL: " + nome + " já está alugado.");
			return false;
		}
		if (status.equalsIgnoreCase("Em Manutenção") || status.equalsIgnoreCase("Pendente de Manutenção")) {
			System.out.println("BLOQUEADO: " + nome + " está " + status + " e não pode ser alugado.");
			return false;
		}
		this.status = "Alugado";
		calcularTotal();
		return true;
	}

	public abstract BigDecimal calcularCustoManutencao();

	public BigDecimal getKm() {
		return km;
	}

	public void setKm(BigDecimal km) {
		if (km == null || km.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("A quilometragem não pode ser nula ou negativa.");
		}
		this.km = km;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if (modelo == null || modelo.trim().isEmpty()) {
			throw new IllegalArgumentException("O modelo não pode ser nulo ou vazio.");
		}
		this.modelo = modelo.trim();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
		}
		this.nome = nome.trim();
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		if (placa == null || placa.trim().isEmpty()) {
			throw new IllegalArgumentException("A placa não pode ser nula ou vazia.");
		}
		this.placa = placa.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status == null || status.trim().isEmpty()) {
			throw new IllegalArgumentException("O status não pode ser nulo ou vazio.");
		}
		this.status = status;
	}

	public BigDecimal getPrecoDiaria() {
		return precoDiaria;
	}

	public void setPrecoDiaria(BigDecimal precoDiaria) {
		if (precoDiaria == null || precoDiaria.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O preço da diária deve ser maior que zero.");
		}
		this.precoDiaria = precoDiaria;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		if (ano < 1950 || ano > Year.now().getValue() + 1) {
			throw new IllegalArgumentException("Ano inválido: " + ano);
		}
		this.ano = ano;
	}

	// quantidade de dias da locação (0 enquanto o veículo não está alugado)
	public int getDiaria() {
		return diaria;
	}

	public void setDiaria(int diaria) {
		if (diaria < 0) {
			throw new IllegalArgumentException("A quantidade de dias não pode ser negativa.");
		}
		this.diaria = diaria;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		if (total == null || total.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("O total não pode ser nulo ou negativo.");
		}
		this.total = total;
	}
}
