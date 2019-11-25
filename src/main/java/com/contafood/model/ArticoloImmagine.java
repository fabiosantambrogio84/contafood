package com.contafood.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode
@Entity
@Table(name = "articolo_immagine")
public class ArticoloImmagine implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_articolo")
    private Articolo articolo;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_complete_path")
    private String fileCompletePath;

    @Column(name = "data_inserimento")
    private Timestamp dataInserimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileCompletePath() {
        return fileCompletePath;
    }

    public void setFileCompletePath(String fileCompletePath) {
        this.fileCompletePath = fileCompletePath;
    }

    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("{");
        result.append("id: " + id);
        result.append(", articolo: " + articolo);
        result.append(", fileName: " + fileName);
        result.append(", filePath: " + filePath);
        result.append(", fileCompletePath: " + fileCompletePath);
        result.append(", dataInserimento: " + dataInserimento);
        result.append("}");

        return result.toString();
    }
}
