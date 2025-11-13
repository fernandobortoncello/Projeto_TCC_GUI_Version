package sistema_tcc.dominio;

/**
 * Representa os diferentes estados de um TCC durante seu ciclo de vida.
 * Atualizado para incluir todos os estados do fluxo.
 */
public enum TccStatus {
    PROPOSTA,        // UC1
    EM_ANDAMENTO,    // UC2
    AGUARDANDO_BANCA,  // UC4
    FINALIZADO         // UC5
}