package zacseriano.economadapi.domain.enums;

public enum TipoPagamentoEnum {
	CRÉDITO("CREDIT"),
	DÉBITO("DEBT");
	
	String descricao;
	
	private TipoPagamentoEnum (String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}