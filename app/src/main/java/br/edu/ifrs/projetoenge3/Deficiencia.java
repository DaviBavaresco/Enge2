package br.edu.ifrs.projetoenge3;
import android.os.Parcel;
import android.os.Parcelable;
public class Deficiencia implements Parcelable{
    private String documentId;
    private String matricula;
    private String deficiencia;
    private String explica;
    private String status;

    public Deficiencia() {
        // Construtor vazio necessário para o Firebase
    }

    public Deficiencia(String documentId, String matricula, String deficiencia, String explica) {
        this.documentId = documentId;
        this.matricula = matricula;
        this.deficiencia = deficiencia;
        this.explica = explica;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExplica() {
        return explica;
    }

    public void setExplica(String explica) {
        this.explica = explica;
    }

    // Métodos Parcelable
    protected Deficiencia(Parcel in) {
        matricula = in.readString();
        deficiencia = in.readString();
        status = in.readString();
        documentId = in.readString();
        explica = in.readString();
    }

    public static final Creator<Deficiencia> CREATOR = new Creator<Deficiencia>() {
        @Override
        public Deficiencia createFromParcel(Parcel in) {
            return new Deficiencia(in);
        }

        @Override
        public Deficiencia[] newArray(int size) {
            return new Deficiencia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(matricula);
        parcel.writeString(deficiencia);
        parcel.writeString(status);
        parcel.writeString(documentId);
        parcel.writeString(explica);
    }
}