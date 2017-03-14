package com.everteam.storage.managers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileId {
    private String repositoryId;

    private String repositoryFileId;

    public static FileId get(String id) {
        Path p = Paths.get(id);

        String repositoryId = p.getRoot().toString();

        FileId fileId = new FileId().repositoryId(repositoryId).repositoryFileId(id.substring(repositoryId.length()));

        return fileId;
    }

    public FileId repositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public FileId repositoryFileId(String repositoryFileId) {
        this.repositoryFileId = repositoryFileId;
        return this;
    }

    public String getRepositoryFileId() {
        return repositoryFileId;
    }

    public void setRepositoryFileId(String repositoryFileId) {
        this.repositoryFileId = repositoryFileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(repositoryId, repositoryFileId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FileId {\n");
        sb.append("    repositoryId: ").append(toIndentedString(repositoryId)).append("\n");
        sb.append("    repositoryFileId: ").append(toIndentedString(repositoryFileId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
