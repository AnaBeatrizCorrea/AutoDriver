import java.util.List;

public interface Oficinas {
    public void agendarRevisao(Veiculo veiculo, String data, String horas);
    public void agendarRevisao(Veiculo veiculo);
    public void concluirRevisao(Veiculo veiculo);

    public void adicionarVeiculoNaFrota(Frota frota, Veiculo veiculo);
    public void adicionarVeiculoNaFrota(Frota frota, List<Veiculo> veiculos);

    public boolean alugarVeiculo(Veiculo veiculo, int dias);
    public void devolverVeiculo(Veiculo veiculo);

    public void gerarRelatorioManutencao();
    public void gerarRelatorioManutencao(Frota frota);
}
