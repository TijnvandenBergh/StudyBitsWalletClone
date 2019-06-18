package nl.quintor.studybits.studybitswallet.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import nl.quintor.studybits.studybitswallet.room.entity.University;

@Dao
public interface UniversityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUniversities(University... universities);

    @Query("SELECT * FROM university")
    public LiveData<List<University>> get();

    @Query("SELECT * FROM university")
    public List<University> getStatic();

    @Query("SELECT * FROM university WHERE theirDid = :did")
    public LiveData<University> getByDid(String did);

    @Query("DELETE FROM university")
    public void delete();
}
