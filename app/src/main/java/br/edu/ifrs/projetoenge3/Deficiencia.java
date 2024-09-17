package br.edu.ifrs.projetoenge3;

public class Deficiencia {
    private String documentId;
    private String matricula;
    private String deficiencia;

    public Deficiencia() {
        // Construtor vazio necessário para o Firebase
    }

    public Deficiencia(String documentId, String matricula, String deficiencia) {
        this.documentId = documentId;
        this.matricula = matricula;
        this.deficiencia = deficiencia;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDeficiencia() {
        return deficiencia;
    }

    public void setDeficiencia(String deficiencia) {
        this.deficiencia = deficiencia;
    }
}