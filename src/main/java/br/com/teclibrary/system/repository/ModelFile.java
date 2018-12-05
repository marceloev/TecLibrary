package br.com.teclibrary.system.repository;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ModelFile {

    private String fileID;
    private InputStream inputStream;
    private byte[] bytes;

    public ModelFile(String fileID, InputStream inputStream) {
        this.fileID = fileID;
        this.inputStream = inputStream;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] transformToByteA() {
        if (bytes != null)
            return bytes;
        try {
            bytes = IOUtils.toByteArray(this.getInputStream());
            return bytes;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelFile)) return false;
        ModelFile that = (ModelFile) o;
        return Objects.equals(getFileID(), this.getFileID()) &&
                Objects.equals(getInputStream(), this.getInputStream());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileID(), getInputStream());
    }

    @Override
    public String toString() {
        return "ModelFileCache{" +
                "fileID='" + fileID + '\'' +
                ", inputStream=" + inputStream +
                '}';
    }
}
