package nl.quintor.studybits.studybitswallet.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class University {
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String endpoint;
    @PrimaryKey
    @NonNull
    private String theirDid;

    @ColumnInfo
    private String credDefId;

    public University(String name, String endpoint, @NonNull String theirDid) {
        this.name = name;
        this.endpoint = endpoint;
        this.theirDid = theirDid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getTheirDid() {
        return theirDid;
    }

    public void setTheirDid(String theirDid) {
        this.theirDid = theirDid;
    }

    public String getCredDefId() {
        return credDefId;
    }

    public void setCredDefId(String credDefId) {
        this.credDefId = credDefId;
    }

    @Override
    public String toString() {
        return "University{" +
                "name='" + name + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", theirDid='" + theirDid + '\'' +
                '}';
    }
}
