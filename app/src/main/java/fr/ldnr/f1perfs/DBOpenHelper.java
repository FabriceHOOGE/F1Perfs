package fr.ldnr.f1perfs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kévin on 19/04/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    // The database
    private SQLiteDatabase db;

    public static class Constants implements BaseColumns {

        // Nom de la BDD
        public static final String DATABASE_NAME = "f1perfs";

        // Version de la BDD
        public static final int DATABASE_VERSION = 1;

        // Nom de la table
        public static final String TABLE_NAME = "Perfs";

        // Noms des colonnes
        public static final String KEY_COL_ID = "_id";
        public static final String KEY_COL_EVENT = "event";
        public static final String KEY_COL_TIME = "time";
        public static final String KEY_COL_TRACK = "track";
        public static final String KEY_COL_PILOT = "pilot";

        // Index des colonnes
        public static final int ID_COLUMN = 1;
        public static final int TIME_COLUMN = 2;
        public static final int TRACK_COLUMN = 3;
        public static final int PILOT_COLUMN = 4;

    }

    /**
     * Requête de création de la BDD
     */
    private static final String DATABASE_CREATE = "create table "
            + Constants.TABLE_NAME + "("
            + Constants.KEY_COL_ID + " integer primary key autoincrement, "
            + Constants.KEY_COL_EVENT + " TEXT, "
            + Constants.KEY_COL_TIME + " TEXT, "
            + Constants.KEY_COL_TRACK + " TEXT, "
            + Constants.KEY_COL_PILOT + " TEXT) ";


    /**
     * @param context
     *            = contexte de la classe appelant le constructeur
     */
    public DBOpenHelper(Context context)
    {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    //est appelé lors de la premiere création de la table. Installation de l'application.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DATABASE_CREATE);
    }

    //est utilisée lors de changement de schema dans la base de données
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Mssg d'informations MAJ de la BDD
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion + " vers la version " + newVersion + ", les anciennes données seront détruites ");
        // Effacement de l'ancienne
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        // Création de la nouvelle
        onCreate(db);
    }


    /**
     * Ouverture BDD *
     *
     * @throws SQLiteException
     */
    public void openDB() throws SQLiteException
    {
        try { db = getWritableDatabase(); }
        catch (SQLiteException ex) { db = getReadableDatabase(); }
    }

    /** Fermeture BDD */
    public void closeDB() {
        db.close();
    }

    /**
     * @param event
     *            = event name to record
     * @param time
     *            = time to record
     * @param track
     *            = track name to record
     * @param pilot
     *            = pilot name to record
     *
     */
    public Boolean insertRecord(String event, String time, String track, String pilot)
    {
        openDB();

        // INSERT
        db.execSQL("INSERT INTO "+Constants.TABLE_NAME+" (event, time, track, pilot) VALUES (?,?,?,?)", new Object[]{event, time, track, pilot});

        // SELECT pour tester l'insert
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+Constants.TABLE_NAME+" WHERE event=? AND time=? AND track=? AND PILOT=?",new String[]{event, time, track, pilot});

        Boolean result = false;

        // Vérifie si l'insert à réussi
        if(cursor.moveToFirst()) result = true;

        cursor.close();
        closeDB();

        return result;
    }


    /**
     * @param select
     *            = tableau des colonnes à sélectionner
     * @param where
     *            = tableau des colonnes où chercher
     * @param values
     *            = tableau des valeurs à chercher
     *
     */
    public ArrayList<ArrayList<String>> selectRecord(String[] select, String[] where, String[] values)
    {
        openDB();

        ArrayList<ArrayList<String>> resultQuery = new ArrayList<>();

        String selectQuery = "SELECT ";

        for (int i = 0; i < select.length; i++)
        {
            selectQuery += select[i];
            if(i < select.length - 1) selectQuery += ", ";
            resultQuery.add(new ArrayList<String>());
        }

        selectQuery += " FROM " + Constants.TABLE_NAME;

        if (where.length > 0 && where.length == values.length)
        {
            String selectWhere = " WHERE ";
            for (int i = 0; i < where.length; i++)
            {
                selectWhere += where[i] + "=?";
                if(i < where.length - 1) selectWhere += " AND ";
            }
            selectQuery += selectWhere;
        }


        Cursor cursor = db.rawQuery(selectQuery, values);

        for (int i = 0; i < select.length; i++)
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                resultQuery.get(i).add(cursor.getString(cursor.getColumnIndex(select[i])));
                cursor.moveToNext();
            }
        }

        cursor.close();
        closeDB();

        return resultQuery;



    }

}
