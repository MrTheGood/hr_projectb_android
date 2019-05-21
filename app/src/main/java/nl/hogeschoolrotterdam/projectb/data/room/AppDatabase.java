package nl.hogeschoolrotterdam.projectb.data.room;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Video;

/**
 * Created by maartendegoede on 23/04/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
@Database(entities = {Memory.class, Media.class, Image.class, Video.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MemoryDao memoryDao();

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Memory ADD COLUMN memoryType INTEGER NOT NULL DEFAULT '0'");

            database.execSQL("BEGIN TRANSACTION;");
            database.execSQL("CREATE TEMPORARY TABLE Backup(date,description TEXT,location,memoryType,id,title);");
            database.execSQL("INSERT INTO Backup SELECT date,description,location,memoryType,id,title FROM Memory;");
            database.execSQL("DROP TABLE Memory;");
            database.execSQL("CREATE TABLE Memory(date INTEGER NOT NULL,description TEXT NOT NULL,location TEXT NOT NULL,memoryType INTEGER NOT NULL,id TEXT NOT NULL PRIMARY KEY,title TEXT NOT NULL);");
            database.execSQL("INSERT INTO Memory SELECT date,description,location,memoryType,id,title FROM Backup;");
            database.execSQL("DROP TABLE Backup;");
            database.execSQL("COMMIT;");
        }
    };

    public static final Migration MIGRATION_1_3 = new Migration(1, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Memory ADD COLUMN memoryType INTEGER NOT NULL DEFAULT '0'");
        }
    };

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Memory ADD COLUMN memoryTypeIconId INTEGER NOT NULL DEFAULT '" + R.drawable.ic_map_adefault + "'");
        }
    };
}
