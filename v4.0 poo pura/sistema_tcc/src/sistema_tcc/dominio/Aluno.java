package sistema_tcc.dominio;

import sistema_tcc.dominio.tipo.Papel;

public class Aluno extends Usuario {
    public Aluno(String mat, String nome, String senha) {
        super(mat, nome, senha, Papel.ALUNO);
    }

    public Tcc criarProposta(String titulo, String descricao) {
        return new Tcc(this, titulo, descricao);
    }
}